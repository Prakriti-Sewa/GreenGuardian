# GreenGuardian Frontend

## 🎨 Overview

The GreenGuardian frontend is built using **Compose Multiplatform**, providing a native experience across Android, iOS, Desktop (JVM), and Web platforms with a single codebase written in Kotlin.

## 📦 Architecture

The frontend follows clean architecture principles with clear separation of concerns:

```
composeApp/src/commonMain/kotlin/in/co/abdev/greenguardian/
├── data/
│   ├── model/          # Data models and DTOs
│   ├── network/        # Ktor HTTP client configuration
│   └── repository/     # Repository layer for API calls
├── ui/
│   ├── navigation/     # Navigation graph and routes
│   ├── screens/        # UI screens (Home, Report, Map, Detail)
│   ├── theme/          # Theme configuration (Dark/Light mode)
│   └── viewmodel/      # ViewModels for state management
└── App.kt              # Main application entry point
```

## ✨ Features Implemented

### 1. **Home Screen**
- ✅ Display list of environmental issues
- ✅ Statistics dashboard (Total, Pending, Resolved)
- ✅ Issue cards with status badges
- ✅ Navigation to issue details
- ✅ Bottom navigation bar
- ✅ Floating action button for quick reporting

### 2. **Report Issue Screen**
- ✅ Form with title, description fields
- ✅ Category selection dropdown (7 categories)
- ✅ Severity level selection (Low, Medium, High, Critical)
- ✅ Location picker (GPS integration ready)
- ✅ Photo upload capability (ready for integration)
- ✅ Form validation
- ✅ Loading states and error handling

### 3. **Map Screen**
- ✅ Map placeholder (ready for MapLibre integration)
- ✅ List of nearby issues
- ✅ Location-based filtering

### 4. **Issue Detail Screen**
- ✅ Full issue information display
- ✅ Status tracking with timeline
- ✅ Category and severity badges
- ✅ Location details with map link
- ✅ Photo display (when available)
- ✅ Reported/Verified/Resolved timestamps

### 5. **Theme Support**
- ✅ Light and Dark mode
- ✅ Material 3 Design System
- ✅ Green-themed color scheme
- ✅ Automatic system theme detection

### 6. **Networking**
- ✅ Ktor Client setup
- ✅ JSON serialization with kotlinx.serialization
- ✅ Repository pattern for API calls
- ✅ Error handling
- ✅ Logging

### 7. **State Management**
- ✅ ViewModels with StateFlow
- ✅ Lifecycle-aware state collection
- ✅ Reactive UI updates

### 8. **Navigation**
- ✅ Type-safe navigation with Navigation Compose
- ✅ Deep linking support
- ✅ Back stack management

## 🔧 Technologies Used

| Technology | Purpose |
|------------|---------|
| **Kotlin Multiplatform** | Cross-platform development |
| **Compose Multiplatform** | UI framework |
| **Ktor Client** | HTTP networking |
| **kotlinx.serialization** | JSON serialization |
| **kotlinx.coroutines** | Asynchronous programming |
| **Navigation Compose** | Screen navigation |
| **Lifecycle ViewModel** | State management |
| **Material 3** | Design system |

## 🚀 Running the Application

### Android
```bash
./gradlew :composeApp:assembleDebug
# Or run from Android Studio
```

### iOS
```bash
# Open iosApp/iosApp.xcodeproj in Xcode
# Then build and run
```

### Desktop (JVM)
```bash
./gradlew :composeApp:run
```

### Web
```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```

## 📱 Screens

### 1. Home Screen
- Displays environmental issue statistics
- Shows list of all reported issues
- Quick access to report new issues
- Navigation to map and profile

### 2. Report Issue
- Title and description input
- Category selection (Illegal Dumping, Plastic Pollution, etc.)
- Severity level (Low, Medium, High, Critical)
- GPS location capture
- Photo attachment
- Submit functionality

### 3. Map View
- Interactive map (MapLibre integration pending)
- Markers for all reported issues
- Filter by category/status
- Tap markers for issue details

### 4. Issue Details
- Complete issue information
- Status timeline (Submitted → Verified → Resolved)
- Location with map link
- Attached photos
- Reporter information

## 🎨 Design System

### Color Palette

**Light Mode:**
- Primary: Green (#4CAF50)
- Background: Light Green (#F1F8E9)
- Surface: White

**Dark Mode:**
- Primary: Light Green (#81C784)
- Background: Dark (#121212)
- Surface: Dark Gray (#1E1E1E)

### Typography
- Uses Material 3 typography scale
- Display, Headline, Title, Body, Label variants

## 🔌 API Integration

The frontend is configured to connect to the backend at:
```kotlin
const val BASE_URL = "http://localhost:8080/api"
```

### Endpoints Used
- `GET /api/issues` - Fetch all issues
- `GET /api/issues/:id` - Fetch issue details
- `POST /api/issues` - Create new issue
- `GET /api/issues/nearby` - Fetch nearby issues
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

## 📝 Data Models

### Issue
```kotlin
data class Issue(
    val id: String,
    val title: String,
    val description: String,
    val category: IssueCategory,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String?,
    val status: IssueStatus,
    val severity: IssueSeverity
)
```

### Categories
- Illegal Dumping
- Plastic Pollution
- Forest Damage
- Water Contamination
- Waste Disposal
- Air Pollution
- Other

### Status Flow
1. **SUBMITTED** - Initial state when reported
2. **VERIFIED** - Verified by admin/moderator
3. **IN_PROGRESS** - Being addressed
4. **RESOLVED** - Issue fixed
5. **REJECTED** - Invalid/duplicate report

## 🔮 Future Enhancements

### High Priority
- [ ] MapLibre integration for interactive maps
- [ ] Image picker implementation (camera + gallery)
- [ ] GPS location service integration
- [ ] Offline support with local database
- [ ] Push notifications for issue updates
- [ ] User authentication UI

### Medium Priority
- [ ] Filter and search functionality
- [ ] Issue comments/discussions
- [ ] User profile management
- [ ] Analytics dashboard
- [ ] Share issue functionality

### Low Priority
- [ ] Multilingual support (i18n)
- [ ] Accessibility improvements
- [ ] Tutorial/onboarding flow
- [ ] Achievement system
- [ ] Social media integration

## 🧪 Testing

```bash
# Run common tests
./gradlew :composeApp:cleanTestDebugUnitTest :composeApp:testDebugUnitTest

# Platform-specific tests
./gradlew :composeApp:testDebugUnitTest  # Android
./gradlew :composeApp:jvmTest            # Desktop
```

## 📄 License

MIT License - See LICENSE file for details

## 🤝 Contributing

Contributions are welcome! Please read the contributing guidelines before submitting PRs.

---

**Built with ❤️ using Kotlin and Compose Multiplatform**
