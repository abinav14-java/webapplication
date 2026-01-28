# InstaClone - Quick Start Guide

## 🚀 Starting the Application

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- MySQL (if using database backend)

### Running the Application

```bash
cd /home/ajith/Desktop/AbinavWorkSpace/webapplication
mvn spring-boot:run
```

The application will start at: **http://localhost:8080**

---

## 📋 First Time Setup

### 1. Register an Account
- Navigate to `/register`
- Fill in username, email, and password
- Click "Register"

### 2. Login
- Navigate to `/login`
- Enter your credentials
- You'll be redirected to the dashboard

---

## 🎮 Feature Walkthrough

### Creating Your First Post
1. Click the **➕** button in the left sidebar
2. Select an image from your computer
3. A modal will appear with image preview
4. Write a caption (use #hashtags for categorization)
5. Click **"Post"** to publish

### Searching Posts
1. Click the **🔍** (Search) button in the sidebar
2. Start typing to search by:
   - Caption keywords
   - Hashtags (e.g., "#nature")
3. Click on results to jump to the post

### Managing Your Profile
1. Click the **👤** (Profile) button
2. View your statistics:
   - Posts count
   - Followers count
   - Following count
3. See all your posts in a grid view
4. Click on any post to scroll to it in the feed

### Editing Your Posts
1. Hover over your own post
2. Click the **⋯** (three dots) menu
3. Select:
   - **"Edit Caption"** - Modify post text
   - **"Delete Post"** - Remove post permanently

### Interacting with Posts
- **Like** - Click the ♥ heart button (turns red when liked)
- **Comment** - Click 💬, type comment, click "Post"
- **Delete Comment** - Click ✕ on your own comments
- **Follow** - Click "Follow" button to follow post author

### Dark Mode
1. Click **🌙** button in sidebar (or **☀️** if in dark mode)
2. The theme changes automatically
3. Your preference is saved locally

### Logout
1. Click **🚪** (Logout) button
2. Confirm the logout
3. You'll be redirected to login page

---

## 💡 Pro Tips

### Formatting Your Captions
- Use **#hashtags** for categorization
- Use **@mentions** to tag other users
- Press **Enter** for line breaks

### Best Practices
- Use relevant hashtags for discoverability
- Write engaging captions
- Reply to comments to build community
- Follow accounts you're interested in

### Performance Tips
- Images load automatically as you scroll
- Search results appear instantly with suggestions
- Theme preference saves automatically
- Your session token is secure

---

## 🎨 UI Overview

### Left Sidebar Icons (Top to Bottom)
```
🏠 Logo - Home (Back to Feed)
➕ Plus - Create New Post
👤 Profile - View My Profile
🔍 Search - Search Posts
🌙 Moon - Toggle Dark Mode (☀️ in Dark Mode)
🚪 Door - Logout
```

### Dashboard Layout
- **Top Section** - InstaClone title with gradient
- **Stats Card** - Posts, Followers, Following counts
- **Feed Area** - Scrollable list of posts
- **Infinite Scroll** - Auto-loads more as you scroll

### Post Structure
```
[Profile Pic] Username  [Follow] Date  [Menu ⋯]
[Large Post Image]
[♥ Likes] [💬 Comments]
[Username] Caption text with #hashtags
[💬 Comments Section] (Expandable)
```

---

## 🔐 Account Security

### Your Data
- Passwords are securely hashed
- JWT tokens expire after set interval
- All API calls require authentication
- Session tokens stored in localStorage

### Best Practices
- Don't share your token
- Logout on shared devices
- Use strong passwords
- Change password periodically

---

## ⚡ Keyboard Shortcuts

| Key | Action |
|-----|--------|
| `Esc` | Close modals |
| `Enter` | Submit forms (in focus) |
| `Tab` | Navigate between elements |

---

## 🐛 Troubleshooting

### Post Won't Load
- Check internet connection
- Clear browser cache (Ctrl+Shift+Delete)
- Refresh page (Ctrl+R or F5)
- Logout and login again

### Images Not Showing
- Ensure image URLs are valid
- Check browser console for errors
- Try uploading image again
- Use supported formats (JPG, PNG, GIF, WebP)

### Search Not Working
- Make sure you've typed something
- Wait for results to load
- Try searching with different keywords
- Check that posts have captions

### Dark Mode Not Saving
- Clear browser cookies
- Logout and login again
- Check browser's localStorage is enabled

---

## 📊 Feature Checklist

After setup, try these to verify everything works:

- [ ] Create a post with caption and image
- [ ] Add a #hashtag to caption
- [ ] Search by hashtag
- [ ] Like a post
- [ ] Comment on a post
- [ ] Reply to a comment
- [ ] Follow another user
- [ ] Edit your post caption
- [ ] Delete a comment
- [ ] Toggle dark mode
- [ ] View your profile
- [ ] Logout and login again

---

## 📞 Support & Feedback

If you encounter any issues:
1. Check the browser console for errors (F12)
2. Verify API endpoints are responding
3. Clear cache and try again
4. Contact the development team

---

## 🎯 Next Steps

1. **Customize** - Modify colors and branding
2. **Deploy** - Push to production server
3. **Invite Friends** - Share your InstaClone instance
4. **Extend** - Add more features as needed
5. **Monitor** - Set up logging and analytics

---

Enjoy your InstaClone experience! 🎉

