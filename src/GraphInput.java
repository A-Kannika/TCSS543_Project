import java.io.*;
import java.util.*;

/**
 * A class that can read a graph (in a specific format) from a file.
 *
 * @author edhong
 * @version 0.0
 */

public class GraphInput {

    /**
     * Load graph data from a text file via user interaction. This method asks
     * the user for a directory and path name. It returns a hashtable of
     * (String, Vertex) pairs. graph needs to already be initialized.
     *
     * @param graph a simple graph
     * @returns a hash table of (String, Vertex) pairs
     */

    /**
     * Load graph data from a text file. The format of the file is: Each line of
     * the file contains 3 tokens, where the first two are strings representing
     * vertex labels and the third is an edge weight (a double). Each line
     * represents one edge.
     * <p>
     * This method returns a hashtable of (String, Vertex) pairs.
     *
     * @param graph    a graph to add edges to. newgraph should already be
     *                 initialized
     * @param filepath the name of the file, including full path.
     * @returns a hash table of (String, Vertex) pairs
     */
    public static SimpleGraph LoadSimpleGraph(SimpleGraph graph, String filepath) {
        // Open the file for reading using a BufferedReader.
        BufferedReader bufferedReader = InputLib.fopen(filepath);
        System.out.println("Opened " + filepath + " for input.");

        // Read the first line of the file.
        String line = InputLib.getLine(bufferedReader);
        StringTokenizer stringTokenizer;
        int n, linenum = 0;

        // A hashtable to store vertices by their names for quick lookup.
        Hashtable<String, Vertex> table = new Hashtable<String, Vertex>();
        SimpleGraph simpleGraph = graph;

        // Process each line in the file until EOF.
        while (line != null) {
            linenum++;
            stringTokenizer = new StringTokenizer(line);
            n = stringTokenizer.countTokens();

            // If the line contains exactly 3 tokens, process it as an edge definition.
            if (n == 3) {
                Double edgedata;
                Vertex vertex1, vertex2;
                String v1name, v2name;

                // Parse vertex names and edge data from the line.
                v1name = stringTokenizer.nextToken();
                v2name = stringTokenizer.nextToken();
                edgedata = Double.valueOf(Double.parseDouble(stringTokenizer.nextToken()));

                // Retrieve or create the first vertex.
                vertex1 = table.get(v1name);
                if (vertex1 == null) {
                    vertex1 = simpleGraph.insertVertex(null, v1name);
                    table.put(v1name, vertex1);
                }

                // Retrieve or create the second vertex.
                vertex2 = table.get(v2name);
                if (vertex2 == null) {
                    vertex2 = simpleGraph.insertVertex(null, v2name);
                    table.put(v2name, vertex2);
                }

                // Add an edge between the two vertices.
                simpleGraph.insertEdge(vertex1, vertex2, edgedata, null);
            } else {
                // Print an error if the line doesn't have exactly 3 tokens.
                System.err.println("Error: invalid number of tokens found on line " + linenum + "!");
            }

            // Read the next line from the file.
            line = InputLib.getLine(bufferedReader);
        }

        // Close the file after processing.
        InputLib.fclose(bufferedReader);

        // Add missing reverse edges for undirected graph representation.
        Vertex start, end;
        Edge e, e2;
        Iterator<Vertex> i;
        Iterator<Edge> j;
        Iterator<Edge> k;
        double be = 0;  // Default weight for back edges.
        boolean backedge = false;

        // Iterate through all vertices.
        for (i = simpleGraph.vertices(); i.hasNext(); ) {
            start = i.next();

            // Iterate through all edges incident to the current vertex.
            for (j = simpleGraph.incidentEdges(start); j.hasNext(); ) {
                e = j.next();
                end = e.getFirstEndpoint();  // Get the edge's first endpoint.
                end = e.getSecondEndpoint(); // Get the edge's second endpoint.

                // Check for existing back edges.
                for (k = simpleGraph.incidentEdges(end); k.hasNext(); ) {
                    e2 = k.next();
                    if (e2.getSecondEndpoint().getName().equals(start.getName())) {
                        backedge = true;  // Back edge already exists.
                    }
                }

                // If no back edge exists, add one.
                if (!backedge) {
                    simpleGraph.insertEdge(end, start, be, null);
                }
                backedge = false; // Reset the backedge flag.
            }
        }

        // Return the updated graph.
        return simpleGraph;
    }

    public static void printList(SimpleGraph G) {
        // Print all vertices in the graph.
        Iterator<Vertex> i;
        Vertex v;
        Edge e;
        System.out.println("Iterating through vertices...");
        for (i = G.vertices(); i.hasNext(); ) {
            v = i.next();
            System.out.println("found vertex " + v.getName());
        }

        // Print adjacency lists for all vertices.
        System.out.println("Iterating through adjacency lists...");
        for (i = G.vertices(); i.hasNext(); ) {
            v = i.next();
            System.out.println("Vertex " + v.getName());
            Iterator<Edge> j;

            // Print all edges incident to the current vertex.
            for (j = G.incidentEdges(v); j.hasNext(); ) {
                e = j.next();

                // Print edge details: endpoints and data (weight).
                System.out.println(e.getFirstEndpoint().getName());
                System.out.println(e.getSecondEndpoint().getName());
                System.out.println(e.getData());
            }
        }
    }
}