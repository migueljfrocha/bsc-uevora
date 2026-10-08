# Room Rent - Web Application

A full-stack web application for managing room rental advertisements built with Spring Boot, Thymeleaf, and PostgreSQL for the Web Technologies course at Universidade de Évora.

## About

This project implements a complete web platform where users can post room offers or search requests, send messages to advertisers, and administrators can manage users and advertisements. The application includes secure authentication, database persistence, and a responsive interface.

## Technologies

- **Backend:** Java, Spring Boot, Spring Security, JPA/Hibernate
- **Frontend:** Thymeleaf, HTML, CSS, JavaScript
- **Database:** PostgreSQL
- **Infrastructure:** Docker, Maven

---

## How to Run

### Prerequisites
- Java 17+
- Maven
- Docker & Docker Compose

### Steps

**1. Create the local environment file:**
```bash
cp .env.example .env
```

On Windows PowerShell:
```powershell
Copy-Item .env.example .env
```

**2. Start PostgreSQL database:**
```bash
docker-compose up -d
```

**3. Configure database (if needed):**

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/roomrent
spring.datasource.username=postgres
spring.datasource.password=yourpassword
```

**4. Build and run:**
```bash
mvn spring-boot:run
```

**5. Access the application:**
```
http://localhost:8080
```

**6. Default admin credentials:**
```
Username: admin
Password: admin123
```

---

## Grade
[![Grade](https://img.shields.io/badge/Grade-18.0%2F20.0-brightgreen)]()

*Web Technologies - 2025/2026*  
*Universidade de Évora*

## Authors

- [**Miguel Pombeiro**](https://github.com/MiguelPombeiro)
- [**Miguel Rocha**](https://github.com/migueljfrocha)

---

## Additional Notes

### Known Limitations
- No real payment processing (simulation only)
- No file uploads for room images
- Limited search functionality (no multi-criteria)
- Limited fault tolerance and server connectivity error handling
