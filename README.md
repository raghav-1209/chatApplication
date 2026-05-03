# Real-Time Chat Application

A modern real-time one-to-one chat application built using **Jetpack Compose** for Android frontend and **Ktor** for backend services. The project follows clean architecture principles with secure authentication, scalable backend design, and production-level development practices.

---

## Features

* Real-time one-to-one messaging
* JWT-based authentication for backend API protection
* Refresh Token + Access Token session management
* Firebase Firestore for instant message synchronization
* PostgreSQL for persistent user and authentication data
* MVVM architecture for clean and maintainable code
* Hilt Dependency Injection
* Repository Pattern implementation
* API Gateway-based backend communication
* Scalable backend design for future microservices migration
* Production-ready architecture and secure session handling

---

## Tech Stack

### Frontend (Android)

* Kotlin
* Jetpack Compose
* MVVM Architecture
* Navigation Component
* Hilt Dependency Injection
* State Management

### Backend

* Ktor
* RESTful APIs
* JWT Authentication
* Refresh Token Handling
* API Gateway Structure

### Database

* PostgreSQL

### Authentication

* JWT Access Token
* Refresh Token

---

## Project Architecture

The project follows **Clean Architecture + MVVM** structure:

```text
Presentation Layer
│
├── UI Screens (Compose)
├── ViewModels
├── State Management
│
Domain Layer
│
├── Use Cases
├── Repository Interfaces
│
Data Layer
│
├── Repository Implementations
├── Remote APIs
├── Local Storage
├── Firebase Integration
│
Backend Layer
│
├── Ktor Server
├── JWT Auth System
├── PostgreSQL Integration
├── API Gateway
├── Authentication Services
```

---

## Authentication Flow

### User SignIn/Login

1. User enters email and password
2. Firebase verifes it
4. Backend generates JWT Access Token + Refresh Token
5. Tokens are securely stored
6. User enters chat application

### Session Management

* Access Token for API requests
* Refresh Token for renewing expired sessions
* Automatic logout when refresh token becomes invalid
* Redirect to login screen for re-authentication

---

## Database Design

### PostgreSQL Stores

* User Information
* Authentication Records
* Refresh Tokens
* User Metadata
* System Management Data
* chat Info




## Security Features


* JWT authentication
* Protected backend routes
* Refresh token management
* Session expiration handling
* Secure user authentication flow
* Production-level authorization structure

---

## Scalability Approach

Current architecture is designed with future scalability in mind:

* API Gateway structure
* Service separation approach
* Ready for microservices migration
* Clean backend boundaries
* Modular codebase for independent service scaling

This allows smooth transition from monolithic backend to microservices architecture when application traffic increases.

---

## Future Improvements

* Group Chat Support
* Media Sharing
* Push Notifications using FCM
* Online/Offline Presence
* Typing Indicators
* Last Seen Feature
* Read Receipts
* Voice Messages
* End-to-End Encryption
* Full Microservices Migration

---

## Learning Outcomes

This project helped in understanding:

* Production-level Android Architecture
* Real-time systems design
* Secure authentication systems
* JWT + Refresh Token implementation
* Backend service design using Ktor
* PostgreSQL integration
* Firebase Firestore real-time sync
* API Gateway architecture
* Scalable backend development

---

## Author

Developed as a production-focused learning project to build strong Android + Backend engineering skills using modern development practices.
