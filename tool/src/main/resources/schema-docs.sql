CREATE TABLE IF NOT EXISTS document_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  description TEXT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS document_file (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  record_id BIGINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  mime_type VARCHAR(100) NOT NULL,
  size BIGINT NOT NULL,
  storage_path VARCHAR(1024) NOT NULL,
  preview_path VARCHAR(1024) NULL,
  sha256 VARCHAR(64) NULL,
  created_at DATETIME NOT NULL,
  CONSTRAINT fk_document_file_record FOREIGN KEY (record_id) REFERENCES document_record(id) ON DELETE CASCADE,
  INDEX idx_record_id (record_id),
  INDEX idx_mime_type (mime_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 