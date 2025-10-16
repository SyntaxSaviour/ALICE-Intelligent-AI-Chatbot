@echo off
echo Clearing Chat History...

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

REM Compile and run the clear chat history utility
echo Compiling and running clear chat history utility...
call mvn compile exec:java@clear-chat-history
if %errorlevel% neq 0 (
    echo Error: Failed to clear chat history
    pause
    exit /b 1
)

echo Chat history cleared successfully!
pause