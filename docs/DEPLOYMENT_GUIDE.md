# InstaClone - Deployment Guide

## 🚀 Deploy Your Application

### Prerequisites
- Java 17 or higher
- MySQL 8.0+ running
- Database credentials ready

### Step 1: Build the Project

```bash
cd /path/to/webapplication
mvn clean package -DskipTests
```

This creates: `target/webapplication-0.0.1-SNAPSHOT.war`

### Step 2: Set Environment Variables

**IMPORTANT: Never hardcode credentials in code or documentation!**

```bash
export DB_PASSWORD='your_actual_mysql_password'
export JWT_SECRET='your_secure_32_character_secret_key'
```

Get your actual credentials from:
- **DB_PASSWORD**: Your MySQL root password (or database user password)
- **JWT_SECRET**: Generate a secure random string (minimum 32 characters)

To generate a secure JWT secret:
```bash
# Linux/Mac
openssl rand -base64 32

# Or use any online random string generator
```

### Step 3: Run the Application

```bash
java -jar target/webapplication-0.0.1-SNAPSHOT.war
```

### Step 4: Access the Application

Open your browser and visit:
```
http://localhost:8080
```

---

## ✅ Verify It's Running

Check if the app is running:
```bash
curl http://localhost:8080/api/posts
```

You should get a JSON response with posts data.

---

## 🛑 Stop the Application

```bash
# Press Ctrl + C in the terminal
```

Or kill the process:
```bash
pkill -f "webapplication-0.0.1"
```

---

## 📝 Notes

- The WAR file is self-contained and includes all dependencies
- No additional servers needed (Tomcat is built-in)
- MySQL database must be running before starting the app
- Default port is 8080
- **NEVER commit credentials to GitHub**
- Use `.env` file or environment variables for secrets

---

## 🔧 Troubleshooting

**Port 8080 already in use:**
```bash
lsof -i :8080
kill -9 <PID>
```

**Database connection failed:**
- Ensure MySQL is running
- Check DB_PASSWORD is correct
- Verify database exists: `mysql -u root -p -e "SHOW DATABASES;"`

**JAR file not found:**
```bash
# Rebuild the project
mvn clean package -DskipTests
```

---

**For questions, refer to the Quick Start guide.**
