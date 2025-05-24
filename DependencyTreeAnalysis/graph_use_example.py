import pydot




# Load graphs
graphs = pydot.graph_from_dot_file("C:/Users/Alvari/Documents/UNI/softeng_700/part4-project/data/dot_files/analysis-ik.dot")


# Output how many graphs were loaded and their names
print(f"Number of graphs: {len(graphs)}")
for i, g in enumerate(graphs):
    print(f"Graph {i+1} name: {g.get_name()}")


# Get the first graph
first_graph = graphs[0]

# Extract node names from edges
nodes = set()
for edge in first_graph.get_edges():
    nodes.add(edge.get_source())
    nodes.add(edge.get_destination())

print("Nodes in the first graph:")
for node in nodes:
    print(node)
