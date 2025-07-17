# AI问答记录 API 接口设计文档

## 概述

本文档描述了AI问答记录功能的后端API接口设计，支持图片记录的管理、搜索、上传等功能。

## 基础配置

- **基础路径**: `/api/gallery`
- **请求头**: `Content-Type: application/json`
- **认证方式**: 待定（可根据需要添加JWT等认证）

## 接口列表

### 1. 获取AI问答记录列表

**接口**: `GET /api/gallery/records`

**描述**: 获取AI问答记录列表，支持分页、分类筛选和排序

**请求参数**:
```javascript
// URL查询参数
{
  page: 1,        // 页码，默认1
  limit: 20,       // 每页数量，默认20
  category: "all", // 分类筛选，可选值：all, frontend, backend, algorithm
  sort: "created_at" // 排序字段，可选值：created_at, title, updated_at
  order: "desc"    // 排序方向，desc或asc
}
```

**请求示例**:
```
GET /api/gallery/records?page=1&limit=20&category=frontend&sort=created_at&order=desc
```

**响应格式**:
```javascript
{
  "success": true,
  "data": {
    "records": [
      {
        "id": 1,
        "title": "Vue3架构图",
        "images": [
          {
            "id": 1,
            "url": "https://example.com/images/vue3-proxy.png",
            "name": "vue3-proxy.png"
          }
        ],
        "created_at": "2024-01-15T10:30:00Z",
        "updated_at": "2024-01-15T10:30:00Z"
      }
    ],
    "pagination": {
      "page": 1,
      "limit": 20,
      "total": 100,
      "total_pages": 5
    }
  }
}
```

---

### 2. 搜索AI问答记录

**接口**: `POST /api/gallery/records/search`

**描述**: 根据关键词搜索AI问答记录

**请求体**:
```javascript
{
  "query": "Vue3",           // 搜索关键词
  "page": 1,                 // 页码
  "limit": 20,               // 每页数量
  "category": "frontend"     // 可选，分类筛选
}
```

**响应格式**:
```javascript
{
  "success": true,
  "data": {
    "records": [
      {
        "id": 1,
        "title": "Vue3架构图",
        "images": [...],
        "created_at": "2024-01-15T10:30:00Z"
      }
    ],
    "pagination": {
      "page": 1,
      "limit": 20,
      "total": 5,
      "total_pages": 1
    }
  }
}
```

---

### 3. 新增AI问答记录

**接口**: `POST /api/gallery/records`

**描述**: 创建新的AI问答记录

**请求体**:
```javascript
{
  "title": "Vue3架构图",
  "images": [
    {
      "id": 1,
      "url": "https://example.com/images/vue3-proxy.png",
      "name": "vue3-proxy.png"
    }
  ],
  "category": "frontend"     // 可选，分类
}
```

**响应格式**:
```javascript
{
  "success": true,
  "data": {
    "id": 1,
    "title": "Vue3架构图",
    "images": [
      {
        "id": 1,
        "url": "https://example.com/images/vue3-proxy.png",
        "name": "vue3-proxy.png"
      }
    ],
    "created_at": "2024-01-15T10:30:00Z"
  }
}
```

---

### 4. 删除AI问答记录

**接口**: `DELETE /api/gallery/records/:id`

**描述**: 删除指定的AI问答记录

**路径参数**:
- `id`: 记录ID

**请求示例**:
```
DELETE /api/gallery/records/1
```

**响应格式**:
```javascript
{
  "success": true,
  "message": "记录删除成功"
}
```

---

### 5. 获取单个记录详情

**接口**: `GET /api/gallery/records/:id`

**描述**: 获取指定记录的详细信息

**路径参数**:
- `id`: 记录ID

**请求示例**:
```
GET /api/gallery/records/1
```

**响应格式**:
```javascript
{
  "success": true,
  "data": {
    "id": 1,
    "title": "Vue3架构图",
    "images": [
      {
        "id": 1,
        "url": "https://example.com/images/vue3-proxy.png",
        "name": "vue3-proxy.png"
      }
    ],
    "created_at": "2024-01-15T10:30:00Z",
    "updated_at": "2024-01-15T10:30:00Z"
  }
}
```

---

### 6. 上传图片

**接口**: `POST /api/gallery/upload`

**描述**: 上传图片文件到服务器

**请求体**: `multipart/form-data`
```javascript
FormData {
  image: File  // 图片文件
}
```

**请求示例**:
```javascript
const formData = new FormData();
formData.append('image', file);
```

**响应格式**:
```javascript
{
  "success": true,
  "data": {
    "id": 1,
    "url": "https://example.com/images/uploaded-image.png",
    "name": "original-name.png",
    "size": 1024000,
    "mime_type": "image/png"
  }
}
```

## 错误处理

### 错误响应格式
```javascript
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "标题不能为空",
    "details": {
      "title": ["标题不能为空"]
    }
  }
}
```

### 常见错误码
- `VALIDATION_ERROR`: 参数验证错误
- `NOT_FOUND`: 资源不存在
- `UNAUTHORIZED`: 未授权访问
- `FORBIDDEN`: 禁止访问
- `INTERNAL_ERROR`: 服务器内部错误

### HTTP状态码
- `200`: 成功
- `400`: 请求参数错误
- `401`: 未授权
- `403`: 禁止访问
- `404`: 资源不存在
- `500`: 服务器内部错误

## 数据模型

### Record（记录）
```javascript
{
  id: Number,           // 记录ID
  title: String,        // 标题
  images: Array,        // 图片列表
  category: String,     // 分类（可选）
  created_at: String,   // 创建时间
  updated_at: String    // 更新时间
}
```

### Image（图片）
```javascript
{
  id: Number,           // 图片ID
  url: String,          // 图片URL
  name: String,         // 原始文件名
  size: Number,         // 文件大小（字节）
  mime_type: String     // MIME类型
}
```

## 前端集成

### Vue.js 组件中的使用示例

```javascript
// API配置
const API_BASE_URL = '/api/gallery'
const API_ENDPOINTS = {
  GET_RECORDS: `${API_BASE_URL}/records`,
  SEARCH_RECORDS: `${API_BASE_URL}/records/search`,
  CREATE_RECORD: `${API_BASE_URL}/records`,
  DELETE_RECORD: `${API_BASE_URL}/records/:id`,
  GET_RECORD: `${API_BASE_URL}/records/:id`,
  UPLOAD_IMAGE: `${API_BASE_URL}/upload`
}

// 通用请求方法
const apiRequest = async (url, options = {}) => {
  try {
    const response = await fetch(url, {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers
      },
      ...options
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }
    
    return await response.json()
  } catch (error) {
    console.error('API请求失败:', error)
    throw error
  }
}

// 获取记录列表
const loadRecords = async (params = {}) => {
  const queryParams = new URLSearchParams(params)
  const url = `${API_ENDPOINTS.GET_RECORDS}?${queryParams}`
  const data = await apiRequest(url)
  return data.data.records
}

// 搜索记录
const searchRecords = async (query) => {
  const response = await apiRequest(API_ENDPOINTS.SEARCH_RECORDS, {
    method: 'POST',
    body: JSON.stringify({ query })
  })
  return response.data.records
}

// 创建记录
const createRecord = async (recordData) => {
  const response = await apiRequest(API_ENDPOINTS.CREATE_RECORD, {
    method: 'POST',
    body: JSON.stringify(recordData)
  })
  return response.data
}

// 上传图片
const uploadImage = async (file) => {
  const formData = new FormData()
  formData.append('image', file)
  
  const response = await fetch(API_ENDPOINTS.UPLOAD_IMAGE, {
    method: 'POST',
    body: formData
  })
  
  if (!response.ok) {
    throw new Error(`上传失败: ${response.status}`)
  }
  
  const result = await response.json()
  return result.data
}
```

## 注意事项

1. **文件上传限制**: 建议限制上传文件的大小和类型
2. **图片处理**: 可以考虑对上传的图片进行压缩和格式转换
3. **安全性**: 需要验证文件类型，防止恶意文件上传
4. **性能优化**: 对于大量数据的查询，建议使用分页和索引
5. **缓存策略**: 可以考虑对静态图片资源进行CDN缓存

## 版本控制

当前版本: v1.0

后续版本更新将在此文档中记录变更内容。 