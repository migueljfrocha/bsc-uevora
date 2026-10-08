# Process Scheduling Simulation - 5-State Model

A C simulation of an Operating System process scheduler using the 5-state model and Round Robin algorithm, developed for the Operating Systems course at Universidade de Évora.

## About
This project simulates the internal workings of an Operating System kernel. It implements the standard five-state model (**NEW**, **READY**, **RUNNING**, **BLOCKED**, **EXIT**) and uses a **Round Robin** scheduling algorithm with a time quantum of 3 instants.

The simulator accepts a matrix of pseudo-instructions representing different programs and runs for 100 time instants, tracking the state of every process at each step.

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
Where <InputTestNumber> is the two digits of the selected test. For example, to run the test 00 "input00":

```bash
make run ARG=00
```

---

## Grade
[![Grade](https://img.shields.io/badge/Grade-20.0%2F20.0-brightgreen)]()

*Operating Systems - 2024/2025*  
*Universidade de Évora*

## Authors

- [**Miguel Pombeiro**](https://github.com/MiguelPombeiro)
- [**Miguel Rocha**](https://github.com/migueljfrocha)