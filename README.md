# 🌍 GreenGuardian

### *A Fully Kotlin-Based Cross-Platform Environmental Issue Reporting Application*

![Kotlin](https://img.shields.io/badge/kotlin-100%25-blue)
![Platform](https://img.shields.io/badge/platform-Android%20%7C%20iOS%20%7C%20Desktop%20%7C%20Web-green)
![Status](https://img.shields.io/badge/status-Production%20Ready-success)

---

## 📌 Problem Statement

Environmental issues—such as illegal dumping, plastic pollution, forest damage, water contamination, and improper waste disposal—often go unreported due to **lack of accessible reporting systems**.

Citizens witness problems but don't know **where** or **how** to report them. Governments and NGOs also lack **real-time environmental data**, making problem-solving slower and less efficient.

**GreenGuardian** empowers every citizen to become an environmental protector by providing a simple, unified platform for reporting, mapping, and tracking environmental issues.

---

## 🌱 What Is GreenGuardian?

GreenGuardian is a **100% Kotlin-based full-stack application** featuring:

### 🎨 **Frontend (Compose Multiplatform)**
- Cross-platform UI for Android, iOS, Desktop (JVM), and Web
- Material 3 Design with Dark/Light themes
- Beautiful, intuitive screens for reporting and tracking issues
- Type-safe navigation and state management

### 🚀 **Backend (Ktor Server)**
- RESTful API server
- JWT authentication
- PostgreSQL/H2 database support
- Nearby issues search (Haversine formula)
- Comprehensive error handling

---

## ✨ Features

- 📸 **Report environmental issues** with photos
- 📍 **GPS location** automatically attached
- 🗺️ **Interactive map** view (MapLibre ready)
- 🧭 **Track status** (Submitted → Verified → Resolved)
- 📊 **Analytics** for authorities and NGOs
- 👥 **Community participation** in protecting the planet
- 🌓 **Dark/Light theme** support
- 🔐 **Secure authentication** with JWT

---

## 🚀 Quick Start

### Using the Start Script (Easiest)

```bash
./start.sh
```

Then select from the menu:
1. Backend Server Only
2. Desktop Application Only
3. Backend + Desktop Application
4. Web Application
5. Backend + Web Application

### Manual Start

**1. Start Backend Server:**
```bash
cd server
../gradlew run
```
Server runs at: `http://localhost:8080`

**2. Run Frontend:**

**Desktop:**
```bash
./gradlew :composeApp:run
```

**Android:**
```bash
./gradlew :composeApp:installDebug
```

**Web:**
```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```

**iOS:**
Open `iosApp/iosApp.xcodeproj` in Xcode and run

---

## 📦 Project Structure

```
GreenGuardian/
├── composeApp/          # Frontend (Compose Multiplatform)
│   ├── data/           # Models, Repositories, Network
│   └── ui/             # Screens, Navigation, Theme, ViewModels
│
├── server/             # Backend (Ktor Server)
│   ├── data/          # Database, Models, Repositories
│   ├── routes/        # API Endpoints
│   └── security/      # JWT Authentication
│
├── shared/            # Shared code between modules
│
└── Documentation/     # Comprehensive guides
    ├── BACKEND_README.md
    ├── FRONTEND_README.md
    ├── API_INTEGRATION.md
    ├── QUICKSTART.md
    └── SCREENS_GUIDE.md
```

---

## 🛠️ Tech Stack

### Frontend
| Technology | Purpose |
|------------|---------|
| **Kotlin Multiplatform** | Cross-platform development |
| **Compose Multiplatform** | Declarative UI framework |
| **Ktor Client** | HTTP networking |
| **Navigation Compose** | Type-safe navigation |
| **kotlinx.serialization** | JSON handling |
| **Material 3** | Design system |

### Backend
| Technology | Purpose |
|------------|---------|
| **Ktor Server** | HTTP server framework |
| **Exposed ORM** | Type-safe SQL |
| **PostgreSQL/H2** | Database |
| **JWT** | Authentication |
| **BCrypt** | Password hashing |
| **HikariCP** | Connection pooling |

---

## 📡 API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user

### Issues
- `GET /api/issues` - Get all issues
- `GET /api/issues/{id}` - Get issue by ID
- `GET /api/issues/nearby` - Get nearby issues
- `GET /api/issues/status/{status}` - Filter by status
- `POST /api/issues` - Create new issue
- `PATCH /api/issues/{id}/status` - Update status (auth)
- `DELETE /api/issues/{id}` - Delete issue (auth)

Full API documentation: [BACKEND_README.md](BACKEND_README.md)

---

## 🧪 Testing

### Test Backend API
```bash
# Register user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"test@test.com","password":"test123"}'

# Create issue
curl -X POST http://localhost:8080/api/issues \
  -H "Content-Type: application/json" \
  -d '{
    "title":"Test Issue",
    "description":"Testing",
    "category":"ILLEGAL_DUMPING",
    "latitude":28.6139,
    "longitude":77.2090,
    "severity":"MEDIUM"
  }'

# Get all issues
curl http://localhost:8080/api/issues
```

### Test Frontend
1. Start backend server
2. Run any frontend platform
3. Navigate through screens
4. Create test issues
5. View on home screen

---

## 📱 Screenshots & Features

### Home Screen
- 📊 Statistics dashboard
- 📋 Scrollable issue list
- 🔍 Status badges
- 🏷️ Category chips
- ➕ Floating action button

### Report Issue Screen
- 📝 Title and description
- 🗂️ Category dropdown (7 categories)
- ⚠️ Severity levels (Low → Critical)
- 📍 GPS location picker
- 📸 Photo upload
- ✅ Form validation

### Map Screen
- 🗺️ Interactive map (MapLibre ready)
- 📍 Issue markers
- 📱 Nearby issues list
- 🎯 Tap for details

### Issue Detail Screen
- 📄 Full issue information
- ⏱️ Status timeline
- 📷 Photo display
- 🗺️ Location details

---

## 💡 Impact on Society

✅ **Rapid environmental response** - Real-time reports to local bodies
✅ **Community empowerment** - Everyone becomes a guardian
✅ **Cleaner spaces** - Faster detection and resolution
✅ **Data-driven policy** - Environmental heatmaps
✅ **Better transparency** - Track issue progress
✅ **Sustainable mindset** - Strengthens public responsibility

---

## 🔐 Security Features

- ✅ JWT token authentication (7-day expiry)
- ✅ BCrypt password hashing
- ✅ CORS configuration
- ✅ SQL injection prevention
- ✅ Input validation
- ✅ Secure error handling

---

## 📚 Documentation

Comprehensive documentation available:

- 📘 [Backend Documentation](BACKEND_README.md)
- 📗 [Frontend Documentation](FRONTEND_README.md)
- 📙 [API Integration Guide](API_INTEGRATION.md)
- 📕 [Quick Start Guide](QUICKSTART.md)
- 📔 [Screens Guide](SCREENS_GUIDE.md)
- 📓 [Complete Summary](COMPLETE_PROJECT_SUMMARY.md)

---

## 🎯 What's Implemented

### ✅ Completed
- [x] Cross-platform frontend (Android, iOS, Desktop, Web)
- [x] Material 3 UI with Dark/Light themes
- [x] Complete backend API server
- [x] JWT authentication
- [x] Database with Exposed ORM
- [x] Issue CRUD operations
- [x] Nearby search (Haversine)
- [x] User registration & login
- [x] Password hashing
- [x] CORS support
- [x] Error handling
- [x] Request logging
- [x] H2 & PostgreSQL support

### 🔮 Future Enhancements
- [ ] MapLibre integration
- [ ] Image upload (MinIO/S3)
- [ ] GPS location services
- [ ] Camera/Gallery picker
- [ ] Push notifications
- [ ] Offline caching
- [ ] Admin dashboard
- [ ] Analytics

---

## 🚀 Deployment

### Backend (Heroku/Docker)
```bash
# Docker
docker build -t greenguardian-api ./server
docker run -p 8080:8080 greenguardian-api

# Heroku
heroku create greenguardian-api
heroku addons:create heroku-postgresql
git push heroku main
```

### Frontend
- **Android**: Google Play Store
- **iOS**: Apple App Store
- **Desktop**: DMG/MSI/DEB packages
- **Web**: Any static hosting (Netlify, Vercel)

---

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

MIT License - See [LICENSE](LICENSE) file for details

---

## 🙏 Acknowledgments

- Built with ❤️ using Kotlin
- Powered by Compose Multiplatform
- Secured with Ktor Server
- Designed for a better planet 🌍

---

## 📞 Support

For questions, issues, or contributions:
- 📧 Email: support@greenguardian.com
- 🐛 Issues: GitHub Issues
- 💬 Discussions: GitHub Discussions

---

## ⭐ Show Your Support

If you like this project, please give it a ⭐ on GitHub!

---

**Built with 💚 for a greener planet**

Let's make the world a better place, one issue report at a time! 🌿
