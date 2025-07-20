# VideoGallery API 文档

## 概述

VideoGallery是一个视频管理系统，提供完整的视频记录CRUD操作和文件上传功能。本文档详细说明了所有API接口的使用方法。

## 基础信息

- **基础URL**: `http://localhost:8080/cpsc/privacy/testmock`
- **API前缀**: `/api/videogallery`
- **数据格式**: JSON
- **文件上传**: multipart/form-data

## API接口列表

### 1. 获取视频记录列表

**接口**: `GET /api/videogallery/records`

**描述**: 获取所有视频记录列表

**请求参数**: 无

**响应示例**:
```json
{
  "records": [
    {
      "id": 1,
      "title": "示例视频",
      "description": "这是一个示例视频",
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00",
      "videos": [
        {
          "id": 1,
          "name": "video.mp4",
          "videoData": "data:video/mp4;base64,UklGRnoGAABXQVZFZm10IBAAAAABAAEAQB8AAEAfAAABAAgAZGF0YQoGAACBhYqFbF1fdJivrJBhNjVgodDbq2EcBj+a2/LDciUFLIHO8tiJNwgZaLvt559NEAxQp+PwtmMcBjiR1/LMeSwFJHfH8N2QQAoUXrTp66hVFApGn+DyvmwhBSuBzvLZiTYIG2m98OScTgwOUarm7blmGgU7k9n1unEiBC13yO/eizEIHWq+8+OWT...",
          "contentType": "video/mp4",
          "fileSize": 1024000,
          "createdAt": "2024-01-01T10:00:00"
        }
      ]
    }
  ],
  "total": 1
}
```

### 2. 搜索视频记录

**接口**: `POST /api/videogallery/records/search`

**描述**: 根据关键词搜索视频记录

**请求参数**:
```json
{
  "query": "搜索关键词"
}
```

**响应示例**:
```json
{
  "records": [
    {
      "id": 1,
      "title": "包含关键词的视频",
      "description": "描述",
      "videos": [...]
    }
  ],
  "total": 1
}
```

### 3. 获取单个视频记录

**接口**: `GET /api/videogallery/records/{id}`

**描述**: 根据ID获取单个视频记录详情

**路径参数**:
- `id`: 记录ID

**响应示例**:
```json
{
  "record": {
    "id": 1,
    "title": "视频标题",
    "description": "视频描述",
    "videos": [...]
  }
}
```

### 4. 新增视频记录

**接口**: `POST /api/videogallery/records`

**描述**: 新增视频记录

**请求参数**:
```json
{
  "title": "视频标题",
  "description": "视频描述",
  "videos": [
    {
      "id": 1,
      "name": "video.mp4",
      "videoData": "data:video/mp4;base64,UklGRnoGAABXQVZFZm10IBAAAAABAAEAQB8AAEAfAAABAAgAZGF0YQoGAACBhYqFbF1fdJivrJBhNjVgodDbq2EcBj+a2/LDciUFLIHO8tiJNwgZaLvt559NEAxQp+PwtmMcBjiR1/LMeSwFJHfH8N2QQAoUXrTp66hVFApGn+DyvmwhBSuBzvLZiTYIG2m98OScTgwOUarm7blmGgU7k9n1unEiBC13yO/eizEIHWq+8+OWT...",
      "contentType": "video/mp4",
      "fileSize": 1024000
    }
  ]
}
```

**响应示例**:
```json
{
  "record": {
    "id": 1,
    "title": "视频标题",
    "description": "视频描述",
    "videos": [...]
  },
  "message": "记录创建成功"
}
```

### 5. 更新视频记录

**接口**: `PUT /api/videogallery/records/{id}`

**描述**: 更新指定ID的视频记录

**路径参数**:
- `id`: 记录ID

**请求参数**: 同新增视频记录

**响应示例**:
```json
{
  "record": {
    "id": 1,
    "title": "更新后的标题",
    "description": "更新后的描述",
    "videos": [...]
  },
  "message": "记录更新成功"
}
```

### 6. 删除视频记录

**接口**: `DELETE /api/videogallery/records/{id}`

**描述**: 删除指定ID的视频记录

**路径参数**:
- `id`: 记录ID

**响应示例**:
```json
{
  "message": "记录删除成功"
}
```

### 7. 上传视频文件

**接口**: `POST /api/videogallery/upload`

**描述**: 上传视频文件到服务器

**请求参数**:
- `video`: 视频文件 (multipart/form-data)

**响应示例**:
```json
{
  "id": 1,
  "name": "video.mp4",
  "videoData": "data:video/mp4;base64,UklGRnoGAABXQVZFZm10IBAAAAABAAEAQB8AAEAfAAABAAgAZGF0YQoGAACBhYqFbF1fdJivrJBhNjVgodDbq2EcBj+a2/LDciUFLIHO8tiJNwgZaLvt559NEAxQp+PwtmMcBjiR1/LMeSwFJHfH8N2QQAoUXrTp66hVFApGn+DyvmwhBSuBzvLZiTYIG2m98OScTgwOUarm7blmGgU7k9n1unEiBC13yO/eizEIHWq+8+OWT...",
  "contentType": "video/mp4"
}
```

## Postman使用说明

### 导入集合

1. 打开Postman
2. 点击"Import"按钮
3. 选择`VideoGallery_API_Postman_Collection.json`文件
4. 导入完成后，您将看到"VideoGallery API Collection"集合

### 环境变量设置

在Postman中设置以下环境变量：

- `baseUrl`: `http://localhost:8080/cpsc/privacy/testmock`
- `recordId`: `1` (用于测试特定记录)

### 测试步骤

1. **启动后端服务**
   ```bash
   cd tool
   mvn spring-boot:run
   ```

2. **测试获取记录列表**
   - 选择"获取视频记录列表"请求
   - 点击"Send"按钮
   - 检查响应状态码和返回数据

3. **测试上传视频文件**
   - 选择"上传视频文件"请求
   - 在Body中选择"form-data"
   - 在"video"字段中选择视频文件
   - 点击"Send"按钮

4. **测试新增记录**
   - 选择"新增视频记录"请求
   - 修改Body中的JSON数据
   - 点击"Send"按钮

5. **测试其他接口**
   - 按照类似步骤测试搜索、更新、删除等接口

## 错误处理

### 常见错误码

- `400 Bad Request`: 请求参数错误
- `404 Not Found`: 资源不存在
- `500 Internal Server Error`: 服务器内部错误

### 错误响应格式

```json
{
  "error": "错误描述信息"
}
```

## 注意事项

1. **文件大小限制**: 视频文件最大50MB
2. **支持格式**: MP4, AVI, MOV等常见视频格式
3. **数据存储**: 视频以base64格式存储在数据库中
4. **性能考虑**: 大文件上传可能影响性能，建议适当压缩

## 相关文件

- `VideoGallery_API_Postman_Collection.json` - Postman集合文件
- `VideoGallery.vue` - 前端Vue组件
- 后端Controller和Service类 