import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class PreFlowPush {
    double[] heights;    // Array storing the height (level) of each vertex
    static double[] excess;     // Array storing the excess flow at each vertex
    static int sourceIDX = -1;  // Index of the source vertex
    static int sinkIDX = -1;    // Index of the sink vertex
    static int maxFlow = 0;  // Variable to store the total flow value
    static SimpleGraph graph;   // Graph object containing the vertices and edges

    // Main Preflow-Push algorithm to compute the maximum flow
    public static int runPreFlowPush(SimpleGraph g) {
        graph = g;

        // Find source and sink vertices
        findVertex("s");  // Source
        findVertex("t");  // Sink

        // Initialize the excess flow array
        excess = new double[graph.numVertices()];

        // Initialize vertex data and excess flow
        Iterator<Vertex> i;
        int index = 0;
        for (i = graph.vertices(); i.hasNext(); ) {
            Vertex v1 = i.next();
            v1.setData((double) 0);  // Set vertex height to 0
            excess[index] = 0;  // Set excess flow for the vertex
            index++;
        }

        // Set source vertex height and initial flow
        index = 0;
        Vertex v1 = graph.vertexList.getFirst();  // Source vertex
        v1.setData((double) graph.numVertices());  // Source height = numVertices
        setFlows();  // Set initial flows on all edges
        setExcess();  // Set initial excess flow values

        // Start preflow-push algorithm
        // No augmenting path found, exit the loop
        while (bfs()) {
            // Run BFS to check if there's a valid augmenting path from source to sink

            // Process each vertex with excess flow
            for (i = graph.vertices(); i.hasNext(); ) {
                Vertex v = i.next();
                if (excess[graph.vertexList.indexOf(v)] > 0 && !v.getName().equals("t")) {
                    // Push flow or relabel if excess flow is found
                    boolean pushed = false;
                    for (Iterator<Edge> it = graph.incidentEdges(v); it.hasNext(); ) {
                        Edge e = it.next();
                        if ((Double) e.getName() > 0 && (Double) v.getData() > (Double) e.getSecondEndpoint().getData()) {
                            int reverseEdgeIndex = findEdge(e.getSecondEndpoint(), v);
                            Push(excess[graph.vertexList.indexOf(v)], (Double) e.getName(), graph.edgeList.indexOf(e), reverseEdgeIndex, graph.vertexList.indexOf(v), graph.vertexList.indexOf(e.getSecondEndpoint()));
                            pushed = true;
                            break;
                        }
                    }

                    // If no flow was pushed, relabel the vertex
                    if (!pushed) {
                        Relabel(graph.vertexList.indexOf(v));
                    }
                }
            }
        }

        // Calculate the maximum flow from the sink
        int maxFlow = 0;
        Iterator<Edge> l = graph.incidentEdges(graph.vertexList.get(sinkIDX));
        while (l.hasNext()) {
            Edge e2 = l.next();
            maxFlow += (Double) e2.getName();
        }

        return maxFlow;
    }

    // BFS to compute the level graph (heights of the vertices)
    public static boolean bfs() {
        // Create an array to store the levels (heights) of vertices
        int[] level = new int[graph.numVertices()];
        for (int i = 0; i < level.length; i++) {
            level[i] = -1;  // Initialize all levels to -1 (unreachable)
        }

        // Start BFS from the source vertex
        level[sourceIDX] = 0;  // Source has level 0
        Queue<Vertex> queue = new LinkedList<>();
        queue.add(graph.vertexList.get(sourceIDX));  // Add source to the queue

        while (!queue.isEmpty()) {
            Vertex u = queue.poll();  // Dequeue a vertex
            Iterator<Edge> incidentEdges = graph.incidentEdges(u);  // Get all incident edges of u

            // Check all adjacent vertices through outgoing edges
            while (incidentEdges.hasNext()) {
                Edge e = incidentEdges.next();
                Vertex v = e.getSecondEndpoint();  // Get the neighbor vertex

                // If the vertex v is reachable (level is not assigned) and the edge has capacity
                if (level[graph.vertexList.indexOf(v)] == -1 && (Double) e.getName() > 0) {
                    level[graph.vertexList.indexOf(v)] = level[graph.vertexList.indexOf(u)] + 1;  // Set level
                    queue.add(v);  // Add vertex v to the queue
                }
            }
        }

        // If the sink has been assigned a level, then we can reach it
        return level[sinkIDX] != -1;
    }

    // Relabel a vertex by incrementing its height
    public static void Relabel(int ind) {
        Vertex v = graph.vertexList.get(ind);  // Get the vertex by index
        v.setData((Double) v.getData() + 1);  // Increment the vertex height
    }

    // Push flow from one vertex to another along an edge
    public static void Push(Double ex, Double cap, int ind1, int ind2, int ind3, int ind4) {
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

    // Find the index of a vertex in the graph based on its name ("s" or "t")
    public static void findVertex(String data) {
        Iterator<Vertex> i;  // Iterator for traversing vertices
        int k = 0;  // Index counter
        Vertex temp;

        // Traverse the vertices to find the one matching the name
        for (i = graph.vertices(); i.hasNext(); k++) {
            temp = i.next();
            if (temp.getName().toString().equals(data)) {
                if (data.equals("t")) {
                    sinkIDX = k;  // If it's the sink vertex, set the sink index
                    break;
                } else if (data.equals("s")) {
                    sourceIDX = k;  // If it's the source vertex, set the source index
                    break;
                }
            }
        }
    }

    // Set the flow values on all edges based on their capacities
    public static void setFlows() {
        Vertex start, end;  // Temporary vertex variables
        Edge e, e2;          // Temporary edge variables
        Iterator<Vertex> i;  // Iterator for traversing vertices
        Iterator<Edge> j;    // Iterator for traversing edges

        double temp;         // Temporary variable for edge capacity
        int ind;             // Edge index

        // Set flow values on all edges based on their capacities
        for (i = graph.vertices(); i.hasNext(); ) {
            start = i.next();
            for (j = graph.incidentEdges(start); j.hasNext(); ) {
                e = j.next();
                e.setName(e.getData());  // Set the initial flow to the edge's capacity
            }
        }

        // Reset flow values on edges incident to the source vertex
        for (j = graph.incidentEdges(graph.vertexList.getFirst()); j.hasNext(); ) {
            e = j.next();
            temp = (Double) e.getName();  // Store the edge's capacity
            e.setName((double) 0);        // Set flow to 0 on the forward edge
            end = e.getSecondEndpoint();  // Get the neighbor vertex
            ind = findEdge(end, graph.vertexList.getFirst());  // Find the reverse edge
            e2 = graph.edgeList.get(ind);  // Get the reverse edge
            e2.setName(temp);  // Set the reverse edge's flow to the capacity
        }
    }

    // Set the initial excess flow values based on the source vertex's outgoing edges
    public static void setExcess() {
        double temp = (double) 0;  // Temporary variable for excess flow calculation
        Iterator<Edge> j;  // Iterator for traversing edges
        Edge e;             // Temporary edge variable
        Vertex end;         // Temporary vertex variable

        // Traverse the edges incident to the source vertex
        for (j = graph.incidentEdges(graph.vertexList.getFirst()); j.hasNext(); ) {
            e = j.next();
            temp = (Double) e.getName();  // Get the edge's capacity (temporary excess)
            end = e.getSecondEndpoint();  // Get the adjacent vertex
            excess[graph.vertexList.indexOf(end)] = temp;  // Set the excess flow at the adjacent vertex
        }
    }

    // Find the reverse edge index in the graph
    public static int findEdge(Vertex v1, Vertex v2) {
        int k = 0;
        Iterator<Edge> i;  // Iterator for traversing edges

        // Traverse all edges to find the reverse edge
        for (i = graph.incidentEdges(v1); i.hasNext(); k++) {
            if (i.next().getSecondEndpoint().equals(v2)) {
                return k;  // Return the index of the reverse edge
            }
        }

        return -1;  // Return -1 if no reverse edge is found
    }
}

// First version
//import java.io.*;
//import java.util.*;
//
//class PreFlowPush {
//    private int vertices;
//    private List<PFPEdge> edges;
//    private int[] height;
//    private int[] excess;
//
//    public PreFlowPush(int vertices) {
//        this.vertices = vertices;
//        this.edges = new ArrayList<>();
//        this.height = new int[vertices];
//        this.excess = new int[vertices];
//    }
//
//    // Add edge to the graph
//    public void addEdge(int u, int v, int capacity) {
//        edges.add(new PFPEdge(u, v, capacity));
//        edges.add(new PFPEdge(v, u, 0)); // Reverse edge
//    }
//
//    // Push flow from vertex u
//    private boolean pushFlow(int u) {
//        for (PFPEdge edge : edges) {
//            if (edge.u == u && edge.flow < edge.capacity) {
//                int flowDelta = Math.min(excess[u], edge.capacity - edge.flow);
//
//                if (flowDelta > 0 && height[u] > height[edge.v]) {
//                    edge.flow += flowDelta;
//                    excess[u] -= flowDelta;
//                    excess[edge.v] += flowDelta;
//
//                    // Update reverse edge
//                    for (PFPEdge reverseEdge : edges) {
//                        if (reverseEdge.u == edge.v && reverseEdge.v == u) {
//                            reverseEdge.flow -= flowDelta;
//                            break;
//                        }
//                    }
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    // Relabel vertex u
//    private void relabel(int u) {
//        int minHeight = Integer.MAX_VALUE;
//        for (PFPEdge edge : edges) {
//            if (edge.u == u && edge.flow < edge.capacity) {
//                minHeight = Math.min(minHeight, height[edge.v]);
//            }
//        }
//        if (minHeight < Integer.MAX_VALUE) {
//            height[u] = minHeight + 1; // Increment height
//        }
//    }
//
//    // Find an active vertex with positive excess flow
//    private int findActiveNode(int source, int sink) {
//        for (int i = 0; i < vertices; i++) {
//            if (i != source && i != sink && excess[i] > 0) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    // Main method to compute the maximum flow
//    public int computeMaxFlow(int source, int sink) {
//        // Initialize preflow
//        height[source] = vertices; // Set source height to the number of vertices
//        for (PFPEdge edge : edges) {
//            if (edge.u == source) {
//                edge.flow = edge.capacity; // Push full capacity
//                excess[edge.v] += edge.capacity;
//
//                // Update reverse edge
//                for (PFPEdge reverseEdge : edges) {
//                    if (reverseEdge.u == edge.v && reverseEdge.v == source) {
//                        reverseEdge.flow -= edge.capacity;
//                        break;
//                    }
//                }
//            }
//        }
//
//        // Process active nodes
//        while (true) {
//            int u = findActiveNode(source, sink);
//            if (u == -1) break; // No active nodes left
//
//            if (!pushFlow(u)) {
//                relabel(u);
//            }
//        }
//
//        // Total flow is the excess at the sink node
//        return excess[sink];
//    }
//
//    // Debugging method to print the state of the algorithm
//    private void debugState() {
//        System.out.println("Heights: " + Arrays.toString(height));
//        System.out.println("Excess: " + Arrays.toString(excess));
//        System.out.println("Edge flows:");
//        for (PFPEdge edge : edges) {
//            System.out.println("Edge (" + edge.u + " -> " + edge.v + ") Flow/Cap: " + edge.flow + "/" + edge.capacity);
//        }
//    }
//
//    // Read input graph and run the algorithm
//    public static int runPreFlowPush(String filepath) throws IOException {
//        BufferedReader br = new BufferedReader(new FileReader(filepath));
//        Map<String, Integer> nodeIndex = new HashMap<>();
//        List<int[]> edgeData = new ArrayList<>();
//        int nodeCounter = 0;
//
//        String line;
//        while ((line = br.readLine()) != null) {
//            String[] parts = line.trim().split("\\s+"); // Space-separated input
//            if (parts.length != 3) {
//                System.err.println("Invalid line: " + line);
//                continue;
//            }
//
//            String from = parts[0];
//            String to = parts[1];
//            int capacity = Integer.parseInt(parts[2]);
//
//            nodeIndex.putIfAbsent(from, nodeCounter++);
//            nodeIndex.putIfAbsent(to, nodeCounter++);
//
//            edgeData.add(new int[]{
//                    nodeIndex.get(from),
//                    nodeIndex.get(to),
//                    capacity
//            });
//        }
//        br.close();
//
//        if (!nodeIndex.containsKey("s") || !nodeIndex.containsKey("t")) {
//            throw new IllegalArgumentException("Source 's' or sink 't' node is missing in the input file.");
//        }
//
//        PreFlowPush graph = new PreFlowPush(nodeCounter);
//
//        for (int[] edge : edgeData) {
//            graph.addEdge(edge[0], edge[1], edge[2]);
//        }
//
//        int source = nodeIndex.get("s");
//        int sink = nodeIndex.get("t");
//
//        return graph.computeMaxFlow(source, sink);
//    }
//
//    public static void main(String[] args) throws IOException {
//        String filepath = "src/InputGraph/graphGenerationCode/Bipartite/g1.txt"; // Update with your actual file path
//        int maxFlow = runPreFlowPush(filepath);
//        System.out.println("Maximum flow is " + maxFlow);
//    }
//}
//
//// Class representing an edge in the graph
//class PFPEdge {
//    int flow;
//    int capacity;
//    int u, v;
//
//    PFPEdge(int u, int v, int capacity) {
//        this.u = u;
//        this.v = v;
//        this.capacity = capacity;
//        this.flow = 0;
//    }
//}

