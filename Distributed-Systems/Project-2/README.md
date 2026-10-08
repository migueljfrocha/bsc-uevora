# Environmental Monitoring System

A distributed system project developed for the **Distributed Systems** course at Universidade de Évora.

## About

This project implements a **Distributed Environmental Monitoring System** designed to manage temperature and humidity levels across the university. The system ingests data from various IoT devices using multiple communication protocols, processes the information in a central server, and provides management tools via a CLI.

The architecture consists of a **Central Server** connected to a PostgreSQL database, responsible for data ingestion, processing, and storage. The system supports three distinct client simulators: **Client MQTT** for low-power sensors, **Client gRPC** for high-performance edge devices, and **Client REST** for standard HTTP communication. Additionally, an **Admin CLI** is provided for administrators to manage devices and query aggregated metrics from the system.

## Technologies

- **Language:** Java 17+
- **Framework:** Spring Boot
- **Database:** PostgreSQL (with Hibernate/JPA)
- **Communication:**
  - REST API (JSON)
  - gRPC (Protocol Buffers)
  - MQTT (ActiveMQ)
- **Containerization:** Docker

---

## How to Run

### Prerequisites
- Java
- Maven
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
- Database: environmentMonitoring
- Username: trabalho2
- Password: sistemasDistribuidos

**4. Compile and run the server**

```bash
cd server
mvn clean compile
mvn spring-boot:run
```

**5. Compile and run clients and simulators**

Open separate terminals for each simulator you wish to run.

To run **Admin CLI**:
```bash
cd admin-cli
mvn clean compile
mvn exec:java -Dexec.mainClass="pt.uevora.sdist.monitoring.AdminCLI"
```

To run **REST Simulator**:
```bash
cd client-rest
mvn clean compile
mvn exec:java -Dexec.mainClass="pt.uevora.sdist.monitoring.ClientRest"
```

To run **gRPC Simulator**:
```bash
cd client-grpc
mvn clean compile
mvn exec:java -Dexec.mainClass="pt.uevora.sdist.monitoring.ClientGrpc"
```

To run **MQTT Simulator**:
```bash
cd client-mqtt
mvn clean compile
mvn exec:java -Dexec.mainClass="pt.uevora.sdist.monitoring.ClientMqtt"
```

---

## Grade

[![Grade](https://img.shields.io/badge/Grade-18.0%2F20.0-brightgreen)]()

*Distributed Systems - 2025/2026*  
*Universidade de Évora*

## Authors

- [**Miguel Pombeiro**](https://github.com/MiguelPombeiro)
- [**Miguel Rocha**](https://github.com/migueljfrocha)
