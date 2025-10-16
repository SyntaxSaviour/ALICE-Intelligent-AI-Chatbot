@echo off
echo Testing ChatService...

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

REM Compile and run the ChatService test
echo Compiling and running ChatService test...
call mvn compile exec:java@test-chat-service
if %errorlevel% neq 0 (
    echo Error: ChatService test failed
    pause
    exit /b 1
)

echo ChatService test completed successfully!
pause