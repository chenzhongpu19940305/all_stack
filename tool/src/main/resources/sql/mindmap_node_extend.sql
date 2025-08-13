-- 为思维导图节点表添加代码片段库和文档管理的关联字段
-- 执行前请确保已备份数据库

-- 添加代码片段库关联字段
ALTER TABLE `mind_map_node` 
ADD COLUMN `code_record_id` BIGINT NULL COMMENT '代码片段记录ID' AFTER `detail_record_title`,
ADD COLUMN `code_record_title` VARCHAR(255) NULL COMMENT '代码片段标题' AFTER `code_record_id`;

-- 添加文档管理关联字段
ALTER TABLE `mind_map_node` 
ADD COLUMN `doc_record_id` BIGINT NULL COMMENT '文档记录ID' AFTER `code_record_title`,
ADD COLUMN `doc_record_title` VARCHAR(255) NULL COMMENT '文档标题' AFTER `doc_record_id`;

-- 添加索引以提高查询性能
ALTER TABLE `mind_map_node` 
ADD INDEX `idx_code_record_id` (`code_record_id`),
ADD INDEX `idx_doc_record_id` (`doc_record_id`);


ALTER TABLE mind_map_node
    MODIFY COLUMN doc_record_id VARCHAR(255) NULL;

-- 查看修改后的表结构
-- DESCRIBE `mind_map_node`; 