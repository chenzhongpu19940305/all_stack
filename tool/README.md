# Tool Service

一个基于Spring Boot的工具服务项目。

## 技术栈

- Spring Boot 2.7.18
- MySQL 8.0
- Maven

## 配置说明

### 数据库配置

项目使用MySQL数据库，配置信息在 `src/main/resources/application.yml` 中：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tool_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 数据库初始化

1. 确保MySQL服务已启动
2. 创建数据库：
   ```sql
   CREATE DATABASE tool_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
3. 修改 `application.yml` 中的数据库连接信息（用户名、密码等）

## 运行项目

1. 确保MySQL服务运行并配置正确
2. 运行项目：
   ```bash
   mvn spring-boot:run
   ```
3. 访问健康检查接口：`http://localhost:8080/api/hello/health`

## API接口

- `GET /api/hello/health` - 健康检查
- `GET /api/hello/welcome` - 欢迎信息

## 注意事项

- 请确保MySQL服务已启动
- 请根据实际情况修改数据库连接配置
- 项目已移除JPA和H2相关配置，改为使用MySQL 