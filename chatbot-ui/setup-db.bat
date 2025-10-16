@echo off
echo Setting up Chatbot database...

REM Set up the environment
call ..\..\setenv.bat >nul

REM Check if Maven is available
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo Error: Maven is not installed or not in PATH
    echo Please run ..\..\setenv.bat first
    pause
    exit /b 1
)

REM Compile and run the database setup
echo Compiling and running database setup...
call mvn compile exec:java
if %errorlevel% neq 0 (
    echo Error: Database setup failed
    pause
    exit /b 1
)

echo Database setup completed successfully!
pause