package FlowNetwork;

import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

/**
 * FlowNetwork class represents a flow network.
 * It contains methods to add edges, build a residual network,
 * and find the maximum flow using the Edmonds-Karp algorithm.
 */
public class FlowNetwork {
    public static final int INFINITY = Integer.MAX_VALUE;
    public static final int NONE = -1;

    private int source, sink;
    private int nodes;
    private List<Edge>[] adjacents;

    @SuppressWarnings("unchecked")
    public FlowNetwork(int nodes, int source, int sink){
        this.nodes = nodes;
        this.source = source;
        this.sink = sink;
        
        adjacents = new List[nodes];
        for (int i = 0; i < nodes; ++i){
            adjacents[i] = new LinkedList<Edge>();
        }  
    }
    

    /**
     * Adds an edge to the flow network.
     * @param vertex1 Vertex where the edge starts.
     * @param vertex2 Vertex where the edge ends.
     * @param capacity Amount of flow that can pass through the edge.
     */
    public void addEdge(int vertex1, int vertex2, int capacity){
        adjacents[vertex1].add(new Edge(vertex2, capacity));
    }


    /**
     * Builds the residual network based on the current flow in the flow network.
     * @return The residual network.
     */
    private FlowNetwork buildResidualNetwork(){
        FlowNetwork r = new FlowNetwork(this.nodes, this.source, this.sink);
        for (int i = 0; i < this.nodes; i++){
            for(Edge e : this.adjacents[i]){
                r.addEdge(i, e.getDestination(), e.getCapacity());
                r.addEdge(e.getDestination(), i, e.getFlow());
            }
        }  
        return r;
    }
    

    /**
     * Updates the residual capacities of the edges (from, to) and (to, from).
     * @param from The starting vertex of the edge.
     * @param to The ending vertex of the edge.
     * @param capacity The capacity of the edge.
     * @param flow The flow through the edge.
     */
    private void updateResidualCapacity(int from, int to, int capacity, int flow){
        for (Edge e : adjacents[from]){
            if (e.getDestination() == to){
                e.setCapacity(capacity - flow); 
                break;
            }
        }

        for (Edge e : adjacents[to]){
            if (e.getDestination() == from){
                e.setCapacity(flow); 
                break;
            }
        }
    }


    /**
     * This method increments the flow along the path from SOURCE to SINK by a given increment, in the residual network.
     * @param p Predecessor array that stores the path from SOURCE to SINK.
     * @param increment Amount to increment the flow.
     * @param r The residual network.
     */

    private void incrementFlow(int[] p, int increment, FlowNetwork r){
        int v = sink;
        int u = p[v];
        while (u != NONE){
            boolean uv = false; // is (u,v) an edge in the network?
            for (Edge e : adjacents[u]){
                if (e.getDestination() == v){
                    e.setFlow(e.getFlow() + increment);
                    r.updateResidualCapacity(u, v, e.getCapacity(), e.getFlow());
                    uv = true; // (u,v) is an edge in the network
                    break;
                }
            } 

            //if (u, v) is not an edge in the network, then (v,u) is
            if (!uv){
                for (Edge e : adjacents[v]){
                    if (e.getDestination() == u){
                        e.setFlow(e.getFlow() - increment);
                        r.updateResidualCapacity(v, u, e.getCapacity(), e.getFlow());
                        break;
                    }
                }
            }
            v = u;
            u = p[v];
        }
    }


    /**
     * Finds a shortest path from the source to the sink in the residual network.
     * It uses a breadth-first search algorithm to find the path.
     * @param p Predecessor array that stores the path from SOURCE to SINK.
     * @return The residual capacity of the path found. If no path is found, it returns 0.
     */
    private int findPath(int[] p){
        int[] cf = new int[nodes];  // Smallest residual capacity of the path
                                    // cf[v] = 0 until the node is found
        Queue<Integer> q = new LinkedList<>();

        for (int u = 0; u < nodes; ++u){
            p[u] = NONE; // predecessor of u
        }
        cf[source] = INFINITY; // Looking for the minimum value

        q.add(source);
        
        while (!q.isEmpty()){
            int u = q.remove();
            if (u == sink){
                break; // Path found
            }
            for (Edge e : adjacents[u]){
                int v = e.getDestination();
                if (e.getCapacity() > 0 && cf[v] == 0){
                    cf[v] = Math.min(cf[u], e.getCapacity());
                    p[v] = u;
                    q.add(v);
                }
            }
        }
        return cf[sink];
    }


    /**
     * This method implements the Edmonds-Karp algorithm to find the maximum flow in the network.
     * @return The maximum flow value in the network.
     */
    public int edmondsKarp(){
        FlowNetwork r = buildResidualNetwork();
        int flowValue = 0; // Initial flow value
        // Predecessor array to store the path from SOURCE to SINK
        int[] p = new int[nodes];
        int increment; // Incremental flow value

        while ((increment = r.findPath(p)) > 0){
            // Increments the flow and updates the residual network
            incrementFlow(p, increment, r);
            flowValue += increment; // Current flow value
        }
       
        return flowValue;
    }

}