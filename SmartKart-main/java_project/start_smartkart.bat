@echo off
TITLE SmartKart One-Click Launcher

echo ===================================================
echo      SmartKart Application - One-Click Launcher
echo ===================================================

echo Ensuring required files are in place...

REM Create directories if they don't exist
if not exist "mysql-connector-j-9.3.0" mkdir "mysql-connector-j-9.3.0"
if not exist "bin" mkdir "bin"

REM Download MySQL Connector if it doesn't exist
if not exist "mysql-connector-j-9.3.0\mysql-connector-j-9.3.0.jar" (
    echo Downloading MySQL Connector...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/9.3.0/mysql-connector-j-9.3.0.jar' -OutFile 'mysql-connector-j-9.3.0\mysql-connector-j-9.3.0.jar'"
)

echo Compiling Java source files...
javac -d bin -cp ".;mysql-connector-j-9.3.0/mysql-connector-j-9.3.0.jar" src/com/smartkart/MainApp.java src/com/smartkart/controller/*.java src/com/smartkart/dao/*.java src/com/smartkart/model/*.java src/com/smartkart/util/*.java src/com/smartkart/view/*.java

if %errorlevel% neq 0 (
    echo.
    echo COMPILE ERROR: Please check the errors above.
    pause
    exit /b %errorlevel%
)

echo.
echo Starting SmartKart application...
echo.

java -cp "bin;mysql-connector-j-9.3.0/mysql-connector-j-9.3.0.jar" com.smartkart.MainApp

echo.
echo ===================================================
echo Application has finished or the window was closed.
echo ===================================================
pause
