-- 视频管理模块数据库表结构

-- 创建视频记录表
CREATE TABLE IF NOT EXISTS `video_records` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `description` text COMMENT '描述',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_title` (`title`),
  KEY `idx_created_at` (`created_at`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='视频记录表';

-- 创建视频文件表
CREATE TABLE IF NOT EXISTS `video_files` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `record_id` bigint(20) NOT NULL COMMENT '视频记录ID',
  `name` varchar(255) NOT NULL COMMENT '文件名',
  `file_path` varchar(500) NOT NULL COMMENT '文件路径',
  `file_size` bigint(20) NOT NULL COMMENT '文件大小（字节）',
  `mime_type` varchar(100) NOT NULL COMMENT '文件类型',
  `duration` int(11) DEFAULT NULL COMMENT '视频时长（秒）',
  `resolution` varchar(50) DEFAULT NULL COMMENT '视频分辨率',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_record_id` (`record_id`),
  KEY `idx_created_at` (`created_at`),
  CONSTRAINT `fk_video_files_record_id` FOREIGN KEY (`record_id`) REFERENCES `video_records` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='视频文件表';

-- 插入示例数据
INSERT INTO `video_records` (`title`, `description`) VALUES 
('示例视频1', '这是一个示例视频记录'),
('示例视频2', '这是另一个示例视频记录');

-- 插入示例视频文件数据
INSERT INTO `video_files` (`record_id`, `name`, `file_path`, `file_size`, `mime_type`, `duration`, `resolution`) VALUES 
(1, 'sample1.mp4', '/uploads/videos/sample1.mp4', 1048576, 'video/mp4', 120, '1920x1080'),
(1, 'sample2.mp4', '/uploads/videos/sample2.mp4', 2097152, 'video/mp4', 180, '1280x720'),
(2, 'demo.mp4', '/uploads/videos/demo.mp4', 3145728, 'video/mp4', 300, '1920x1080'); 