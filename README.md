# 🏨 Desktop Booking Management System

![Java](https://img.shields.io/badge/Java-11%2B-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-Desktop-blue)
![SQLite](https://img.shields.io/badge/Database-SQLite-green)
![Status](https://img.shields.io/badge/Status-Production%20Ready-brightgreen)

A complete desktop application for managing customer bookings with authentication, database storage, and reporting.

---

## Quick Overview

**What it does:** Manages customer bookings for businesses (salons, clinics, restaurants, etc.)  
**Technology:** JavaFX + SQLite desktop application  
**Key Features:** User login, booking management, conflict detection, reporting  
**My Role:** Full-stack developer (database design to UI implementation)

---

## Get Started in 2 Minutes

### Prerequisites
- Java JDK 11 or higher
- JavaFX SDK (download from https://gluonhq.com/products/javafx/)
- SQLite JDBC driver

### Installation

1. **Extract JavaFX SDK** to:
C:\javafx-sdk-25.0.2\


2. **Place SQLite JDBC jar** in `lib/` folder

3. **Run the application**

**Windows:**
Double-click START.bat


**Or from command line:**
```bash
cd DesktopBookingSystem
START.bat
🔑 Default Login
Username: admin
Password: admin123
Role: Administrator
Project Structure
DesktopBookingSystem/
├── src/                    # All source code
│   ├── Main.java          # Main application controller
│   ├── Booking.java       # Booking data model
│   ├── User.java          # User authentication model
│   └── DB.java            # Database operations
├── lib/                   # External libraries
│   └── sqlite-jdbc-*.jar  # SQLite database driver
├── booking.db             # SQLite database (auto-generated)
├── START.bat              # Windows launcher (compile + run)
├── README.md              # This documentation
└── .vscode/               # VS Code configuration
✨ Features
🔐 Authentication System
Secure login with username/password

Role-based access control

Password strength validation

Default admin user included

Booking Management
Add new bookings (customer name, date, time, notes)

Edit/update booking status or details

Delete bookings with confirmation

View all bookings in sortable table

Smart Validation
Time format validation (HH:MM 24-hour)

Date validation

Booking conflict detection (prevents double booking)

Required field checking

Reports & Analytics
Real-time statistics dashboard

Filter bookings by status

Chronological booking view

Counts by status (Pending / Confirmed / Completed / Cancelled)

Database Features
SQLite lightweight storage

Automatic table creation

Prepared statements (prevents SQL injection)

Connection management

🛠️ Technical Implementation
Tech Stack
Frontend: JavaFX (modern desktop UI)

Backend: Java

Database: SQLite (file-based, no installation needed)

Architecture: MVC Pattern

Code Highlights
// Booking conflict detection
public static boolean isBookingConflict(String date, String time) {
    // Prevents double bookings at same date/time
}

// Password strength validation
public static boolean isPasswordStrong(String password) {
    // Minimum 6 chars, at least one letter and digit
}

// Time format validation
private boolean isValidTime(String time) {
    // Validates HH:MM format (00:00 to 23:59)
}
📊 Database Schema
Users Table
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT DEFAULT 'user'
);
Bookings Table
CREATE TABLE bookings (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    date TEXT NOT NULL,
    time TEXT NOT NULL,
    notes TEXT,
    status TEXT DEFAULT 'Pending'
);
🎯 Skills Demonstrated
Technical Skills
✅ Full-Stack Development (UI, business logic, database)

✅ JavaFX programming (Scene management, TableView, event handling)

✅ Database design (SQLite schema, CRUD operations, JDBC)

✅ Software architecture (MVC pattern, separation of concerns)

✅ Input validation (time, date, conflict detection)

✅ Error handling (user-friendly messages, exception handling)

Professional Skills
Requirements analysis and implementation

User experience design

Code documentation

Testing and debugging

Project organization

💻 Running from Source
Compile & Run (Windows)
cd src
javac --module-path "..\javafx-sdk-25.0.2\lib" --add-modules javafx.controls -cp "..\lib\*" *.java
java --module-path "..\javafx-sdk-25.0.2\lib" --add-modules javafx.controls -cp ".;..\lib\*" Main
VS Code Setup
Install Extension Pack for Java

Open project folder

double tap the START.bat

🐛 Troubleshooting
Issue	Solution
JavaFX not found	Set correct path in START.bat
SQLite class missing	Ensure jar is in lib/ folder
Database locked	Close app and delete booking.db
Compilation errors	Check Java version (needs JDK 11+)

📈 Future Enhancements
Planned features:

Email notifications for bookings
Calendar view integration
Export to PDF/Excel
User registration page
Password encryption

About This Project
Purpose: Portfolio project demonstrating full-stack desktop development skills
Development Time: 3 weeks
Lines of Code: ~1,200
Key Achievement: Complete working application from database to UI

📞 Contact
Mabutsi Kgaogelo
Java Developer

GitHub: https://github.com/Kgaogelo02
Email: Mabutsikgaogelo@gmail.com