@echo off
echo Creating Desktop Shortcut...

REM Run the PowerShell script to create the desktop shortcut
powershell -ExecutionPolicy Bypass -File "%~dp0create-shortcut.ps1"

if %errorlevel% neq 0 (
    echo Error: Failed to create desktop shortcut
    pause
    exit /b 1
)

echo Desktop shortcut created successfully!
echo You can now launch the Chatbot application by double-clicking the 'Chatbot' icon on your desktop.
pause