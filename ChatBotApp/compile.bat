@echo off
echo Compiling Java Chatbot Application...

javac -cp "lib/*" -d out src/*.java

if %errorlevel% == 0 (
    echo Compilation successful!
    echo.
    echo To run the application, use:
    echo java -cp "out;lib/*" ChatWindow
) else (
    echo Compilation failed. Please check for errors above.
)

pause