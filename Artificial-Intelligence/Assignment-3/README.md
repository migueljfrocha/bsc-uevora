# Two-Player Games

A Prolog assignment about adversarial search and intelligent game agents, developed for the Artificial Intelligence course at Universidade de Évora.

## About

This assignment implements intelligent agents for two deterministic two-player games: a variant of **Nim** and the **Frogs and Toads** game.

The work explores game state representation, terminal states, utility functions, evaluation functions, minimax, depth-limited minimax, alpha-beta pruning, and interactive agent execution.

## Technologies

**Language:** Prolog  
**Concepts:** Minimax, alpha-beta pruning, utility functions, evaluation functions, game agents

---

## How to Run

### Prerequisites

- A Prolog interpreter compatible with the predicates used in the assignment, for example [GNU Prolog](http://gprolog.org/#download)

### Interactive Agent

```prolog
[agenteJog].
agente(problema1, minmax).
```

```prolog
[agenteJog].
agente(problema2, mmAlfaBeta).
```

`agenteJog.pl` is the recommended interactive agent because it prints possible moves to help the player. `agente.pl` can also be used.

### Manual Algorithm Execution

Consult one strategy file and run it with one of the problem files:

```prolog
[minmax].
g(problema1).
```

Available strategy files:

- `minmax.pl` - Minimax
- `minmaxCorte.pl` - Minimax with depth cutoff
- `mmAlfaBeta.pl` - Minimax with alpha-beta pruning
- `mmCAlfaBeta.pl` - Minimax with alpha-beta pruning and depth cutoff

---

## Grade

[![Grade](https://img.shields.io/badge/Grade-6.0%2F6.0-brightgreen)]()

*Artificial Intelligence - 2025/2026*  
*Universidade de Évora*

## Authors

- [**Miguel Pombeiro**](https://github.com/MiguelPombeiro)
- [**Miguel Rocha**](https://github.com/migueljfrocha)
