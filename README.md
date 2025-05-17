# 🚀 CollabSync – Task Management System

**CollabSync** is a **fullstack task management platform** designed to streamline collaboration between individuals and teams. It showcases real-world backend engineering practices using **Java Spring Boot**, **PostgreSQL**, **Kafka**, and **CI/CD**. Built with **clean architecture**, **modular structure**, and a focus on **scalability and maintainability**.

> 🛠️ This is an active work in progress. Core backend modules are implemented and the frontend is under development.

---

## 🔧 Tech Stack

### 📦 Backend
- **Java 21**
- **Spring Boot 3**
- **Spring Data JPA**
- **Spring Security (JWT + Role-based auth)**
- **PostgreSQL**
- **Apache Kafka** – event-driven notifications
- **WebSockets** *(optional, upcoming)* – for real-time UI updates
- **Docker** – for containerized development & deployment
- **Lombok** – boilerplate reduction

### 🖥️ Frontend *(WIP)*
- **React.js**
- **TypeScript**
- **Tailwind CSS**
- **Axios**
- **React Router**

---

## ✅ Implemented Features

### 🧑‍💼 User Module
- User registration & login (JWT-based)
- Role-based access (ADMIN, MEMBER)

### 📁 Project Module
- Create / update / delete projects
- Add & remove collaborators
- Kafka notifications on project changes

### 🧩 Task Module
- Create tasks linked to projects
- Assign tasks to users
- Fetch tasks by project ID

### 💬 Comment Module
- Add comments to tasks
- Fetch comments for a task
- Kafka notification on new comment

### 🔔 Notification Module
- Kafka-based producer/consumer
- Event-driven notifications on project and comment changes

---

## 🔄 In Progress / Coming Soon

- 🌐 **Frontend React UI** with Tailwind CSS
- 📡 **WebSocket-based real-time updates**
- 🧪 **Automated testing**
- 📦 **Docker Compose for local dev setup**
- 🔒 **MFA + OAuth2 support**
- 📊 **Dashboard analytics**

---

## 🧪 How to Run (Backend Only)

### 1. Clone the repository

```
git clone https://github.com/akshatbatra10/CollabSync.git
cd CollabSync
```

## 2. Set up PostgreSQL
Create a database named collabsync_db

Update the application.yml or application-dev.yml with DB credentials

## 3. Start Kafka & PostgreSQL via Docker (Optional)
Use Docker Compose (WIP) or your own local setup for:

- Apache Kafka (port 9092)

- Zookeeper (port 2181)

- PostgreSQL (port 5432)

## 4. Run the Application
Make sure you're using Java 21 and have Maven installed:
```
./mvnw spring-boot:run
```
---

## 🧠 Design Highlights
- ✅ Modular Monolith architecture (microservice-ready)
- 📚 Clean Code & Domain-Driven Design structure
- 🔐 Stateless JWT Security with extensible roles
- 📣 Event-driven communication via Kafka
- 🧩 Clear DTO separation for request/response contracts

---

