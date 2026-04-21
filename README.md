# Tour — Travel Sharing System

A production-ready backend-first travel sharing platform where users can post photos, write about their trips, and explore journeys shared by others.

---

## Project Structure

```
tour/
├── backend/    ← Main Spring Boot project (PRIMARY)
└── frontend/   ← Minimal Vue 3 placeholder (SECONDARY)
```

---

## Tech Stack

| Layer     | Technology                                     |
|-----------|------------------------------------------------|
| Backend   | Java 8 · Spring Boot 2.7.18 · MyBatis-Plus 3.5 |
| Auth      | Spring Security · JWT (jjwt 0.9.1)             |
| Database  | MySQL 8 (schema in `backend/src/main/resources/schema.sql`) |
| Frontend  | Vue 3 · Vite · Axios                           |
| Build     | Maven 3                                        |

---

## Prerequisites

* JDK 8+ (tested with JDK 17 as well)
* Maven 3.6+
* MySQL 8
* Node.js 18+ (for frontend only)

---

## Database Setup

```sql
-- Create the database and all tables
mysql -u root -p < backend/src/main/resources/schema.sql
```

The schema creates **9 tables** and seeds the three default roles:
`ROLE_USER`, `ROLE_MODERATOR`, `ROLE_ADMIN`.

---

## Backend — How to Run

### Environment Variables

| Variable           | Default                       | Description              |
|--------------------|-------------------------------|--------------------------|
| `DB_HOST`          | `localhost`                   | MySQL host               |
| `DB_PORT`          | `3306`                        | MySQL port               |
| `DB_USER`          | `root`                        | MySQL username           |
| `DB_PASSWORD`      | *(empty)*                     | MySQL password           |
| `JWT_SECRET`       | `TravelApp…Production`        | **Change in production** |
| `FILE_UPLOAD_PATH` | `./uploads`                   | Local file upload dir    |

### Run with Maven

```bash
cd backend

# Export environment variables first
export DB_HOST=localhost
export DB_PORT=3306
export DB_USER=myuser
export DB_PASSWORD=mypass
export JWT_SECRET=change-this-secret-in-production

mvn spring-boot:run
```

The API will be available at **http://localhost:8080**.

Swagger UI: **http://localhost:8080/swagger-ui/index.html**

### Build a JAR

```bash
cd backend
mvn clean package -DskipTests
java -jar target/tour-backend-1.0.0.jar
```

---

## Frontend — How to Run

```bash
cd frontend
npm install
npm run dev
```

The dev server starts on **http://localhost:5173** and calls the backend at `http://localhost:8080/posts`.

---

## Key API Endpoints

| Method | Path                   | Auth     | Description              |
|--------|------------------------|----------|--------------------------|
| POST   | `/auth/register`       | Public   | Register new user        |
| POST   | `/auth/login`          | Public   | Login, returns JWT token |
| GET    | `/posts`               | Public   | List posts (paginated)   |
| GET    | `/posts/{id}`          | Public   | Get post detail          |
| POST   | `/posts`               | Required | Create post              |
| DELETE | `/posts/{id}`          | Required | Delete post              |
| GET    | `/posts/feed`          | Required | Feed from followed users |
| GET    | `/sections`            | Public   | List sections            |
| POST   | `/files/upload`        | Required | Upload file              |
| GET    | `/users/{id}`          | Public   | Get user profile         |
| POST   | `/users/{id}/follow`   | Required | Follow user              |
| POST   | `/posts/{id}/like`     | Required | Like post                |
| POST   | `/posts/{id}/favorite` | Required | Favorite post            |
| POST   | `/comments`            | Required | Add comment              |
| GET    | `/admin/users`         | ADMIN    | List all users           |

Full API documentation is available via Swagger UI once the server is running.

---

## Authentication

Include the JWT token in the `Authorization` header:

```
Authorization: Bearer <your-token>
```

---

## Roles

| Role             | Permissions                                    |
|------------------|------------------------------------------------|
| `ROLE_USER`      | Read, post, comment, like, favorite, follow    |
| `ROLE_MODERATOR` | + Delete posts in assigned section             |
| `ROLE_ADMIN`     | Full CRUD on users, roles, sections, posts     |

---

## Future Extensions (not yet implemented)

* Alibaba Cloud OSS file storage
* Real AI content moderation (hook: `ContentModerationService`)
* Recommendation / feed ranking
* City-level sections
* Amap map integration (hook: `MapUtil.calculateDistance`)
