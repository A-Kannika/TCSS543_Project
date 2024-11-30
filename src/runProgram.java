import java.io.FileNotFoundException;

public class runProgram {
    public static void main(String[] args) {

        SimpleGraph graph = new SimpleGraph();
        if(args.length!=0)  {
            // Test Ford Fulkerson Algorithm
            String filePath = args[0];
            int FFMaxFlow = 0;
            long startTime = System.nanoTime();
            try {
                FFMaxFlow = FordFulkerson.readGraphFromFile(filePath);
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + filePath);
            }
            long endTime = System.nanoTime();
            System.out.println("Ford-Fulkerson Runtime: " + (endTime - startTime) / 100000 + " ms");
            System.out.println("Ford-Fulkerson Maximum Flow: " + FFMaxFlow);


            // Test Scaling Ford Fulkerson Algorithm
            graph = GraphInput.LoadSimpleGraph(graph,args[0]);
            ScalingFordFulkerson scalingFordFulkerson = new ScalingFordFulkerson();
            startTime = System.nanoTime();
            double maximumFlow = scalingFordFulkerson.calculateMaxFlow(graph);
            endTime = System.nanoTime();
            System.out.println("Scaling Ford-Fulkerson Runtime: " + (endTime - startTime) / 100000 + " ms");
            System.out.println("Scaling Ford-Fulkerson Maximum Flow: " + maximumFlow);
            }
        }
    }

