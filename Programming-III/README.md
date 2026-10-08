# Battleship AI Agent

An autonomous battleship game agent implemented in OCaml, capable of managing its own board, implementing intelligent attack strategies, and communicating via text-based protocol for the Programming III course at Universidade de Évora.

## About

This project implements an interactive battleship agent that plays autonomously against other agents or test scripts. The agent manages defense and attack boards, implements smart targeting strategies including "hunt" and "destroy" modes, and communicates through stdin/stdout using a text-based protocol.

## Technologies

**Language:** OCaml  
**Communication:** Text protocol via stdin/stdout

---

## How to Run

### Prerequisites
- OCaml

### Steps

**1. Build:**

```bash
ocamlopt -o agente GameState.ml Utils.ml Defense.ml Attack.ml Main.ml
```

**2. Run:**

```bash
./agente
```
Example of configuration phase:

```
init N                    # Set board size to N×N (default 8)
barco <name> L1 C1 L2 C2  # Place ship at coordinates
random                    # Auto-place all ships
vou eu                    # Start game (agent goes first)
vai tu                    # Start game (opponent goes first)
```
Protocol response meanings:
```
tiro L C                  # Fire at (L, C)
água                      # Shot missed
tiro <ship>               # Hit a ship (not sunk)
afundado <ship>           # Ship sunk
perdi                     # All ships sunk (game over)
```

There's no script here to test it running automatically. You can test the agent manually by running it and typing the protocol commands into the terminal (stdin) to simulate an opponent, or you can create a script to send the commands automatically.

---

## Grade

[![Grade](https://img.shields.io/badge/Grade-17.0%2F20.0-brightgreen)]()

*Programming III - 2025/2026*  
*Universidade de Évora*

## Authors

- [**Miguel Pombeiro**](https://github.com/MiguelPombeiro)
- [**Miguel Rocha**](https://github.com/migueljfrocha)

---

## Additional Notes

### Possible Improvements
- Eliminate mutable state using functional approach
- Pass state as function arguments (Prolog-style)
- Custom ship names support
- More comprehensive test coverage
