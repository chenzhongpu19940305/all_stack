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