# Homework 3 - Problem 4 Write-Up

## Problem Summary

For this problem, I implemented Breadth-First Search and Depth-First Search on Graph (a) and Graph (b). I represented each graph in two different ways: an adjacency list and an adjacency matrix. Then I used both BFS and DFS to find a path from vertex `0` to vertex `7` in Graph (a), and from vertex `A` to vertex `O` in Graph (b). I also ran each algorithm five times and reported the average execution time and memory usage.

## Graph Representations

### Adjacency List

An adjacency list stores each vertex with a list of its neighbors. For example, if vertex `0` is connected to vertices `2`, `3`, `7`, and `8`, the adjacency list stores:

```text
0 -> [2, 3, 7, 8]
```

This representation is useful because it only stores edges that actually exist.

### Adjacency Matrix

An adjacency matrix uses a two-dimensional array. If there is an edge from vertex `i` to vertex `j`, then `matrix[i][j] = 1`. If there is no edge, then `matrix[i][j] = 0`.

This representation is easy to understand and makes edge lookup simple, but it can use more memory because it stores every possible pair of vertices.

## BFS Explanation

BFS stands for Breadth-First Search. It uses a queue. The main idea is that BFS explores all nearby vertices first before moving farther away. Because of that, BFS usually finds the shortest path in an unweighted graph.

## DFS Explanation

DFS stands for Depth-First Search. It uses a stack. The main idea is that DFS follows one path as deep as possible before backtracking. DFS can find a valid path, but it does not always find the shortest path.

## Experimental Results

After running the program, the results printed by my computer were used for comparison. The exact time and memory values can change slightly each time because Java memory management and the operating system can affect the measurements.

### What I compared

For each graph, I compared:

1. BFS using adjacency list
2. DFS using adjacency list
3. BFS using adjacency matrix
4. DFS using adjacency matrix

For each one, I reported:

- path found
- number of nodes visited before reaching the target
- average execution time over 5 runs
- average memory usage over 5 runs

## Analysis

In general, BFS is better when the goal is to find the shortest path in an unweighted graph, because BFS searches level by level. DFS is better when we only need to explore deeply or find any path, but it can take a path that is longer than necessary.

The adjacency list is usually more space efficient because it only stores actual edges. The adjacency matrix uses more memory because it creates a full `V x V` table, even if many possible edges do not exist. For these graphs, both representations work because the graphs are small, but the adjacency list is usually the better choice for larger sparse graphs.

## Why BFS and DFS may return different paths

BFS and DFS may find different paths because they visit vertices in different orders. BFS checks all immediate neighbors first, while DFS keeps going deeper down one branch. If there are multiple possible paths to the target, the first path found by BFS may be different from the first path found by DFS.

## Conclusion

Both BFS and DFS successfully found paths in the graphs. BFS is more reliable for shortest paths in unweighted graphs. DFS can still find a path, but the path depends more heavily on the order of the neighbors. Between the two graph representations, adjacency lists are usually more efficient for sparse graphs, while adjacency matrices are simple and useful when quick edge lookup is needed.
