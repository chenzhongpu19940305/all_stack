-- 更新gallery_images表结构，将图片数据存储在数据库中
-- 如果表已存在，先删除旧表
DROP TABLE IF EXISTS gallery_images;

-- 重新创建图片表，使用image_data字段存储base64图片数据
CREATE TABLE gallery_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL COMMENT '图片名称',
    image_data LONGTEXT NOT NULL COMMENT '图片base64数据',
    file_size BIGINT COMMENT '文件大小(字节)',
    content_type VARCHAR(100) COMMENT '文件类型',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    record_id BIGINT NOT NULL COMMENT '关联的记录ID',
    FOREIGN KEY (record_id) REFERENCES gallery_records(id) ON DELETE CASCADE,
    INDEX idx_record_id (record_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图片表';

-- 确保gallery_records表存在
CREATE TABLE IF NOT EXISTS gallery_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL COMMENT '记录标题',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_title (title),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI问答记录表';

-- 插入示例记录数据
INSERT INTO gallery_records (title, created_at) VALUES 
('Java算法实现示例', NOW()),
('Spring Boot项目结构', NOW()),
('Vue.js前端开发', NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title); 