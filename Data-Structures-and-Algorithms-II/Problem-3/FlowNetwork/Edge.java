package FlowNetwork;

/**
 * Edge class represents an edge in a flow network.
 * It contains the destination node, capacity of the edge,
 * and the current flow through the edge.
 */
class Edge {
    private int destination;
    private int capacity;
    private int flow;

    public Edge(int destination, int capacity){
        this.destination = destination;
        this.capacity = capacity;
        this.flow = 0;
    }

    public int getDestination() { 
        return this.destination; 
    }
    public int getCapacity() { 
        return this.capacity; 
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity; 
    }
    public int getFlow() {
        return this.flow;
    }
    public void setFlow(int flow) {
        this.flow = flow; 
    }
}