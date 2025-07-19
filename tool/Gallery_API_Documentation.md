# Gallery API 接口文档

## 概述
Gallery API 提供了AI问答记录管理和图片上传功能，支持记录的增删改查和图片文件上传。

## 基础信息
- **基础URL**: `http://localhost:8080/cpsc/privacy/testmock`
- **Content-Type**: `application/json`
- **字符编码**: UTF-8

## 接口列表

### 1. 获取所有记录
**接口地址**: `GET /api/gallery/records`

**请求参数**: 无

**响应示例**:
```json
{
  "records": [
    {
      "id": 1,
      "title": "AI问答记录1",
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00",
      "images": [
        {
          "id": 1,
          "name": "image1.jpg",
          "imageData": "data:image/jpeg;base64,...",
          "fileSize": 1024,
          "contentType": "image/jpeg",
          "createdAt": "2024-01-01T10:00:00"
        }
      ]
    }
  ],
  "total": 1
}
```

### 2. 搜索记录
**接口地址**: `POST /api/gallery/records/search`

**请求参数**:
```json
{
  "query": "AI问答"
}
```

**响应示例**:
```json
{
  "records": [
    {
      "id": 1,
      "title": "AI问答记录1",
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00",
      "images": []
    }
  ],
  "total": 1
}
```

### 3. 创建新记录
**接口地址**: `POST /api/gallery/records`

**请求参数**:
```json
{
  "title": "测试AI问答记录",
  "images": [
    {
      "id": 1,
      "name": "test-image.jpg",
      "imageData": "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAYEBQYFBAYGBQYHBwYIChAKCgkJChQODwwQFxQYGBcUFhYaHSUfGhsjHBYWICwgIyYnKSopGR8tMC0oMCUoKSj/2wBDAQcHBwoIChMKChMoGhYaKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCj/wAARCAABAAEDASIAAhEBAxEB/8QAFQABAQAAAAAAAAAAAAAAAAAAAAv/xAAUEAEAAAAAAAAAAAAAAAAAAAAA/8QAFQEBAQAAAAAAAAAAAAAAAAAAAAX/xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oADAMBAAIRAxEAPwCdABmX/9k=",
      "fileSize": 1024,
      "contentType": "image/jpeg",
      "createdAt": "2024-01-01T10:00:00"
    }
  ]
}
```

**响应示例**:
```json
{
  "record": {
    "id": 1,
    "title": "测试AI问答记录",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00",
    "images": [
      {
        "id": 1,
        "name": "test-image.jpg",
        "imageData": "data:image/jpeg;base64,...",
        "fileSize": 1024,
        "contentType": "image/jpeg",
        "createdAt": "2024-01-01T10:00:00"
      }
    ]
  },
  "message": "记录创建成功"
}
```

### 4. 获取单个记录详情
**接口地址**: `GET /api/gallery/records/{id}`

**路径参数**:
- `id`: 记录ID (Long)

**响应示例**:
```json
{
  "record": {
    "id": 1,
    "title": "AI问答记录1",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00",
    "images": [
      {
        "id": 1,
        "name": "image1.jpg",
        "imageData": "data:image/jpeg;base64,...",
        "fileSize": 1024,
        "contentType": "image/jpeg",
        "createdAt": "2024-01-01T10:00:00"
      }
    ]
  }
}
```

### 5. 删除记录
**接口地址**: `DELETE /api/gallery/records/{id}`

**路径参数**:
- `id`: 记录ID (Long)

**响应示例**:
```json
{
  "message": "记录删除成功"
}
```

### 6. 上传图片
**接口地址**: `POST /api/gallery/upload`

**请求参数**:
- `image`: 图片文件 (MultipartFile)

**响应示例**:
```json
{
  "id": 1,
  "name": "uploaded-image.jpg",
  "imageData": "data:image/jpeg;base64,...",
  "contentType": "image/jpeg"
}
```

## 错误响应格式
当接口调用失败时，会返回以下格式的错误信息：

```json
{
  "error": "错误描述信息"
}
```

## 状态码说明
- `200`: 请求成功
- `400`: 请求参数错误
- `404`: 资源不存在
- `500`: 服务器内部错误

## 使用说明

### Postman导入步骤
1. 打开Postman
2. 点击"Import"按钮
3. 选择`Gallery_API_Postman_Collection.json`文件导入集合
4. 选择`Gallery_API_Environment.json`文件导入环境
5. 在右上角选择导入的环境
6. 开始测试接口

### 环境变量说明
- `baseUrl`: API基础地址，默认为`http://localhost:8080/cpsc/privacy/testmock`
- `recordId`: 记录ID，用于测试单个记录相关接口
- `imageId`: 图片ID，用于测试图片相关接口
- `searchQuery`: 搜索关键词
- `recordTitle`: 记录标题

### 测试流程建议
1. 首先测试"获取所有记录"接口，确认服务正常运行
2. 测试"创建新记录"接口，创建测试数据
3. 使用返回的记录ID测试"获取单个记录详情"接口
4. 测试"搜索记录"接口
5. 测试"上传图片"接口
6. 最后测试"删除记录"接口清理测试数据

## 注意事项
1. 确保Spring Boot应用已启动并运行在8080端口
2. 确保MySQL数据库已启动并配置正确
3. 图片上传接口支持的文件类型：image/jpeg, image/png, image/gif等
4. 图片文件大小限制：10MB
5. 所有时间格式均为ISO 8601格式：`yyyy-MM-ddTHH:mm:ss` 