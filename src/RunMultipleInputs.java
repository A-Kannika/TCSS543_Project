import java.io.IOException;

public class RunMultipleInputs {
    public static void main(String[] args) {
        // Input file paths for Bipartite Graph
        String[] inputFiles1 = {
                "InputGraph/graphGenerationCode/Bipartite/bg1.txt",
                "InputGraph/graphGenerationCode/Bipartite/bg2.txt",
                "InputGraph/graphGenerationCode/Bipartite/bg3.txt",
                "InputGraph/graphGenerationCode/Bipartite/bg4.txt",
                "InputGraph/graphGenerationCode/Bipartite/bg5.txt"
        };

        // Input file paths for Fixed Degree Graph
        String[] inputFiles2 = {
                "InputGraph/graphGenerationCode/FixedDegree/fd1.txt",
                "InputGraph/graphGenerationCode/FixedDegree/fd2.txt",
                "InputGraph/graphGenerationCode/FixedDegree/fd3.txt",
                "InputGraph/graphGenerationCode/FixedDegree/fd4.txt",
                "InputGraph/graphGenerationCode/FixedDegree/fd5.txt"
        };

        // Input file paths for Mesh Graph
        String[] inputFiles3 = {
                "InputGraph/graphGenerationCode/Mesh/mg1.txt",
                "InputGraph/graphGenerationCode/Mesh/mg2.txt",
                "InputGraph/graphGenerationCode/Mesh/mg3.txt",
                "InputGraph/graphGenerationCode/Mesh/mg4.txt",
                "InputGraph/graphGenerationCode/Mesh/mg5.txt"
        };

        // Input file paths for Mesh Graph
        String[] inputFiles4 = {
                "InputGraph/graphGenerationCode/Random/rg1.txt",
                "InputGraph/graphGenerationCode/Random/rg2.txt",
                "InputGraph/graphGenerationCode/Random/rg3.txt",
                "InputGraph/graphGenerationCode/Random/rg4.txt",
                "InputGraph/graphGenerationCode/Random/rg5.txt"
        };

        for (String filePath : inputFiles2) {
            System.out.println("\nRunning for file: " + filePath);
            try {
                Process process = new ProcessBuilder(
                        "java",
                        "-cp", ".",
                        "runProgram",
                        filePath
                ).inheritIO().start();

                // Wait for the process to finish
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    System.err.println("Error running for file: " + filePath);
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("An error occurred while running for file: " + filePath);
                e.printStackTrace();
            }
        }
    }
}
