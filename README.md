# Homework 3 - Problem 4: BFS and DFS Analysis

## What this program does

This program implements BFS and DFS on the two graphs from Homework 3.

The assignment asks for the following:

- Use Graph (a) and Graph (b).
- Implement BFS and DFS.
- Represent each graph as:
  - an adjacency list
  - an adjacency matrix
- Find a path from vertex `0` to `7` for Graph (a).
- Find a path from vertex `A` to `O` for Graph (b).
- Run each algorithm five times.
- Report average execution time and memory usage.
- Compare BFS and DFS and compare adjacency lists with adjacency matrices.

## Files

```text
src/GraphTraversalAnalysis.java
README.md
Problem4_Writeup.md
sample-output.txt
```

## How to compile and run

From the project folder:

```bash
javac src/GraphTraversalAnalysis.java
java -cp src GraphTraversalAnalysis
```

## Short analysis

BFS uses a queue and searches by levels. This means BFS usually finds the shortest path in terms of number of edges when the graph is unweighted.

DFS uses a stack and searches deeply down one path before backtracking. This means DFS may find a path quickly, but it is not guaranteed to find the shortest path.

The adjacency list representation stores only existing neighbors, so it is usually better for sparse graphs. The adjacency matrix stores every possible vertex-to-vertex connection, so it uses more space, but checking whether a specific edge exists is simple.

## Why BFS and DFS can find different paths

BFS and DFS explore vertices in different orders. BFS explores the closest vertices first, while DFS follows one branch deeply before coming back. Because of this, even when both algorithms reach the same target, the path they return can be different.
