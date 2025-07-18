# MySQL 数据库设置指南

## 1. 安装MySQL

### Windows
1. 下载MySQL安装包：https://dev.mysql.com/downloads/installer/
2. 运行安装程序，选择"Server only"或"Custom"
3. 设置root密码为：123456
4. 完成安装

### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```

### macOS
```bash
brew install mysql
brew services start mysql
```

## 2. 创建数据库

1. 登录MySQL：
```bash
mysql -u root -p
```

2. 执行初始化脚本：
```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS gallery_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE gallery_db;

-- 创建gallery_records表
CREATE TABLE IF NOT EXISTS gallery_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建gallery_images表
CREATE TABLE IF NOT EXISTS gallery_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    image_data LONGBLOB NOT NULL,
    record_id BIGINT,
    FOREIGN KEY (record_id) REFERENCES gallery_records(id) ON DELETE CASCADE
);

-- 插入一些测试数据
INSERT INTO gallery_records (title) VALUES 
('AI问答记录1'),
('AI问答记录2'),
('AI问答记录3');
```

## 3. 配置应用

### 3.1 添加MySQL依赖

在 `pom.xml` 中添加以下依赖：

```xml
<!-- MySQL 数据库驱动 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
    <scope>runtime</scope>
</dependency>
```

### 3.2 配置数据源

在 `application.yml` 中配置：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/gallery_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
```

## 4. 启动应用

1. 确保MySQL服务正在运行
2. 启动Spring Boot应用：
```bash
mvn spring-boot:run
```

3. 访问测试页面：http://localhost:8080/gallery-test

## 5. 常见问题

### 5.1 连接失败
- 检查MySQL服务是否启动
- 检查用户名密码是否正确
- 检查数据库是否存在

### 5.2 字符编码问题
- 确保数据库使用utf8mb4字符集
- 检查连接URL中的字符编码参数

### 5.3 时区问题
- 在连接URL中添加serverTimezone参数
- 确保MySQL服务器时区设置正确

## 6. 数据库管理

### 6.1 查看数据
```sql
USE gallery_db;
SELECT * FROM gallery_records;
SELECT * FROM gallery_images;
```

### 6.2 备份数据库
```bash
mysqldump -u root -p gallery_db > backup.sql
```

### 6.3 恢复数据库
```bash
mysql -u root -p gallery_db < backup.sql
``` 