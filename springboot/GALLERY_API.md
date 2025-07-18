# Gallery API 文档

## 概述

Gallery API 提供了AI问答记录的管理功能，包括记录的增删改查、图片上传等功能。

## 接口列表

### 1. 获取AI问答记录列表

**接口地址：** `GET /api/gallery/records`

**请求参数：** 无

**响应示例：**
```json
{
  "success": true,
  "records": [
    {
      "id": 1,
      "title": "AI问答记录1",
      "images": [
        {
          "id": 1,
          "url": "/uploads/gallery/test1.jpg",
          "name": "test1.jpg"
        }
      ]
    }
  ]
}
```

### 2. 获取单个记录详情

**接口地址：** `GET /api/gallery/records/{id}`

**请求参数：**
- `id`: 记录ID

**响应示例：**
```json
{
  "success": true,
  "record": {
    "id": 1,
    "title": "AI问答记录1",
    "images": [
      {
        "id": 1,
        "url": "/uploads/gallery/test1.jpg",
        "name": "test1.jpg"
      }
    ]
  }
}
```

### 3. 搜索AI问答记录

**接口地址：** `POST /api/gallery/records/search`

**请求参数：**
```json
{
  "query": "搜索关键词"
}
```

**响应示例：**
```json
{
  "success": true,
  "records": [
    {
      "id": 1,
      "title": "AI问答记录1",
      "images": []
    }
  ]
}
```

### 4. 新增AI问答记录

**接口地址：** `POST /api/gallery/records`

**请求参数：**
```json
{
  "title": "记录标题",
  "images": [
    {
      "url": "/uploads/gallery/image1.jpg",
      "name": "image1.jpg"
    }
  ]
}
```

**响应示例：**
```json
{
  "success": true,
  "message": "记录创建成功",
  "record": {
    "id": 4,
    "title": "记录标题",
    "images": [
      {
        "id": 5,
        "url": "/uploads/gallery/image1.jpg",
        "name": "image1.jpg"
      }
    ]
  }
}
```

### 5. 删除AI问答记录

**接口地址：** `DELETE /api/gallery/records/{id}`

**请求参数：**
- `id`: 记录ID

**响应示例：**
```json
{
  "success": true,
  "message": "记录删除成功"
}
```

### 6. 上传图片

**接口地址：** `POST /api/gallery/upload`

**请求参数：**
- `image`: 图片文件（multipart/form-data）

**响应示例：**
```json
{
  "success": true,
  "message": "图片上传成功",
  "url": "/api/gallery/images/1234567890",
  "id": 1234567890,
  "name": "image.jpg"
}
```

### 7. 获取图片数据

**接口地址：** `GET /api/gallery/images/{imageId}`

**请求参数：**
- `imageId`: 图片ID

**响应：**
- 返回图片的二进制数据，Content-Type为图片的实际类型

## 数据库表结构

### gallery_records 表
```sql
CREATE TABLE gallery_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### gallery_images 表
```sql
CREATE TABLE gallery_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    image_data LONGBLOB NOT NULL,
    record_id BIGINT,
    FOREIGN KEY (record_id) REFERENCES gallery_records(id) ON DELETE CASCADE
);
```

## 测试

启动应用后，可以访问 `http://localhost:8080/gallery-test` 来测试所有接口功能。

## 注意事项

1. 图片数据直接存储在MySQL数据库的LONGBLOB字段中
2. 图片URL格式为 `/api/gallery/images/{imageId}`，通过API获取图片数据
3. 支持的文件格式：jpg, jpeg, png, gif 等常见图片格式
4. 最大文件大小：10MB
5. 所有接口都支持跨域访问
6. 图片上传后需要先获取图片ID，然后在创建记录时使用图片ID进行关联 