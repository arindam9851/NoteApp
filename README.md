# 📝 Notes App (Android)

A modern **offline-first Notes application** built with **Kotlin, Jetpack Compose, Clean Architecture, and MVI**.

The app supports **local persistence, Firebase backup, daily background sync, and shareable notes similar to Google Drive links**.

Designed with **scalability, maintainability, and production-grade architecture** in mind.

---

# ✨ Features

## 📱 Core Features
- Create, update, and delete notes
- Color-coded notes
- Automatic timestamps
- Clean and minimal UI built with **Jetpack Compose**

---

## 🔄 Offline First
- Notes are stored locally using **Room Database**
- App works fully **offline**
- Data **syncs automatically** when internet becomes available

---

## ☁️ Firebase Backup
- Secure note backup using **Firebase**
- User-based data storage
- Automatic **daily 24 hours sync using WorkManager**

---

## 🔗 Share Notes (Google Drive Style)
- Generate a **shareable link** for notes
- Anyone with the link can **view the note**
- Uses an **HTML index page** to display shared notes

---

## 🔐 Authentication
- Login using **Google Sign-In**
- Login using **Mobile Number (OTP)**

---

## ⚙️ Background Sync
- **WorkManager** performs periodic synchronization
- Ensures local notes are backed up to **Firebase**

---

## 🧱 Scalable Architecture
Built with **production-ready patterns**:

- Clean Architecture
- MVI (Model View Intent)
- Repository Pattern
- Dependency Injection with **Hilt**

---

## 🗄 Database Migration
Handles schema changes safely using **Room Migrations**

---

# 🏗 Architecture

The project follows **Clean Architecture with MVI pattern**.


---

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

## Layers

### Presentation
- Jetpack Compose UI
- ViewModels
- MVI state management

### Domain
- Business logic
- Use cases

### Data
- Repository implementations
- Room database
- Firebase integration

---

# 🛠 Tech Stack

| Technology | Purpose |
|-----------|--------|
| Kotlin | Primary language |
| Jetpack Compose | Modern UI |
| MVI | State management |
| Clean Architecture | Scalable architecture |
| Room Database | Local storage |
| Firebase | Cloud backup |
| Firebase Authentication | Google & Phone login |
| WorkManager | Background sync |
| Hilt | Dependency injection |

---

# 📦 Dependencies

Key libraries used:

- Jetpack Compose
- Room Database
- Firebase Authentication
- Firebase Firestore / Storage
- Hilt (Dagger)
- WorkManager
- Kotlin Coroutines
- Lifecycle Components

---

# 🔄 Sync Strategy

The app follows an **offline-first synchronization model**:

1. Notes are created locally in **Room**
2. **WorkManager** runs periodic sync
3. Unsynced notes are uploaded to **Firebase**
4. Firebase acts as **backup storage**

---

# 🔗 Note Sharing

Notes can be shared using a **public link similar to Google Drive**.

### Example Flow

1. User taps **Share**
2. A **share URL** is generated
3. The note becomes accessible via a **public HTML page**
4. Anyone with the link can **view the note**

---

# 🚀 Future Improvements

- Real-time note sync
- Collaborative notes
- Note folders / tags
- Rich text editor
- Web version of the notes app

---

# ☁️ Firebase Hosting Setup

You can host shared notes (HTML pages) using **Firebase Hosting**.

### Steps

1. Install Firebase CLI (if not already installed):
```bash
npm install -g firebase-tools
firebase login
firebase init hosting
Choose your Firebase project
Set public directory (e.g., public/)
Configure as a single-page app? Yes
Overwrite existing index.html? No
Deploy to Firebase Hosting:
firebase deploy --only hosting

# 👨‍💻 Author

**Arindam Ghosh**

Android Developer | Kotlin | Jetpack Compose | Clean Architecture | KMM
