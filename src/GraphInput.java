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
     * (String, Vertex) pairs. newgraph needs to already be initialized.
     *
     * @param newgraph a simple graph
     * @returns a hash table of (String, Vertex) pairs
     */
    
    /**
     * Load graph data from a text file. The format of the file is: Each line of
     * the file contains 3 tokens, where the first two are strings representing
     * vertex labels and the third is an edge weight (a double). Each line
     * represents one edge.
     *
     * This method returns a hashtable of (String, Vertex) pairs.
     *
     * @param newgraph a graph to add edges to. newgraph should already be
     * initialized
     * @param pathandfilename the name of the file, including full path.
     * @returns a hash table of (String, Vertex) pairs
     */
    public static SimpleGraph LoadSimpleGraph(SimpleGraph newgraph, String pathandfilename) {
        BufferedReader inbuf = InputLib.fopen(pathandfilename);
        System.out.println("Opened " + pathandfilename + " for input.");
        String line = InputLib.getLine(inbuf); // get first line
        StringTokenizer sTok;
        int n, linenum = 0;
        Hashtable<String,Vertex> table = new Hashtable<String,Vertex>();
        SimpleGraph sg = newgraph;

        while (line != null) {
            linenum++;
            sTok = new StringTokenizer(line);
            n = sTok.countTokens();
            if (n == 3) {
                Double edgedata;
                Vertex v1, v2;
                String v1name, v2name;

                v1name = sTok.nextToken();
                v2name = sTok.nextToken();
                edgedata = Double.valueOf(Double.parseDouble(sTok.nextToken()));
                v1 =  table.get(v1name);
                if (v1 == null) {
                    v1 = sg.insertVertex(null, v1name);
                    table.put(v1name, v1);
                }
                v2 = table.get(v2name);
                if (v2 == null) {
                    v2 = sg.insertVertex(null, v2name);
                    table.put(v2name, v2);
                }
                sg.insertEdge(v1, v2, edgedata, null);
            } else {
                System.err.println("Error:invalid number of tokens found on line " + linenum + "!");

            }
            line = InputLib.getLine(inbuf);
        }

        InputLib.fclose(inbuf);

        //addbackedges(sg);
        Vertex start, end;
        Edge e, e2;
        Iterator<Vertex> i;
        Iterator<Edge> j;
        Iterator<Edge> k;
        double be = 0;
        boolean backedge = false;
        for (i = sg.vertices(); i.hasNext();) {
            start =  i.next();
            for (j = sg.incidentEdges(start); j.hasNext();) {
                e = j.next();
                end = e.getFirstEndpoint();
                end = e.getSecondEndpoint();
                for (k = sg.incidentEdges(end); k.hasNext();) {
                    e2 =  k.next();
                    if (e2.getSecondEndpoint().getName().equals(start.getName())) {

                        backedge = true;
                    }
                }
                if (backedge == false) {
                    sg.insertEdge(end, start, be, null);

                }
                backedge = false;
            }
        }
        return sg;

    }

    /**
     * Code to test the methods of this class.
     */
    
             

    public static void printlist(SimpleGraph G) {

        Iterator<Vertex> i;
        Vertex v;
        Edge e;
        System.out.println("Iterating through vertices...");
        for (i = G.vertices(); i.hasNext();) {
            v =  i.next();
            System.out.println("found vertex " + v.getName());
        }

        System.out.println("Iterating through adjacency lists...");
        for (i = G.vertices(); i.hasNext();) {
            v =  i.next();
            System.out.println("Vertex " + v.getName());
            Iterator<Edge> j;

            for (j = G.incidentEdges(v); j.hasNext();) {
                e =  j.next();

                System.out.println(e.getFirstEndpoint().getName());
                System.out.println(e.getSecondEndpoint().getName());
                System.out.println(e.getData());
            }
        }
    }

    static void addbackedges(SimpleGraph G) {
        Vertex start, end;
        Edge e, e2;
        Iterator<Vertex> i;
                Iterator<Edge> j;
                        Iterator<Edge> k;
        double be = 0;
        boolean backedge = false;
        for (i = G.vertices(); i.hasNext();) {
            start =  i.next();
            for (j = G.incidentEdges(start); j.hasNext();) {
                e = j.next();
                end = e.getFirstEndpoint();
                end = e.getSecondEndpoint();
                for (k = G.incidentEdges(end); k.hasNext();) {
                    e2 =  k.next();
                    if (e2.getSecondEndpoint().getName().equals(start.getName())) {

                        backedge = true;
                    }
                }
                if (backedge == false) {
                    G.insertEdge(end, start, be, null);

                }
                backedge = false;
            }
        }
    }
}
