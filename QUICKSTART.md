# 🚀 GreenGuardian - Quick Start Guide

## Prerequisites

- **JDK 11 or higher**
- **Android Studio** (for Android development)
- **Xcode** (for iOS development, macOS only)
- **Gradle** (included via wrapper)

## 🏃 Running the Application

### Option 1: Android

1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Select an emulator or connect a physical device
4. Click the "Run" button or use:
   ```bash
   ./gradlew :composeApp:installDebug
   ```

### Option 2: Desktop (JVM)

```bash
./gradlew :composeApp:run
```

The desktop app will launch immediately!

### Option 3: iOS (macOS only)

1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Select a simulator or device
3. Click "Run" (▶️) button

### Option 4: Web

```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```

Browser will open automatically at http://localhost:8080

## 📱 Testing Without Backend

The frontend includes mock data and error handling, so you can explore the UI without a running backend. You'll see:
- Empty states with helpful messages
- Loading indicators
- Error messages with retry buttons

## 🔌 Connecting to Backend

1. Start the backend server (from the `server` directory)
2. Update the base URL in `NetworkClient.kt` if needed:
   ```kotlin
   const val BASE_URL = "http://localhost:8080/api"
   ```
3. For Android emulator, use: `http://10.0.2.2:8080/api`
4. For iOS simulator, use: `http://localhost:8080/api`

## 🎯 Exploring the App

### Home Screen
- View statistics of reported issues
- Scroll through the issues list
- Tap on an issue to see details
- Use the floating "+" button to report new issues

### Report Issue Screen
1. Enter a title (e.g., "Illegal dumping near park")
2. Add a description
3. Select a category from the dropdown
4. Choose severity level
5. Click "Get Current Location" (mock location for now)
6. Optionally add a photo
7. Click "Submit Report"

### Map Screen
- Currently shows a placeholder
- Lists nearby issues below the map
- Tap on an issue to see details

### Issue Details
- View complete information about an issue
- See the status timeline
- Check location details
- View attached photos

## 🎨 Theme Switching

The app automatically adapts to your system's dark/light mode preference.

To test:
- **Android**: Settings → Display → Dark theme
- **iOS**: Settings → Display & Brightness → Dark
- **Desktop**: System preferences
- **Web**: Browser/system settings

## 🛠️ Development Tips

### Hot Reload (Experimental)
The project includes Compose Hot Reload plugin. Make UI changes and see them instantly without restarting!

### Debug Logging
Network requests are logged to the console. Look for:
```
[Ktor] GET http://localhost:8080/api/issues
```

### Clean Build
If you encounter issues:
```bash
./gradlew clean
./gradlew :composeApp:assembleDebug
```

## 📂 Project Structure Quick Reference

```
composeApp/src/commonMain/kotlin/in/co/abdev/greenguardian/
├── App.kt                    # Main entry point
├── data/
│   ├── model/               # Data models
│   ├── network/             # HTTP client
│   └── repository/          # API calls
└── ui/
    ├── navigation/          # App navigation
    ├── screens/             # UI screens
    ├── theme/               # Colors & typography
    └── viewmodel/           # State management
```

## 🐛 Troubleshooting

### "Could not resolve dependency"
```bash
./gradlew --refresh-dependencies
```

### "SDK not found"
- Android: Install Android SDK via Android Studio
- iOS: Install Xcode Command Line Tools

### "Module not found"
```bash
./gradlew clean build
```

### Network errors in app
- Check if backend is running
- Verify BASE_URL in NetworkClient.kt
- For Android emulator, use 10.0.2.2 instead of localhost

## 📚 Next Steps

1. **Start the backend server** (see server/README.md)
2. **Test the API integration**
3. **Add real location services**
4. **Integrate MapLibre for maps**
5. **Add authentication flow**

## 💡 Pro Tips

- Use Android Studio's Layout Inspector to debug UI
- Check Logcat for network requests and errors
- Use the Compose Preview for rapid UI development
- Test on multiple screen sizes
- Try both light and dark themes

## 🤝 Need Help?

- Check the main README.md
- Review FRONTEND_README.md for detailed documentation
- Look at FRONTEND_IMPLEMENTATION.md for architecture details

---

**Happy Coding! 🌍💚**

Let's make the world a better place, one issue report at a time!
