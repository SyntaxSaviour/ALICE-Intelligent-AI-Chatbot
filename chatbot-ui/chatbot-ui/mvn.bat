@echo off
REM This script runs Maven with the correct environment setup

REM Get the directory where this script is located
setlocal
set "SCRIPT_DIR=%~dp0"

REM Navigate to the project directory (where pom.xml is located)
cd /d "%SCRIPT_DIR%"

REM Run Maven with all provided arguments using the full path to Maven
call ..\..\apache-maven-3.9.11\bin\mvn.cmd %*