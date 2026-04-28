import java.util.*;

/*
 * Homework 3 - Problem 4
 *
 * This problem compares BFS and DFS on two graphs using:
 * - adjacency list
 * - adjacency matrix 
 *
 * Graph (a): numeric vertices, find a path from 0 to 7.
 * Graph (b): alphabetic vertices, find a path from A to O.
 */
public class GraphTraversalAnalysis {

    private static final int RUNS = 5;

    /*
     * Stores the result of one BFS or DFS run
     */
    static class SearchResult {
        List<String> path;
        int nodesVisited;
        long timeNanoseconds;
        long memoryBytes;

        SearchResult(List<String> path, int nodesVisited) {
            this.path = path;
            this.nodesVisited = nodesVisited;
        }
    }

    /*
     * Small interface so we can pass BFS/DFS functions into the timing method
     */
    interface SearchAlgorithm {
        SearchResult run();
    }

    public static void main(String[] args) {
        /*
         * Graph (a) treated as undirected
         */
        String[] graphAVertices = {
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"
        };

        String[][] graphAEdges = {
                {"0", "2"}, {"0", "3"}, {"0", "7"}, {"0", "8"},
                {"1", "2"}, {"1", "3"}, {"1", "4"},
                {"3", "5"}, {"5", "7"},
                {"4", "6"}, {"6", "9"}, {"6", "11"}, {"9", "11"},
                {"8", "10"}
        };

        /*
         * Graph (b) is treated as directed
        */ 

        String[] graphBVertices = {
                "A", "B", "C", "D", "E", "F", "G", "H",
                "I", "J", "K", "L", "M", "N", "O"
        };

        String[][] graphBEdges = {
                {"A", "C"}, {"A", "B"},
                {"B", "D"}, {"B", "G"},
                {"C", "D"},
                {"D", "E"},
                {"E", "F"}, {"E", "K"},
                {"F", "H"}, {"F", "M"}, {"F", "G"},
                {"G", "H"}, {"G", "L"}, {"G", "N"}, {"G", "K"},
                {"H", "I"},
                {"I", "J"},
                {"J", "H"}, {"J", "D"},
                {"K", "L"},
                {"M", "O"},
                {"N", "O"}
        };

        System.out.println("==================================================");
        System.out.println("GRAPH (A): Path from 0 to 7");
        System.out.println("==================================================");
        runAllExperiments(graphAVertices, graphAEdges, "0", "7", false);

        System.out.println();
        System.out.println("==================================================");
        System.out.println("GRAPH (B): Path from A to O");
        System.out.println("==================================================");
        runAllExperiments(graphBVertices, graphBEdges, "A", "O", true);
    }

    /*
     * Builds both graph representations and runs BFS/DFS with each one
     */
    private static void runAllExperiments(
            String[] vertices,
            String[][] edges,
            String start,
            String target,
            boolean directed
    ) {
        Map<String, List<String>> adjacencyList = buildAdjacencyList(vertices, edges, directed);
        int[][] adjacencyMatrix = buildAdjacencyMatrix(vertices, edges, directed);
        Map<String, Integer> indexMap = buildIndexMap(vertices);

        printAdjacencyList(adjacencyList);
        printAdjacencyMatrix(vertices, adjacencyMatrix);

        runAverage("BFS using adjacency list",
                () -> bfsAdjacencyList(adjacencyList, start, target));

        runAverage("DFS using adjacency list",
                () -> dfsAdjacencyList(adjacencyList, start, target));

        runAverage("BFS using adjacency matrix",
                () -> bfsAdjacencyMatrix(vertices, adjacencyMatrix, indexMap, start, target));

        runAverage("DFS using adjacency matrix",
                () -> dfsAdjacencyMatrix(vertices, adjacencyMatrix, indexMap, start, target));
    }

    /*
     * Runs one algorithm five times and prints the averages
     */
    private static void runAverage(String title, SearchAlgorithm algorithm) {
        long totalTime = 0;
        long totalMemory = 0;
        SearchResult lastResult = null;

        for (int i = 0; i < RUNS; i++) {
            System.gc();

            Runtime runtime = Runtime.getRuntime();
            long beforeMemory = runtime.totalMemory() - runtime.freeMemory();
            long startTime = System.nanoTime();

            SearchResult result = algorithm.run();

            long endTime = System.nanoTime();
            long afterMemory = runtime.totalMemory() - runtime.freeMemory();

            result.timeNanoseconds = endTime - startTime;
            result.memoryBytes = Math.max(0, afterMemory - beforeMemory);

            totalTime += result.timeNanoseconds;
            totalMemory += result.memoryBytes;
            lastResult = result;
        }

        System.out.println();
        System.out.println(title);
        System.out.println("Path found: " + lastResult.path);
        System.out.println("Nodes visited before reaching target: " + lastResult.nodesVisited);
        System.out.println("Average execution time over 5 runs: " + (totalTime / RUNS) + " ns");
        System.out.println("Average memory change over 5 runs: " + (totalMemory / RUNS) + " bytes");
    }

    private static Map<String, Integer> buildIndexMap(String[] vertices) {
        Map<String, Integer> indexMap = new LinkedHashMap<>();

        for (int i = 0; i < vertices.length; i++) {
            indexMap.put(vertices[i], i);
        }

        return indexMap;
    }

    /*
     * Adjacency list 
     *
     * Directed graph: add only from -> to
     * Undirected graph: add from -> to and to -> from
     */
    private static Map<String, List<String>> buildAdjacencyList(
            String[] vertices,
            String[][] edges,
            boolean directed
    ) {
        Map<String, List<String>> graph = new LinkedHashMap<>();

        for (String vertex : vertices) {
            graph.put(vertex, new ArrayList<>());
        }

        for (String[] edge : edges) {
            String from = edge[0];
            String to = edge[1];

            graph.get(from).add(to);

            if (!directed) {
                graph.get(to).add(from);
            }
        }

        return graph;
    }

    /*
     * Adjacency matrix
     *
     * matrix[i][j] = 1 means there is an edge from vertex i to vertex j
     * matrix[i][j] = 0 means there is no edge
     */
    private static int[][] buildAdjacencyMatrix(
            String[] vertices,
            String[][] edges,
            boolean directed
    ) {
        Map<String, Integer> indexMap = buildIndexMap(vertices);
        int[][] matrix = new int[vertices.length][vertices.length];

        for (String[] edge : edges) {
            int from = indexMap.get(edge[0]);
            int to = indexMap.get(edge[1]);

            matrix[from][to] = 1;

            if (!directed) {
                matrix[to][from] = 1;
            }
        }

        return matrix;
    }

    /*
     * BFS using an adjacency list
     *
     * REMEMBER: BFS uses a queue and explores the graph level by level
     */
    private static SearchResult bfsAdjacencyList(
            Map<String, List<String>> graph,
            String start,
            String target
    ) {
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();

        queue.add(start);
        visited.add(start);
        parent.put(start, null);

        int nodesVisited = 0;

        while (!queue.isEmpty()) {
            String current = queue.remove();
            nodesVisited++;

            if (current.equals(target)) {
                return new SearchResult(buildPath(parent, target), nodesVisited);
            }

            for (String neighbor : graph.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        return new SearchResult(new ArrayList<>(), nodesVisited);
    }

    /*
     * DFS using an adjacency list
     *
     * REMEMBER: DFS uses a stack and follows one branch deeply before backtracking
     */
    private static SearchResult dfsAdjacencyList(
            Map<String, List<String>> graph,
            String start,
            String target
    ) {
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();

        stack.push(start);
        parent.put(start, null);

        int nodesVisited = 0;

        while (!stack.isEmpty()) {
            String current = stack.pop();

            if (visited.contains(current)) {
                continue;
            }

            visited.add(current);
            nodesVisited++;

            if (current.equals(target)) {
                return new SearchResult(buildPath(parent, target), nodesVisited);
            }

            List<String> neighbors = graph.get(current);

            /*
             * Push in reverse order so that the stack pops vertices
             * in the same order they appear in the adjacency list
             */
            for (int i = neighbors.size() - 1; i >= 0; i--) {
                String neighbor = neighbors.get(i);

                if (!visited.contains(neighbor)) {
                    parent.putIfAbsent(neighbor, current);
                    stack.push(neighbor);
                }
            }
        }

        return new SearchResult(new ArrayList<>(), nodesVisited);
    }

    /*
     * BFS using an adjacency matrix
     */
    private static SearchResult bfsAdjacencyMatrix(
            String[] vertices,
            int[][] matrix,
            Map<String, Integer> indexMap,
            String start,
            String target
    ) {
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[vertices.length];
        int[] parent = new int[vertices.length];
        Arrays.fill(parent, -1);

        int startIndex = indexMap.get(start);
        int targetIndex = indexMap.get(target);

        queue.add(startIndex);
        visited[startIndex] = true;

        int nodesVisited = 0;

        while (!queue.isEmpty()) {
            int current = queue.remove();
            nodesVisited++;

            if (current == targetIndex) {
                return new SearchResult(buildPath(vertices, parent, targetIndex), nodesVisited);
            }

            for (int neighbor = 0; neighbor < vertices.length; neighbor++) {
                if (matrix[current][neighbor] == 1 && !visited[neighbor]) {
                    visited[neighbor] = true;
                    parent[neighbor] = current;
                    queue.add(neighbor);
                }
            }
        }

        return new SearchResult(new ArrayList<>(), nodesVisited);
    }

    /*
     * DFS using an adjacency matrix
     */
    private static SearchResult dfsAdjacencyMatrix(
            String[] vertices,
            int[][] matrix,
            Map<String, Integer> indexMap,
            String start,
            String target
    ) {
        Stack<Integer> stack = new Stack<>();
        boolean[] visited = new boolean[vertices.length];
        int[] parent = new int[vertices.length];
        Arrays.fill(parent, -1);

        int startIndex = indexMap.get(start);
        int targetIndex = indexMap.get(target);

        stack.push(startIndex);

        int nodesVisited = 0;

        while (!stack.isEmpty()) {
            int current = stack.pop();

            if (visited[current]) {
                continue;
            }

            visited[current] = true;
            nodesVisited++;

            if (current == targetIndex) {
                return new SearchResult(buildPath(vertices, parent, targetIndex), nodesVisited);
            }

            /*
             * Scan backward so DFS order is similar to the adjacency list DFS
             */
            for (int neighbor = vertices.length - 1; neighbor >= 0; neighbor--) {
                if (matrix[current][neighbor] == 1 && !visited[neighbor]) {
                    if (parent[neighbor] == -1) {
                        parent[neighbor] = current;
                    }
                    stack.push(neighbor);
                }
            }
        }

        return new SearchResult(new ArrayList<>(), nodesVisited);
    }

    /*
     * Reconstructs a path when parents are stored by vertex label
     */
    private static List<String> buildPath(Map<String, String> parent, String target) {
        List<String> path = new ArrayList<>();
        String current = target;

        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }

        Collections.reverse(path);
        return path;
    }

    /*
     * Reconstructs a path when parents are stored by index
     */
    private static List<String> buildPath(String[] vertices, int[] parent, int targetIndex) {
        List<String> path = new ArrayList<>();
        int current = targetIndex;

        while (current != -1) {
            path.add(vertices[current]);
            current = parent[current];
        }

        Collections.reverse(path);
        return path;
    }

    private static void printAdjacencyList(Map<String, List<String>> graph) {
        System.out.println();
        System.out.println("Adjacency List:");
        for (String vertex : graph.keySet()) {
            System.out.println(vertex + " -> " + graph.get(vertex));
        }
    }

    private static void printAdjacencyMatrix(String[] vertices, int[][] matrix) {
        System.out.println();
        System.out.println("Adjacency Matrix:");

        System.out.print("    ");
        for (String vertex : vertices) {
            System.out.printf("%4s", vertex);
        }
        System.out.println();

        for (int i = 0; i < vertices.length; i++) {
            System.out.printf("%4s", vertices[i]);
            for (int j = 0; j < vertices.length; j++) {
                System.out.printf("%4d", matrix[i][j]);
            }
            System.out.println();
        }
    }
}
