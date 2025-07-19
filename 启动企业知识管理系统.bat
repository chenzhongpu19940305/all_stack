@echo off
echo 正在启动企业知识管理系统...
echo.

echo 1. 启动后端服务...
cd tool
start "后端服务" cmd /k "mvn spring-boot:run"
cd ..

echo 2. 等待后端服务启动...
timeout /t 10 /nobreak >nul

echo 3. 启动前端服务...
cd js
start "前端服务" cmd /k "npm run dev"
cd ..

echo.
echo 系统启动完成！
echo 前端地址: http://localhost:5173/enterprise-wiki
echo 后端地址: http://localhost:8080
echo.
echo 按任意键退出...
pause >nul 