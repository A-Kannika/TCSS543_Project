public class runProgram {
    public static void main(String[] args) {


            SimpleGraph G1, G2, G3;
            GraphInput g= new GraphInput();
            G1 = new SimpleGraph();
            G2 = new SimpleGraph();
            G3 = new SimpleGraph();
            if(args.length!=0)
            {
                G1= GraphInput.LoadSimpleGraph(G1,args[0]);
                G2=GraphInput.LoadSimpleGraph(G2, args[0]);
                G3 = GraphInput.LoadSimpleGraph(G3,args[0]);
                ScalingFordFulkerson ff2 = new ScalingFordFulkerson();
                long startTime2 = System.nanoTime();
                double maxflow2 = ff2.calculateMaxFlow(G2);
                long endTime2 = System.nanoTime();
                System.out.println("Scaling Ford-Fulkerson took: " + (endTime2 - startTime2) / 100000 + " ms");
                System.out.println("Scaling Ford-Fulkerson Maximum Flow: " + maxflow2);

            }
            else
            {

            }
        }
    }

