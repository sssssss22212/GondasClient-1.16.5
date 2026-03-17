@echo off
REM Build script for Windows
REM Requires Java 8 JDK

echo ==========================================
echo   GondasClient v2.0.0 Build Script
echo   For Minecraft 1.16.5 Forge
echo ==========================================

REM Check Java version
java -version 2>&1 | findstr /i "version" | findstr /i "1.8" >nul
if errorlevel 1 (
    echo WARNING: Java 8 is recommended for building Forge mods.
    echo Current Java version:
    java -version
    echo.
    echo If build fails, please install Java 8 JDK.
)

echo.
echo Running Gradle build...
call gradlew.bat build --no-daemon

if exist "build\libs\GondasClient-1.16.5-2.0.0.jar" (
    echo.
    echo ==========================================
    echo   BUILD SUCCESSFUL!
    echo ==========================================
    echo.
    echo Output: build\libs\GondasClient-1.16.5-2.0.0.jar
    echo.
    echo Installation:
    echo 1. Copy the JAR file to your Minecraft mods folder
    echo 2. For PojavLauncher: /games/PojavLauncher/mods/
    echo 3. For MCLauncher: check your launcher's mods folder
) else (
    echo.
    echo Build failed. Check the output above for errors.
)

pause
