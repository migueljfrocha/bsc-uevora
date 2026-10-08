# Real-Time Communication System (UDP)

A final project for the **Computer Networks** course at Universidade de Évora. This application implements a real-time chat service using **UDP** sockets, featuring user authentication, groups, file transfers, and reliability mechanisms over an unreliable protocol.

## About

This project implements a Client-Server architecture where multiple clients communicate via a central server. The system handles real-time character-by-character messaging (emulating a live typing experience), group management, broadcast messaging, and file transfers using TCP side-channels.

## Technologies
**Language:** C  
**Build Tool:** Make   
**Data Storage:** CSV for persistent user and group data.

---

## How to Run

### Prerequisites
- GCC
- Make

### Steps

**1. Start the Server:**
```bash
cd Server
make
./server
```

**2. Start the Client:**
```bash
cd Client
make
./client
```
**3. Usage**
Once logged in, the following commands are available in the main menu:

- `/help` - Show available commands.
- `/broadcast` - Start a real-time message to everyone. Press ENTER to finish.
- `/pm` - Send a private message. You will be prompted for the recipient's name.
- `/group` - Enter the Group Management API (Create, Invite, Kick, etc.).
- `/sendfile` - Initiate a file transfer.
- `/exit` - Disconnect cleanly from the server.
  
---

## Grade
[![Grade](https://img.shields.io/badge/Grade-19.0%2F20.0-brightgreen)]()

*Computer Networks - 2024/2025*  
*Universidade de Évora*

## Authors

- [**Miguel Pombeiro**](https://github.com/MiguelPombeiro)
- [**Miguel Rocha**](https://github.com/migueljfrocha)
- [**André Zhan**](https://github.com/andr-zhan)
- [**André Gonçalves**](https://github.com/andrefsg05)
