# 视频管理模块使用说明

## 概述
视频管理模块提供了完整的视频记录管理功能，包括视频记录的增删改查、视频文件上传、搜索等功能。

## 功能特性
- ✅ 视频记录管理（增删改查）
- ✅ 视频文件关联管理
- ✅ 分页查询
- ✅ 标题搜索
- ✅ 逻辑删除
- ✅ 文件上传支持

## 数据库表结构

### 1. video_records（视频记录表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键ID，自增 |
| title | VARCHAR(255) | 视频标题 |
| description | TEXT | 视频描述 |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |
| deleted | TINYINT(1) | 逻辑删除标志（0-未删除，1-已删除） |

### 2. video_files（视频文件表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键ID，自增 |
| record_id | BIGINT | 关联的视频记录ID |
| name | VARCHAR(255) | 文件名 |
| file_path | VARCHAR(500) | 文件路径 |
| file_size | BIGINT | 文件大小（字节） |
| mime_type | VARCHAR(100) | 文件类型 |
| duration | INT | 视频时长（秒） |
| resolution | VARCHAR(50) | 视频分辨率 |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |

## API接口

### 基础路径
```
http://localhost:8080/czp/tool/api/videogallery
```

### 1. 获取视频记录列表
```
GET /records?page=1&size=20
```

**响应示例：**
```json
{
  "success": true,
  "records": [
    {
      "id": 1,
      "title": "示例视频1",
      "description": "这是一个示例视频记录",
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00",
      "videos": [
        {
          "id": 1,
          "name": "sample1.mp4",
          "filePath": "/uploads/videos/sample1.mp4",
          "fileSize": 1048576,
          "mimeType": "video/mp4",
          "duration": 120,
          "resolution": "1920x1080"
        }
      ]
    }
  ],
  "page": 1,
  "size": 20
}
```

### 2. 搜索视频记录
```
POST /records/search
Content-Type: application/json

{
  "query": "示例"
}
```

### 3. 创建视频记录
```
POST /records
Content-Type: application/json

{
  "title": "新视频",
  "description": "视频描述",
  "videos": [
    {
      "name": "video.mp4",
      "filePath": "/uploads/videos/video.mp4",
      "fileSize": 1048576,
      "mimeType": "video/mp4",
      "duration": 120,
      "resolution": "1920x1080"
    }
  ]
}
```

### 4. 更新视频记录
```
PUT /records/{id}
Content-Type: application/json

{
  "title": "更新后的标题",
  "description": "更新后的描述",
  "videos": [...]
}
```

### 5. 删除视频记录
```
DELETE /records/{id}
```

### 6. 获取单个视频记录
```
GET /records/{id}
```

### 7. 上传视频文件
```
POST /upload
Content-Type: multipart/form-data

video: [文件]
```

## 部署步骤

### 1. 创建数据库表
执行 `src/main/resources/sql/video_tables.sql` 文件中的SQL语句：

```sql
-- 在MySQL中执行
source /path/to/video_tables.sql;
```

### 2. 配置数据库连接
确保 `application.yml` 中的数据库连接配置正确：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3307/czp_java?useUnicode=true&characterEncoding=utf8&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root
```

### 3. 启动应用
```bash
cd tool
mvn spring-boot:run
```

### 4. 测试接口
使用Postman或其他API测试工具测试接口是否正常工作。

## 前端集成

前端可以通过以下方式调用API：

```javascript
// 获取视频记录列表
const response = await fetch('http://localhost:8080/czp/tool/api/videogallery/records?page=1&size=20');
const data = await response.json();

// 创建视频记录
const createResponse = await fetch('http://localhost:8080/czp/tool/api/videogallery/records', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    title: '新视频',
    description: '视频描述',
    videos: []
  })
});
```

## 注意事项

1. **文件上传**：当前版本的文件上传接口返回模拟数据，需要根据实际需求实现文件保存逻辑
2. **视频处理**：可以集成FFmpeg等工具来获取视频时长、分辨率等信息
3. **权限控制**：建议添加用户认证和权限控制
4. **文件存储**：建议使用对象存储服务（如阿里云OSS、腾讯云COS）来存储视频文件
5. **视频转码**：可以添加视频转码功能，支持多种格式和分辨率

## 扩展功能建议

1. **视频缩略图生成**
2. **视频转码服务**
3. **视频播放统计**
4. **用户收藏功能**
5. **视频分类标签**
6. **批量操作支持**
7. **视频审核流程** 