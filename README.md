# InstaClone - Social Media Application

A fully-featured Instagram-like social media platform built with Spring Boot and Vanilla JavaScript.

## 🚀 Quick Start

```bash
# 1. Build
mvn clean package -DskipTests

# 2. Set your environment variables (see docs/QUICK_START.md)
export DB_PASSWORD='your_mysql_password'
export JWT_SECRET='your_secure_jwt_secret'

# 3. Run
java -jar target/webapplication-0.0.1-SNAPSHOT.war
```

Visit: **http://localhost:8080**

## 📚 Tech Stack

**Backend:** Java 17 | Spring Boot 3.5.0 | Spring Security | JWT | MySQL 8.0  
**Frontend:** Vanilla JavaScript ES6+ | HTML5 | CSS3  
**Build:** Maven

## ✨ Features (15+)

- ✅ User Authentication (JWT-based)
- ✅ Create/Edit/Delete Posts
- ✅ Like & Unlike Posts
- ✅ Comments
- ✅ Follow/Unfollow Users
- ✅ Search (Keywords & #Hashtags)
- ✅ Dark Mode
- ✅ User Profiles
- ✅ Infinite Scroll
- ✅ Responsive Design
- ✅ Toast Notifications
- ✅ Image Lazy Loading
- ✅ Real-time Updates
- ✅ Modern UI with Gradients
- ✅ Local Storage Sessions

## 📖 Documentation

- **[Quick Start Guide](docs/QUICK_START.md)** - Detailed setup
- **[Features](docs/FEATURES.md)** - Feature details
- **[Deployment Guide](docs/DEPLOYMENT_GUIDE.md)** - Docker, JAR, WAR

## ��️ Project Structure

```
src/
├── main/java/com/abinav/webapplication/
│   ├── controller/        (API endpoints)
│   ├── service/           (Business logic)
│   ├── repository/        (Database access)
│   ├── model/             (Entities)
│   ├── dto/               (API responses)
│   ├── connection/        (Security & JWT)
│   └── utility/           (Helpers)
├── resources/
│   ├── templates/         (HTML pages)
│   ├── static/js/         (JavaScript)
│   └── application.properties
└── test/                  (Tests)
```

## 🔐 Security

- JWT Token Authentication
- BCrypt Password Hashing
- CSRF Protection
- SQL Injection Prevention
- Authorization Checks

## 📊 Build Commands

```bash
# Build only
mvn clean package -DskipTests

# Build with tests
mvn clean package

# Compile only
mvn compile

# Run tests
mvn test
```

## 🎯 How to Use

1. **Register** - Create account at `/register`
2. **Login** - Sign in with credentials
3. **Posts** - Click ➕ to add posts
4. **Interact** - Like ♥, comment 💬, follow users
5. **Search** - Use �� for keywords or #hashtags
6. **Dark Mode** - Toggle 🌙 for dark theme

## 🚀 Deployment

See [DEPLOYMENT_GUIDE.md](docs/DEPLOYMENT_GUIDE.md) for deployment options.

---

**Status:** ✅ Production-Ready | **Last Updated:** February 2026
