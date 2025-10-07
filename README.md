# 🌱 Plants-Fresher

<div align="center">
  <!-- <img src="docs/app_icon.png" alt="Plants-Fresher Logo" width="120"/> -->
  
  [![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
  [![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg)](https://android-arsenal.com/api?level=21)
  [![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
  [![Contributions](https://img.shields.io/badge/Contributions-Welcome-orange.svg)](CONTRIBUTING.md)
</div>

**Plants-Fresher** is a modern Android application that brings the joy of plant parenthood to everyone. Discover, purchase, and nurture fresh indoor and outdoor plants with comprehensive care guidance—all in one beautiful, intuitive app.

---

## ✨ Key Features

### 🌿 Plant Discovery & Catalog
- Browse an extensive, curated collection of plants with high-quality images
- Detailed plant profiles including botanical names, origins, and growth characteristics
- Category filtering: Succulents, Ferns, Flowering Plants, Air Purifiers, and more
- Difficulty ratings: Beginner-friendly to Expert-level plants

### 🛒 Seamless E-Commerce Experience
- Intuitive shopping cart with real-time price calculations
- Secure checkout process with multiple payment options
- Wishlist functionality to save favorite plants
- Special offers and seasonal promotions

### 📘 Intelligent Care System
- Personalized care schedules based on plant species and your location
- Watering reminders with smart scheduling
- Light requirement indicators with room placement suggestions
- Growth tracking and progress photos
- Seasonal care adjustments

### 🔍 Advanced Search & Filtering
Search and filter by:
- Plant name or botanical name
- Light requirements (Low, Medium, Bright, Direct)
- Watering frequency (Daily, Weekly, Bi-weekly, Monthly)
- Pet-friendly options
- Air purifying capabilities
- Size categories (Small, Medium, Large)

### 🧾 Order Management
- Complete order history with detailed receipts
- Real-time delivery tracking with push notifications
- Easy reordering of previously purchased plants
- Customer support integration

### 🔔 Smart Notifications
- Customizable plant care reminders
- Order status updates and delivery alerts
- New plant arrivals and personalized recommendations
- Care tips based on weather and season

---

## 🏗️ Architecture & Tech Stack

### Core Technologies
- **Language:** Java (Android SDK 21+)
- **Architecture Pattern:** MVVM (Model-View-ViewModel) with Clean Architecture principles
- **Dependency Injection:** Dagger 2 / Hilt
- **Concurrency:** RxJava / Coroutines for asynchronous operations

### UI Layer
- **Layout:** XML-based layouts with Material Design 3 components
- **Navigation:** Navigation Component with single-activity architecture
- **Animations:** MotionLayout and Lottie for smooth transitions
- **Image Loading:** Glide with placeholder and error handling

### Data Layer
- **Local Database:** Room for offline plant care data and favorites
- **Networking:** Retrofit 2 with OkHttp interceptors
- **Serialization:** Gson for JSON parsing
- **Caching:** OkHttp cache and Room for offline-first approach

### Backend Services
- **Authentication:** Firebase Authentication
- **Cloud Storage:** Firebase Storage for plant images
- **Analytics:** Firebase Analytics for user behavior tracking
- **Push Notifications:** Firebase Cloud Messaging (FCM)
- **Crash Reporting:** Firebase Crashlytics

### Additional Libraries
- **Permissions:** Easy Permissions library
- **Image Handling:** UCrop for profile picture editing
- **Data Validation:** Custom validators for forms
- **Testing:** JUnit, Mockito, Espresso

---

## 📋 Prerequisites

Before you begin, ensure you have:
- **Android Studio:** Arctic Fox or later (latest stable version recommended)
- **JDK:** Version 11 or higher
- **Android SDK:** API Level 21 (Lollipop) or higher
- **Gradle:** 7.0+ (included with Android Studio)
- **Firebase Project:** Set up a Firebase project for backend services

---

## 📦 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/AkashYadav8080/Plants-Fresher.git
cd Plants-Fresher
```

### 2. Firebase Configuration
1. Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Add an Android app to your Firebase project
3. Download the `google-services.json` file
4. Place it in the `app/` directory
5. Enable the following Firebase services:
   - Authentication (Email/Password, Google Sign-In)
   - Cloud Firestore or Realtime Database
   - Cloud Storage
   - Cloud Messaging
   - Analytics

### 3. API Configuration
Create a `local.properties` file in the root directory and add:
```properties
API_BASE_URL="https://your-api-endpoint.com/"
API_KEY="your_api_key_here"
```

### 4. Open in Android Studio
1. Launch Android Studio
2. Select "Open an Existing Project"
3. Navigate to the cloned directory and select it
4. Wait for Gradle sync to complete

### 5. Build the Project
```bash
./gradlew clean build
```

### 6. Run the Application
- Connect an Android device via USB with Developer Options enabled
- Or set up an Android Virtual Device (AVD)
- Click the "Run" button or press `Shift + F10`

---

## 📱 App Structure

```
Plants-Fresher/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/yourpackage/plantsfresher/
│   │   │   │   ├── data/          # Data layer (repositories, models, local/remote sources)
│   │   │   │   ├── domain/        # Business logic (use cases, entities)
│   │   │   │   ├── presentation/  # UI layer (activities, fragments, viewmodels)
│   │   │   │   ├── di/            # Dependency injection modules
│   │   │   │   └── utils/         # Utility classes and helpers
│   │   │   ├── res/               # Resources (layouts, drawables, strings)
│   │   │   └── AndroidManifest.xml
│   │   └── test/                  # Unit tests
│   └── build.gradle
├── docs/                          # Documentation and assets
├── gradle/
├── .gitignore
├── build.gradle
├── settings.gradle
├── README.md
├── CONTRIBUTING.md
└── LICENSE
```
---

## 🔐 Permissions

The app requires the following permissions:

| Permission | Purpose | Required |
|------------|---------|----------|
| `INTERNET` | API calls and data synchronization | Yes |
| `ACCESS_NETWORK_STATE` | Check network connectivity | Yes |
| `POST_NOTIFICATIONS` | Care reminders and order updates | Optional |
| `CAMERA` | Take plant progress photos | Optional |
| `READ_EXTERNAL_STORAGE` | Upload plant images | Optional |
| `WRITE_EXTERNAL_STORAGE` | Save plant care guides (API < 29) | Optional |

All optional permissions are requested at runtime with clear explanations.

---

## 🧪 Testing

### Run Unit Tests
```bash
./gradlew test
```

### Run Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

### Generate Coverage Report
```bash
./gradlew jacocoTestReport
```

---

## 🚀 Deployment

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

Make sure to configure your signing configuration in `app/build.gradle` before creating a release build.

---

## 🤝 Contributing

We welcome contributions from the community! Here's how you can help:

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Commit your changes**
   ```bash
   git commit -m 'Add some amazing feature'
   ```
4. **Push to the branch**
   ```bash
   git push origin feature/amazing-feature
   ```
5. **Open a Pull Request**

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for detailed guidelines on our code of conduct and development process.

### Contribution Areas
- 🐛 Bug fixes and issue resolution
- ✨ New features and enhancements
- 📝 Documentation improvements
- 🎨 UI/UX design refinements
- 🌐 Localization and translations
- ✅ Test coverage expansion
  
---

## 👨‍💻 Author

**Akash Yadav**
- GitHub: [@AkashYadav8080](https://github.com/AkashYadav8080)
  
---

## 🙏 Acknowledgments

- Plant care data sourced from reputable botanical databases
- Icons and illustrations from [Material Design Icons](https://materialdesignicons.com/)
- Community contributors and testers
- Open source libraries that made this project possible

---

## 📞 Support

If you encounter any issues or have questions:
- 📧 Email: support@plantsfresher.com
- 🐛 [Report a Bug](https://github.com/AkashYadav8080/Plants-Fresher/issues)
- 💡 [Request a Feature](https://github.com/AkashYadav8080/Plants-Fresher/issues/new?labels=enhancement)
- 💬 [Join our Discord Community](https://discord.gg/your-invite)

---

<div align="center">
  <p>Made with 💚 by plant lovers, for plant lovers</p>
  <p>⭐ Star this repo if you find it helpful!</p>
</div>
