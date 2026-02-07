@echo off
cls
echo ========================================
echo    BOOKING MANAGEMENT SYSTEM
echo    Starting...
echo ========================================
echo.

REM Go to the directory where this batch file is located
cd /d "%~dp0"

REM Go to src folder
cd src

echo Step 1: Compiling Java files...
echo.

javac --module-path C:\javafx-sdk-25.0.2\lib --add-modules javafx.controls -cp "..\lib\*" *.java

if %errorlevel% neq 0 (
    echo.
    echo ========================================
    echo ERROR: Compilation failed!
    echo ========================================
    echo.
    echo Possible issues:
    echo 1. Check that all .java files are in the src folder
    echo 2. Check that sqlite-jdbc jar is in the lib folder
    echo 3. Check that JavaFX is at C:\javafx-sdk-25.0.2
    echo.
    pause
    exit /b 1
)

echo ========================================
echo Compilation successful!
echo ========================================
echo.
echo Step 2: Starting application...
echo.

java --module-path C:\javafx-sdk-25.0.2\lib --add-modules javafx.controls -cp ".;..\lib\*" Main

if %errorlevel% neq 0 (
    echo.
    echo ========================================
    echo ERROR: Failed to start!
    echo ========================================
    echo.
    pause
)

echo.
echo Application closed.