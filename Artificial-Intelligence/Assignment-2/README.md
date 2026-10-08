# Constraint Satisfaction Problems

A Prolog assignment about constraint satisfaction, developed for the Artificial Intelligence course at Universidade de Évora.

## About

This assignment implements and compares search strategies for solving **Latin Square** and **Futoshiki** constraint satisfaction problems.

The work models variables, domains, constraints, and successor generation in Prolog. It includes plain backtracking, forward checking, and an improved forward checking version using the Minimum Remaining Values (MRV) heuristic.

## Technologies

**Language:** Prolog  
**Concepts:** Constraint satisfaction, backtracking, forward checking, MRV heuristic

---

## How to Run

### Prerequisites

- A Prolog interpreter compatible with the predicates used in the assignment, for example [GNU Prolog](http://gprolog.org/#download)

### Examples

Run a problem together with the selected algorithm file, then call `p`.

```prolog
[quadradolatino, backtracking].
p.
```

```prolog
[futoshiki, backFK].
p.
```

```prolog
[futoshiki, backFKMelhorado].
p.
```

Available problem files:

- `quadradolatino.pl` - Latin Square problem
- `futoshiki.pl` - Futoshiki problem

Available algorithm files:

- `backtracking.pl` - Backtracking
- `backFK.pl` - Backtracking with forward checking
- `backFKMelhorado.pl` - Backtracking with forward checking and MRV (minimum remaining values)
  
---

## Grade

[![Grade](https://img.shields.io/badge/Grade-6.0%2F6.0-brightgreen)]()

*Artificial Intelligence - 2025/2026*  
*Universidade de Évora*

## Authors

- [**Miguel Pombeiro**](https://github.com/MiguelPombeiro)
- [**Miguel Rocha**](https://github.com/migueljfrocha)
