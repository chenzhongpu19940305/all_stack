-- 思维导图表结构
CREATE TABLE IF NOT EXISTS mind_map (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    layout VARCHAR(50) DEFAULT 'mindmap',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 思维导图节点表结构
CREATE TABLE IF NOT EXISTS mind_map_node (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    map_id BIGINT NOT NULL,
    parent_id BIGINT NULL,
    text VARCHAR(500) NOT NULL,
    x INT NOT NULL DEFAULT 0,
    y INT NOT NULL DEFAULT 0,
    width INT NOT NULL DEFAULT 100,
    height INT NOT NULL DEFAULT 50,
    shape VARCHAR(50) NOT NULL DEFAULT 'rectangle',
    background_color VARCHAR(20) NOT NULL DEFAULT '#2196F3',
    border_color VARCHAR(20) NOT NULL DEFAULT '#1976D2',
    font_size INT NOT NULL DEFAULT 14,
    is_root TINYINT(1) NOT NULL DEFAULT 0,
    collapsed TINYINT(1) NOT NULL DEFAULT 0,
    detail_record_id BIGINT NULL,
    detail_record_title VARCHAR(255) NULL,
    doc_record_id VARCHAR(255) NULL,
    doc_record_title VARCHAR(255) NULL,
    code_record_id BIGINT NULL,
    code_record_title VARCHAR(255) NULL,
    hyperlink VARCHAR(500) NULL,
    CONSTRAINT fk_mind_map_node_map FOREIGN KEY (map_id) REFERENCES mind_map(id) ON DELETE CASCADE,
    INDEX idx_map_id (map_id),
    INDEX idx_parent_id (parent_id)
); 