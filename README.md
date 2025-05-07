
# Maximum Flow Algorithms

**Authors:** Kannika Armstrong, Abinava Bharathi Babu, Aqueno Amalraj, Thanvi Yadav Sirla  
**University:** University of Washington Tacoma  
**Emails:** `{akannika, abibabu, aqueno10, thanvys}@uw.edu`

---

## Overview

This project implements several algorithms for solving the **maximum flow problem** in a network:

1. **Ford-Fulkerson Algorithm** – Uses BFS to find augmenting paths.
2. **Scaling Ford-Fulkerson** – Prioritizes high-capacity paths using a scaling parameter.
3. **Preflow-Push Algorithm** – Pushes flow using vertex heights and excess flow.

---

## Features

- Computes **maximum flow** between a source and sink.
- Accepts **graph input from file**.
- Dynamically updates the **residual graph**.
- Supports **multiple algorithms** for comparison.

---

## Environment & Requirements

- **Operating Systems:** macOS (Apple Silicon), Windows 11  
- **Java Version:** Java SE 8+ (Tested on OpenJDK 17.0.2 and 21.0.1)

---

## How to Run

### 1. Compile the Code

Navigate to the directory containing the `.java` files and run:

```bash
javac *.java
```

### 2. Run the Program

#### Option 1: Single Input File

```bash
java -cp . runProgram InputGraph/graphGenerationCode/Bipartite/bg1.txt
```

Replace the file path with your actual input file.

#### Option 2: Multiple Input Files

Edit `RunMultipleInputs.java` to include a `String[]` of input file paths, then run:

```bash
java -cp . RunMultipleInputs
```

---

## Code Structure

| File                    | Description                                                                 |
|-------------------------|-----------------------------------------------------------------------------|
| `FordFulkerson.java`    | Implements classic Ford-Fulkerson algorithm                                 |
| `ScalingFordFulkerson.java` | Optimized Ford-Fulkerson using scaling parameters                      |
| `PreFlowPush.java`      | Implements the Preflow-Push algorithm                                       |
| `runProgram.java`       | Reads a single input file and runs the selected algorithm                   |
| `RunMultipleInputs.java`| Processes multiple input files and prints results                           |

---

## Routine Descriptions

### `FordFulkerson`
- **Purpose:** Find max flow using augmenting paths (BFS)
- **Input:** Source, sink, adjacency matrix
- **Output:** Max flow value

### `ScalingFordFulkerson`
- **Purpose:** Optimize Ford-Fulkerson via capacity scaling
- **Input:** Source, sink, adjacency matrix
- **Output:** Max flow value

### `PreFlowPush`
- **Purpose:** Use preflows and vertex height to find max flow
- **Input:** Source, sink, adjacency matrix
- **Output:** Max flow value

### `runProgram`
- **Purpose:** Run an algorithm on a single input file
- **Input:** File path
- **Output:** Runtime and max flow

### `RunMultipleInputs`
- **Purpose:** Batch processing of multiple input files
- **Input:** List of file paths
- **Output:** Results for each input file

---

## Example Commands

```bash
# Single input example
java -cp . runProgram InputGraph/graphGenerationCode/Bipartite/bg1.txt

# Multiple inputs example
java -cp . RunMultipleInputs
```

---
