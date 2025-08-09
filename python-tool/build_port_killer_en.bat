@echo off
chcp 65001 >nul
echo ========================================
echo Port Killer Tool - Build Script
echo ========================================
echo.

echo Installing dependencies...
pip install -r requirements.txt

if %errorlevel% neq 0 (
    echo Dependency installation failed!
    pause
    exit /b 1
)

echo.
echo Building exe file...
pyinstaller --onefile --windowed --name="PortKiller" --icon=NONE port_killer.py

if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Build completed!
echo exe file location: dist\PortKiller.exe
echo ========================================
echo.
echo Press any key to exit...
pause > nul