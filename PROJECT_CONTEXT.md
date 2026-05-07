# PetAdopt — Project Context

> **Living document.** Keep this up to date as the project evolves.
> Last updated: 2026-05-06

---

## 1. Project Summary

PetAdopt is a microservices-based web application that connects animal shelters with prospective pet adopters. It digitalizes the end-to-end adoption workflow — from browsing and favoriting pets, through submitting adoption applications, to shelter-side review and approval.

---

## 2. Architecture Overview

```
┌──────────────┐        ┌──────────────────┐        ┌─────────────────┐
│   React UI   │──HTTP──▶  API Gateway     │──route──▶  Microservices  │
│ (Vite, :5173)│        │ (Spring Cloud    │        │                 │
│              │        │  :8080)          │        │  ┌─ Auth (:8082)│
└──────────────┘        └────────┬─────────┘        │  ├─ Pet  (:8081)│
                                 │                  │  ├─ Adoption    │
                        ┌────────▼─────────┐        │  └─ Interaction │
                        │  Eureka Server   │        └─────────────────┘
                        │  (Discovery      │
                        │   :8761)         │        ┌─────────────────┐
                        └──────────────────┘        │  PostgreSQL     │
                                                    │  (:5432)        │
                                                    └─────────────────┘
```

All frontend requests go through **one** entry point: the API Gateway at `http://localhost:8080`.

The gateway uses Spring Cloud + Eureka for service discovery and routes requests by path prefix.

---

## 3. Services & Ownership

| Service             | Owner (Dev) | Branch                | Port  | Description                                   |
|---------------------|-------------|-----------------------|-------|-----------------------------------------------|
| Eureka Server       | Dev 1       | `origin/dev1`         | 8761  | Service discovery registry                    |
| API Gateway         | Dev 1       | `origin/dev1`         | 8080  | Single entry point, routing, CORS, token fwd  |
| Auth Service        | Dev 2       | `origin/feat/Dev2`    | 8082  | JWT auth, registration, RBAC                  |
| Pet Service         | Dev 3       | `origin/Dev3`         | 8081  | Pet CRUD, filtering, AOP logging              |
| Adoption Service    | Dev 4       | `origin/adoption-service` | 8083  | Adoption applications, status transitions     |
| Interaction Service | Dev 5       | `origin/Dev5`         | 8084  | Favorites, reviews, notifications             |
| Frontend (React)    | Dev 6       | `origin/testing`      | 5173  | UI — connects to API Gateway only             |
| Docker / Infra      | Dev 1       | `origin/dev1`         | —     | docker-compose.yml for full stack             |

---

## 4. Branch Map

| Branch                  | Developer | Feature / Responsibility               |
|-------------------------|-----------|----------------------------------------|
| `origin/main`           | —         | Stable base (merged)                   |
| `origin/dev1`           | Dev 1     | Eureka, API Gateway, Docker, Infra     |
| `origin/feat/Dev2`      | Dev 2     | Auth Service (JWT, RBAC)               |
| `origin/Dev3`           | Dev 3     | Pet Service + AOP logging              |
| `origin/adoption-service` | Dev 4   | Adoption workflow engine               |
| `origin/Dev5`           | Dev 5     | Interaction Service (favs, reviews, notifs) |
| `origin/testing`        | Dev 6     | Frontend integration (React)           |

---

## 5. API Endpoint Map

All endpoints are prefixed with the API Gateway base: `http://localhost:8080`

### Auth Service (`/api/auth/**`)

| Method | Path                      | Description                  | Auth? |
|--------|---------------------------|------------------------------|-------|
| POST   | `/api/auth/register`      | Register new adopter user    | No    |
| POST   | `/api/auth/register/shelter` | Register shelter account (pending approval) | No |
| POST   | `/api/auth/login`         | Login, returns JWT           | No    |
| POST   | `/api/auth/logout`        | Logout / invalidate token    | Yes   |
| POST   | `/api/auth/refresh`       | Refresh access token         | Yes   |
| GET    | `/api/auth/me`            | Get current user profile     | Yes   |

### Pet Service (`/api/pets/**`)

| Method | Path                      | Description                           | Auth?  |
|--------|---------------------------|---------------------------------------|--------|
| GET    | `/api/pets`               | List all pets (query: species, status, breed) | No |
| GET    | `/api/pets/{id}`          | Get pet by ID                         | No     |
| POST   | `/api/pets`               | Add new pet                           | Yes (Employee/Admin) |
| PUT    | `/api/pets/{id}`          | Update pet                            | Yes (Employee/Admin) |
| DELETE | `/api/pets/{id}`          | Delete pet                            | Yes (Employee/Admin) |

### Adoption Service (`/api/adoption-requests/**`)

| Method | Path                              | Description                    | Auth? |
|--------|-----------------------------------|--------------------------------|-------|
| POST   | `/api/adoption-requests`          | Submit adoption application    | Yes (Adopter) |
| GET    | `/api/adoption-requests/user/{adopterId}` | Get user's applications | Yes   |
| PUT    | `/api/adoption-requests/{id}/approve` | Approve application         | Yes (Employee) |
| PUT    | `/api/adoption-requests/{id}/reject`  | Reject application (+ reason) | Yes (Employee) |

### Interaction Service — Favorites (`/api/favorites/**`)

| Method | Path                          | Description                  | Auth? |
|--------|-------------------------------|------------------------------|-------|
| POST   | `/api/favorites`              | Add pet to favorites         | Yes   |
| GET    | `/api/favorites/user/{userId}` | Get user's favorites        | Yes   |
| DELETE | `/api/favorites?userId=&petId=` | Remove favorite             | Yes   |

### Interaction Service — Reviews (`/api/reviews/**`)

| Method | Path                          | Description                  | Auth? |
|--------|-------------------------------|------------------------------|-------|
| POST   | `/api/reviews`                | Create a review              | Yes   |
| GET    | `/api/reviews/pet/{petId}`    | Get reviews for a pet        | No    |
| GET    | `/api/reviews/adopter/{adopterId}` | Get reviews by adopter  | Yes   |
| PUT    | `/api/reviews/{id}`           | Update review                | Yes   |
| DELETE | `/api/reviews/{id}`           | Delete review                | Yes   |

### Interaction Service — Notifications (`/api/notifications/**`)

| Method | Path                                     | Description              | Auth? |
|--------|------------------------------------------|--------------------------|-------|
| POST   | `/api/notifications`                     | Create notification      | Yes   |
| GET    | `/api/notifications/user/{userId}`       | Get user's notifications | Yes   |
| GET    | `/api/notifications/user/{userId}/unread` | Get unread notifications | Yes   |
| PATCH  | `/api/notifications/{id}/read`           | Mark as read             | Yes   |
| DELETE | `/api/notifications/{id}`                | Delete notification      | Yes   |

### Legacy / Old Backend Endpoints (still in frontend, MUST MIGRATE)

These are the old .NET-era endpoints currently hardcoded in the frontend:

| Old Path               | New Equivalent                              |
|------------------------|---------------------------------------------|
| `/shelter/pets`        | `/api/pets` (with owner filter)             |
| `/shelter/pets/{id}`   | `/api/pets/{id}`                            |
| `/shelter/requests`    | `/api/adoption-requests/user/{id}` or similar |
| `/shelter/requests/{id}/accept` | `/api/adoption-requests/{id}/approve` |
| `/shelter/requests/{id}/reject` | `/api/adoption-requests/{id}/reject`  |
| `/admin/pets/status/{filter}` | `/api/pets?status={filter}`          |
| `/admin/pets/{id}/approve`    | `/api/pets/{id}` (PUT with status)   |
| `/admin/pets/{id}/reject`     | `/api/pets/{id}` (PUT with status)   |
| `/admin/users/status/{filter}` | `/api/auth/**` (TBD with Dev 2)     |
| `/admin/users/{id}/approve`   | `/api/auth/**` (TBD with Dev 2)     |
| `/admin/users/{id}/reject`    | `/api/auth/**` (TBD with Dev 2)     |

---

## 6. Frontend Integration Status

### Completed
- [x] React + Vite project scaffolded
- [x] Routing set up (react-router-dom v7)
- [x] Axios `apiClient` with JWT interceptor exists (`services/apiClient.js`)
- [x] AuthContext with login/logout/validateSession
- [x] NotificationContext with SignalR connection
- [x] ProtectedRoute component for role-based route guards
- [x] All pages built (Login, Register, Browse, Favorites, Pet Details, etc.)

### Pending — Must Do
- [x] **Change `apiClient.js` BASE_URL** from `http://localhost:5251/api` → `http://localhost:8080/api`
- [x] **Update `vite.config.js` proxy** from `localhost:5251` → `localhost:8080`
- [x] **Refactor all old `.NET` endpoint paths** to match new Spring Boot endpoints
- [x] **Update SignalR URLs** (3 hardcoded instances) → new notification approach (REST polling or SSE; SignalR is .NET-specific)
- [x] **Align request/response shapes** — old backend used `{ data: { ... } }` wrappers, Spring Boot returns objects directly
- [x] **Update favorites API calls** — new backend requires `userId` in path/query, not just JWT-based
- [x] **Update notifications API calls** — new backend requires `userId` in path
- [x] **Update adoption requests** — new backend uses `user/{adopterId}` path instead of JWT-inferred
- [x] **Handle new error response format** — Spring Boot returns `{ timestamp, status, error, message, path }` vs old backend's flat strings
- [x] **Test JWT flow end-to-end** — registration → login → token storage → protected requests
- [x] **Remove `@microsoft/signalr` dependency** (not needed for Spring Boot backend)
- [x] **Remove `signalRService.js`** (deprecated)

---

## 7. How to Run Locally

### Full Stack (Docker Compose) — from Dev 1's branch
```bash
# From repo root, after merging dev1's docker-compose:
docker-compose up --build
```
This starts: PostgreSQL (:5432), Eureka (:8761), API Gateway (:8080), Pet Service (:8081)

### Frontend Only
```bash
cd frontend
npm install
npm run dev
# App runs at http://localhost:5173
# Proxies /api/* to the API Gateway at localhost:8080 (after vite.config.js is updated)
```

### Individual Backend Services (without Docker)
```bash
cd backend/pet-service
./mvnw spring-boot:run
# Runs on :8081, requires PostgreSQL and Eureka to be available
```

---

## 8. Tech Stack

| Layer       | Technology                                 |
|-------------|--------------------------------------------|
| Frontend    | React 19, Vite 8, Axios, react-router-dom v7 |
| Styling     | Tailwind CSS (CDN), custom CSS             |
| Auth (FE)   | JWT stored in localStorage, jwt-decode     |
| Backend     | Java 17+, Spring Boot 3, Spring Cloud      |
| API Gateway | Spring Cloud Gateway                       |
| Discovery   | Spring Cloud Eureka                        |
| Database    | PostgreSQL 15                              |
| DevOps      | Docker, Docker Compose                     |

---

## 9. User Roles

| Role     | Frontend Label | Can Do                                                |
|----------|---------------|-------------------------------------------------------|
| Admin    | `Admin`       | Manage users, approve/reject shelter registrations, manage pets |
| Employee | `Shelter`     | Add/edit/delete pets, review adoption applications    |
| Adopter  | `Adopter`     | Browse pets, submit applications, manage favorites    |

---

## 10. Notes

- The frontend was originally built for a **.NET backend** (port 5251). The migration to Spring Boot microservices (port 8080) is in progress.
- SignalR (used for real-time notifications) has been **removed** and replaced with REST polling.
- The API Gateway now has routes for all microservices: `/api/pets/**`, `/api/auth/**`, `/api/adoption-requests/**`, `/api/favorites/**`, `/api/reviews/**`, and `/api/notifications/**`.
