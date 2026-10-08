# Distributed Management System - Apartment Reservations

A distributed system project developed for the **Distributed Systems** course at Universidade de Évora.

## About

This project implements a **Distributed Management System** for an "Airbnb"-style Apartment Reservation. The architecture consists of a central Server connected to a PostgreSQL database and two distinct client applications, each utilizing a different communication technology to demonstrate flexibility.

The **General Client** is designed for regular users to register, log in, search for apartments using specific filters, and make reservations. This client communicates with the server using **TCP Sockets** and exchanges data via serialized objects. Conversely, the **Admin Client** allows administrators to approve users and apartments, manage resource states, and perform CRUD operations. This module communicates with the server using **Java RMI (Remote Method Invocation)**, for privileged administrative tasks.

## Technologies

-   **Language:** Java
-   **Database:** PostgreSQL
-   **Communication:**
    -   TCP Sockets
    -   Java RMI
-   **Containerization:** Docker

---

## How to Run

### Prerequisites
- Java
- Docker
  
### Steps

**1. Create the local environment file**

```bash
cp .env.example .env
```

On Windows PowerShell:

```powershell
Copy-Item .env.example .env
```

**2. Initialize Docker**
```bash
docker compose up
```

**3. Set up the database**

Open http://localhost:16543 (PgAdmin) and log in with the values from your local `.env` file.
- Email: admin@admin.com
- Password: admin123

Then register a new server in pgAdmin:
- Host name/address: postgres
- Port: 5432
- Database: gestao_apartamentos
- Username: trabalho1
- Password: sistemasDistribuidos

**4. Create and populate the tables**

In the created database, run `database/table_creation.sql` and `database/testing.sql`.

**5. Compile**

```bash
mkdir -p out
javac -d out -classpath "out:lib/postgresql.jar" $(find src -name "*.java")
```

**6. Run the server**

```bash
java -classpath "out:lib/postgresql.jar" server.Server
```

**7. Run the general and admin clients**

```bash
java -classpath "out:lib/postgresql.jar" adminClient.AdminClient
```

```bash
java -classpath "out:lib/postgresql.jar" generalClient.GeneralClient
```

---

## Grade

[![Grade](https://img.shields.io/badge/Grade-19.0%2F20.0-brightgreen)]()

*Distributed Systems - 2025/2026*  
*Universidade de Évora*

## Authors

- [**Miguel Pombeiro**](https://github.com/MiguelPombeiro)
- [**Miguel Rocha**](https://github.com/migueljfrocha)
