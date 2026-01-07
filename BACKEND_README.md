# 🚀 GreenGuardian Backend API

## Overview

A fully-featured Ktor backend server for the GreenGuardian environmental issue reporting platform, built entirely in Kotlin.

## 🏗️ Architecture

```
server/src/main/kotlin/in/co/abdev/greenguardian/
├── data/
│   ├── model/
│   │   └── Models.kt           # Database tables & DTOs
│   ├── repository/
│   │   ├── IssueRepository.kt  # Issue CRUD operations
│   │   └── UserRepository.kt   # User & auth operations
│   └── DatabaseFactory.kt      # Database configuration
├── routes/
│   ├── IssueRoutes.kt          # Issue API endpoints
│   └── AuthRoutes.kt           # Authentication endpoints
├── security/
│   └── JwtConfig.kt            # JWT authentication setup
└── Application.kt              # Main server configuration
```

## 📦 Tech Stack

| Technology | Purpose |
|------------|---------|
| **Ktor Server** | HTTP server framework |
| **Exposed ORM** | Database ORM |
| **H2 Database** | In-memory database (development) |
| **PostgreSQL** | Production database (ready) |
| **HikariCP** | Connection pooling |
| **JWT** | Token-based authentication |
| **BCrypt** | Password hashing |
| **kotlinx.serialization** | JSON serialization |

## 🚀 Running the Server

### Development Mode (H2 Database)

```bash
cd server
../gradlew run
```

Server will start at: `http://localhost:8080`

### Production Mode (PostgreSQL)

Set environment variables:
```bash
export USE_H2=false
export DATABASE_URL=jdbc:postgresql://localhost:5432/greenguardian
export DB_USER=postgres
export DB_PASSWORD=yourpassword

../gradlew run
```

## 📡 API Endpoints

### Authentication

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "securepass123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Registration successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "uuid",
    "name": "John Doe",
    "email": "john@example.com",
    "role": "CITIZEN"
  }
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "securepass123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "uuid",
    "name": "John Doe",
    "email": "john@example.com",
    "role": "CITIZEN"
  }
}
```

### Issues

#### Get All Issues
```http
GET /api/issues
```

**Response:**
```json
{
  "success": true,
  "issues": [
    {
      "id": "uuid",
      "title": "Illegal Dumping Near Park",
      "description": "Large pile of waste...",
      "category": "ILLEGAL_DUMPING",
      "latitude": 28.6139,
      "longitude": 77.2090,
      "imageUrl": null,
      "status": "SUBMITTED",
      "reportedBy": "john@example.com",
      "reportedAt": 1735392000000,
      "verifiedAt": null,
      "resolvedAt": null,
      "severity": "MEDIUM"
    }
  ]
}
```

#### Get Issue by ID
```http
GET /api/issues/{id}
```

#### Get Nearby Issues
```http
GET /api/issues/nearby?lat=28.6139&lng=77.2090&radius=10
```

Query Parameters:
- `lat` (required): Latitude
- `lng` (required): Longitude
- `radius` (optional): Radius in kilometers (default: 10)

#### Get Issues by Status
```http
GET /api/issues/status/{status}
```

Valid statuses: `SUBMITTED`, `VERIFIED`, `IN_PROGRESS`, `RESOLVED`, `REJECTED`

#### Create Issue
```http
POST /api/issues
Content-Type: application/json

{
  "title": "Illegal Dumping Near Park",
  "description": "Large pile of construction waste...",
  "category": "ILLEGAL_DUMPING",
  "latitude": 28.6139,
  "longitude": 77.2090,
  "imageBase64": null,
  "severity": "MEDIUM"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Issue created successfully",
  "issue": {
    "id": "new-uuid",
    ...
  }
}
```

#### Update Issue Status (Authenticated)
```http
PATCH /api/issues/{id}/status?status=VERIFIED
Authorization: Bearer {token}
```

#### Delete Issue (Authenticated)
```http
DELETE /api/issues/{id}
Authorization: Bearer {token}
```

## 🗃️ Database Schema

### Users Table
```sql
CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'CITIZEN',
    created_at TIMESTAMP DEFAULT NOW()
);
```

### Issues Table
```sql
CREATE TABLE issues (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(50) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    image_url VARCHAR(500),
    status VARCHAR(50) DEFAULT 'SUBMITTED',
    severity VARCHAR(50) DEFAULT 'MEDIUM',
    reported_by VARCHAR(255) NOT NULL,
    reported_at TIMESTAMP DEFAULT NOW(),
    verified_at TIMESTAMP,
    resolved_at TIMESTAMP,
    user_id UUID REFERENCES users(id)
);
```

## 🔐 Authentication

### JWT Token
The server uses JWT (JSON Web Tokens) for authentication.

**Token Structure:**
```json
{
  "userId": "uuid",
  "email": "user@example.com",
  "exp": 1735392000
}
```

**Token Expiration:** 7 days

### Using Authenticated Endpoints
Include the token in the Authorization header:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 🔒 Security Features

- ✅ Password hashing with BCrypt
- ✅ JWT token-based authentication
- ✅ CORS configuration
- ✅ SQL injection prevention (Exposed ORM)
- ✅ Input validation
- ✅ Error handling
- ✅ Request logging

## 📊 Features

### Implemented
- [x] User registration & login
- [x] JWT authentication
- [x] Issue CRUD operations
- [x] Nearby issues (Haversine formula)
- [x] Status filtering
- [x] Password hashing
- [x] Database migrations
- [x] CORS support
- [x] Error handling
- [x] Request logging
- [x] H2 in-memory database
- [x] PostgreSQL support

### To Be Added
- [ ] Image upload to storage (MinIO/S3)
- [ ] Email notifications
- [ ] Admin dashboard APIs
- [ ] Issue comments
- [ ] Analytics endpoints
- [ ] Rate limiting
- [ ] API versioning

## 🧪 Testing the API

### Using cURL

**Register a user:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

**Create an issue:**
```bash
curl -X POST http://localhost:8080/api/issues \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Issue",
    "description": "Testing the API",
    "category": "ILLEGAL_DUMPING",
    "latitude": 28.6139,
    "longitude": 77.2090,
    "severity": "MEDIUM"
  }'
```

**Get all issues:**
```bash
curl http://localhost:8080/api/issues
```

**Get nearby issues:**
```bash
curl "http://localhost:8080/api/issues/nearby?lat=28.6139&lng=77.2090&radius=10"
```

### Using Postman

Import the API collection (create one from the endpoints above).

## ⚙️ Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `USE_H2` | `true` | Use H2 in-memory database |
| `DATABASE_URL` | `jdbc:postgresql://localhost:5432/greenguardian` | PostgreSQL URL |
| `DB_USER` | `postgres` | Database username |
| `DB_PASSWORD` | `postgres` | Database password |

### application.conf (Optional)

Create `server/src/main/resources/application.conf`:
```hocon
ktor {
    deployment {
        port = 8080
        host = 0.0.0.0
    }
    application {
        modules = [ in.co.abdev.greenguardian.ApplicationKt.module ]
    }
}
```

## 🐛 Error Responses

All errors follow a consistent format:

```json
{
  "success": false,
  "message": "Error description"
}
```

### HTTP Status Codes
- `200 OK` - Successful request
- `201 Created` - Resource created
- `400 Bad Request` - Invalid input
- `401 Unauthorized` - Authentication required
- `404 Not Found` - Resource not found
- `409 Conflict` - Resource already exists
- `500 Internal Server Error` - Server error

## 📝 Logging

The server logs all API requests:

```
2025-12-29 14:30:00.123 [eventLoopGroupProxy-4-1] INFO  Application - GET /api/issues
2025-12-29 14:30:00.456 [eventLoopGroupProxy-4-1] INFO  Application - POST /api/issues
```

## 🔄 Database Migration

The database schema is automatically created on server startup using Exposed's `SchemaUtils.create()`.

For production, consider using proper migration tools like Flyway or Liquibase.

## 🚀 Deployment

### Docker (Coming Soon)
```dockerfile
FROM gradle:jdk11 AS builder
COPY . /app
WORKDIR /app
RUN gradle :server:build

FROM openjdk:11-jre-slim
COPY --from=builder /app/server/build/libs/*.jar /app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app.jar"]
```

### Heroku
```bash
heroku create greenguardian-api
heroku addons:create heroku-postgresql
git push heroku main
```

## 📚 Code Examples

### Creating a Custom Repository Method

```kotlin
class IssueRepository {
    fun getIssuesByUser(userEmail: String): List<IssueDTO> = transaction {
        Issues.select { Issues.reportedBy eq userEmail }
            .map { it.toIssueDTO() }
    }
}
```

### Adding a New Route

```kotlin
fun Route.customRoutes() {
    get("/api/custom") {
        call.respond(mapOf("message" to "Custom endpoint"))
    }
}
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make changes
4. Write tests
5. Submit a pull request

## 📄 License

MIT License

---

**Backend is ready! Start the server and connect your frontend!** 🚀🌿
