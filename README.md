# 🐾 PetAdopt: Microservices-Based Pet Adoption System

Welcome to the **PetAdopt** project! This is a Spring Cloud microservices application with a React frontend, orchestrated using Docker.

---

## 🚀 Getting Started

### 1. Prerequisites
- **Docker Desktop** (Make sure it is running)
- **Node.js** (v18+)

### 2. Launch the Application
Run the following command from the project root to build and start all microservices, databases, and the frontend:

```powershell
docker-compose up --build
```

### 3. Seed the Database
Once the containers are up, you must seed the initial data (users, pets, etc.):

```powershell
docker exec -i petadopt-db psql -U odoo -d authdb < seed.sql
```

---

## 🗄️ Database Access (pgAdmin)

The project uses a single PostgreSQL container (`petadopt-db`) hosting four separate databases.

### Connection Details:
- **Host**: `localhost`
- **Port**: `5432`
- **Username**: `odoo`
- **Password**: `odoo`

### Databases to Register:
1. `authdb` (User accounts & Security)
2. `petdb` (Pet listings)
3. `adoptiondb` (Applications & Workflows)
4. `interactiondb` (Favorites, Reviews, Notifications)

---

## ⚠️ Common Issues & Troubleshooting

### 1. Port 5432 Conflict (PostgreSQL)
If you have PostgreSQL installed locally on Windows, it will block Docker from starting the database. 

**To fix this, stop your local service:**
1. Open **PowerShell** as Administrator.
2. Run: `Stop-Service postgresql*` (or manually stop it in `services.msc`).
3. Restart the Docker containers.

### 2. Authentication Failures
If you receive "Invalid Credentials," ensure you are using the correct password hashes in the database.
- **Default Password**: `password123`
- **Test User**: `ahmed@example.com`

---


---

## 🌐 Port Map
- **Frontend**: `http://localhost:3000`
- **API Gateway**: `http://localhost:8080`
- **Eureka Dashboard**: `http://localhost:8761`
- **PostgreSQL**: `localhost:5432`
