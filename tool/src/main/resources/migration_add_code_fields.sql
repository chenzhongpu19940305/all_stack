-- 数据库迁移脚本：为gallery_records表添加代码相关字段
-- 执行时间：2024年

-- 添加代码相关字段
ALTER TABLE gallery_records 
ADD COLUMN description TEXT COMMENT '记录描述' AFTER title,
ADD COLUMN code LONGTEXT COMMENT '代码内容' AFTER description,
ADD COLUMN language VARCHAR(50) COMMENT '编程语言' AFTER code;

-- 添加索引
ALTER TABLE gallery_records 
ADD INDEX idx_language (language);

-- 更新现有记录的描述字段（如果为空）
UPDATE gallery_records 
SET description = '暂无描述' 
WHERE description IS NULL OR description = '';

-- 更新现有记录的语言字段（如果为空）
UPDATE gallery_records 
SET language = 'text' 
WHERE language IS NULL OR language = '';

-- 验证迁移结果
SELECT 
    COUNT(*) as total_records,
    COUNT(description) as records_with_description,
    COUNT(code) as records_with_code,
    COUNT(language) as records_with_language
FROM gallery_records; 