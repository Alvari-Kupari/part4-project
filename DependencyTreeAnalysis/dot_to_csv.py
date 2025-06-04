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
        # Check for omitted conflict pattern (omitted for conflict with ...)
        omitted_conflict_match = re.search(r'\(([^)]+) - omitted for conflict with ([^)]+)\)', coord_string)
        if omitted_conflict_match:
            base_coords = omitted_conflict_match.group(1)
            selected_version = omitted_conflict_match.group(2)  # This is the winning version
            parsed = self._parse_coordinates_string(base_coords)
            if parsed:
                conflicting_version = parsed['version']  # The version that was omitted
                return parsed, selected_version, conflicting_version, 'omitted_conflict'
            return None, selected_version, None, 'omitted_conflict'
        
        # Check for version managed pattern (version managed from ...)
        version_managed_match = re.search(r'\(version managed from ([^)]+)\)', coord_string)
        if version_managed_match:
            managed_from_text = version_managed_match.group(1)
            # Extract just the version number, removing any scope information
            conflicting_version = managed_from_text.split(';')[0].strip()
            # Remove the "(version managed from ...)" text from string to parse normal coords
            cleaned_str = re.sub(r'\(version managed from [^)]+\)', '', coord_string).strip()
            parsed = self._parse_coordinates_string(cleaned_str)
            if parsed:
                selected_version = parsed['version']  # The managed version
                return parsed, selected_version, conflicting_version, 'version_managed'
            return None, None, conflicting_version, 'version_managed'
        
        # Remove any other parentheses content (like (jar), (runtime), etc.)
        coord_string = re.sub(r'\s*\([^)]*\)', '', coord_string).strip()
        
        parsed = self._parse_coordinates_string(coord_string)
        return parsed, None, None, None

    def _parse_coordinates_string(self, coord_string):
        """Parse Maven coordinate string into components"""
        parts = coord_string.split(':')
        
        if len(parts) < 3:
            return None
            
        group_id = parts[0]
        artifact_id = parts[1]
        
        # Handle different Maven coordinate formats:
        # groupId:artifactId:version (3 parts)
        # groupId:artifactId:packaging:version (4 parts)
        # groupId:artifactId:packaging:version:scope (5 parts)
        # groupId:artifactId:packaging:classifier:version:scope (6 parts)
        
        if len(parts) == 3:
            # groupId:artifactId:version
            version = parts[2]
            scope = 'compile'
        elif len(parts) == 4:
            # groupId:artifactId:packaging:version
            version = parts[3]
            scope = 'compile'
        elif len(parts) == 5:
            # groupId:artifactId:packaging:version:scope
            version = parts[3]
            scope = parts[4]
        elif len(parts) == 6:
            # groupId:artifactId:packaging:classifier:version:scope
            version = parts[4]
            scope = parts[5]
        else:
            # For more than 6 parts, assume last is scope and second-to-last is version
            version = parts[-2]
            scope = parts[-1]
        
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
        
        # Store submodule-specific data for proper depth calculation
        submodule_data = {}
        
        for submodule_name, section_content in digraph_sections:
            dependencies, edges, conflicts = self._parse_section(section_content, submodule_name.strip())
            
            # Store per-submodule data
            submodule_data[submodule_name.strip()] = {
                'dependencies': dependencies,
                'edges': edges,
                'conflicts': conflicts,
                'all_nodes': list(dependencies.keys())  # Store all node strings for depth lookup
            }
            
            all_dependencies.update(dependencies)
            all_edges.extend(edges)
            all_conflicts.extend(conflicts)
        
        return all_dependencies, all_edges, all_conflicts, submodule_data
    
    def _parse_section(self, section_content, submodule_name):
        """Parse a single digraph section for omitted conflicts and version managed"""
        dependencies = {}
        edges = []
        conflicts = []
        
        # Extract all dependency nodes from edges (both sides of ->)
        edge_pattern = r'"([^"]+)"\s*->\s*"([^"]+)"'
        for match in re.finditer(edge_pattern, section_content):
            parent = match.group(1)
            child = match.group(2)
            
            # Parse parent dependency
            parent_parsed, parent_selected, parent_conflicting, parent_conflict_type = self.parse_maven_coordinates(parent)
            if parent_parsed:
                dependencies[parent] = parent_parsed
                
            # Parse child dependency and check for conflicts
            child_parsed, child_selected, child_conflicting, child_conflict_type = self.parse_maven_coordinates(child)
            if child_parsed:
                dependencies[child] = child_parsed
                
                # If this child has a conflict, record it
                if child_conflict_type:
                    conflict = {
                        'submodule': submodule_name,
                        'library_name': child_parsed['full_name'],
                        'version_selected': child_selected,
                        'conflicting_version': child_conflicting,
                        'scope_selected': child_parsed['scope'],
                        'scope_conflicting': child_parsed['scope'],
                        'parent_node': parent,
                        'child_node': child,
                        'conflict_type': child_conflict_type
                    }
                    conflicts.append(conflict)
            
            # Only add edge if both dependencies were successfully parsed
            if parent_parsed and child_parsed:
                edges.append((parent, child))
    
        # Filter out version managed conflicts where selected version == conflicting version
        filtered_conflicts = []
        for c in conflicts:
            if c['conflict_type'] == 'version_managed' and c['version_selected'] == c['conflicting_version']:
                continue
            filtered_conflicts.append(c)
        
        return dependencies, edges, filtered_conflicts

    def calculate_depths_for_submodule(self, dependencies, edges):
        """Calculate the depth of each dependency node in a single submodule"""
        if not edges:
            return {dep: 0 for dep in dependencies}
            
        G = nx.DiGraph()
        G.add_edges_from(edges)
        
        # Find root nodes (nodes with no incoming edges)
        root_nodes = [node for node in G.nodes() if G.in_degree(node) == 0]
        
        depths = {}
        
        # Calculate depths from each root
        for root in root_nodes:
            try:
                paths = nx.single_source_shortest_path_length(G, root)
                for node, depth in paths.items():
                    # Take minimum depth if node is reachable from multiple roots
                    if node not in depths or depth < depths[node]:
                        depths[node] = depth
            except Exception as e:
                print(f"Error calculating paths from root {root}: {e}")
                continue
        
        # Set depth 0 for any nodes not reached from roots
        for node in dependencies:
            if node not in depths:
                depths[node] = 0
        
        return depths
    
    def add_depth_to_conflicts(self, conflicts, submodule_data):
        """Add depth information to conflicts using correct submodule-specific depths"""
        for conflict in conflicts:
            child_node = conflict['child_node']
            submodule = conflict['submodule']
            
            # Get the submodule-specific data
            if submodule not in submodule_data:
                print(f"Warning: Submodule {submodule} not found in data")
                conflict['depth_selected'] = 0
                conflict['depth_conflicting'] = 0
                continue
                
            submodule_deps = submodule_data[submodule]['dependencies']
            submodule_edges = submodule_data[submodule]['edges']
            all_nodes = submodule_data[submodule]['all_nodes']
            
            # Calculate depths for this specific submodule
            submodule_depths = self.calculate_depths_for_submodule(submodule_deps, submodule_edges)
            
            if conflict['conflict_type'] == 'omitted_conflict':
                # For omitted conflicts:
                # - conflicting_version depth = depth of the omitted node (child_node)
                # - selected_version depth = depth of the winning node (same library, selected version, no conflict label)
                
                conflict['depth_conflicting'] = submodule_depths.get(child_node, 0)
                
                # Find the winning node (same library, selected version, no conflict indicators)
                selected_library = conflict['library_name']
                selected_version = conflict['version_selected']
                winning_depth = None
                winning_scope = None
                
                for node in all_nodes:
                    parsed_node, node_selected, node_conflicting, node_conflict_type = self.parse_maven_coordinates(node)
                    if (parsed_node and 
                        parsed_node['full_name'] == selected_library and 
                        parsed_node['version'] == selected_version and
                        node_conflict_type is None):  # No conflict indicators
                        node_depth = submodule_depths.get(node, 0)
                        if winning_depth is None or node_depth < winning_depth:
                            winning_depth = node_depth
                            winning_scope = parsed_node['scope']  # Get scope from winning node
                
                conflict['depth_selected'] = winning_depth if winning_depth is not None else 0
                conflict['scope_selected'] = winning_scope if winning_scope is not None else conflict['scope_conflicting']
                
            elif conflict['conflict_type'] == 'version_managed':
                # For version managed: both depths are the same (same node, just managed version)
                node_depth = submodule_depths.get(child_node, 0)
                conflict['depth_selected'] = node_depth
                conflict['depth_conflicting'] = node_depth
                # For version managed, the scope is the same for both (already set correctly)
            
            else:
                # Fallback for any other conflict types
                node_depth = submodule_depths.get(child_node, 0)
                conflict['depth_selected'] = node_depth
                conflict['depth_conflicting'] = node_depth
            
            # Remove internal keys that we don't need in the final output
            del conflict['parent_node']
            del conflict['child_node']
        
        return conflicts
    
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
            'scope_conflicting',
            'conflict_type'
        ]
        
        with open(output_path, 'w', newline='', encoding='utf-8') as csvfile:
            writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
            writer.writeheader()
            
            for conflict in conflicts:
                writer.writerow(conflict)
    
    def analyze_all_projects(self):
        dot_files = list(self.dot_files_dir.glob('*.dot'))
        
        if not dot_files:
            print("No .dot files found in the directory")
            return
        
        for dot_file in dot_files:
            project_name = dot_file.stem
            print(f"Analyzing project: {project_name}")
            
            try:
                dependencies, edges, conflicts, submodule_data = self.parse_dot_file(dot_file)
                print(f"  Found {len(dependencies)} dependencies and {len(edges)} edges")
                print(f"  Found {len(conflicts)} version conflicts")
                
                # Add depth information using submodule-specific calculations
                conflicts = self.add_depth_to_conflicts(conflicts, submodule_data)
                
                for conflict in conflicts:
                    conflict['project'] = project_name
                
                self.all_conflicts.extend(conflicts)
                
                print(f"  Total conflicts found (after filtering): {len(conflicts)}")
                
                self.export_project_to_csv(project_name, conflicts)
                print(f"  Exported to: {self.output_dir}/{project_name}_version_conflicts.csv")
                
                if conflicts:
                    print(f"  Example conflicts:")
                    for conflict in conflicts[:3]:
                        submodule_info = f" [{conflict.get('submodule', 'main')}]" if conflict.get('submodule') != 'main' else ""
                        print(f"    {conflict['library_name']}{submodule_info}: {conflict['version_selected']} (depth {conflict['depth_selected']}) vs {conflict['conflicting_version']} (depth {conflict['depth_conflicting']}) [{conflict['conflict_type']}]")
                else:
                    print(f"  No conflicts detected")
                
            except Exception as e:
                print(f"  Error analyzing {project_name}: {str(e)}")
                import traceback
                traceback.print_exc()
                
                self.export_project_to_csv(project_name, [])
    
    def print_summary(self):
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
    dot_files_dir = "../data/dot_files"
    output_dir = "output"
    
    if not os.path.exists(dot_files_dir):
        print(f"Error: Directory {dot_files_dir} not found!")
        return
    
    analyzer = MavenDependencyAnalyzer(dot_files_dir, output_dir)
    
    print("Starting Maven dependency version conflict analysis...")
    analyzer.analyze_all_projects()
    
    print(f"\nIndividual project CSV files exported to: {output_dir}/")
    
    analyzer.print_summary()

if __name__ == "__main__":
    main()
