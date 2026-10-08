# Salvar Comida - Food Waste Platform

A web interface for food waste reduction, allowing restaurants to advertise expiring meals and clients to reserve them. Built with vanilla JavaScript, HTML, and CSS for the Web Technologies course at Universidade de Évora.

## About

This project implements a client-side web interface with four distinct user flows: visitors (public), clients, restaurants, and administrators. The platform uses a hybrid MPA/SPA approach, where each user type has its dedicated HTML page with SPA-style interactions.

## Technologies

**Languages:** HTML, CSS, JavaScript  
**Architecture:** Hybrid MPA/SPA  
**API Communication:** XMLHttpRequest 

---

## How to Run

### Prerequisites
- Modern web browser
- Local web server

### Steps

**Start a local web server:**

```bash
npx http-server -p 8000 -o
```

**Or use VS Code Live Server:**
- Right-click on `index.html` → "Open with Live Server"

**Open in browser (verify the port):**
```
http://localhost:8000 or http://localhost:5500
```

---

## Grade
[![Grade](https://img.shields.io/badge/Grade-17.0%2F20.0-brightgreen)]()

*Web Technologies - 2025/2026*  
*Universidade de Évora*

## Authors

- [**Miguel Pombeiro**](https://github.com/MiguelPombeiro)
- [**Miguel Rocha**](https://github.com/migueljfrocha)

---

## Additional Notes

This project might not fully work, because the API provided by the teacher might no longer be active.

### Known Limitations
- No real authentication system
- Client-side only
- Simulated user sessions
- Limited error handling for network failures