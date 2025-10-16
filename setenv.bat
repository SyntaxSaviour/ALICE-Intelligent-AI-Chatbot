@echo off
echo Setting up environment for Maven...

REM Get the current directory (where this script is located)
set "SCRIPT_DIR=%~dp0"

REM Set MAVEN_HOME to the apache-maven directory
set "MAVEN_HOME=%SCRIPT_DIR%apache-maven-3.9.11"

REM Add Maven bin to PATH
set "PATH=%MAVEN_HOME%\bin;%PATH%"

echo MAVEN_HOME set to: %MAVEN_HOME%
echo Maven added to PATH

echo.
echo Verifying Maven installation...
mvn -version

echo.
echo Environment setup complete!
echo You can now run Maven commands in this terminal session.