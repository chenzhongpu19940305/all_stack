-- Gallery模块数据库迁移脚本
-- 请在MySQL中执行以下SQL语句来更新现有数据库结构

-- 1. 为gallery_records表添加description字段
ALTER TABLE gallery_records 
ADD COLUMN description TEXT COMMENT '记录描述' AFTER title;

-- 2. 更新现有记录的description字段
UPDATE gallery_records 
SET description = CONCAT('关于', title, '的详细记录') 
WHERE description IS NULL OR description = '';

-- 3. 验证表结构
SELECT 'gallery_records表结构:' as info;
DESCRIBE gallery_records;

SELECT 'gallery_images表结构:' as info;
DESCRIBE gallery_images;

-- 4. 验证数据
SELECT '更新后的记录数据:' as info;
SELECT id, title, description, created_at FROM gallery_records;

-- 5. 验证关联查询
SELECT '记录及其关联图片:' as info;
SELECT 
    r.id,
    r.title,
    r.description,
    COUNT(i.id) as image_count
FROM gallery_records r
LEFT JOIN gallery_images i ON r.id = i.record_id
GROUP BY r.id, r.title, r.description
ORDER BY r.created_at DESC; 