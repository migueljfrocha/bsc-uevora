# State-Space Search

A Prolog assignment about uninformed and informed search algorithms, developed for the Artificial Intelligence course at Universidade de Évora.

## About

This assignment models a grid-based state-space problem where an agent must move a machine to an exit while avoiding obstacles and, in some cases, collecting objects.

The work includes the state representation, transition operators, auxiliary predicates, uninformed search, informed search, and heuristic design. The implemented algorithms compare iterative deepening, A*, and greedy search across different examples.

## Technologies

**Language:** Prolog  
**Concepts:** State-space search, heuristics, iterative deepening, A*, greedy search

---

## How to Run

### Prerequisites

- A Prolog interpreter compatible with the predicates used in the assignment, for example [GNU Prolog](http://gprolog.org/#download)

### Uninformed Search

```prolog
[pni].
pesquisa(problema).
```

### Informed Search

```prolog
[pi].
pesquisa(problema, a).  % A*
pesquisa(problema, g).  % Greedy
```

To test different examples or heuristics, edit `problema.pl` and switch the selected initial/final states or heuristic predicate.

---

## Grade

[![Grade](https://img.shields.io/badge/Grade-6.0%2F6.0-brightgreen)]()

*Artificial Intelligence - 2025/2026*  
*Universidade de Évora*

## Authors

- [**Miguel Pombeiro**](https://github.com/MiguelPombeiro)
- [**Miguel Rocha**](https://github.com/migueljfrocha)
