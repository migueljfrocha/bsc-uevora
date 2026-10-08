# Eventastic - Event Management System

A backend system for managing event lifecycles, registrations, and payments, built with Java for the Methods and Software Development course at Universidade de Évora.

## About

This project implements a system named **Eventastic**, designed to centralize information related to events, such as conferences, workshops, and meetups. It allows administrators to manage events and registration phases, while participants can register and track their payment status.

The project was developed in two phases:
1.  **Specification:** Requirement analysis, use case definitions, and UML modeling, originally made in the repository wiki.
   
2.  **Implementation:** Java implementation acting as a library/API with a CLI demonstration, following the specification made in the first phase and its constraints.

## Technologies

- **Language:** Java 21
- **Build Tool:** Maven
- **Testing:** JUnit 5

## How to Run

### Prerequisites
- Java 21+
- Maven

### Steps

To run the project:

```bash
mvn exec:java
```

### Workflow Visualization
Since the original commit history is not available here, the following graph illustrates the branching and versioning strategy used during development:

```mermaid
gitGraph
   commit id: "Initial Start"
   branch develop
   checkout develop

   branch feature/ISS1
   checkout feature/ISS1
   commit id: "43d5200" msg: "ISS1: Dirs & POM"
   commit id: "1b3b5d6" msg: "ISS1: Update POM/GitIgnore"
   checkout develop
   merge feature/ISS1

   branch feature/ISS2
   checkout feature/ISS2
   commit id: "cb7aafd" msg: "ISS2: EventReg & User"
   commit id: "14bc0d8" msg: "ISS2: Staff & User Update"
   commit id: "6c12449" msg: "ISS2: Event & Operator"
   commit id: "81119de" msg: "ISS2: RegistrationPhase"
   commit id: "838ee00" msg: "ISS2: Domain Classes"
   checkout develop
   merge feature/ISS2

   branch feature/ISS3
   checkout feature/ISS3
   commit id: "1c644da" msg: "ISS3: Options & Validations"
   commit id: "ffffb5b" msg: "ISS3: Event CRUD"
   checkout develop
   merge feature/ISS3

   branch feature/ISS4
   checkout feature/ISS4
   commit id: "3dcea42" msg: "ISS4: List Admin"
   commit id: "87feabe" msg: "ISS4: List Use"
   checkout develop
   merge feature/ISS4

   branch feature/ISS5
   checkout feature/ISS5
   commit id: "7d0a9b3" msg: "ISS5: Event Details"
   checkout develop
   merge feature/ISS5

   branch feature/ISS6
   checkout feature/ISS6
   commit id: "2530d15" msg: "ISS6: Invites Class"
   commit id: "47dc2af" msg: "ISS6: Accept Logic"
   commit id: "33d705d" msg: "ISS6: Remove Operator"
   checkout develop
   merge feature/ISS6

   branch feature/ISS7
   checkout feature/ISS7
   commit id: "7d7c8c0" msg: "ISS7: Active Phase"
   commit id: "c4cf026" msg: "ISS7: Transfer Desc"
   commit id: "2c791c4" msg: "ISS7: Calc Value"
   commit id: "537e8f2" msg: "ISS7: Payment Details"
   checkout develop
   merge feature/ISS7

   branch feature/ISS8
   checkout feature/ISS8
   commit id: "3274970" msg: "ISS8: Search Participant"
   commit id: "0ed0d01" msg: "ISS8: Export List"
   checkout develop
   merge feature/ISS8

   branch feature/ISS9
   checkout feature/ISS9
   commit id: "685cd61" msg: "ISS9: Reg Status"
   commit id: "c1befd3" msg: "ISS9: Event Registration"
   checkout develop
   merge feature/ISS9

   branch feature/ISS10
   checkout feature/ISS10
   commit id: "318e5ef" msg: "ISS10: Refactoring"
   commit id: "e602897" msg: "ISS10: User Registration"
   commit id: "b712f44" msg: "ISS10: Admin Funcs"
   commit id: "b10f010" msg: "ISS10: Operator Funcs"
   commit id: "a0f4dcf" msg: "ISS10: Participant Funcs"
   commit id: "a557e93" msg: "ISS10: Event Funcs"
   checkout develop
   merge feature/ISS10

   branch feature/ISS11
   checkout feature/ISS11
   commit id: "4bbbfd5" msg: "ISS11: Tests"
   commit id: "f19b57b" msg: "ISS11: Example Lib"
   checkout develop
   merge feature/ISS11

   branch feature/ISS12
   checkout feature/ISS12
   commit id: "3585928" msg: "ISS12: Report Start"
   commit id: "04a654d" msg: "ISS12: Report Final"
   checkout develop
   merge feature/ISS12

   checkout main
   merge develop
   commit id: "6747ebb" msg: "Final: Dependencies"
   commit id: "dab4866" msg: "Final: Comments & Polish"
```

## Grade

**Part 1**: 

[![Grade](https://img.shields.io/badge/Grade-20.0%2F20.0-brightgreen)]()

**Part 2**:

[![Grade](https://img.shields.io/badge/Grade-20.0%2F20.0-brightgreen)]()

*Methods and Software Development - 2025/2026*  
*Universidade de Évora*

## Authors

- [**Miguel Pombeiro**](https://github.com/MiguelPombeiro)
- [**Miguel Rocha**](https://github.com/migueljfrocha)

---

## Additional Notes

The first phase was implemented in the wiki of the submitted repository, which had to be taken down, but its contents are preserved as Markdown files.

In the second phase, the goal was to create issues to implement the use cases and manage branches and versions with regular commits. Since the original repository is no longer available here, that workflow is represented as a graph.
