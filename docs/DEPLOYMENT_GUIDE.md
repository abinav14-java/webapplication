# InstaClone - Deployment & Distribution Guide

## 🚀 How to Share & Run This Project

This guide explains how to distribute and run the InstaClone application to other people.

---

## Option 1: Share as Source Code (Recommended for Developers)

### Prerequisites for Others
- Java 11 or higher installed
- Maven 3.6+ installed
- MySQL 8.0+ installed and running
- Git (optional, for cloning)

### Steps for Others to Run

#### 1. Get the Code
**Via GitHub:**
```bash
git clone <your-github-repo-url>
cd webapplication
```

**Via ZIP File:**
- Download the ZIP file
- Extract it to a folder
- Open terminal/command prompt in that folder

#### 2. Setup Database
```bash
# Connect to MySQL
mysql -u root -p

# Create database
CREATE DATABASE instaclo;
CREATE USER 'instauser'@'localhost' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON instaclo.* TO 'instauser'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

#### 3. Configure Application
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/instaclo
spring.datasource.username=instauser
spring.datasource.password=password123
spring.jpa.hibernate.ddl-auto=update
```

#### 4. Build & Run
```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Or run JAR directly
java -jar target/webapplication-0.0.1-SNAPSHOT.jar
```

#### 5. Access Application
Open browser: **http://localhost:8080**

---

## Option 2: Share as JAR File (Easy for Non-Developers)

### Create JAR File
```bash
cd /home/ajith/Desktop/AbinavWorkSpace/webapplication
mvn clean package -DskipTests
```

This creates: `target/webapplication-0.0.1-SNAPSHOT.jar`

### Give JAR to Others
**Steps for them:**

#### 1. Prerequisites
- Java 11+ installed (just JRE, no development tools needed)
- MySQL running with database created

#### 2. Database Setup
Same as Option 1 (steps 2-3)

#### 3. Run JAR
```bash
java -jar webapplication-0.0.1-SNAPSHOT.jar
```

#### 4. Access
Open: **http://localhost:8080**

---

## Option 3: Share as WAR File (For Web Servers)

### Create WAR File
Edit `pom.xml` and change packaging:
```xml
<packaging>war</packaging>
```

Then build:
```bash
mvn clean package -DskipTests
```

This creates: `target/webapplication-0.0.1-SNAPSHOT.war`

### Deploy to Tomcat (For Others)

#### 1. Prerequisites
- Apache Tomcat 9.0+ installed
- MySQL running

#### 2. Database Setup
Same as Option 1 (steps 2-3)

#### 3. Deploy WAR
**Copy WAR file to Tomcat:**
```bash
# Windows
copy webapplication-0.0.1-SNAPSHOT.war "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\"

# Linux/Mac
cp webapplication-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/
```

#### 4. Start Tomcat
**Windows:**
```cmd
C:\Program Files\Apache Software Foundation\Tomcat 9.0\bin\startup.bat
```

**Linux/Mac:**
```bash
/usr/local/tomcat/bin/startup.sh
```

#### 5. Access
Open: **http://localhost:8080/webapplication**

---

## Option 4: Using Docker (Most Portable)

### Create Dockerfile
Create file named `Dockerfile`:
```dockerfile
FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/webapplication-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/instaclo
ENV SPRING_DATASOURCE_USERNAME=instauser
ENV SPRING_DATASOURCE_PASSWORD=password123

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
```

### Create Docker Compose
Create `docker-compose.yml`:
```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: instaclo
      MYSQL_USER: instauser
      MYSQL_PASSWORD: password123
      MYSQL_ROOT_PASSWORD: root123
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  app:
    build: .
    depends_on:
      - mysql
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/instaclo
      SPRING_DATASOURCE_USERNAME: instauser
      SPRING_DATASOURCE_PASSWORD: password123

volumes:
  mysql_data:
```

### Run with Docker
**For Others:**
```bash
# Prerequisites: Docker and Docker Compose installed

# Run
docker-compose up

# Access: http://localhost:8080
```

---

## Quick Start Scripts

### Windows Batch Script (`run.bat`)
```batch
@echo off
echo Starting InstaClone...
java -jar target/webapplication-0.0.1-SNAPSHOT.jar
pause
```

### Linux/Mac Script (`run.sh`)
```bash
#!/bin/bash
echo "Starting InstaClone..."
java -jar target/webapplication-0.0.1-SNAPSHOT.jar
```

Make executable:
```bash
chmod +x run.sh
./run.sh
```

---

## Configuration for Different Environments

### Local Development
```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/instaclo
spring.jpa.hibernate.ddl-auto=update
logging.level.root=INFO
```

### Production
```properties
server.port=8080
spring.datasource.url=jdbc:mysql://prod-db-server:3306/instaclo
spring.datasource.username=prod_user
spring.datasource.password=secure_password
spring.jpa.hibernate.ddl-auto=validate
logging.level.root=WARN
server.ssl.enabled=true
```

### Cloud Deployment (AWS, Azure, etc.)
```properties
server.port=${PORT:8080}
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
```

---

## System Requirements by Option

### Option 1: Source Code
- Java 11+
- Maven 3.6+
- MySQL 8.0+
- 500MB disk space
- 2GB RAM

### Option 2: JAR File
- Java 11+ (JRE only)
- MySQL 8.0+
- 300MB disk space
- 1GB RAM

### Option 3: WAR File
- Java 11+
- Tomcat 9.0+
- MySQL 8.0+
- 400MB disk space
- 1GB RAM

### Option 4: Docker
- Docker Desktop
- Docker Compose
- 2GB disk space
- 2GB RAM

---

## Troubleshooting for Others

### Issue: Port 8080 Already in Use
**Solution:**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8080
kill -9 <PID>

# Or use different port:
java -jar app.jar --server.port=8081
```

### Issue: MySQL Connection Error
**Check:**
1. MySQL is running: `mysql -u root -p`
2. Database exists: `SHOW DATABASES;`
3. User permissions: Correct username/password in config
4. Connection string correct in `application.properties`

### Issue: Java Not Found
**Fix:**
```bash
# Install Java
# Windows: Download from java.com
# Mac: brew install openjdk@11
# Linux: sudo apt install openjdk-11-jre-headless
```

### Issue: Build Fails
**Try:**
```bash
# Clean and rebuild
mvn clean install -DskipTests

# Check Java version
java -version

# Check Maven version
mvn -version
```

---

## Distribution Checklist

Before sharing, verify:

- [ ] Code compiles without errors: `mvn clean install`
- [ ] All dependencies included in `pom.xml`
- [ ] `application.properties` has good defaults
- [ ] Database schema creates automatically
- [ ] No hardcoded passwords in code
- [ ] README.md is clear and complete
- [ ] QUICKSTART.md covers setup
- [ ] JAR/WAR files are built and tested
- [ ] Docker setup works if using containers

---

## Step-by-Step for Non-Technical Users

### Easiest Way: Docker Desktop
```bash
1. Install Docker Desktop
2. Download docker-compose.yml
3. Open terminal in that folder
4. Run: docker-compose up
5. Wait for "Application started" message
6. Open browser: http://localhost:8080
7. Done! 🎉
```

### Alternative: Java Installer
```bash
1. Install Java from java.com
2. Download webapplication.jar
3. Double-click the JAR file
   OR open terminal and run:
   java -jar webapplication.jar
4. Open browser: http://localhost:8080
```

---

## Creating an Installer (Windows)

Use NSIS (Nullsoft Scriptable Install System):

```nsis
!include "MUI2.nsh"

Name "InstaClone"
OutFile "InstaClone-Installer.exe"
InstallDir "$PROGRAMFILES\InstaClone"

!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_LANGUAGE "English"

Section "Install"
  SetOutPath "$INSTDIR"
  File "webapplication.jar"
  File "run.bat"
  
  CreateDirectory "$SMPROGRAMS\InstaClone"
  CreateShortcut "$SMPROGRAMS\InstaClone\InstaClone.lnk" "$INSTDIR\run.bat"
  CreateShortcut "$DESKTOP\InstaClone.lnk" "$INSTDIR\run.bat"
SectionEnd
```

---

## Network Sharing (Same WiFi)

Run on machine A, access from machine B:

```bash
# Find machine A IP:
# Windows: ipconfig
# Linux/Mac: ifconfig

# Access from machine B:
http://192.168.1.100:8080
```

---

## Summary Table

| Option | Difficulty | Size | For Whom |
|--------|-----------|------|----------|
| Source Code | Medium | 200MB | Developers |
| JAR File | Easy | 50MB | Technical Users |
| WAR File | Medium | 50MB | Enterprise |
| Docker | Easy | 1GB | Any User |
| Installer | Easy | 50MB | End Users |

---

## Recommended Distribution Method

**For most people:** Use **Docker** (simplest)
**For developers:** Share **source code**
**For easy deployment:** Create **JAR** with launcher script
**For enterprise:** Use **WAR** with Tomcat

---

## Support for Others

Provide them:
1. ✓ Complete documentation
2. ✓ Video tutorials
3. ✓ Example database backup
4. ✓ Common issues FAQ
5. ✓ Contact for support

---

**Good luck sharing InstaClone! 🚀**

