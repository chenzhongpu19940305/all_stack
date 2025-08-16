-- 测试超链接功能的SQL脚本
-- 在应用超链接字段后运行此脚本来验证功能

-- 1. 检查表结构
DESCRIBE mind_map_node;

-- 2. 检查是否有现有的思维导图数据
SELECT COUNT(*) as total_maps FROM mind_map;
SELECT COUNT(*) as total_nodes FROM mind_map_node;

-- 3. 查看一个示例节点的所有字段（如果有数据的话）
SELECT 
    id, 
    map_id, 
    parent_id, 
    text, 
    hyperlink,
    detail_record_id,
    doc_record_id,
    code_record_id
FROM mind_map_node 
LIMIT 5;

-- 4. 测试插入一个带超链接的测试节点（可选）
-- INSERT INTO mind_map_node (
--     map_id, 
--     parent_id, 
--     text, 
--     x, y, width, height,
--     shape, background_color, border_color, font_size,
--     is_root, collapsed,
--     hyperlink
-- ) VALUES (
--     1, -- 确保这个map_id存在
--     NULL,
--     '测试超链接节点',
--     100, 100, 150, 60,
--     'rounded', '#FF9800', '#F57C00', 14,
--     0, 0,
--     'https://www.example.com'
-- );

-- 5. 验证超链接字段的索引
SHOW INDEX FROM mind_map_node WHERE Key_name = 'idx_hyperlink';

-- 6. 测试查询带超链接的节点
SELECT 
    id, 
    text, 
    hyperlink,
    CASE 
        WHEN hyperlink IS NOT NULL THEN '有超链接'
        ELSE '无超链接'
    END as has_hyperlink
FROM mind_map_node 
WHERE hyperlink IS NOT NULL 
ORDER BY id DESC;
