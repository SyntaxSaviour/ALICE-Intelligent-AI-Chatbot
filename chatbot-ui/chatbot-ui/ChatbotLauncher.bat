@echo off
REM Hide the console window
if not DEFINED IS_MINIMIZED set IS_MINIMIZED=1 && start "" /min "%~dpnx0" %* && exit

echo Launching Chatbot Application...

REM Navigate to the project directory
cd /d "%~dp0"

REM Set up the environment
call ..\..\setenv.bat >nul

REM Check if Maven is available
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo Error: Maven is not installed or not in PATH
    timeout /t 5 >nul
    exit /b 1
)

REM Compile and run the application
echo Starting the Chatbot Application...
call mvn clean compile javafx:run

if %errorlevel% neq 0 (
    echo Error: Failed to start the application
    timeout /t 5 >nul
    exit /b 1
)