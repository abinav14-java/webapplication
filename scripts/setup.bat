@echo off
REM InstaClone - Easy Setup Script for Windows

setlocal enabledelayedexpansion

echo.
echo ╔═══════════════════════════════════════════════════════╗
echo ║          InstaClone - Setup ^& Run Script              ║
echo ║              Windows Edition                          ║
echo ╚═══════════════════════════════════════════════════════╝
echo.

REM Check Java
echo Checking Java installation...
java -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java not found!
    echo Please install Java from: https://java.com
    echo.
    pause
    exit /b 1
)
for /f "tokens=*" %%i in ('java -version 2^>^&1 ^| findstr /R "version"') do set JAVA_VERSION=%%i
echo [OK] Java found: %JAVA_VERSION%
echo.

REM Check Maven (if running from source)
if exist "pom.xml" (
    echo Checking Maven installation...
    mvn -version >nul 2>&1
    if errorlevel 1 (
        echo [ERROR] Maven not found!
        echo Please install Maven from: https://maven.apache.org/download.cgi
        echo.
        pause
        exit /b 1
    )
    echo [OK] Maven found
    echo.
    
    REM Build
    echo Building application...
    call mvn clean package -DskipTests
    if errorlevel 1 (
        echo [ERROR] Build failed!
        pause
        exit /b 1
    )
    echo [OK] Build successful
    echo.
)

REM Check MySQL
echo Checking MySQL connection...
mysql -u root -proot -e "SELECT 1" >nul 2>&1
if errorlevel 1 (
    echo [WARNING] MySQL not running or wrong credentials
    echo Make sure MySQL is running:
    echo   - Start MySQL Service in Windows Services
    echo   - Or run: mysql -u root -p (and enter your password)
    echo.
    set /p CONTINUE="Continue anyway (y/n)? "
    if /i not "!CONTINUE!"=="y" (
        exit /b 1
    )
) else (
    echo [OK] MySQL is running
    
    REM Create database if not exists
    echo Setting up database...
    mysql -u root -proot -e "CREATE DATABASE IF NOT EXISTS instaclo;"
    mysql -u root -proot -e "CREATE USER IF NOT EXISTS 'instauser'@'localhost' IDENTIFIED BY 'password123';"
    mysql -u root -proot -e "GRANT ALL PRIVILEGES ON instaclo.* TO 'instauser'@'localhost';"
    mysql -u root -proot -e "FLUSH PRIVILEGES;"
    echo [OK] Database configured
)
echo.

REM Run application
echo Starting InstaClone...
echo.

if exist "target\webapplication-0.0.1-SNAPSHOT.jar" (
    echo [OK] Running from JAR file
    java -jar target\webapplication-0.0.1-SNAPSHOT.jar
    goto end
)

if exist "pom.xml" (
    echo [OK] Running from Maven
    call mvn spring-boot:run
    goto end
)

echo [ERROR] Could not find JAR or pom.xml
pause
exit /b 1

:end
echo.
echo ═════════════════════════════════════════════════════════
echo [OK] Application should be running at:
echo      http://localhost:8080
echo ═════════════════════════════════════════════════════════
echo.
pause
