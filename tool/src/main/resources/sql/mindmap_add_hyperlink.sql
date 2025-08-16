-- 为现有的mind_map_node表添加超链接字段
-- 如果字段不存在则添加
ALTER TABLE mind_map_node 
ADD COLUMN IF NOT EXISTS hyperlink VARCHAR(500) NULL 
COMMENT '节点超链接地址';

-- 为超链接字段添加索引以提高查询性能
CREATE INDEX IF NOT EXISTS idx_hyperlink ON mind_map_node(hyperlink);

-- 查看表结构确认字段已添加
DESCRIBE mind_map_node;
