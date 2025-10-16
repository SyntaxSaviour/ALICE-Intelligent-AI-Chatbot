@echo off
echo Setting up and running Chatbot UI...

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

REM Compile the project
echo Compiling the project...
call mvn clean compile
if %errorlevel% neq 0 (
    echo Error: Compilation failed
    pause
    exit /b 1
)

REM Run the application
echo Starting the Chatbot UI...
call mvn javafx:run