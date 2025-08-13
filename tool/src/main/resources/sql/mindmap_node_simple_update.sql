-- 更新思维导图节点表，将文档关联字段改为VARCHAR类型以支持UUID
-- 代码片段关联字段保持BIGINT类型
-- 执行前请备份数据库

-- 只修改文档关联字段类型
ALTER TABLE mind_map_node 
MODIFY COLUMN doc_record_id VARCHAR(255) NULL;

-- 验证表结构
DESCRIBE mind_map_node;
