# InstaClone - Social Media Application

A fully-featured Instagram-like social media platform built with Spring Boot and Vanilla JavaScript. Share photos, like posts, comment, follow friends, and much more!

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

## 🎨 Design System

### Colors
- **Primary**: `#667eea` to `#764ba2` (Purple gradient)
- **Accent**: `#E1306C` (Instagram pink)
- **Background**: Light gray (`#f5f7fa`) or dark (`#1a1a2e`)
- **Success**: `#27ae60` (Green)
- **Error**: `#e74c3c` (Red)

### Typography
- **Font**: System fonts (Arial, Helvetica, sans-serif)
- **Headings**: Bold (600-800 weight)
- **Body**: Regular (400 weight)

### Components
- **Cards**: 16px border-radius, shadow `0 8px 24px rgba(0,0,0,0.08)`
- **Buttons**: 20px border-radius, gradient backgrounds
- **Inputs**: 8px border-radius, light borders
- **Avatars**: 48px circular with 2px border

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

## 🐛 Troubleshooting

### Issue: Post won't load
**Solution**: Check network tab in browser console, verify API is running

### Issue: Images not showing
**Solution**: Ensure image URLs are valid, check CORS settings

### Issue: Search not working
**Solution**: Verify posts have captions, try different keywords

### Issue: Dark mode not saving
**Solution**: Clear localStorage, check browser cache

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

### Code Coverage
```bash
mvn jacoco:report
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
- [MDN Web Docs](https://developer.mozilla.org/)

## 📄 License

This project is licensed under the MIT License - see LICENSE file for details.

## 👥 Contributing

Contributions are welcome! Please follow these steps:
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Submit a pull request

## 📞 Support

For issues or questions, please:
1. Check the FEATURES.md for feature documentation
2. Check the QUICKSTART.md for usage guide
3. Review browser console for errors
4. Contact the development team

## 🎉 Future Enhancements

- [ ] Nested comment replies
- [ ] Image filters and effects
- [ ] Direct messaging
- [ ] Stories (24-hour posts)
- [ ] Trending hashtags
- [ ] Video support
- [ ] Push notifications
- [ ] User recommendations
- [ ] Analytics dashboard
- [ ] Content moderation

---

**Version**: 1.0.0  
**Last Updated**: January 2026  
**Built with ❤️**


