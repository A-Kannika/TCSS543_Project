
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;

public class FordFulkerson {

    static int V; // Number of vertices in the graph
    static Map<String, Integer> nodeMapping; // Maps node names to numeric indices

    // BFS to find an augmenting path
    private static boolean bfs(int[][] residualGraph, int source, int sink, int[] parent) {
        boolean[] visited = new boolean[V];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited[source] = true;
        parent[source] = -1;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v = 0; v < V; v++) {
                // Check if the edge has residual capacity
                if (!visited[v] && residualGraph[u][v] > 0) {
                    queue.add(v);
                    visited[v] = true;
                    parent[v] = u;

                    // If sink is reached, return true
                    if (v == sink) {
                        return true;
                    }
                }
            }
        }
        return false; // No more augmenting paths
    }

    // Ford-Fulkerson algorithm to find the maximum flow
    public static int fordFulkerson(int[][] graph, int source, int sink) {
        int u, v;

        // Initialize residual graph
        int[][] residualGraph = new int[V][V];
        for (u = 0; u < V; u++) {
            for (v = 0; v < V; v++) {
                residualGraph[u][v] = graph[u][v];
            }
        }

        int[] parent = new int[V]; // Stores the path
        int maxFlow = 0; // Initialize the max flow

        // Augment the flow while there is an augmenting path
        while (bfs(residualGraph, source, sink, parent)) {
            // Find the bottleneck capacity (minimum residual capacity on the path)
            int pathFlow = Integer.MAX_VALUE;
            for (v = sink; v != source; v = parent[v]) {
                u = parent[v];
                pathFlow = Math.min(pathFlow, residualGraph[u][v]);
            }

            // Update the residual capacities of the edges and reverse edges
            for (v = sink; v != source; v = parent[v]) {
                u = parent[v];
                residualGraph[u][v] -= pathFlow;
                residualGraph[v][u] += pathFlow;
            }

            // Add path flow to overall flow
            maxFlow += pathFlow;
        }

        return maxFlow;
    }

    // Method to read the graph from a text-based file
    public static int readGraphFromFile(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));
        nodeMapping = new HashMap<>();
        int nodeIndex = 0;

        // Read edges and dynamically determine the size of the graph
        while (scanner.hasNext()) {
            String from = scanner.next();
            String to = scanner.next();
            int capacity = scanner.nextInt();

            // Map nodes to indices
            if (!nodeMapping.containsKey(from)) {
                nodeMapping.put(from, nodeIndex++);
            }
            if (!nodeMapping.containsKey(to)) {
                nodeMapping.put(to, nodeIndex++);
            }
        }

        scanner.close();

        // Set the number of vertices
        V = nodeIndex;

        // Reinitialize the scanner to populate the adjacency matrix
        int[][] graph = new int[V][V];
        scanner = new Scanner(new File(filePath));
        while (scanner.hasNext()) {
            String from = scanner.next();
            String to = scanner.next();
            int capacity = scanner.nextInt();

            // Populate the adjacency matrix
            int fromIndex = nodeMapping.get(from);
            int toIndex = nodeMapping.get(to);
            graph[fromIndex][toIndex] = capacity;
        }

        scanner.close();

        // Determine source and sink from mappings
        int source = nodeMapping.get("s");
        int sink = nodeMapping.get("t");

        // Call Ford-Fulkerson and print the result
//        System.out.println("The maximum possible flow is " + fordFulkerson(graph, source, sink));

        return fordFulkerson(graph, source, sink);
    }

    public static void main(String[] args) {
        // Specify the input file path
        String filePath = "graph1.txt";

        try {
            readGraphFromFile(filePath);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        }
    }
}
