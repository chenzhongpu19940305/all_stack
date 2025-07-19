-- Gallery模块数据库建表语句
-- 请在MySQL中执行以下SQL语句

-- 1. 创建AI问答记录表
CREATE TABLE IF NOT EXISTS gallery_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL COMMENT '记录标题',
    description TEXT COMMENT '记录描述',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_title (title),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI问答记录表';

-- 2. 创建图片表
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

-- 3. 插入示例数据
INSERT INTO gallery_records (title, description, created_at) VALUES 
('Java算法实现示例', '包含各种Java算法实现的截图和说明', NOW()),
('Spring Boot项目结构', 'Spring Boot项目的目录结构和配置文件', NOW()),
('Vue.js前端开发', 'Vue.js组件开发和路由配置示例', NOW());

-- 4. 验证表创建成功
SELECT 'gallery_records表结构:' as info;
DESCRIBE gallery_records;

SELECT 'gallery_images表结构:' as info;
DESCRIBE gallery_images;

SELECT '示例数据:' as info;
SELECT * FROM gallery_records; 