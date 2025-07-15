-- 创建数据库
CREATE DATABASE IF NOT EXISTS algorithm_study DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE algorithm_study;

-- 算法表
CREATE TABLE algorithms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL COMMENT '算法名称',
    category VARCHAR(100) NOT NULL COMMENT '算法分类',
    description TEXT COMMENT '算法描述',
    code TEXT COMMENT '代码实现',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '算法表';

-- 算法图片表
CREATE TABLE algorithm_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    algorithm_id BIGINT NOT NULL COMMENT '算法ID',
    image_data LONGTEXT NOT NULL COMMENT '图片base64数据',
    description TEXT COMMENT '图片说明',
    sort_order INT DEFAULT 0 COMMENT '排序顺序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (algorithm_id) REFERENCES algorithms(id) ON DELETE CASCADE
) COMMENT '算法图片表';

-- 创建索引
CREATE INDEX idx_algorithm_category ON algorithms(category);
CREATE INDEX idx_algorithm_images_algorithm_id ON algorithm_images(algorithm_id);
CREATE INDEX idx_algorithm_images_sort_order ON algorithm_images(sort_order); 