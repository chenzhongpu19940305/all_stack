-- 创建算法表
CREATE TABLE IF NOT EXISTS algorithms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    description TEXT,
    code TEXT,
    created_at DATETIME,
    updated_at DATETIME
);

-- 创建算法图片表
CREATE TABLE IF NOT EXISTS algorithm_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    algorithm_id BIGINT NOT NULL,
    src TEXT NOT NULL,
    `desc` TEXT,
    sort_order INT DEFAULT 0,
    created_at DATETIME,
    FOREIGN KEY (algorithm_id) REFERENCES algorithms(id) ON DELETE CASCADE
);

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME
); 