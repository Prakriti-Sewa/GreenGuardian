# 🔌 API Integration Guide

## Backend Configuration

The frontend is configured to communicate with the Ktor backend server.

### Base URL Configuration

Located in: `composeApp/src/commonMain/kotlin/in/co/abdev/greenguardian/data/network/NetworkClient.kt`

```kotlin
const val BASE_URL = "http://localhost:8080/api"
```

### Platform-Specific URLs

Different platforms require different localhost addresses:

| Platform | Base URL | Note |
|----------|----------|------|
| Desktop (JVM) | `http://localhost:8080/api` | Direct connection |
| Web Browser | `http://localhost:8080/api` | Same machine |
| Android Emulator | `http://10.0.2.2:8080/api` | Emulator's host loopback |
| iOS Simulator | `http://localhost:8080/api` | macOS localhost |
| Physical Device | `http://<YOUR_IP>:8080/api` | Replace with your machine's IP |

## 📡 API Endpoints

### Issues API

#### 1. Get All Issues
```http
GET /api/issues
```

**Response:**
```json
{
  "success": true,
  "issues": [
    {
      "id": "uuid-1234",
      "title": "Illegal Dumping Near Park",
      "description": "Large pile of waste...",
      "category": "ILLEGAL_DUMPING",
      "latitude": 28.6139,
      "longitude": 77.2090,
      "imageUrl": "http://example.com/image.jpg",
      "status": "SUBMITTED",
      "reportedBy": "user@email.com",
      "reportedAt": 1735392000000,
      "verifiedAt": null,
      "resolvedAt": null,
      "severity": "MEDIUM"
    }
  ]
}
```

#### 2. Get Issue by ID
```http
GET /api/issues/{issueId}
```

**Response:**
```json
{
  "success": true,
  "message": "Issue found",
  "issue": {
    "id": "uuid-1234",
    "title": "...",
    ...
  }
}
```

#### 3. Create New Issue
```http
POST /api/issues
Content-Type: application/json
```

**Request Body:**
```json
{
  "title": "Illegal Dumping Near Park",
  "description": "Large pile of construction waste...",
  "category": "ILLEGAL_DUMPING",
  "latitude": 28.6139,
  "longitude": 77.2090,
  "imageBase64": "data:image/jpeg;base64,/9j/4AAQ...",
  "severity": "MEDIUM"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Issue created successfully",
  "issue": {
    "id": "newly-created-uuid",
    ...
  }
}
```

#### 4. Get Nearby Issues
```http
GET /api/issues/nearby?lat=28.6139&lng=77.2090&radius=10
```

**Query Parameters:**
- `lat` (required): Latitude
- `lng` (required): Longitude  
- `radius` (optional): Radius in kilometers (default: 10)

**Response:**
```json
{
  "success": true,
  "issues": [...]
}
```

### Authentication API

#### 1. Login
```http
POST /api/auth/login
Content-Type: application/json
```

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "securepassword123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "user-uuid",
    "name": "John Doe",
    "email": "user@example.com",
    "role": "CITIZEN"
  }
}
```

#### 2. Register
```http
POST /api/auth/register
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "user@example.com",
  "password": "securepassword123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Registration successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "new-user-uuid",
    "name": "John Doe",
    "email": "user@example.com",
    "role": "CITIZEN"
  }
}
```

## 📦 Data Models

### Issue Category Enum
```kotlin
enum class IssueCategory {
    ILLEGAL_DUMPING,
    PLASTIC_POLLUTION,
    FOREST_DAMAGE,
    WATER_CONTAMINATION,
    WASTE_DISPOSAL,
    AIR_POLLUTION,
    OTHER
}
```

### Issue Status Enum
```kotlin
enum class IssueStatus {
    SUBMITTED,    // Initial state
    VERIFIED,     // Verified by admin
    IN_PROGRESS,  // Being worked on
    RESOLVED,     // Fixed
    REJECTED      // Invalid/duplicate
}
```

### Issue Severity Enum
```kotlin
enum class IssueSeverity {
    LOW,       // Minor issue
    MEDIUM,    // Moderate concern
    HIGH,      // Serious problem
    CRITICAL   // Immediate action needed
}
```

## 🔐 Authentication Flow

### Token Storage (To be implemented)
```kotlin
// Store token after login
val token = authResponse.token
// Save to secure storage (platform-specific)

// Use token in subsequent requests
httpClient.get("/api/issues") {
    header("Authorization", "Bearer $token")
}
```

### Authenticated Requests
All authenticated endpoints should include:
```http
Authorization: Bearer <JWT_TOKEN>
```

## 🔄 Repository Pattern

### IssueRepository
```kotlin
class IssueRepository {
    private val client = NetworkClient.httpClient
    
    suspend fun getIssues(): Result<List<Issue>>
    suspend fun getIssueById(id: String): Result<Issue>
    suspend fun createIssue(request: CreateIssueRequest): Result<Issue>
    suspend fun getNearbyIssues(lat: Double, lng: Double, radius: Double): Result<List<Issue>>
}
```

### Usage in ViewModel
```kotlin
viewModelScope.launch {
    repository.getIssues()
        .onSuccess { issues ->
            _uiState.value = _uiState.value.copy(issues = issues)
        }
        .onFailure { error ->
            _uiState.value = _uiState.value.copy(error = error.message)
        }
}
```

## 🧪 Testing the API

### Using cURL

#### Get all issues:
```bash
curl http://localhost:8080/api/issues
```

#### Create an issue:
```bash
curl -X POST http://localhost:8080/api/issues \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Issue",
    "description": "Testing from cURL",
    "category": "ILLEGAL_DUMPING",
    "latitude": 28.6139,
    "longitude": 77.2090,
    "severity": "MEDIUM"
  }'
```

#### Get nearby issues:
```bash
curl "http://localhost:8080/api/issues/nearby?lat=28.6139&lng=77.2090&radius=10"
```

## 🐛 Error Handling

### Network Errors
```kotlin
try {
    val response = client.get("$baseUrl/issues")
    // Process response
} catch (e: Exception) {
    // Handle network errors
    Result.failure(e)
}
```

### HTTP Error Codes
| Code | Meaning | Frontend Handling |
|------|---------|-------------------|
| 200 | Success | Display data |
| 201 | Created | Show success message |
| 400 | Bad Request | Show validation errors |
| 401 | Unauthorized | Redirect to login |
| 404 | Not Found | Show "not found" message |
| 500 | Server Error | Show retry option |

## 📊 Logging

### Ktor Client Logging
All requests are logged to console:

```
[Ktor] GET http://localhost:8080/api/issues
[Ktor] Response: 200 OK
[Ktor] Body: {"success": true, "issues": [...]}
```

### Enable/Disable Logging
In `NetworkClient.kt`:
```kotlin
install(Logging) {
    logger = Logger.DEFAULT
    level = LogLevel.INFO  // Change to NONE to disable
}
```

## 🔧 Platform-Specific Configuration

### Android
Add to `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

For local development with emulator:
```kotlin
const val BASE_URL = "http://10.0.2.2:8080/api"
```

### iOS
Add to `Info.plist`:
```xml
<key>NSAppTransportSecurity</key>
<dict>
    <key>NSAllowsArbitraryLoads</key>
    <true/>
</dict>
```

### Web
Configure CORS on backend to allow:
```
Origin: http://localhost:8080
```

## 🚀 Production Configuration

### Environment Variables
```kotlin
object Config {
    val BASE_URL = when {
        BuildConfig.DEBUG -> "http://localhost:8080/api"
        else -> "https://api.greenguardian.com/api"
    }
}
```

### SSL/TLS
For production, ensure:
- HTTPS connections
- Certificate pinning
- Secure token storage

## 📱 Network State Handling

### Check Connectivity
```kotlin
// Platform-specific implementation needed
suspend fun isNetworkAvailable(): Boolean {
    // Android: ConnectivityManager
    // iOS: Reachability
    // Desktop/Web: Browser APIs
}
```

### Offline Mode
```kotlin
if (!isNetworkAvailable()) {
    // Load from local cache
    // Show offline indicator
    // Queue requests for later
}
```

## 🎯 Integration Checklist

- [x] Ktor Client configured
- [x] JSON serialization setup
- [x] Repository pattern implemented
- [x] Error handling added
- [x] Loading states managed
- [ ] Token authentication (to be added)
- [ ] Refresh token logic (to be added)
- [ ] Offline caching (to be added)
- [ ] Network state detection (to be added)
- [ ] Request retry logic (to be added)

## 💡 Best Practices

1. **Always handle errors gracefully**
   ```kotlin
   .onFailure { error ->
       _uiState.value = _uiState.value.copy(
           error = error.message ?: "Unknown error"
       )
   }
   ```

2. **Show loading states**
   ```kotlin
   _uiState.value = _uiState.value.copy(isLoading = true)
   ```

3. **Use Result type for safety**
   ```kotlin
   suspend fun getData(): Result<List<Item>>
   ```

4. **Log network requests in debug builds**
   ```kotlin
   level = if (BuildConfig.DEBUG) LogLevel.INFO else LogLevel.NONE
   ```

5. **Implement timeouts**
   ```kotlin
   install(HttpTimeout) {
       requestTimeoutMillis = 15000
       connectTimeoutMillis = 15000
   }
   ```

---

**The frontend is ready to communicate with the backend!** 🚀

Once the backend server is running, all API calls will work seamlessly across all platforms.
