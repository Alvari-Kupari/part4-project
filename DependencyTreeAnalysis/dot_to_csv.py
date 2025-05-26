import os
import re
import csv
from collections import defaultdict, namedtuple
import networkx as nx
from pathlib import Path

# Define a structure to hold dependency information
Dependency = namedtuple('Dependency', ['group_id', 'artifact_id', 'version', 'scope', 'depth'])

class MavenDependencyAnalyzer:
    def __init__(self, dot_files_dir, output_dir="output"):
        self.dot_files_dir = Path(dot_files_dir)
        self.output_dir = Path(output_dir)
        self.all_conflicts = []
        
        # Create output directory if it doesn't exist
        self.output_dir.mkdir(exist_ok=True)
        
    def parse_maven_coordinates(self, coord_string):
        """Parse Maven coordinates from the node string"""
        # Remove any additional information in parentheses first, but keep the base coordinates
        original = coord_string
        
        # Handle omitted conflicts like "(com.netflix.servo:servo-core:jar:0.5.3:test - omitted for conflict with 0.12.21)"
        conflict_match = re.search(r'\(([^)]+) - omitted for conflict with ([^)]+)\)', coord_string)
        if conflict_match:
            base_coords = conflict_match.group(1)
            conflicting_version = conflict_match.group(2)
            return self._parse_coordinates_string(base_coords), conflicting_version
        
        # Remove version managed information but keep base coordinates
        coord_string = re.sub(r'\s*\([^)]*\)', '', coord_string)
        
        return self._parse_coordinates_string(coord_string), None
        
    def _parse_coordinates_string(self, coord_string):
        """Parse Maven coordinate string into components"""
        # Split by colon to get Maven coordinates
        parts = coord_string.split(':')
        
        if len(parts) >= 4:  # groupId:artifactId:type:version:scope
            group_id = parts[0]
            artifact_id = parts[1]
            # Skip type (jar, war, etc.)
            version = parts[3]
            scope = parts[4] if len(parts) > 4 else 'compile'
        elif len(parts) == 3:  # groupId:artifactId:version
            group_id = parts[0]
            artifact_id = parts[1]
            version = parts[2]
            scope = 'compile'
        else:
            return None
            
        return {
            'group_id': group_id,
            'artifact_id': artifact_id,
            'version': version,
            'scope': scope,
            'full_name': f"{group_id}:{artifact_id}"
        }
        
    def parse_dot_file(self, dot_file_path):
        """Parse a single dot file and extract dependency information from all submodules"""
        all_dependencies = {}
        all_edges = []
        all_conflicts = []
        
        with open(dot_file_path, 'r', encoding='utf-8') as file:
            content = file.read()
            
        # Split content by digraph sections to handle multiple submodules
        digraph_sections = re.findall(r'digraph\s+([^{]+)\s*{([^}]+)}', content, re.DOTALL)
        
        if not digraph_sections:
            # Fallback: treat entire content as one section
            digraph_sections = [('main', content)]
        
        for submodule_name, section_content in digraph_sections:
            dependencies, edges, conflicts = self._parse_section(section_content, submodule_name.strip())
            all_dependencies.update(dependencies)
            all_edges.extend(edges)
            all_conflicts.extend(conflicts)
        
        return all_dependencies, all_edges, all_conflicts
    
    def _parse_section(self, section_content, submodule_name):
        """Parse a single digraph section"""
        dependencies = {}
        edges = []
        conflicts = []
        
        # Extract all dependency nodes from edges (both sides of ->)
        edge_pattern = r'"([^"]+)"\s*->\s*"([^"]+)"'
        for match in re.finditer(edge_pattern, section_content):
            parent = match.group(1)
            child = match.group(2)
            
            # Parse parent dependency
            parent_parsed, parent_conflict_version = self.parse_maven_coordinates(parent)
            if parent_parsed:
                dependencies[parent] = parent_parsed
                
            # Parse child dependency and check for conflicts
            child_parsed, child_conflict_version = self.parse_maven_coordinates(child)
            if child_parsed:
                dependencies[child] = child_parsed
                
                # If this child has a conflict, record it
                if child_conflict_version:
                    conflict = {
                        'submodule': submodule_name,
                        'library_name': child_parsed['full_name'],
                        'version_selected': child_conflict_version,
                        'conflicting_version': child_parsed['version'],
                        'depth_selected': 0,  # Will be calculated later
                        'depth_conflicting': 1,  # Conflicting is deeper
                        'scope_selected': child_parsed['scope'],
                        'scope_conflicting': child_parsed['scope']
                    }
                    conflicts.append(conflict)
            
            # Only add edge if both dependencies were successfully parsed
            if parent_parsed and child_parsed:
                edges.append((parent, child))
        
        return dependencies, edges, conflicts
    
    def calculate_depths(self, dependencies, edges):
        """Calculate the depth of each dependency in the tree"""
        if not edges:
            # If no edges, all dependencies are at depth 0
            return {dep: 0 for dep in dependencies}
            
        # Create a directed graph
        G = nx.DiGraph()
        G.add_edges_from(edges)
        
        # Find root nodes (nodes with no incoming edges)
        root_nodes = [node for node in G.nodes() if G.in_degree(node) == 0]
        
        depths = {}
        for root in root_nodes:
            # Calculate shortest path from root to all reachable nodes
            try:
                paths = nx.single_source_shortest_path_length(G, root)
                for node, depth in paths.items():
                    if node not in depths or depth < depths[node]:
                        depths[node] = depth
            except:
                continue
        
        # Set depth 0 for any nodes not reachable from roots
        for node in dependencies:
            if node not in depths:
                depths[node] = 0
        
        return depths
    
    def find_version_conflicts(self, project_name, dependencies, depths, omitted_conflicts):
        """Find version conflicts within a single project"""
        # Group dependencies by library name (groupId:artifactId)
        library_versions = defaultdict(list)
        
        for node_id, dep_info in dependencies.items():
            library_name = dep_info['full_name']
            depth = depths.get(node_id, 0)
            
            library_versions[library_name].append({
                'node_id': node_id,
                'version': dep_info['version'],
                'depth': depth,
                'scope': dep_info['scope']
            })
        
        # Start with omitted conflicts (these are explicit conflicts)
        project_conflicts = []
        for conflict in omitted_conflicts:
            conflict['project'] = project_name
            project_conflicts.append(conflict)
        
        # Find additional conflicts (same library with different versions)
        for library_name, versions in library_versions.items():
            unique_versions = set(v['version'] for v in versions)
            if len(unique_versions) > 1:
                # Sort by depth to identify which version is selected (usually the shallowest)
                versions.sort(key=lambda x: x['depth'])
                selected_version = versions[0]
                
                for conflicting in versions[1:]:
                    if conflicting['version'] != selected_version['version']:
                        # Check if this conflict is already recorded from omitted conflicts
                        already_recorded = any(
                            c['library_name'] == library_name and 
                            c['version_selected'] == selected_version['version'] and
                            c['conflicting_version'] == conflicting['version']
                            for c in project_conflicts
                        )
                        
                        if not already_recorded:
                            conflict = {
                                'project': project_name,
                                'submodule': 'main',
                                'library_name': library_name,
                                'version_selected': selected_version['version'],
                                'conflicting_version': conflicting['version'],
                                'depth_selected': selected_version['depth'],
                                'depth_conflicting': conflicting['depth'],
                                'scope_selected': selected_version['scope'],
                                'scope_conflicting': conflicting['scope']
                            }
                            project_conflicts.append(conflict)
        
        return project_conflicts
    
    def export_project_to_csv(self, project_name, conflicts):
        """Export project conflicts to individual CSV file (even if empty)"""
        output_file = f"{project_name}_version_conflicts.csv"
        output_path = self.output_dir / output_file
        
        fieldnames = [
            'project',
            'submodule',
            'library_name',
            'version_selected',
            'conflicting_version', 
            'depth_selected',
            'depth_conflicting',
            'scope_selected',
            'scope_conflicting'
        ]
        
        with open(output_path, 'w', newline='', encoding='utf-8') as csvfile:
            writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
            writer.writeheader()
            
            for conflict in conflicts:
                writer.writerow(conflict)
    
    def analyze_all_projects(self):
        """Analyze all dot files in the directory"""
        dot_files = list(self.dot_files_dir.glob('*.dot'))
        
        if not dot_files:
            print("No .dot files found in the directory")
            return
        
        for dot_file in dot_files:
            project_name = dot_file.stem
            print(f"Analyzing project: {project_name}")
            
            try:
                dependencies, edges, omitted_conflicts = self.parse_dot_file(dot_file)
                print(f"  Found {len(dependencies)} dependencies and {len(edges)} edges")
                print(f"  Found {len(omitted_conflicts)} omitted conflicts")
                
                depths = self.calculate_depths(dependencies, edges)
                conflicts = self.find_version_conflicts(project_name, dependencies, depths, omitted_conflicts)
                
                # Keep track of all conflicts for summary
                self.all_conflicts.extend(conflicts)
                
                print(f"  Total conflicts found: {len(conflicts)}")
                
                # Always export CSV (even if empty)
                self.export_project_to_csv(project_name, conflicts)
                print(f"  Exported to: {self.output_dir}/{project_name}_version_conflicts.csv")
                
                # Debug: Print first few conflicts
                if conflicts:
                    print(f"  Example conflicts:")
                    for conflict in conflicts[:3]:
                        submodule_info = f" [{conflict.get('submodule', 'main')}]" if conflict.get('submodule') != 'main' else ""
                        print(f"    {conflict['library_name']}{submodule_info}: {conflict['version_selected']} vs {conflict['conflicting_version']}")
                else:
                    print(f"  No conflicts detected")
                
            except Exception as e:
                print(f"  Error analyzing {project_name}: {str(e)}")
                import traceback
                traceback.print_exc()
                
                # Still create empty CSV for failed analysis
                self.export_project_to_csv(project_name, [])
    
    def print_summary(self):
        """Print analysis summary"""
        total_conflicts = len(self.all_conflicts)
        unique_libraries = len(set(c['library_name'] for c in self.all_conflicts))
        projects_with_conflicts = len(set(c['project'] for c in self.all_conflicts))
        
        print(f"\n=== ANALYSIS SUMMARY ===")
        print(f"Total version conflicts found: {total_conflicts}")
        print(f"Unique libraries with conflicts: {unique_libraries}")
        print(f"Projects with conflicts: {projects_with_conflicts}")
        
        if self.all_conflicts:
            print(f"\nMost common conflicting libraries:")
            library_counts = defaultdict(int)
            for conflict in self.all_conflicts:
                library_counts[conflict['library_name']] += 1
            
            for library, count in sorted(library_counts.items(), key=lambda x: x[1], reverse=True)[:10]:
                print(f"  {library}: {count} conflicts")

def main():
    # Path to the dot files directory
    dot_files_dir = "../data/dot_files"
    output_dir = "output"
    
    # Check if directory exists
    if not os.path.exists(dot_files_dir):
        print(f"Error: Directory {dot_files_dir} not found!")
        return
    
    # Initialize analyzer
    analyzer = MavenDependencyAnalyzer(dot_files_dir, output_dir)
    
    # Perform analysis
    print("Starting Maven dependency version conflict analysis...")
    analyzer.analyze_all_projects()
    
    print(f"\nIndividual project CSV files exported to: {output_dir}/")
    
    # Print summary
    analyzer.print_summary()

if __name__ == "__main__":
    main()