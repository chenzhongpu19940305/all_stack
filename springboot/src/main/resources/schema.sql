-- 创建视频表
CREATE TABLE IF NOT EXISTS videos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(100) NOT NULL,
    description TEXT,
    thumbnail VARCHAR(500),
    video_url VARCHAR(500),
    duration VARCHAR(20),
    views BIGINT DEFAULT 0,
    likes BIGINT DEFAULT 0,
    category VARCHAR(50),
    upload_time DATETIME,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

-- 创建分类表
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    icon_url VARCHAR(500),
    sort_order INT DEFAULT 0,
    active BOOLEAN DEFAULT TRUE,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

-- 插入初始分类数据
INSERT INTO categories (name, code, description, sort_order, active, created_at, updated_at) VALUES
('全部', 'all', '所有视频分类', 0, true, NOW(), NOW()),
('业务', 'business', '业务相关视频', 1, true, NOW(), NOW()),
('前端', 'frontend', '前端开发视频', 2, true, NOW(), NOW()),
('后端', 'backend', '后端开发视频', 3, true, NOW(), NOW()),
('测试', 'test', '测试相关视频', 4, true, NOW(), NOW()),
('运维', 'ops', '运维相关视频', 5, true, NOW(), NOW()),
('需求', 'requirement', '需求管理视频', 6, true, NOW(), NOW());

-- 插入初始视频数据
INSERT INTO videos (title, author, description, thumbnail, video_url, duration, views, likes, category, upload_time, created_at, updated_at) VALUES
('Vue.js 3.0 完整教程', '前端大师', '从零开始学习Vue.js 3.0，包含响应式原理、组合式API等核心概念。', 'https://via.placeholder.com/300x200/42b883/ffffff?text=Vue.js', 'https://example.com/vue-tutorial.mp4', '15:30', 125000, 3200, 'frontend', NOW(), NOW(), NOW()),
('Spring Boot 实战开发', 'Java工程师', '使用Spring Boot快速构建Web应用，包含数据库操作、REST API等。', 'https://via.placeholder.com/300x200/ff6b6b/ffffff?text=Spring', 'https://example.com/spring-tutorial.mp4', '22:15', 89000, 2100, 'backend', NOW(), NOW(), NOW()),
('业务需求分析实战', '产品经理', '深入分析业务需求，掌握需求分析方法论，提升产品设计能力。', 'https://via.placeholder.com/300x200/4ecdc4/ffffff?text=业务', 'https://example.com/business-analysis.mp4', '18:45', 156000, 4500, 'business', NOW(), NOW(), NOW()),
('自动化测试实践', '测试工程师', '从零开始学习自动化测试，包含单元测试、集成测试、端到端测试等。', 'https://via.placeholder.com/300x200/ffa726/ffffff?text=测试', 'https://example.com/testing-tutorial.mp4', '12:30', 234000, 6700, 'test', NOW(), NOW(), NOW()),
('DevOps 运维实践', '运维工程师', '学习DevOps最佳实践，包含CI/CD、容器化、监控等运维技能。', 'https://via.placeholder.com/300x200/9c27b0/ffffff?text=运维', 'https://example.com/devops-tutorial.mp4', '25:20', 345000, 8900, 'ops', NOW(), NOW(), NOW()),
('需求管理方法论', '需求分析师', '系统学习需求管理，包含需求收集、分析、验证等完整流程。', 'https://via.placeholder.com/300x200/ff5722/ffffff?text=需求', 'https://example.com/requirement-management.mp4', '20:15', 178000, 5200, 'requirement', NOW(), NOW(), NOW());

-- 创建应用表
CREATE TABLE IF NOT EXISTS applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    status VARCHAR(20) DEFAULT 'enabled',
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

-- 创建部署单元表
CREATE TABLE IF NOT EXISTS deployment_units (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    status VARCHAR(20) DEFAULT 'enabled',
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

-- 创建特性表
CREATE TABLE IF NOT EXISTS features (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    application_code VARCHAR(50) NOT NULL,
    deployment_unit_code VARCHAR(50) NOT NULL,
    image_url VARCHAR(500),
    status VARCHAR(20) DEFAULT 'enabled',
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (application_code) REFERENCES applications(code),
    FOREIGN KEY (deployment_unit_code) REFERENCES deployment_units(code)
);

-- 创建特性配置表
CREATE TABLE IF NOT EXISTS feature_configs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    feature_id BIGINT NOT NULL,
    `key` VARCHAR(100) NOT NULL,
    value TEXT,
    description TEXT,
    status VARCHAR(20) DEFAULT 'enabled',
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (feature_id) REFERENCES features(id)
);

-- 创建gallery_records表
CREATE TABLE IF NOT EXISTS gallery_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建gallery_images表
CREATE TABLE IF NOT EXISTS gallery_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    image_data LONGBLOB NOT NULL,
    record_id BIGINT,
    FOREIGN KEY (record_id) REFERENCES gallery_records(id) ON DELETE CASCADE
);

-- 插入一些测试数据
INSERT INTO gallery_records (title) VALUES 
('AI问答记录1'),
('AI问答记录2'),
('AI问答记录3'); 