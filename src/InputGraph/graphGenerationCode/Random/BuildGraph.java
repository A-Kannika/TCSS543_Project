package InputGraph.graphGenerationCode.Random;
/* Disclaimer: Might not be ready to work with your code */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
public class BuildGraph {
	public void buildGraph(String fileName, String directory, int vertices, int
			dense, int maxCapacity,
						   int minCapacity) {
		Random random = new Random();
		try {
			String dirName = directory;//
			if (dirName.equals("")) {
				dirName = ".";
			}
			File outputfile = new File(dirName, fileName);
			int[][] Graph = new int[vertices][vertices];
			int n, m;
			for (n = 0; n < vertices; n++)
				for (m = n + 1; m < vertices; m++) {
					int randomInt = (random.nextInt((maxCapacity -
							minCapacity + 1)) + minCapacity);
					int k = (int) (1000.0 * Math.random() / 10.0);
					int b = (k < dense) ? 1 : 0;
					if (b == 0) {
						Graph[n][m] = Graph[m][n] = b;
					} else {
						Graph[n][m] = Graph[m][n] = randomInt;
					}
				}
			PrintWriter output = new PrintWriter(new FileWriter(outputfile));
			for (int x = 0; x < Graph.length; x++) {
				if (x == 0) {
					for (int y = 0; y < Graph[x].length; y++) {
						String value = String.valueOf(Graph[x][y]);
						if (y != 0) {
							if (value.equals("0") == false) {
								output.print("s " +
										String.valueOf(y) + " " + value + "\n");
							}
						}
					}
				} else {
					if (x == Graph.length - 1) {
						for (int y = 0; y < Graph[x].length; y++) {
							String value = String.valueOf(Graph[x]
									[y]);
							if (y != 0) {
								if (value.equals("0") == false) {
									output.print(String.valueOf(y) + " t " + value + "\n");
								}
							}
						}
					} else {
						for (int y = 0; y < Graph[x].length; y++) {
							String value = String.valueOf(Graph[x]
									[y]);
							if (y != 0) {
								if (value.equals("0") == false) {
									output.print(x + " " +
											String.valueOf(y) + " " + value + "\n");
								}
							}
						}
					}
				}
			}
			output.close();
		} catch (IOException e) {
			System.err.println("Error opening file" + e);
			return;
		}
		System.out.print("\nDone");
	}


		public static void main(String[] args) {
			BuildGraph builder = new BuildGraph();
			builder.buildGraph("rg1.txt", ".", 10, 10, 10, 1);  // Low density, 10 vertices, capacity 1-10
			builder.buildGraph("rg2.txt", ".", 10, 30, 20, 1);  // Medium density, 10 vertices, capacity 1-20
			builder.buildGraph("rg3.txt", ".", 10, 50, 30, 1);  // High density, 10 vertices, capacity 1-30
			builder.buildGraph("rg4.txt", ".", 20, 10, 10, 1);  // Low density, 20 vertices, capacity 1-10
			builder.buildGraph("rg5.txt", ".", 20, 70, 50, 1);  // High density, 20 vertices, capacity 1-50
		}


}
