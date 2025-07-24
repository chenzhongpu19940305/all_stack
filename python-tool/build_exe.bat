@echo off
echo ========================================
echo JDK切换工具 - 构建脚本
echo ========================================
echo.

echo 正在安装依赖...
pip install -r requirements.txt

if %errorlevel% neq 0 (
    echo 依赖安装失败！
    pause
    exit /b 1
)

echo.
echo 正在构建exe文件...
pyinstaller --onefile --windowed --name="JDK切换工具" --icon=NONE jdk_switcher.py

if %errorlevel% neq 0 (
    echo 构建失败！
    pause
    exit /b 1
)

echo.
echo ========================================
echo 构建完成！
echo exe文件位置: dist\JDK切换工具.exe
echo ========================================
echo.
echo 按任意键退出...
pause > nul