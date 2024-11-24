import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class ScalingFordFulkerson {
    private SimpleGraph graph;   // The graph
    private double maxFlow;  // The total max flow
    private double delta;    // Scaling parameter (Δ)
    private int sourcePos; // Position of the source vertex
    private int sinkPos;   // Position of the sink vertex

    public double calculateMaxFlow(SimpleGraph graph) {
        this.graph = graph;
        this.maxFlow = 0;

        // Initialize source and sink
        sourcePos = findVertex("s");
        sinkPos = findVertex("t");

        // Compute initial Δ as the least power of 2 greater than or equal to max capacity
        delta = calculateInitialDelta();

        // Scaling loop
        while (delta >= 1) {
            while (augmentPathBFS()) {
                double bottleneck = updateResidualGraph();
                maxFlow += bottleneck;
            }
            delta /= 2;  // Halve the scaling parameter
        }

        return maxFlow;
    }

    // Calculate the least power of 2 ≥ max capacity of any edge
    private double calculateInitialDelta() {
        double maxCapacity = 0;
        for (Edge edge : graph.edgeList) {
            maxCapacity = Math.max(maxCapacity, (Double) edge.getData());
        }
        return Math.pow(2, Math.ceil(Math.log(maxCapacity) / Math.log(2)));
    }

    // Find the index of a vertex by its name
    private int findVertex(String name) {
        int index = 0;
        for (Vertex vertex : graph.vertexList) {
            if (vertex.getName().equals(name)) {
                return index;
            }
            index++;
        }
        throw new IllegalArgumentException("Vertex " + name + " not found.");
    }

    // Find an augmenting path using BFS and mark vertices
    private boolean augmentPathBFS() {
        boolean[] visited = new boolean[graph.numVertices()];
        Queue<Vertex> queue = new LinkedList<>();
        Vertex source = graph.vertexList.get(sourcePos);

        visited[sourcePos] = true;
        queue.add(source);
        source.setData(null); // Clear any previous parent reference

        while (!queue.isEmpty()) {
            Vertex current = queue.poll();
            int currentIndex = graph.vertexList.indexOf(current);

            for (Iterator<Edge> it = graph.incidentEdges(current); it.hasNext(); ) {
                Edge edge = it.next();
                Vertex next = edge.getSecondEndpoint();
                int nextIndex = graph.vertexList.indexOf(next);

                // Check if the edge has sufficient residual capacity and the vertex is unvisited
                if (!visited[nextIndex] && (Double) edge.getData() >= delta) {
                    visited[nextIndex] = true;
                    next.setData(current); // Set parent for path reconstruction

                    // If we reached the sink, return true
                    if (nextIndex == sinkPos) {
                        return true;
                    }

                    queue.add(next);
                }
            }
        }

        return false; // No path to the sink was found
    }

    // Update the residual graph along the augmenting path
    private double updateResidualGraph() {
        double bottleneck = Double.MAX_VALUE;
        Vertex current = graph.vertexList.get(sinkPos);

        // Find bottleneck capacity
        while (current != null && current.getData() != null) {
            Vertex parent = (Vertex) current.getData();
            Edge edge = findEdge(parent, current);
            bottleneck = Math.min(bottleneck, (Double) edge.getData());
            current = parent;
        }

        // Update residual capacities
        current = graph.vertexList.get(sinkPos);
        while (current != null && current.getData() != null) {
            Vertex parent = (Vertex) current.getData();
            Edge forwardEdge = findEdge(parent, current);
            Edge reverseEdge = findEdge(current, parent);

            // Decrease forward edge capacity
            forwardEdge.setData((Double) forwardEdge.getData() - bottleneck);

            // Increase reverse edge capacity
            if (reverseEdge != null) {
                reverseEdge.setData((Double) reverseEdge.getData() + bottleneck);
            } else {
                graph.insertEdge(current, parent, bottleneck, null); // Create reverse edge if not present
            }

            current = parent;
        }

        return bottleneck;
    }

    // Find an edge between two vertices
    private Edge findEdge(Vertex from, Vertex to) {
        for (Iterator<Edge> it = graph.incidentEdges(from); it.hasNext(); ) {
            Edge edge = it.next();
            if (edge.getSecondEndpoint().equals(to)) {
                return edge;
            }
        }
        return null;
    }
}
