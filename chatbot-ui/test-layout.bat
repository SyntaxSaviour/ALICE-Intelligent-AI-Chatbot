@echo off
echo Testing Layout Fix...

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
call mvn compile
if %errorlevel% neq 0 (
    echo Error: Compilation failed
    pause
    exit /b 1
)

echo.
echo Layout fix has been implemented.
echo To test the resizing:
echo 1. Run the application with .\run.bat
echo 2. Try maximizing the window
echo 3. The chat area should now expand to fill the available space
echo 4. Message bubbles should adjust their width appropriately
echo.
echo Changes made:
echo - Removed fixed width values from FXML elements
echo - Added VBox.vgrow="ALWAYS" to make elements expand
echo - Removed fixed prefWidth from content-column in CSS
echo - Added dynamic width calculation for message bubbles
echo - Added width listener to update content when resized
echo.
echo These changes should allow the chat area to properly expand
echo when the window is maximized or resized.
pause