# Memory Manager Simulator + Memory Scheduling Simulator

A C simulation project featuring Page Replacement tools (FIFO/LRU) and a simulation integrating Round Robin Process Scheduling with Virtual Memory Paging. Developed for the Operating Systems course at Universidade de Évora.

## About

This project explores Memory Management techniques in Operating Systems through two distinct components:

### Part 1: Page Replacement Simulators

A dedicated simulation of mapping logical addresses to physical frames. It processes sequences of memory accesses to demonstrate how the OS handles page faults under limited memory conditions. The simulation implements both **FIFO** (First-In-First-Out) and **LRU** (Least Recently Used) algorithms, handling frame allocation, page table updates, and segmentation fault detection.

### Part 2: Integrated Kernel Memory Manager

An evolution of the Process Scheduler (from Project 1) that integrates a Virtual Memory System into the execution lifecycle. It features a paging system with per-process page tables and dynamic shared memory management. Using just-in-time allocation with LRU replacement, it handles `LOAD`, `STORE`, and `SWAP` instructions while managing critical memory signals like `SIG_SEGV` and `SIG_ILL`.


## Technologies

**Language:** C  
**Build Tool:** Make 

---

## How to Run

### Prerequisites
- GCC
- Make

### Steps

You can compile and run specific test inputs using the Makefile.

```bash
make run ARG=<InputTestNumber>
```
- Where <InputTestNumber> is the two digits of the selected test. 

For example, to run the test 00 "input00":

```bash
make run ARG=00
```

This applies to both parts of the project.

---

## Grade
[![Grade](https://img.shields.io/badge/Grade-20.0%2F20.0-brightgreen)]()

*Operating Systems - 2024/2025*  
*Universidade de Évora*

## Authors

- [**Miguel Pombeiro**](https://github.com/MiguelPombeiro)
- [**Miguel Rocha**](https://github.com/migueljfrocha)