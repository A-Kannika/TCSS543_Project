public class runProgram {
    public static void main(String[] args) {
            SimpleGraph graph = new SimpleGraph();
            GraphInput graphInput = new GraphInput();
            if(args.length!=0)  {
                graph = GraphInput.LoadSimpleGraph(graph,args[0]);
                ScalingFordFulkerson scalingFordFulkerson = new ScalingFordFulkerson();
                long startTime = System.nanoTime();
                double maximumFlow = scalingFordFulkerson.calculateMaxFlow(graph);
                long endTime = System.nanoTime();
                System.out.println("Scaling Ford-Fulkerson Runtime: " + (endTime - startTime) / 100000 + " ms");
                System.out.println("Scaling Ford-Fulkerson Maximum Flow: " + maximumFlow);

            }
        }
    }

