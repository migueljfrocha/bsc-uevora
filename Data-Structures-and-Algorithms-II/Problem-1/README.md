# The Dream Factory - Dynamic Programming

An algorithmic problem solution developed for the **Data Structures and Algorithms II** course at Universidade de Évora.

## About

**The Dream Factory** is an optimization problem that requires packing a sequence of "dreams" (sized items) into "numbers" (containers of fixed capacity) while minimizing the total wasted capacity.

The constraints of the problem are:
1.  Dreams must be packed in the exact order they arrive (FIFO).
2.  A number can carry multiple dreams if the sum of their sizes does not exceed the number's value.

And the objective is to minimize the total difference between the used number values and the actual size of dreams they carry.

This solution utilizes **Dynamic Programming** to efficiently determine the optimal packing strategy that results in the minimum waste, avoiding the computational cost of a brute-force approach.

## Algorithm

The solution is implemented using a **Bottom-Up Dynamic Programming** approach:

1.  **State Definition:** An array `w[j]` is used to store the minimum wasted capacity possible when packing the first `j` dreams.
2.  **Base Case:** `w[0] = 0` (0 waste for 0 dreams).
3.  **Transition:** For each dream `j`, the algorithm iterates backwards from `j-1` down to `0` (index `k`), calculating the sum of dreams in the segment `[k, j]`.
    *   It checks if this sum fits into any available "number".
    *   Binary search is used to efficiently find the smallest "number" that can hold this sum.
    *   The minimum waste `w[j]` is updated based on `w[k] + (number_capacity - current_sum)`.
4.  **Result:** `w[nDreams]` contains the minimum waste for all dreams.

**Complexity:**
- **Time:** $O(D^2 \cdot \log N)$ - Where $D$ is the number of dreams and $N$ is the count of available numbers (due to the nested loop and binary search).
- **Space:** $O(D)$ - To store the dynamic programming state array `w`.

## Technologies

**Language:** Java  
**Concept:** Dynamic Programming

## How to Run

### Prerequisites
- Java

### Compilation

```bash
javac Main.java
```

### Execution

```bash
java -Xmx256m -Xss16m Main < teste-1.in
```

You can replace the teste-1.in file with any other file that has a correct input.

---

## Grade

[![Grade](https://img.shields.io/badge/Grade-18.4%2F20.0-brightgreen)]()

*Data Structures and Algorithms II - 2024/2025*  
*Universidade de Évora*

## Authors

- [**Miguel Pombeiro**](https://github.com/MiguelPombeiro)
- [**Miguel Rocha**](https://github.com/migueljfrocha)
