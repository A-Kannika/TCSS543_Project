import java.io.*;
import java.util.*;

class PreFlowPush {
    private int vertices;
    private List<PFPEdge> edges;
    private int[] height;
    private int[] excess;

    public PreFlowPush(int vertices) {
        this.vertices = vertices;
        this.edges = new ArrayList<>();
        this.height = new int[vertices];
        this.excess = new int[vertices];
    }

    public void addEdge(int u, int v, int capacity) {
        edges.add(new PFPEdge(u, v, capacity));
        edges.add(new PFPEdge(v, u, 0));  // Reverse edge
    }

    private void push(int u) {
        for (PFPEdge edge : edges) {
            if (edge.u == u && edge.flow < edge.capacity) {
                int flowDelta = Math.min(excess[u], edge.capacity - edge.flow);

                if (flowDelta > 0 && height[u] > height[edge.v]) {
                    edge.flow += flowDelta;

                    // Find and update reverse edge
                    for (PFPEdge reverseEdge : edges) {
                        if (reverseEdge.u == edge.v && reverseEdge.v == u) {
                            reverseEdge.flow -= flowDelta;
                            break;
                        }
                    }

                    excess[u] -= flowDelta;
                    excess[edge.v] += flowDelta;
                }
            }
        }
    }

    private void relabel(int u) {
        int minHeight = Integer.MAX_VALUE;
        for (PFPEdge edge : edges) {
            if (edge.u == u && edge.flow < edge.capacity) {
                minHeight = Math.min(minHeight, height[edge.v]);
            }
        }
        height[u] = minHeight + 1;
    }

    public int computeMaxFlow(int source, int sink) {
        // Preflow initialization
        height[source] = vertices;
        for (PFPEdge edge : edges) {
            if (edge.u == source) {
                edge.flow = edge.capacity;
                excess[edge.v] += edge.capacity;

                // Update reverse edge
                for (PFPEdge reverseEdge : edges) {
                    if (reverseEdge.u == edge.v && reverseEdge.v == source) {
                        reverseEdge.flow -= edge.capacity;
                        break;
                    }
                }
            }
        }

        // Main algorithm loop
        while (true) {
            int u = -1;
            for (int i = 0; i < vertices; i++) {
                if (i != source && i != sink && excess[i] > 0) {
                    u = i;
                    break;
                }
            }

            if (u == -1) break;

            boolean pushed = false;
            for (PFPEdge edge : edges) {
                if (edge.u == u && edge.flow < edge.capacity) {
                    if (height[u] > height[edge.v]) {
                        push(u);
                        pushed = true;
                        break;
                    }
                }
            }

            if (!pushed) {
                relabel(u);
            }
        }

        return excess[sink];
    }

    public static int runPreFlowPush(String filepath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        Map<String, Integer> nodeIndex = new HashMap<>();
        List<int[]> edgeData = new ArrayList<>();
        int nodeCounter = 0;

        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.trim().split("\t");
            if (parts.length != 3) {
                System.err.println("Invalid line: " + line);
                continue;
            }

            String from = parts[0];
            String to = parts[1];
            int capacity = Integer.parseInt(parts[2]);

            if (!nodeIndex.containsKey(from)) {
                nodeIndex.put(from, nodeCounter++);
            }
            if (!nodeIndex.containsKey(to)) {
                nodeIndex.put(to, nodeCounter++);
            }

            edgeData.add(new int[]{
                    nodeIndex.get(from),
                    nodeIndex.get(to),
                    capacity
            });
        }
        br.close();

        // Create graph with correct number of vertices
        PreFlowPush graph = new PreFlowPush(nodeCounter);

        // Add edges
        for (int[] edge : edgeData) {
            graph.addEdge(edge[0], edge[1], edge[2]);
        }

        int source = nodeIndex.get("s");
        int sink = nodeIndex.get("t");

//        System.out.println("Maximum flow is " + graph.computeMaxFlow(source, sink));
        return graph.computeMaxFlow(source, sink);
    }

    public static void main(String[] args) throws IOException {
        int max = runPreFlowPush("src/InputGraph/graphGenerationCode/Bipartite/g1.txt");
        System.out.println(max);
    }
}

class PFPEdge {
    int flow;
    int capacity;
    int u, v;

    PFPEdge(int u, int v, int capacity) {
        this.u = u;
        this.v = v;
        this.capacity = capacity;
        this.flow = 0;
    }
}