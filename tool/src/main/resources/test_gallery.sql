-- 测试Gallery模块数据库结构
-- 确保数据库表结构正确

-- 检查gallery_records表
DESCRIBE gallery_records;

-- 检查gallery_images表
DESCRIBE gallery_images;

-- 插入测试数据
INSERT INTO gallery_records (title, created_at) VALUES 
('测试记录1', NOW()),
('测试记录2', NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title);

-- 获取记录ID用于测试
SET @record_id = (SELECT id FROM gallery_records WHERE title = '测试记录1' LIMIT 1);

-- 插入测试图片数据（base64格式）
INSERT INTO gallery_images (name, image_data, file_size, content_type, created_at, record_id) VALUES 
('test_image_1.png', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==', 100, 'image/png', NOW(), @record_id),
('test_image_2.jpg', 'data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCAABAAEDASIAAhEBAxEB/8QAFQABAQAAAAAAAAAAAAAAAAAAAAv/xAAUEAEAAAAAAAAAAAAAAAAAAAAA/8QAFQEBAQAAAAAAAAAAAAAAAAAAAAX/xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oADAMBAAIRAxEAPwA/8A', 200, 'image/jpeg', NOW(), @record_id);

-- 查询测试数据
SELECT 
    r.id as record_id,
    r.title,
    r.created_at,
    COUNT(i.id) as image_count
FROM gallery_records r
LEFT JOIN gallery_images i ON r.id = i.record_id
GROUP BY r.id, r.title, r.created_at;

-- 查询图片数据
SELECT 
    i.id,
    i.name,
    SUBSTRING(i.image_data, 1, 50) as image_data_preview,
    i.file_size,
    i.content_type,
    i.record_id
FROM gallery_images i
WHERE i.record_id = @record_id; 