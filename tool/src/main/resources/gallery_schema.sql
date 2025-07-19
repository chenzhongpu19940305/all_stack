-- Gallery模块数据库初始化脚本
-- 创建AI问答记录表
CREATE TABLE IF NOT EXISTS gallery_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL COMMENT '记录标题',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_title (title),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI问答记录表';

-- 创建图片表
CREATE TABLE IF NOT EXISTS gallery_images (
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

-- 插入示例数据
INSERT INTO gallery_records (title, created_at) VALUES 
('Java算法实现示例', NOW()),
('Spring Boot项目结构', NOW()),
('Vue.js前端开发', NOW());

-- 注意：实际图片数据需要先上传文件后才能插入 