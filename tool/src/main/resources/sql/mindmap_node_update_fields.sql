-- 更新思维导图节点表，将关联字段改为VARCHAR类型以支持UUID
-- 执行前请备份数据库

-- 1. 添加新的VARCHAR字段
ALTER TABLE mind_map_node 
ADD COLUMN doc_record_id_new VARCHAR(255) NULL AFTER detail_record_title,
ADD COLUMN doc_record_title_new VARCHAR(255) NULL AFTER doc_record_id_new,
ADD COLUMN code_record_id_new VARCHAR(255) NULL AFTER doc_record_title_new,
ADD COLUMN code_record_title_new VARCHAR(255) NULL AFTER code_record_id_new;

-- 2. 删除旧的BIGINT字段（如果存在）
-- ALTER TABLE mind_map_node DROP COLUMN IF EXISTS doc_record_id;
-- ALTER TABLE mind_map_node DROP COLUMN IF EXISTS doc_record_title;
-- ALTER TABLE mind_map_node DROP COLUMN IF EXISTS code_record_id;
-- ALTER TABLE mind_map_node DROP COLUMN IF EXISTS code_record_title;

-- 3. 重命名新字段
ALTER TABLE mind_map_node 
CHANGE COLUMN doc_record_id_new doc_record_id VARCHAR(255) NULL,
CHANGE COLUMN doc_record_title_new doc_record_title VARCHAR(255) NULL,
CHANGE COLUMN code_record_id_new code_record_id VARCHAR(255) NULL,
CHANGE COLUMN code_record_title_new code_record_title VARCHAR(255) NULL;

-- 4. 验证表结构
DESCRIBE mind_map_node;
