# Palm Island Neighbours - Tree Diameter

An algorithmic problem solution developed for the **Data Structures and Algorithms II** course at Universidade de Évora.

## About

**Palm Island Neighbours** is a graph theory problem that asks for the "largest smallest path" between any two inhabitants on an island with a tree-like topology (no cycles). In graph theory terms, this is equivalent to finding the **Diameter of a Tree** (the longest path between any two nodes in an acyclic graph).

The problem models the island's inhabitants as nodes and connections as edges in an unweighted, undirected, acyclic graph.

## Algorithm

The solution is implemented using a **Two-pass BFS (Breadth-First Search)** strategy, which is efficient for trees:
1.  **First BFS:** Start from an arbitrary node (e.g., node 1) to find the farthest node, `u`. This node `u` is guaranteed to be one of the endpoints of the tree's diameter.
2.  **Second BFS:** Start a new search from node `u` to find the farthest node from it, `v`.
3.  **Result:** The distance between `u` and `v` is the diameter of the tree.

**Complexity:**
- **Time:** $O(V + E)$ - Since it runs two BFS traversals.
- **Space:** $O(V + E)$ - To store the adjacency list and BFS queues.

## Technologies

- **Language:** Java
- **Concept:** Graph Theory, BFS, Tree Diameter

## How to Run

### Prerequisites
- Java

### Compilation

```bash
javac Main.java
```

### Execution

```bash
java -Xmx128m -Xss8m Main < teste-1.in
```

You can replace the teste-1.in file with any other file that has a correct input.

---

## Grade

[![Grade](https://img.shields.io/badge/Grade-19.7%2F20.0-brightgreen)]()

*Data Structures and Algorithms II - 2024/2025*  
*Universidade de Évora*

## Authors

- [**Miguel Pombeiro**](https://github.com/MiguelPombeiro)
- [**Miguel Rocha**](https://github.com/migueljfrocha)

