# 📚 InstaClone - Project Organization Guide

## Quick Start (Choose Your Path)

### 🏃 I just want to run it! (5 minutes)
```bash
cd scripts
./run.sh
```
👉 See: [`docs/QUICK_START.md`](docs/QUICK_START.md)

### 🎓 I want to understand the code
👉 See: [`docs/IMPLEMENTATION_SUMMARY.md`](docs/IMPLEMENTATION_SUMMARY.md)

### 💼 I'm preparing for an interview
👉 See: [`docs/INTERVIEW_GUIDE.md`](docs/INTERVIEW_GUIDE.md) (20 questions + architecture)

### 🚀 I want to deploy this
👉 See: [`docs/DEPLOYMENT_GUIDE.md`](docs/DEPLOYMENT_GUIDE.md) (Docker, JAR, WAR, source)

### 🏗️ I want to see the project structure
👉 See: [`docs/PROJECT_STRUCTURE.md`](docs/PROJECT_STRUCTURE.md)

### ✨ I want to see all features
👉 See: [`docs/FEATURES.md`](docs/FEATURES.md)

---

## 📁 Directory Structure

```
instac lone/
├── README.md                    ← Main project description
├── QUICKSTART.md                ← Alternative quick start
├── pom.xml                      ← Maven configuration
│
├── 📁 docs/                     ← All documentation
│   ├── QUICK_START.md           (Setup in 5 min)
│   ├── FEATURES.md              (15+ features)
│   ├── PROJECT_STRUCTURE.md     (Code organization)
│   ├── INTERVIEW_GUIDE.md       (20 questions + architecture)
│   ├── IMPLEMENTATION_SUMMARY.md (Technical details)
│   └── DEPLOYMENT_GUIDE.md      (Deploy to production)
│
├── 📁 scripts/                  ← Start & setup scripts
│   ├── run.sh                   (Start app - Linux/Mac)
│   ├── setup.sh                 (Setup - Linux/Mac)
│   ├── setup.bat                (Setup - Windows)
│   └── VERIFY_FEATURES.sh       (Test all features)
│
├── 📁 config/                   ← Configuration
│   ├── .env.example             (Template for env vars)
│   └── .env                     (Your local config - DO NOT COMMIT)
│
├── 📁 src/                      ← Source code
│   └── main/
│       ├── java/com/abinav/webapplication/
│       │   ├── connection/      (Security & JWT)
│       │   ├── controller/      (REST API)
│       │   ├── service/         (Business logic interfaces)
│       │   ├── serviceImpl/      (Business logic implementations)
│       │   ├── repository/      (Database access)
│       │   ├── model/           (Database entities)
│       │   ├── dto/             (API response formats)
│       │   ├── logic/           (User details loader)
│       │   └── utility/         (Helpers & utilities)
│       └── resources/
│           ├── templates/       (HTML pages)
│           ├── static/js/       (JavaScript files)
│           ├── application.properties
│           └── logback-spring.xml
│
├── 📁 target/                   ← Build output (auto-generated)
└── 📁 logs/                     ← Application logs
```

---

## 🎯 Step-by-Step Guide

### Step 1: Read the Overview (2 min)
```
Start: README.md
```

### Step 2: Setup & Run (5 min)
```
Choose your OS:
- Linux/Mac: Run `scripts/run.sh`
- Windows: Run `scripts/setup.bat` then `java -jar target/webapplication-0.0.1-SNAPSHOT.war`

OR Read: docs/QUICK_START.md for detailed instructions
```

### Step 3: Explore Features (10 min)
- Open: http://localhost:8080
- Register a new account
- Create a post
- Like and comment
- Search for hashtags
- Try dark mode

### Step 4: Understand the Code (30 min)
```
Read in order:
1. docs/PROJECT_STRUCTURE.md     (See what each file does)
2. src/main/java/...             (Browse the code)
3. docs/IMPLEMENTATION_SUMMARY.md (Understand the architecture)
```

### Step 5: Deep Dive (Optional - 1 hour)
```
Read: docs/INTERVIEW_GUIDE.md
- Understand design decisions
- Learn what each technology does
- See why certain choices were made
```

### Step 6: Deploy (Optional)
```
Read: docs/DEPLOYMENT_GUIDE.md
- Choose deployment method (Docker/JAR/WAR/Source)
- Follow step-by-step instructions
- Deploy to cloud or on-premises
```

---

## 🚀 Common Tasks

### Start the Application
```bash
./scripts/run.sh              # Linux/Mac
scripts\setup.bat             # Windows
```

### View Application Logs
```bash
tail -f logs/spring.log
```

### Rebuild the Project
```bash
mvn clean install -DskipTests
```

### Run Tests
```bash
mvn test
```

### Create Database Backup
```bash
mysqldump -u root -p social_media > backup.sql
```

### Change MySQL Password
```bash
# Edit: config/.env
DB_PASSWORD=your_new_password

# Restart app: ./scripts/run.sh
```

---

## 📊 Project Statistics

| Metric | Value |
|--------|-------|
| **Total Lines of Code** | ~5,000 |
| **Backend Code** | ~3,500 (Java) |
| **Frontend Code** | ~850 (JavaScript) |
| **Configuration** | ~250 (XML, Properties) |
| **Documentation** | ~2,000+ lines |
| **Database Tables** | 5 |
| **API Endpoints** | 20+ |
| **Features Implemented** | 15+ |
| **Test Coverage** | Ready for testing |

---

## 🔐 Security Features

✅ JWT Authentication (Stateless)
✅ Password Hashing (BCrypt)
✅ Authorization Checks (Own posts only)
✅ CSRF Protection Disabled (JWT is safe from CSRF)
✅ Environment Variables (No hardcoded secrets)
✅ Connection Pooling (HikariCP)
✅ SQL Injection Prevention (JPA Parameterized)

---

## ⚡ Performance Features

✅ Connection Pooling (5-10 connections)
✅ Database Indexing (on user_id, created_at)
✅ Lazy Loading (fetch only when needed)
✅ DTO Conversion (N+1 problem solved)
✅ Infinite Scroll (load 10 posts at a time)
✅ Image Lazy Loading (load on scroll)
✅ Response Caching (optional headers)
✅ Debounced Search (reduce API calls)

---

## 🐛 Debugging

### Check Server Status
```bash
curl http://localhost:8080/api/posts
```

### View Environment Variables
```bash
cat config/.env
```

### Check Database Connection
```bash
mysql -u root -p social_media -e "SELECT COUNT(*) FROM posts;"
```

### View Application Logs
```bash
tail -100 logs/spring.log | grep ERROR
```

---

## 📚 Learning Resources

**For Understanding Architecture:**
- `docs/INTERVIEW_GUIDE.md` - Best resource!
- `docs/IMPLEMENTATION_SUMMARY.md` - Technical details

**For Deployment:**
- `docs/DEPLOYMENT_GUIDE.md` - 4 different methods

**For Features:**
- `docs/FEATURES.md` - What each feature does

**For Setup:**
- `docs/QUICK_START.md` - Step-by-step guide

---

## 🎓 Interview Prep

If preparing for technical interviews, read:
1. `docs/INTERVIEW_GUIDE.md` (Complete reference)
2. `docs/IMPLEMENTATION_SUMMARY.md` (Architecture)
3. Source code in `src/main/java/`

**Topics covered:**
- System design
- Database optimization
- Authentication/Authorization
- Performance tuning
- Technology choices & trade-offs
- Scaling strategies

---

## ✅ Pre-Deployment Checklist

Before deploying to production:

- [ ] Read `docs/QUICK_START.md`
- [ ] Test all features locally
- [ ] Update `config/.env` with production credentials
- [ ] Run `mvn clean package` successfully
- [ ] Test using `scripts/VERIFY_FEATURES.sh`
- [ ] Read `docs/DEPLOYMENT_GUIDE.md`
- [ ] Choose deployment method
- [ ] Set up monitoring and logging
- [ ] Configure backups
- [ ] Deploy to staging first
- [ ] Test in staging environment
- [ ] Deploy to production

---

## 📞 Support

**Setup Issues?**
→ Read `docs/QUICK_START.md`

**Code Questions?**
→ Read `docs/IMPLEMENTATION_SUMMARY.md`

**Deployment Help?**
→ Read `docs/DEPLOYMENT_GUIDE.md`

**Interview Prep?**
→ Read `docs/INTERVIEW_GUIDE.md`

**Architecture Deep Dive?**
→ Read `docs/INTERVIEW_GUIDE.md` + Source Code

---

## 🎉 You're All Set!

Choose what you want to do:

1. **Run it**: `./scripts/run.sh`
2. **Learn it**: Read `docs/IMPLEMENTATION_SUMMARY.md`
3. **Deploy it**: Follow `docs/DEPLOYMENT_GUIDE.md`
4. **Master it**: Study `docs/INTERVIEW_GUIDE.md`

---

**Last Updated**: January 28, 2026
**Project**: InstaClone Social Media Platform
