📝 Notes App (Android)

A modern offline-first Notes application built with Kotlin, Jetpack Compose, Clean Architecture, and MVI.
The app supports local persistence, Firebase backup, daily background sync, and shareable notes similar to Google Drive links.
Designed with scalability, maintainability, and production-grade architecture in mind.
✨ Features
📱 Core Features
Create, update, and delete notes
Color-coded notes
Automatic timestamps
Clean and minimal UI built with Jetpack Compose
🔄 Offline First
Notes are stored locally using Room Database
App works fully offline
Data syncs automatically when internet is available
☁️ Firebase Backup
Secure note backup using Firebase
User-based data storage
Automatic daily sync using WorkManager
🔗 Share Notes (Google Drive style)
Generate a shareable link for notes
Anyone with the link can view the note
Uses an HTML index page to display shared notes
🔐 Authentication
Login using Google Sign-In
Login using Mobile Number (OTP)
⚙️ Background Sync
WorkManager performs periodic synchronization
Ensures local notes are backed up to Firebase
🧱 Scalable Architecture
Built with production-ready patterns:
Clean Architecture
MVI (Model View Intent)
Repository Pattern
Dependency Injection with Hilt
🗄 Database Migration
Handles schema changes safely using Room Migrations
🏗 Architecture
The project follows Clean Architecture with MVI pattern.
presentation/
    ui/
    viewmodel/
    state/

domain/
    repository/
    usecase/

data/
    repository/
    datasource/
    room/
    firebase/
Layers
Presentation
Jetpack Compose UI
ViewModels
MVI state management
Domain
Business logic
Use cases
Data
Repository implementations
Room database
Firebase integration
🛠 Tech Stack
Technology	Purpose
Kotlin	Primary language
Jetpack Compose	Modern UI
MVI	State management
Clean Architecture	Scalable architecture
Room Database	Local storage
Firebase	Cloud backup
Firebase Authentication	Google & Phone login
WorkManager	Background sync
Hilt	Dependency injection
📦 Dependencies
Key libraries used:
Jetpack Compose
Room Database
Firebase Authentication
Firebase Firestore / Storage
Hilt (Dagger)
WorkManager
Kotlin Coroutines
Lifecycle Components
🔄 Sync Strategy
The app follows an offline-first synchronization model:
Notes are created locally in Room
WorkManager runs periodic sync
Unsynced notes are uploaded to Firebase
Firebase acts as backup storage
🔗 Note Sharing
Notes can be shared using a public link similar to Google Drive.
Example flow:
User taps Share
A share URL is generated
The note becomes accessible via a public HTML page
Anyone with the link can view the note
🚀 Future Improvements
Real-time note sync
Collaborative notes
Note folders / tags
Rich text editor
Web version of the notes app
