-- Code snippets tables
CREATE TABLE IF NOT EXISTS code_snippet_records (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  description TEXT NULL,
  code LONGTEXT NOT NULL,
  language VARCHAR(100) NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  INDEX idx_title (title),
  INDEX idx_language (language),
  INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Code snippet tags table (for future extensibility)
CREATE TABLE IF NOT EXISTS code_snippet_tags (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  record_id BIGINT NOT NULL,
  tag_name VARCHAR(100) NOT NULL,
  created_at DATETIME NOT NULL,
  CONSTRAINT fk_code_snippet_tag_record FOREIGN KEY (record_id) REFERENCES code_snippet_records(id) ON DELETE CASCADE,
  INDEX idx_record_id (record_id),
  INDEX idx_tag_name (tag_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;