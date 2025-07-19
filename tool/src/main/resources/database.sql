-- 创建数据库
CREATE DATABASE IF NOT EXISTS tool_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE tool_db;

-- 创建示例表（如果需要的话）
-- CREATE TABLE IF NOT EXISTS example_table (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     name VARCHAR(100) NOT NULL,
--     description TEXT,
--     created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci; 