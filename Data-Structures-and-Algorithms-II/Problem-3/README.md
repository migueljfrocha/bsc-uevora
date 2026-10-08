# Card Exchange - Maximum Flow

An algorithmic problem solution developed for the **Data Structures and Algorithms II** course at Universidade de Évora.

## About

**Card Exchange** is a problem that determines if a group of participants can satisfyingly exchange cards. Each participant brings a card and is willing to exchange it for specific other cards. The goal is to verify if it is possible for **every** participant to successfully trade their card for one they want.

This problem is modeled as a **Bipartite Matching** problem, which is solved by transforming it into a **Flow Network**. The network is constructed by splitting participants into "Givers" and "Receivers," adding a Source ($s$) and a Sink ($t$), and calculating the **Maximum Flow**. If the max flow equals the number of participants, a valid exchange exists.

## Algorithm

The solution is implemented using the **Edmonds-Karp** algorithm:

1.  **Network Construction:**
    *   Create a **Source** connected to every participant (Giver role) with capacity 1.
    *   Create a **Sink** connected from every participant (Receiver role) with capacity 1.
    *   Add edges between Givers and Receivers based on the input declarations (interest in a card) with capacity 1.
2.  **Edmonds-Karp:**
    *   Repeatedly find the shortest augmenting path from Source to Sink in the **Residual Network** using **BFS (Breadth-First Search)**.
    *   Augment the flow along this path.
    *   Update residual capacities.
3.  **Result:** If `Max Flow == Number of Participants`, output "YES", otherwise "NO".

**Complexity:**
- **Time:** $O(V E^2)$ - In this specific problem context: $O(N(N+M)^2)$, where $N$ is participants and $M$ is declarations.
- **Space:** $O(N + M)$ - To store the graph adjacency lists and residual network.

## Technologies

- **Language:** Java
- **Concept:** Flow Networks, Maximum Flow, Edmonds-Karp

## How to Run

### Prerequisites
- Java
  
### Compilation

```bash
javac Main.java
```

### Execution

```bash
java -Xmx256m -Xss8m Main < teste-1.in
```

---

## Grade

[![Grade](https://img.shields.io/badge/Grade-18.9%2F20.0-brightgreen)]()

*Data Structures and Algorithms II - 2024/2025*  
*Universidade de Évora*

## Authors

- [**Miguel Pombeiro**](https://github.com/MiguelPombeiro)
- [**Miguel Rocha**](https://github.com/migueljfrocha)

