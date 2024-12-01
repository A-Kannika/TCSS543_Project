/**
 * ScalingFordFulkerson.java
 *
 * Implementation of the Scaling Ford-Fulkerson algorithm to compute the maximum flow
 * in a flow network using a scaling parameter (Δ) to optimize path selection.
 *
 * Author: Kannika Armstrong
 *
 * This program leverages breadth-first search (BFS) to find augmenting paths
 * and maintains a residual graph for capacity adjustments. The scaling parameter
 * improves efficiency by prioritizing paths with higher capacities during the initial iterations.
 */

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class ScalingFordFulkerson {
    private SimpleGraph graph;   // The graph representing the flow network
    private int maxFlow;      // The computed maximum flow
    private double delta;        // Scaling parameter (Δ)
    private int sourcePos;       // Index of the source vertex in the graph
    private int sinkPos;         // Index of the sink vertex in the graph

    /**
     * Calculates the maximum flow in the given graph using the Scaling Ford-Fulkerson method.
     *
     * @param graph The flow network represented as a SimpleGraph
     * @return The computed maximum flow
     */
    public int calculateMaxFlow(SimpleGraph graph) {
        this.graph = graph;
        this.maxFlow = 0;

        // Find the positions of the source and sink vertices
        sourcePos = findVertex("s");
        sinkPos = findVertex("t");

        // Compute initial scaling parameter (Δ) as the least power of 2 ≥ max edge capacity
        delta = calculateInitialDelta();

        // Perform the scaling iterations
        while (delta >= 1) {
            // While there exists an augmenting path for the current Δ
            while (augmentPathBFS()) {
                // Update the residual graph and compute the bottleneck capacity
                double bottleneck = updateResidualGraph();
                maxFlow += bottleneck; // Add bottleneck capacity to total flow
            }
            delta /= 2;  // Halve the scaling parameter for the next iteration
        }

        return maxFlow;
    }

    /**
     * Calculates the initial value of Δ as the least power of 2 ≥ max edge capacity.
     *
     * @return The computed initial scaling parameter
     */
    private double calculateInitialDelta() {
        double maxCapacity = 0;

        // Iterate through all edges to find the maximum capacity
        for (Edge edge : graph.edgeList) {
            maxCapacity = Math.max(maxCapacity, (Double) edge.getData());
        }

        // Compute Δ as 2^ceil(log2(maxCapacity))
        return Math.pow(2, Math.ceil(Math.log(maxCapacity) / Math.log(2)));
    }

    /**
     * Finds the index of a vertex in the graph by its name.
     *
     * @param name The name of the vertex
     * @return The index of the vertex
     */
    private int findVertex(String name) {
        int index = 0;

        // Search for the vertex in the graph's vertex list
        for (Vertex vertex : graph.vertexList) {
            if (vertex.getName().equals(name)) {
                return index; // Return the index if found
            }
            index++;
        }

        // Throw an error if the vertex is not found
        throw new IllegalArgumentException("Vertex " + name + " not found.");
    }

    /**
     * Finds an augmenting path using BFS and marks vertices with their parent for path reconstruction.
     *
     * @return True if an augmenting path is found, false otherwise
     */
    private boolean augmentPathBFS() {
        boolean[] visited = new boolean[graph.numVertices()]; // Track visited vertices
        Queue<Vertex> queue = new LinkedList<>();
        Vertex source = graph.vertexList.get(sourcePos);

        visited[sourcePos] = true; // Mark the source as visited
        queue.add(source);
        source.setData(null); // Clear any previous parent reference

        // Perform BFS to find an augmenting path
        while (!queue.isEmpty()) {
            Vertex current = queue.poll();
            int currentIndex = graph.vertexList.indexOf(current);

            // Iterate through edges incident to the current vertex
            for (Iterator<Edge> it = graph.incidentEdges(current); it.hasNext(); ) {
                Edge edge = it.next();
                Vertex next = edge.getSecondEndpoint();
                int nextIndex = graph.vertexList.indexOf(next);

                // Check if the edge has sufficient residual capacity and the vertex is unvisited
                if (!visited[nextIndex] && (Double) edge.getData() >= delta) {
                    visited[nextIndex] = true;
                    next.setData(current); // Set the parent vertex

                    // Return true if we reach the sink
                    if (nextIndex == sinkPos) {
                        return true;
                    }

                    queue.add(next);
                }
            }
        }

        return false; // No augmenting path found
    }

    /**
     * Updates the residual graph along the augmenting path and returns the bottleneck capacity.
     *
     * @return The bottleneck capacity of the augmenting path
     */
    private double updateResidualGraph() {
        double bottleneck = Double.MAX_VALUE; // Initialize to infinity
        Vertex current = graph.vertexList.get(sinkPos);

        // Traverse the augmenting path to find the bottleneck capacity
        while (current != null && current.getData() != null) {
            Vertex parent = (Vertex) current.getData();
            Edge edge = findEdge(parent, current);
            bottleneck = Math.min(bottleneck, (Double) edge.getData());
            current = parent;
        }

        // Update residual capacities along the augmenting path
        current = graph.vertexList.get(sinkPos);
        while (current != null && current.getData() != null) {
            Vertex parent = (Vertex) current.getData();
            Edge forwardEdge = findEdge(parent, current);
            Edge reverseEdge = findEdge(current, parent);

            // Decrease forward edge capacity
            forwardEdge.setData((Double) forwardEdge.getData() - bottleneck);

            // Increase reverse edge capacity or create a reverse edge if not present
            if (reverseEdge != null) {
                reverseEdge.setData((Double) reverseEdge.getData() + bottleneck);
            } else {
                graph.insertEdge(current, parent, bottleneck, null);
            }

            current = parent;
        }

        return bottleneck; // Return the bottleneck capacity
    }

    /**
     * Finds an edge between two vertices.
     *
     * @param from The starting vertex
     * @param to The ending vertex
     * @return The edge if found, or null if not present
     */
    private Edge findEdge(Vertex from, Vertex to) {
        // Iterate through edges incident to the "from" vertex
        for (Iterator<Edge> it = graph.incidentEdges(from); it.hasNext(); ) {
            Edge edge = it.next();
            if (edge.getSecondEndpoint().equals(to)) {
                return edge; // Return the edge if found
            }
        }

        return null; // Edge not found
    }
}
