#!/bin/bash

# InstaClone - Easy Setup Script for Others

echo "╔═══════════════════════════════════════════════════════╗"
echo "║          InstaClone - Setup & Run Script              ║"
echo "╚═══════════════════════════════════════════════════════╝"
echo ""

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Check Java
echo -e "${YELLOW}Checking Java installation...${NC}"
if ! command -v java &> /dev/null; then
    echo -e "${RED}✗ Java not found!${NC}"
    echo "Install Java from: https://java.com"
    exit 1
fi
JAVA_VERSION=$(java -version 2>&1 | head -1)
echo -e "${GREEN}✓ Java found: $JAVA_VERSION${NC}"
echo ""

# Check Maven (if running from source)
if [ -f "pom.xml" ]; then
    echo -e "${YELLOW}Checking Maven installation...${NC}"
    if ! command -v mvn &> /dev/null; then
        echo -e "${RED}✗ Maven not found!${NC}"
        echo "Install Maven from: https://maven.apache.org/download.cgi"
        exit 1
    fi
    echo -e "${GREEN}✓ Maven found${NC}"
    echo ""
    
    # Build
    echo -e "${YELLOW}Building application...${NC}"
    mvn clean package -DskipTests
    if [ $? -ne 0 ]; then
        echo -e "${RED}✗ Build failed!${NC}"
        exit 1
    fi
    echo -e "${GREEN}✓ Build successful${NC}"
    echo ""
fi

# Check MySQL
echo -e "${YELLOW}Checking MySQL connection...${NC}"
if mysql -u root -proot -e "SELECT 1" &> /dev/null; then
    echo -e "${GREEN}✓ MySQL is running${NC}"
    
    # Create database if not exists
    echo -e "${YELLOW}Setting up database...${NC}"
    mysql -u root -proot -e "CREATE DATABASE IF NOT EXISTS instaclo;" 2>/dev/null
    mysql -u root -proot -e "CREATE USER IF NOT EXISTS 'instauser'@'localhost' IDENTIFIED BY 'password123';" 2>/dev/null
    mysql -u root -proot -e "GRANT ALL PRIVILEGES ON instaclo.* TO 'instauser'@'localhost';" 2>/dev/null
    mysql -u root -proot -e "FLUSH PRIVILEGES;" 2>/dev/null
    echo -e "${GREEN}✓ Database configured${NC}"
else
    echo -e "${YELLOW}⚠ MySQL not running or wrong credentials${NC}"
    echo "Make sure MySQL is running with:"
    echo "  Windows: Start MySQL from Services"
    echo "  Mac: brew services start mysql"
    echo "  Linux: sudo service mysql start"
    echo ""
    read -p "Continue anyway? (y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi
echo ""

# Run application
echo -e "${YELLOW}Starting InstaClone...${NC}"
echo ""

if [ -f "target/webapplication-0.0.1-SNAPSHOT.jar" ]; then
    # Run JAR
    echo -e "${GREEN}✓ Running from JAR file${NC}"
    java -jar target/webapplication-0.0.1-SNAPSHOT.jar
elif [ -f "pom.xml" ]; then
    # Run from Maven
    echo -e "${GREEN}✓ Running from Maven${NC}"
    mvn spring-boot:run
else
    echo -e "${RED}✗ Could not find JAR or pom.xml${NC}"
    exit 1
fi

echo ""
echo -e "${GREEN}═════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}✓ Application should be running at:${NC}"
echo -e "${GREEN}  http://localhost:8080${NC}"
echo -e "${GREEN}═════════════════════════════════════════════════════${NC}"
