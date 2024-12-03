import java.util.Iterator;

/*
 * PreflowPush algorithm implementation for finding maximum flow in a flow network.
 * This class uses the Preflow-Push algorithm to calculate the maximum flow between a source and a sink in a flow network represented as a graph.
 */

public class PreFlowPush {
    double[] excess;       // Array to store the excess flow at each vertex
    int sourceIDX = -1;    // Index of the source vertex
    int sinkIDX = -1;      // Index of the sink vertex
    int maxFlow = 0;       // Variable to store the total flow in the network
    SimpleGraph graph;     // The graph representing the flow network

    // Main function to compute the maximum flow using the Preflow-Push algorithm
    public int preflow(SimpleGraph g) {
        graph = g;

        // Iterators for traversing vertices and edges
        Iterator<Vertex> i;
        Iterator<Vertex> j;
        Iterator<Edge> k;
        Iterator<Edge> l;

        Vertex v1, v2, v;    // Temporary vertex variables
        Edge e1, e2;         // Temporary edge variables

        int index = 0;       // Index for vertices array
        int index2 = -10;    // Temporary index for excess flow
        int count = 0;       // Counter for detecting if any flow was pushed
        boolean check = false; // Flag to check if flow is pushed
        Double maxex = (double) -1; // Temporary variable for maximum excess flow

        // Find the source ("s") and sink ("t") vertices
        findvertex("t");
        findvertex("s");

        excess = new double[graph.numVertices()];  // Initialize excess array

        // Initialize vertex heights and excess flow values
        for (j = graph.vertices(); j.hasNext(); ) {
            v1 = j.next();
            v1.setData((double) 0); // Set initial height of all vertices to 0
            excess[index] = 0;      // Set excess flow to 0
            index++;
        }

        // Set the source vertex's height to the number of vertices in the graph
        index = 0;
        v1 = graph.vertexList.getFirst();
        v1.setData((double) graph.numVertices());

        // Set initial flow values on the edges and excess flow for the source
        setflows();
        setexcess();

        // Main loop of the Preflow-Push algorithm
        while (true) {
            // Find a vertex with excess flow
            for (i = graph.vertices(); i.hasNext(); ) {
                v = i.next();
                if (excess[index] > 0 && !(v.getName().equals("t"))) {
                    v1 = v;
                    index2 = index;
                    count++;
                    break;
                }
                index++;
            }
            index = 0;

            // If no vertex with excess flow is found, break the loop
            if (count == 0) {
                break;
            }
            count = 0;

            // Try pushing flow through incident edges
            for (k = graph.incidentEdges(v1); k.hasNext(); ) {
                e1 = k.next();        // Get the edge from v1
                v2 = e1.getSecondEndpoint(); // Get the neighboring vertex
                if ((Double) e1.getName() > 0 && (Double) v1.getData() > (Double) v2.getData()) {
                    int index3 = findedge(v2, v1);  // Find the reverse edge
                    Push(excess[index2], (Double) e1.getName(), graph.edgeList.indexOf(e1), index3, index2, graph.vertexList.indexOf(v2));  // Push flow
                    check = true;
                    break;  // Exit the loop once flow is pushed
                }
            }

            // If no flow was pushed, relabel the vertex
            if (!check) {
                Relabel(graph.vertexList.indexOf(v1));
            }
            check = false;
        }

        // Calculate the maximum flow by summing flows on edges incident to the sink
        for (l = graph.incidentEdges(graph.vertexList.get(sinkIDX)); l.hasNext(); ) {
            e2 = l.next();
            maxFlow = (int) (maxFlow + (Double) e2.getName());  // Add flow from the sink's incident edges to the total max flow
        }
        return maxFlow;  // Return the total max flow
    }

    // Relabel a vertex by increasing its height
    public void Relabel(int ind) {
        Vertex v = graph.vertexList.get(ind);
        v.setData((Double) v.getData() + 1);  // Increase the vertex's height
    }

    // Push flow from one vertex to another along an edge
    public void Push(Double ex, Double cap, int ind1, int ind2, int ind3, int ind4) {
        Double d = Math.min(ex, cap);  // Calculate the flow to push (minimum of excess flow and edge capacity)
        Edge e1 = graph.edgeList.get(ind1);  // Get the edge from vertex 1 to vertex 2
        Edge e2 = graph.edgeList.get(ind2);  // Get the reverse edge from vertex 2 to vertex 1

        // Update the flow values on the forward and reverse edges
        e1.setName((Double) e1.getName() - d);  // Subtract the flow from the forward edge
        e2.setName((Double) e2.getName() + d);  // Add the flow to the reverse edge

        // Update the excess flow values at both vertices
        excess[ind3] = excess[ind3] - d;
        excess[ind4] = excess[ind4] + d;
    }

    // Find the index of a vertex (either source "s" or sink "t")
    public void findvertex(String data) {
        Iterator<Vertex> i;
        int k = 0;
        Vertex temp;

        // Traverse all vertices to find the one matching the given name
        for (i = graph.vertices(); i.hasNext(); k++) {
            temp = i.next();
            if (temp.getName().toString().equals(data)) {
                if (data.equals("t")) {
                    sinkIDX = k;  // If it's the sink, store the sink index
                    break;
                } else if (data.equals("s")) {
                    sourceIDX = k;  // If it's the source, store the source index
                    break;
                }
            }
        }
    }

    // Set initial flow values on all edges based on their capacities
    public void setflows() {
        Vertex start, end;
        Edge e, e2;
        Iterator<Vertex> i;
        Iterator<Edge> j;
        double temp;
        int ind;

        // Set the initial flow value on each edge based on its capacity
        for (i = graph.vertices(); i.hasNext(); ) {
            start = i.next();
            for (j = graph.incidentEdges(start); j.hasNext(); ) {
                e = j.next();
                e.setName(e.getData());  // Set the flow to the edge's capacity
            }
        }

        // Set the reverse edge's flow to the same capacity as the forward edge
        for (j = graph.incidentEdges(graph.vertexList.getFirst()); j.hasNext(); ) {
            e = j.next();
            temp = (Double) e.getName();
            e.setName((double) 0);  // Set flow to 0 on the forward edge
            end = e.getSecondEndpoint();  // Get the neighboring vertex
            ind = findedge(end, graph.vertexList.getFirst());  // Find the reverse edge
            e2 = graph.edgeList.get(ind);  // Get the reverse edge
            e2.setName(temp);  // Set the reverse edge's flow to the capacity
        }
    }

    // Set the excess flow values for all vertices connected to the source
    public void setexcess() {
        double temp = (double) 0;
        Iterator<Edge> j;
        Edge e;
        Vertex end;

        // Set excess flow for each vertex connected to the source
        for (j = graph.incidentEdges(graph.vertexList.getFirst()); j.hasNext(); ) {
            e = j.next();
            end = e.getSecondEndpoint();
            int ind = graph.vertexList.indexOf(end);  // Find the neighbor vertex's index
            excess[ind] = (Double) e.getData();  // Set the excess flow for the neighbor
            temp = temp + (Double) e.getData();  // Accumulate the flow for excess calculation
        }
        excess[0] = -temp;  // Set the source's excess flow to the negative of the accumulated flow
    }

    // Find the index of an edge between two vertices (from start to end)
    public int findedge(Vertex st, Vertex en) {
        Iterator<Edge> j;
        Edge e;

        // Traverse the edges incident to the start vertex and find the edge connecting start to end
        for (j = graph.incidentEdges(st); j.hasNext(); ) {
            e = j.next();
            if (e.getSecondEndpoint() == en) {
                return graph.edgeList.indexOf(e);  // Return the index of the found edge
            }
        }
        return -1;  // Return -1 if no such edge is found
    }
}

//// First version
////import java.io.*;
////import java.util.*;
////
////class PreFlowPush {
////    private int vertices;
////    private List<PFPEdge> edges;
////    private int[] height;
////    private int[] excess;
////
////    public PreFlowPush(int vertices) {
////        this.vertices = vertices;
////        this.edges = new ArrayList<>();
////        this.height = new int[vertices];
////        this.excess = new int[vertices];
////    }
////
////    // Add edge to the graph
////    public void addEdge(int u, int v, int capacity) {
////        edges.add(new PFPEdge(u, v, capacity));
////        edges.add(new PFPEdge(v, u, 0)); // Reverse edge
////    }
////
////    // Push flow from vertex u
////    private boolean pushFlow(int u) {
////        for (PFPEdge edge : edges) {
////            if (edge.u == u && edge.flow < edge.capacity) {
////                int flowDelta = Math.min(excess[u], edge.capacity - edge.flow);
////
////                if (flowDelta > 0 && height[u] > height[edge.v]) {
////                    edge.flow += flowDelta;
////                    excess[u] -= flowDelta;
////                    excess[edge.v] += flowDelta;
////
////                    // Update reverse edge
////                    for (PFPEdge reverseEdge : edges) {
////                        if (reverseEdge.u == edge.v && reverseEdge.v == u) {
////                            reverseEdge.flow -= flowDelta;
////                            break;
////                        }
////                    }
////                    return true;
////                }
////            }
////        }
////        return false;
////    }
////
////    // Relabel vertex u
////    private void relabel(int u) {
////        int minHeight = Integer.MAX_VALUE;
////        for (PFPEdge edge : edges) {
////            if (edge.u == u && edge.flow < edge.capacity) {
////                minHeight = Math.min(minHeight, height[edge.v]);
////            }
////        }
////        if (minHeight < Integer.MAX_VALUE) {
////            height[u] = minHeight + 1; // Increment height
////        }
////    }
////
////    // Find an active vertex with positive excess flow
////    private int findActiveNode(int source, int sink) {
////        for (int i = 0; i < vertices; i++) {
////            if (i != source && i != sink && excess[i] > 0) {
////                return i;
////            }
////        }
////        return -1;
////    }
////
////    // Main method to compute the maximum flow
////    public int computeMaxFlow(int source, int sink) {
////        // Initialize preflow
////        height[source] = vertices; // Set source height to the number of vertices
////        for (PFPEdge edge : edges) {
////            if (edge.u == source) {
////                edge.flow = edge.capacity; // Push full capacity
////                excess[edge.v] += edge.capacity;
////
////                // Update reverse edge
////                for (PFPEdge reverseEdge : edges) {
////                    if (reverseEdge.u == edge.v && reverseEdge.v == source) {
////                        reverseEdge.flow -= edge.capacity;
////                        break;
////                    }
////                }
////            }
////        }
////
////        // Process active nodes
////        while (true) {
////            int u = findActiveNode(source, sink);
////            if (u == -1) break; // No active nodes left
////
////            if (!pushFlow(u)) {
////                relabel(u);
////            }
////        }
////
////        // Total flow is the excess at the sink node
////        return excess[sink];
////    }
////
////    // Debugging method to print the state of the algorithm
////    private void debugState() {
////        System.out.println("Heights: " + Arrays.toString(height));
////        System.out.println("Excess: " + Arrays.toString(excess));
////        System.out.println("Edge flows:");
////        for (PFPEdge edge : edges) {
////            System.out.println("Edge (" + edge.u + " -> " + edge.v + ") Flow/Cap: " + edge.flow + "/" + edge.capacity);
////        }
////    }
////
////    // Read input graph and run the algorithm
////    public static int runPreFlowPush(String filepath) throws IOException {
////        BufferedReader br = new BufferedReader(new FileReader(filepath));
////        Map<String, Integer> nodeIndex = new HashMap<>();
////        List<int[]> edgeData = new ArrayList<>();
////        int nodeCounter = 0;
////
////        String line;
////        while ((line = br.readLine()) != null) {
////            String[] parts = line.trim().split("\\s+"); // Space-separated input
////            if (parts.length != 3) {
////                System.err.println("Invalid line: " + line);
////                continue;
////            }
////
////            String from = parts[0];
////            String to = parts[1];
////            int capacity = Integer.parseInt(parts[2]);
////
////            nodeIndex.putIfAbsent(from, nodeCounter++);
////            nodeIndex.putIfAbsent(to, nodeCounter++);
////
////            edgeData.add(new int[]{
////                    nodeIndex.get(from),
////                    nodeIndex.get(to),
////                    capacity
////            });
////        }
////        br.close();
////
////        if (!nodeIndex.containsKey("s") || !nodeIndex.containsKey("t")) {
////            throw new IllegalArgumentException("Source 's' or sink 't' node is missing in the input file.");
////        }
////
////        PreFlowPush graph = new PreFlowPush(nodeCounter);
////
////        for (int[] edge : edgeData) {
////            graph.addEdge(edge[0], edge[1], edge[2]);
////        }
////
////        int source = nodeIndex.get("s");
////        int sink = nodeIndex.get("t");
////
////        return graph.computeMaxFlow(source, sink);
////    }
////
////    public static void main(String[] args) throws IOException {
////        String filepath = "src/InputGraph/graphGenerationCode/Bipartite/g1.txt"; // Update with your actual file path
////        int maxFlow = runPreFlowPush(filepath);
////        System.out.println("Maximum flow is " + maxFlow);
////    }
////}
////
////// Class representing an edge in the graph
////class PFPEdge {
////    int flow;
////    int capacity;
////    int u, v;
////
////    PFPEdge(int u, int v, int capacity) {
////        this.u = u;
////        this.v = v;
////        this.capacity = capacity;
////        this.flow = 0;
////    }
////}
//
