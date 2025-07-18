# Gallery MySQL 存储实现

## 概述

本实现将图片数据直接存储在MySQL数据库中，而不是使用静态文件存储。这样可以更好地管理图片数据，支持事务操作，并且便于备份和迁移。

## 主要变更

### 1. 数据库表结构

**gallery_images 表：**
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

- `image_data`: 使用LONGBLOB类型存储图片的二进制数据
- `content_type`: 存储图片的MIME类型（如image/jpeg, image/png等）
- `record_id`: 关联到gallery_records表的外键

### 2. 实体类变更

**GalleryImage.java:**
- 移除了`url`字段
- 添加了`contentType`和`imageData`字段
- `imageData`使用`byte[]`类型存储二进制数据

### 3. API接口变更

**新增接口：**
- `GET /api/gallery/images/{imageId}` - 获取图片数据

**修改接口：**
- `POST /api/gallery/upload` - 返回图片ID而不是文件路径

### 4. 使用流程

1. **上传图片：**
   ```
   POST /api/gallery/upload
   Content-Type: multipart/form-data
   
   响应：
   {
     "success": true,
     "id": 1234567890,
     "url": "/api/gallery/images/1234567890",
     "name": "image.jpg"
   }
   ```

2. **创建记录：**
   ```
   POST /api/gallery/records
   Content-Type: application/json
   
   {
     "title": "记录标题",
     "images": [
       {
         "id": 1234567890,
         "name": "image.jpg"
       }
     ]
   }
   ```

3. **获取图片：**
   ```
   GET /api/gallery/images/1234567890
   
   响应：图片二进制数据，Content-Type为实际图片类型
   ```

## 优势

1. **数据一致性：** 图片数据与记录数据在同一数据库中，保证事务一致性
2. **备份便利：** 所有数据都在数据库中，便于备份和恢复
3. **管理简单：** 不需要管理文件系统，所有操作都通过数据库
4. **安全性：** 图片数据不会暴露在文件系统中

## 注意事项

1. **数据库大小：** 图片数据会显著增加数据库大小
2. **性能考虑：** 大图片可能影响查询性能，建议对图片大小进行限制
3. **内存使用：** 加载图片时会占用更多内存
4. **备份策略：** 需要定期备份数据库，因为包含了大量二进制数据

## 测试

启动应用后访问 `http://localhost:8080/gallery-test` 进行测试：

1. 先上传图片获取图片ID
2. 使用图片ID创建记录
3. 查看记录列表，图片会通过API动态加载 