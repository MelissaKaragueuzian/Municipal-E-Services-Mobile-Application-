# Municipal E-Services Mobile Application

An Android application built with Jetpack Compose for managing municipal service requests with multi-role access (Citizen, Mayor, Admin).

## Features

### ✅ Completed Features

1. **Multi-Role Authentication**
   - Login screen with email/password
   - Role-based navigation (Citizen, Mayor, Admin)
   - Session management

2. **Citizen Features**
   - Submit service requests with attachments
   - View list of submitted requests
   - View request details and status
   - Select municipality and service type

3. **Mayor Features**
   - Review incoming requests for their municipality
   - Approve or reject requests
   - View request details

4. **Admin Features**
   - Manage municipalities (create, approve, delete)
   - Manage users (create, delete)
   - View all municipalities and users

5. **Technical Implementation**
   - **Architecture**: MVVM with Repository pattern
   - **UI**: 100% Jetpack Compose (Material3)
   - **Database**: Room for local storage
   - **State Management**: LiveData and StateFlow
   - **Navigation**: Compose Navigation
   - **File Storage**: Local file storage for attachments

## Test Accounts

The app automatically initializes with sample data on first launch. Use these credentials to login:

### Citizen Account
- **Email**: `citizen@example.com`
- **Password**: `citizen123`
- **Role**: Citizen
- **Municipality**: Sample Municipality

### Mayor Account
- **Email**: `mayor@example.com`
- **Password**: `mayor123`
- **Role**: Mayor
- **Municipality**: Sample Municipality

### Admin Account
- **Email**: `admin@example.com`
- **Password**: `admin123`
- **Role**: Admin

## Project Structure

```
app/src/main/java/com/example/municipalityservices/
├── data/
│   ├── local/
│   │   ├── DAO/              # Room DAOs
│   │   ├── Entities/         # Room entities
│   │   ├── AppDatabase.kt    # Room database
│   ├── repository/
│   │   ├── LocalRepository.kt
│   │   └── RemoteRepository.kt (interface for future Firebase)
│   └── DatabaseInitializer.kt
├── model/                     # Domain models
├── ui/
│   ├── screens/              # Compose screens
│   ├── navigation/           # Navigation setup
│   └── viewmodel/            # ViewModels
├── util/
│   ├── FileStorageHelper.kt
│   └── SessionManager.kt
└── MainActivity.kt
```

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM
- **Database**: Room
- **Coroutines**: Kotlin Coroutines
- **Navigation**: Compose Navigation
- **Material Design**: Material3

## Dependencies

Key dependencies (see `app/build.gradle.kts` for full list):
- Jetpack Compose (Material3, Navigation)
- Room Database
- Lifecycle & ViewModel
- Coroutines
- Material Icons Extended

## Future Enhancements (Scaffolded)

The following features are prepared for future Firebase integration:

1. **Firebase Authentication** - Replace local auth
2. **Cloud Firestore** - Sync data remotely
3. **Firebase Storage** - Upload attachments
4. **FCM Push Notifications** - Real-time notifications
5. **WorkManager** - Periodic sync

The `RemoteRepository` interface is already defined and ready for Firebase implementation.

## Building and Running

1. Open the project in Android Studio
2. Sync Gradle files
3. Run on an emulator or physical device (API 24+)
4. The app will automatically initialize with sample data on first launch

## Permissions

The app requires the following permissions (already configured):
- `READ_EXTERNAL_STORAGE` - For reading attachments
- `WRITE_EXTERNAL_STORAGE` - For saving files (API ≤ 32)
- `READ_MEDIA_IMAGES` - For accessing images (API 33+)
- `READ_MEDIA_VIDEO` - For accessing videos (API 33+)

## Notes

- All data is stored locally using Room database
- File attachments are stored in app's external files directory
- User session is managed in memory (SessionManager)
- The app is designed to easily integrate Firebase later

## Screenshots

*Add screenshots of the app here*

## License

This project is part of a course assignment.


