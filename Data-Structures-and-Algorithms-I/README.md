# Spellchecker with Correction Suggestions

A project developed for the **Data Structures and Algorithms I** course at Universidade de Évora. This application implements a spellchecker using Hash Tables to efficiently validate words against a dictionary and generate correction suggestions for misspelled words.

## About

This project focuses on the implementation and usage of **Hash Tables** to solve spell checking. The application reads a text file, verifies the spelling of each word against a provided dictionary, and identifies incorrect words.

For every misspelled word, the system generates a list of possible suggestions based on specific modification rules (inserting, deleting, swapping, or replacing characters). To optimize performance and avoid redundant processing, misspelled words and their suggestions are stored in a secondary Hash Table.

## Technologies
**Language:** C  
**Build Tool:** Make

---

## How to Run

### Prerequisites
- GCC
- Make

### Steps

### Compilation

```bash
make
```

### Execution

```bash
./spellChecker
```

## Result
- Open the output.txt file generated to see the correction of misspelled words.

## Grade

[![Grade](https://img.shields.io/badge/Grade-20.0%2F20.0-brightgreen)]()

*Data Structures and Algorithms I - 2023/2024*  
*Universidade de Évora*

## Authors

- [**Miguel Pombeiro**](https://github.com/MiguelPombeiro)
- [**Miguel Rocha**](https://github.com/migueljfrocha)
