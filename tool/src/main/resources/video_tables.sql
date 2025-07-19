-- 视频记录表
CREATE TABLE IF NOT EXISTS video_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    title VARCHAR(255) NOT NULL COMMENT '视频标题',
    description TEXT COMMENT '视频描述',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频记录表';

-- 视频文件表
CREATE TABLE IF NOT EXISTS video_files (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    record_id BIGINT NOT NULL COMMENT '视频记录ID',
    video_data LONGTEXT NOT NULL COMMENT '视频数据(base64或URL)',
    name VARCHAR(255) NOT NULL COMMENT '文件名',
    size BIGINT COMMENT '文件大小(字节)',
    type VARCHAR(100) COMMENT '文件类型',
    FOREIGN KEY (record_id) REFERENCES video_records(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频文件表';

-- 创建索引
CREATE INDEX idx_video_records_create_time ON video_records(create_time);
CREATE INDEX idx_video_records_title ON video_records(title);
CREATE INDEX idx_video_files_record_id ON video_files(record_id);

-- 插入测试数据
INSERT INTO video_records (title, description) VALUES 
('Vue.js入门教程', '从零开始学习Vue.js框架'),
('React基础教程', 'React框架基础知识讲解'),
('JavaScript进阶', 'JavaScript高级特性学习');

-- 注意：video_files表的数据需要根据实际的视频文件来插入
-- 这里只是示例结构，实际使用时需要根据您的视频保存逻辑来插入数据 