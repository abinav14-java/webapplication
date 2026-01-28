# 🚀 InstaClone - Quick Start Guide

Welcome to InstaClone! This is a fully-featured Instagram-like social media application built with Spring Boot and vanilla JavaScript.

## ⚡ Quick Start (30 seconds)

### **Option 1: Easiest - Just Run (Linux/Mac)**
```bash
chmod +x setup.sh
./setup.sh
```
Then open: **http://localhost:8080**

### **Option 2: Easiest - Just Run (Windows)**
Double-click: **`setup.bat`**

Then open: **http://localhost:8080**

### **Option 3: Manual Setup**

#### Prerequisites
- **Java 11+** - [Download](https://java.com)
- **MySQL 8.0+** - [Download](https://www.mysql.com/downloads/)

#### Steps

1. **Start MySQL** (if not already running)
   ```bash
   # Linux
   sudo service mysql start
   
   # Mac
   brew services start mysql
   
   # Windows - Start MySQL Service in Services panel
   ```

2. **Configure Database**
   ```sql
   mysql -u root -p
   > CREATE DATABASE instaclo;
   > CREATE USER 'instauser'@'localhost' IDENTIFIED BY 'password123';
   > GRANT ALL PRIVILEGES ON instaclo.* TO 'instauser'@'localhost';
   > FLUSH PRIVILEGES;
   ```

3. **Run Application**
   
   From source code:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   
   Or from JAR file:
   ```bash
   java -jar webapplication-0.0.1-SNAPSHOT.jar
   ```

4. **Open in Browser**
   Navigate to: **http://localhost:8080**

## 📋 System Requirements

| Requirement | Version | Download |
|-------------|---------|----------|
| **Java** | 11 or higher | [java.com](https://java.com) |
| **MySQL** | 8.0 or higher | [mysql.com](https://www.mysql.com/downloads/) |
| **Browser** | Modern (Chrome, Firefox, Safari, Edge) | - |
| **RAM** | 2 GB minimum | - |
| **Disk Space** | 500 MB | - |

## 🔐 Default Credentials

The application will create sample data. Use any valid credentials to register a new account.

**MySQL Credentials:**
- Username: `instauser`
- Password: `password123`

## 🌐 Accessing the App

Once running, you should see:
```
Started WebapplicationApplication in X.XXX seconds
```

Then open your browser:
- **Local**: http://localhost:8080
- **From another computer**: http://<your-ip>:8080

To find your IP:
```bash
# Linux/Mac
ifconfig | grep inet

# Windows
ipconfig
```

## 🆘 Troubleshooting

### Java not found
```bash
# Check if Java is installed
java -version

# If not, download from https://java.com
```

### MySQL not running
```bash
# Check if MySQL is running
mysql -u root -p

# If not:
# Linux: sudo service mysql start
# Mac: brew services start mysql
# Windows: Start MySQL from Services
```

### Port 8080 already in use
```bash
# Find what's using port 8080
# Linux/Mac: lsof -i :8080
# Windows: netstat -ano | findstr :8080

# Or change the port in application.properties:
# server.port=8081
```

### Cannot connect to database
- Verify MySQL is running
- Check database credentials in `application.properties`
- Ensure database `instaclo` exists
- Check MySQL user has permissions

### Build errors
```bash
# Clean and rebuild
mvn clean install

# Or skip tests
mvn clean package -DskipTests
```

## 📚 Documentation

For more detailed information, see:

- **[README.md](README.md)** - Full project documentation
- **[FEATURES.md](FEATURES.md)** - Feature descriptions and usage
- **[QUICKSTART.md](QUICKSTART.md)** - Setup and configuration
- **[DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md)** - Deploy to production
- **[IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)** - Technical details

## ✨ Features

✅ Dark mode toggle
✅ User profiles with stats
✅ Create, edit, delete posts
✅ Like and comment on posts
✅ Follow/unfollow users
✅ Full-text search & hashtags
✅ User mentions (@)
✅ Infinite scroll feed
✅ Real-time notifications (toasts)
✅ Responsive design (mobile, tablet, desktop)
✅ Image uploads & preview
✅ User authentication (JWT)
✅ And more...

## 🎯 Next Steps

1. **Create Account** - Register with your email
2. **Explore** - Create posts, follow users
3. **Customize** - Toggle dark mode, search profiles
4. **Share** - Invite friends to join

## 💡 Tips

- **Dark Mode**: Click the 🌙 button in sidebar
- **Search**: Use 🔍 button for keywords and #hashtags
- **Profile**: Click 👤 to view your profile
- **Logout**: Click 🚪 when done

## 🐛 Issues?

Check the troubleshooting section above, or:

1. Check logs in `logs/` directory
2. Verify all prerequisites are installed
3. Check MySQL is running with sample data

## 📞 Support

For detailed technical info, see [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md)

---

**Enjoy InstaClone!** 🎉

Built with ❤️ using Spring Boot + JavaScript
