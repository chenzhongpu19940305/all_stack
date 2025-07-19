# VideoGallery API 接口文档

## 概述
VideoGallery页面用于管理哔哩哔哩视频记录，提供视频的上传、查看、搜索、更新和删除功能。

## 基础配置
- **API基础路径**: `/api/videogallery`
- **请求格式**: JSON
- **响应格式**: JSON

## 接口列表

### 1. 获取视频记录列表

#### 接口信息
- **URL**: `GET /api/videogallery/records`
- **方法**: GET
- **描述**: 获取所有视频记录列表

#### 请求参数
| 参数名 | 类型 | 必填 | 描述 | 示例 |
|--------|------|------|------|------|
| page | number | 否 | 页码，默认1 | 1 |
| size | number | 否 | 每页数量，默认20 | 20 |
| sort | string | 否 | 排序字段 | "createTime" |
| order | string | 否 | 排序方向，"asc"或"desc" | "desc" |

#### 请求示例
```javascript
// 获取第一页，每页20条记录
GET /api/videogallery/records?page=1&size=20&sort=createTime&order=desc
```

#### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "视频标题",
        "description": "视频描述",
        "videos": [
          {
            "id": 1,
            "videoData": "data:video/mp4;base64,UklGRnoGAABXQVZFZm10IBAAAAABAAEAQB8AAEAfAAABAAgAZGF0YQoGAACBhYqFbF1fdJivrJBhNjVgodDbq2EcBj+a2/LDciUFLIHO8tiJNwgZaLvt559NEAxQp+PwtmMcBjiR1/LMeSwFJHfH8N2QQAoUXrTp66hVFApGn+DyvmwhBSuBzvLZiTYIG2m98OScTgwOUarm7blmGgU7k9n1unEiBC13yO/eizEIHWq+8+OWT...",
            "name": "video1.mp4",
            "size": 1024000,
            "type": "video/mp4"
          }
        ],
        "createTime": "2024-01-15T10:30:00Z",
        "updateTime": "2024-01-15T10:30:00Z"
      }
    ],
    "total": 100,
    "page": 1,
    "size": 20
  }
}
```

#### 响应字段说明
| 字段名 | 类型 | 描述 |
|--------|------|------|
| code | number | 响应状态码 |
| message | string | 响应消息 |
| data.records | array | 视频记录数组 |
| data.total | number | 总记录数 |
| data.page | number | 当前页码 |
| data.size | number | 每页数量 |

### 2. 搜索视频记录

#### 接口信息
- **URL**: `POST /api/videogallery/records/search`
- **方法**: POST
- **描述**: 根据关键词搜索视频记录

#### 请求参数
| 参数名 | 类型 | 必填 | 描述 | 示例 |
|--------|------|------|------|------|
| query | string | 是 | 搜索关键词 | "编程教程" |
| page | number | 否 | 页码 | 1 |
| size | number | 否 | 每页数量 | 20 |

#### 请求示例
```javascript
POST /api/videogallery/records/search
Content-Type: application/json

{
  "query": "编程教程",
  "page": 1,
  "size": 20
}
```

#### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "JavaScript编程教程",
        "description": "从零开始学习JavaScript",
        "videos": [...],
        "createTime": "2024-01-15T10:30:00Z"
      }
    ],
    "total": 5,
    "page": 1,
    "size": 20
  }
}
```

### 3. 新增视频记录

#### 接口信息
- **URL**: `POST /api/videogallery/records`
- **方法**: POST
- **描述**: 创建新的视频记录

#### 请求参数
| 参数名 | 类型 | 必填 | 描述 | 示例 |
|--------|------|------|------|------|
| title | string | 是 | 视频标题 | "Vue.js入门教程" |
| description | string | 否 | 视频描述 | "从零开始学习Vue.js框架" |
| videos | array | 是 | 视频文件数组 | 见下方说明 |

#### videos数组字段说明
| 字段名 | 类型 | 必填 | 描述 | 示例 |
|--------|------|------|------|------|
| videoData | string | 是 | 视频文件的base64编码或URL | "data:video/mp4;base64,..." |
| name | string | 是 | 视频文件名 | "tutorial.mp4" |
| size | number | 否 | 文件大小(字节) | 1024000 |
| type | string | 否 | 文件MIME类型 | "video/mp4" |

#### 请求示例
```javascript
POST /api/videogallery/records
Content-Type: application/json

{
  "title": "Vue.js入门教程",
  "description": "从零开始学习Vue.js框架",
  "videos": [
    {
      "videoData": "data:video/mp4;base64,UklGRnoGAABXQVZFZm10IBAAAAABAAEAQB8AAEAfAAABAAgAZGF0YQoGAACBhYqFbF1fdJivrJBhNjVgodDbq2EcBj+a2/LDciUFLIHO8tiJNwgZaLvt559NEAxQp+PwtmMcBjiR1/LMeSwFJHfH8N2QQAoUXrTp66hVFApGn+DyvmwhBSuBzvLZiTYIG2m98OScTgwOUarm7blmGgU7k9n1unEiBC13yO/eizEIHWq+8+OWT...",
      "name": "vue-tutorial.mp4",
      "size": 2048000,
      "type": "video/mp4"
    }
  ]
}
```

#### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "Vue.js入门教程",
    "description": "从零开始学习Vue.js框架",
    "videos": [...],
    "createTime": "2024-01-15T10:30:00Z"
  }
}
```

### 4. 更新视频记录

#### 接口信息
- **URL**: `PUT /api/videogallery/records/{id}`
- **方法**: PUT
- **描述**: 更新指定的视频记录

#### 路径参数
| 参数名 | 类型 | 必填 | 描述 | 示例 |
|--------|------|------|------|------|
| id | number | 是 | 视频记录ID | 1 |

#### 请求参数
| 参数名 | 类型 | 必填 | 描述 | 示例 |
|--------|------|------|------|------|
| title | string | 是 | 视频标题 | "Vue.js进阶教程" |
| description | string | 否 | 视频描述 | "深入学习Vue.js高级特性" |
| videos | array | 是 | 视频文件数组 | 同新增接口 |

#### 请求示例
```javascript
PUT /api/videogallery/records/1
Content-Type: application/json

{
  "title": "Vue.js进阶教程",
  "description": "深入学习Vue.js高级特性",
  "videos": [
    {
      "videoData": "data:video/mp4;base64,...",
      "name": "vue-advanced.mp4",
      "size": 3072000,
      "type": "video/mp4"
    }
  ]
}
```

#### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "Vue.js进阶教程",
    "description": "深入学习Vue.js高级特性",
    "videos": [...],
    "updateTime": "2024-01-15T11:30:00Z"
  }
}
```

### 5. 删除视频记录

#### 接口信息
- **URL**: `DELETE /api/videogallery/records/{id}`
- **方法**: DELETE
- **描述**: 删除指定的视频记录

#### 路径参数
| 参数名 | 类型 | 必填 | 描述 | 示例 |
|--------|------|------|------|------|
| id | number | 是 | 视频记录ID | 1 |

#### 请求示例
```javascript
DELETE /api/videogallery/records/1
```

#### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

### 6. 获取单个视频记录

#### 接口信息
- **URL**: `GET /api/videogallery/records/{id}`
- **方法**: GET
- **描述**: 获取指定ID的视频记录详情

#### 路径参数
| 参数名 | 类型 | 必填 | 描述 | 示例 |
|--------|------|------|------|------|
| id | number | 是 | 视频记录ID | 1 |

#### 请求示例
```javascript
GET /api/videogallery/records/1
```

#### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "Vue.js入门教程",
    "description": "从零开始学习Vue.js框架",
    "videos": [
      {
        "id": 1,
        "videoData": "data:video/mp4;base64,...",
        "name": "vue-tutorial.mp4",
        "size": 2048000,
        "type": "video/mp4"
      }
    ],
    "createTime": "2024-01-15T10:30:00Z",
    "updateTime": "2024-01-15T10:30:00Z"
  }
}
```

### 7. 上传视频文件

#### 接口信息
- **URL**: `POST /api/videogallery/upload`
- **方法**: POST
- **描述**: 上传视频文件到服务器

#### 请求参数
| 参数名 | 类型 | 必填 | 描述 | 示例 |
|--------|------|------|------|------|
| video | file | 是 | 视频文件 | 文件对象 |

#### 请求示例
```javascript
POST /api/videogallery/upload
Content-Type: multipart/form-data

FormData:
- video: [文件对象]
```

#### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "videoData": "data:video/mp4;base64,UklGRnoGAABXQVZFZm10IBAAAAABAAEAQB8AAEAfAAABAAgAZGF0YQoGAACBhYqFbF1fdJivrJBhNjVgodDbq2EcBj+a2/LDciUFLIHO8tiJNwgZaLvt559NEAxQp+PwtmMcBjiR1/LMeSwFJHfH8N2QQAoUXrTp66hVFApGn+DyvmwhBSuBzvLZiTYIG2m98OScTgwOUarm7blmGgU7k9n1unEiBC13yO/eizEIHWq+8+OWT...",
    "name": "uploaded-video.mp4",
    "size": 2048000,
    "type": "video/mp4"
  }
}
```

## 错误码说明

| 错误码 | 描述 | 解决方案 |
|--------|------|----------|
| 200 | 成功 | - |
| 400 | 请求参数错误 | 检查请求参数格式 |
| 401 | 未授权 | 检查认证信息 |
| 403 | 禁止访问 | 检查权限 |
| 404 | 资源不存在 | 检查资源ID是否正确 |
| 413 | 文件过大 | 检查文件大小限制 |
| 415 | 不支持的媒体类型 | 检查文件格式 |
| 500 | 服务器内部错误 | 联系管理员 |

## 数据模型

### VideoRecord (视频记录)
```typescript
interface VideoRecord {
  id: number;                    // 记录ID
  title: string;                 // 标题
  description?: string;          // 描述(可选)
  videos: VideoFile[];           // 视频文件数组
  createTime: string;            // 创建时间
  updateTime: string;            // 更新时间
}
```

### VideoFile (视频文件)
```typescript
interface VideoFile {
  id: number;                    // 文件ID
  videoData: string;             // 视频数据(base64或URL)
  name: string;                  // 文件名
  size?: number;                 // 文件大小(字节)
  type?: string;                 // MIME类型
}
```

## 使用示例

### 前端调用示例
```javascript
// 获取视频记录列表
const loadRecords = async () => {
  try {
    const response = await fetch('/api/videogallery/records?page=1&size=20');
    const data = await response.json();
    return data.data.records;
  } catch (error) {
    console.error('加载记录失败:', error);
  }
};

// 搜索视频记录
const searchRecords = async (query) => {
  try {
    const response = await fetch('/api/videogallery/records/search', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ query, page: 1, size: 20 })
    });
    const data = await response.json();
    return data.data.records;
  } catch (error) {
    console.error('搜索失败:', error);
  }
};

// 上传视频文件
const uploadVideo = async (file) => {
  try {
    const formData = new FormData();
    formData.append('video', file);
    
    const response = await fetch('/api/videogallery/upload', {
      method: 'POST',
      body: formData
    });
    const data = await response.json();
    return data.data;
  } catch (error) {
    console.error('上传失败:', error);
  }
};
```

## 注意事项

1. **文件大小限制**: 建议单个视频文件不超过100MB
2. **支持格式**: 主要支持MP4格式，其他格式需要后端支持
3. **编码格式**: 视频数据建议使用base64编码或URL存储
4. **安全性**: 上传文件需要进行格式验证和大小检查
5. **性能优化**: 大文件建议使用分片上传
6. **缓存策略**: 视频文件建议使用CDN加速

## 更新日志

- **v1.0.0** (2024-01-15): 初始版本，支持基本的CRUD操作
- **v1.1.0** (2024-01-16): 添加搜索功能和分页支持
- **v1.2.0** (2024-01-17): 优化文件上传，支持拖拽上传 