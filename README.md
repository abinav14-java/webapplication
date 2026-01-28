# InstaClone - Social Media Application

A fully-featured Instagram-like social media platform built with Spring Boot and Vanilla JavaScript. Share photos, like posts, comment, follow friends, and much more!

> **📋 New to the project?** See [`INDEX.md`](INDEX.md) for a complete navigation guide!

## 📖 Quick Navigation

**First time?** Start here: [`docs/QUICK_START.md`](docs/QUICK_START.md) (5 minutes)

**Documentation:**
- [`docs/QUICK_START.md`](docs/QUICK_START.md) - Setup and run in 5 minutes
- [`docs/FEATURES.md`](docs/FEATURES.md) - All 15+ features explained
- [`docs/PROJECT_STRUCTURE.md`](docs/PROJECT_STRUCTURE.md) - Code organization
- [`docs/INTERVIEW_GUIDE.md`](docs/INTERVIEW_GUIDE.md) - Technical deep dive (20 questions)
- [`docs/IMPLEMENTATION_SUMMARY.md`](docs/IMPLEMENTATION_SUMMARY.md) - Architecture details
- [`docs/DEPLOYMENT_GUIDE.md`](docs/DEPLOYMENT_GUIDE.md) - Deployment options (Docker/JAR/WAR)

**Scripts:**
- `scripts/run.sh` - Start application (Linux/Mac)
- `scripts/setup.sh` - Initial setup (Linux/Mac)
- `scripts/setup.bat` - Initial setup (Windows)

## 🚀 Tech Stack

### Backend
- **Java 11+** - Programming language
- **Spring Boot 2.x** - Web framework
- **Spring Security** - JWT authentication
- **Maven** - Build tool
- **MySQL** - Database
- **Lombok** - Boilerplate reduction

### Frontend
- **Vanilla JavaScript (ES6+)** - No frameworks
- **HTML5** - Markup
- **CSS3** - Styling with gradients and animations
- **Local Storage** - Session management

## ✨ Features

### Core Features
- **User Authentication** - Secure JWT-based login and registration
- **Posts** - Create, edit, and delete photo posts with captions
- **Likes** - Like/unlike posts with real-time count updates
- **Comments** - Add and delete comments on posts
- **Follow/Unfollow** - Build your social network
- **User Profile** - View your stats and posts

### Advanced Features
- **Dark Mode** - Toggle between light and dark themes
- **Search** - Find posts by keywords or hashtags
- **Hashtags** - Click to search posts with specific hashtags
- **@Mentions** - Tag other users in captions
- **Infinite Scroll** - Auto-load posts as you scroll
- **Toast Notifications** - Feedback for all actions
- **Responsive Design** - Works on desktop, tablet, and mobile
- **Image Lazy Loading** - Optimized performance

### UI/UX Features
- **Gradient Backgrounds** - Modern purple/blue theme
- **Smooth Animations** - Hover effects and transitions
- **Loading States** - Visual feedback during operations
- **Confirmation Dialogs** - Safety for destructive actions
- **Auto-saving** - Theme and preferences persist

## 📋 Getting Started

### Prerequisites
```bash
- Java 11 or higher
- Maven 3.6+
- MySQL 8.0+
- Modern web browser
```

### Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd webapplication
```

2. **Database Setup**
```sql
CREATE DATABASE instaclo;
```

3. **Update `application.properties`**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/instaclo
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

4. **Build the project**
```bash
mvn clean install
```

5. **Run the application**
```bash
mvn spring-boot:run
```

The application will start at: **http://localhost:8080**

## 🎯 Usage Guide

### Creating an Account
1. Navigate to `/register`
2. Enter username, email, and password
3. Click "Register"
4. You'll be auto-logged in and redirected to dashboard

### Creating a Post
1. Click the **➕** (plus) button in the sidebar
2. Select an image from your computer
3. Add a caption with #hashtags and @mentions
4. Click "Post"

### Interacting with Posts
- **Like**: Click the ♥ heart button
- **Comment**: Click 💬, type, and click "Post"
- **Follow**: Click "Follow" button on posts by other users

### Managing Your Profile
1. Click **👤** (profile) button
2. View all your posts in grid view
3. See your stats (Posts, Followers, Following)
4. Click post to jump to it in feed

### Searching
1. Click **🔍** (search) button
2. Type keywords or "#hashtag"
3. Results appear in real-time
4. Click result to jump to post

### Dark Mode
- Click **🌙** (moon) button to toggle
- Preference saves automatically

## 🏗️ Project Structure

```
webapplication/
├── src/
│   ├── main/
│   │   ├── java/com/abinav/webapplication/
│   │   │   ├── controller/          # REST API endpoints
│   │   │   ├── service/             # Business logic
│   │   │   ├── repository/          # Data access layer
│   │   │   ├── model/               # Entity classes
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   └── utility/             # Helper classes
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── templates/           # HTML pages
│   │       │   ├── dashboard.html
│   │       │   ├── login.html
│   │       │   └── register.html
│   │       └── static/
│   │           └── js/              # JavaScript files
│   │               └── dashboard.js
│   └── test/                        # Test files
├── pom.xml                          # Maven configuration
└── README.md                        # This file
```

## 🔑 API Endpoints

### Authentication
```
POST   /api/auth/register      - Create new account
POST   /api/auth/login         - Login user
```

### Posts
```
GET    /api/posts              - Get all posts (feed)
POST   /api/posts              - Create new post
GET    /api/posts/{id}         - Get single post
PUT    /api/posts/{id}         - Update post caption
DELETE /api/posts/{id}         - Delete post
GET    /api/posts/user/{email} - Get user's posts
```

### Comments
```
GET    /api/posts/{postId}/comments              - Get post comments
POST   /api/posts/{postId}/comments              - Add comment
DELETE /api/posts/{postId}/comments/{commentId}  - Delete comment
```

### Likes
```
POST   /api/posts/{id}/like         - Like post
DELETE /api/posts/{id}/unlike       - Unlike post
GET    /api/posts/{id}/likes/count  - Get like count
GET    /api/posts/{id}/liked-by-user - Check if user liked
```

### Follow
```
POST   /api/users/{userId}/follow           - Follow user
DELETE /api/users/{userId}/unfollow         - Unfollow user
GET    /api/users/{userId}/followers/count  - Get follower count
GET    /api/users/{userId}/following/count  - Get following count
```

## 🔐 Security Features

- **JWT Authentication** - Secure token-based auth
- **Password Hashing** - Bcrypt password encryption
- **CORS Protection** - Cross-origin request control
- **XSS Prevention** - HTML escaping on all user inputs
- **SQL Injection Prevention** - Prepared statements
- **CSRF Protection** - SameSite cookie attributes

## 📊 Database Schema

### Users Table
```sql
id, username, email, password, created_at, updated_at
```

### Posts Table
```sql
id, user_id, caption, image_url, created_at, updated_at
```

### Comments Table
```sql
id, post_id, user_id, content, created_at, updated_at
```

### Likes Table
```sql
id, post_id, user_id, created_at
```

### Follows Table
```sql
id, follower_id, following_id, created_at
```


## 🚀 Deployment
### Using Docker
```bash
docker build -t instaclo .
docker run -p 8080:8080 instaclo
```

### Using Java
```bash
java -jar target/webapplication-0.0.1-SNAPSHOT.jar
```

### Environment Variables
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://host:3306/instaclo
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=password
JWT_SECRET=your_secret_key
JWT_EXPIRATION=86400000
```

## 🔄 Development

### Running in Development Mode
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8080"
```

### Building WAR File
```bash
mvn clean package -P war
```

### Running Tests
```bash
mvn test
```

## 📈 Performance Optimization

- **Image Lazy Loading** - Images load on-demand
- **Debounced Search** - Reduces API calls (300ms delay)
- **Local Caching** - Token and userId cached
- **Efficient Rendering** - Single pass DOM updates
- **Minified Assets** - Smaller file sizes

## 🎓 Learning Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [JWT Authentication](https://jwt.io/)
- [REST API Best Practices](https://restfulapi.net/)

---
**Last Updated**: January 2026  




