package Graph;
import java.util.List;
import java.util.LinkedList;

/**
 * Graph class to represent a non directed graph
 * It uses an adjacency list to represent the graph
 * It has methods to add edges and to find the diameter of the graph
 */
public class Graph {
    private List<Integer>[] adjecentList;
    private int nNodes;


    @SuppressWarnings("unchecked")
    public Graph(int nHabitants) {
        this.nNodes = nHabitants;
        this.adjecentList = new List[nHabitants + 1];

        for (int i = 0; i <= nHabitants; i++) {
            adjecentList[i] = new LinkedList<>();
        }
    }


    /**
     * Adds an bidirectional edge to the non directed graph
     * @param a Vertex a
     * @param b Vertex b
     */
    public void addEdge(int a, int b) {
        adjecentList[a].add(b);
        adjecentList[b].add(a);
    }


    /**
     * BFS algorithm to find the distance from a start node to all other nodes
     * @param start The starting node
     * @return An array with the distance from the start node to all other nodes
     */
    private int[] BFS (int start) {
        boolean[] visited = new boolean[nNodes + 1];
        int[] distance = new int[nNodes + 1];
        LinkedList<Integer> queue = new LinkedList<>();

        visited[start] = true;
        queue.add(start);
        distance[start] = 0;

        while (!queue.isEmpty()) {
            int node = queue.poll();
            for (int neighbor : adjecentList[node]) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    distance[neighbor] = distance[node] + 1;
                    queue.add(neighbor);
                }
            }
        }

        return distance;
    }
    

    /**
     * Solves the problem of finding the maximum distance between any two nodes in the graph
     * This is equivalent to finding the diameter of the graph (largest smallest path)
     * Starts from an arbitrary node, finds the farthest node from it, and then finds the farthest node from that node
     * @return The maximum distance between any two nodes in the graph
     */
    public int getDiameter() {

        int startNode = 1, maxDistance = Integer.MIN_VALUE;

        int[] distance = BFS(startNode);
        // Find the farthest node from the start node (one end of the diameter)
        for (int i = 1; i <= nNodes; i++) {
            if (distance[i] > distance[startNode]) {
                startNode = i;
            }
        }

        // Get the farthest node from the previous node.
        distance = BFS(startNode);
        // The maximum distance found in the BFS is the diameter of the graph
        for (int i = 1; i <= nNodes; i++) {
            if (distance[i] > maxDistance) {
                maxDistance = distance[i];
            }
        }
        return maxDistance;

    }

}