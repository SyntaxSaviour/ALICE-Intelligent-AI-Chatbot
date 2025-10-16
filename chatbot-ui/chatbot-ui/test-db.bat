@echo off
echo Testing database connection...

REM Check if Maven is available
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo Error: Maven is not installed or not in PATH
    echo Please install Maven and try again
    pause
    exit /b 1
)

REM Create a simple test class
echo public class TestDB { public static void main(String[] args) { System.out.println("Database test utility - please run setup-db.bat instead"); } } > TestDB.java

REM Compile the test class
javac TestDB.java
if %errorlevel% neq 0 (
    echo Error: Failed to compile test class
    del TestDB.java TestDB.class 2>nul
    pause
    exit /b 1
)

REM Run the test
java TestDB
del TestDB.java TestDB.class 2>nul

echo For actual database setup, please run setup-db.bat
pause