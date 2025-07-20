# Gallery图片上传接口配置文档

## 接口概述

Gallery系统提供了专门的图片上传接口，支持将图片文件上传并转换为base64格式存储在数据库中。

## 接口信息

### 1. 主要上传接口
- **URL**: `/api/gallery/upload`
- **方法**: POST
- **参数**: `image` (MultipartFile)
- **返回**: JSON格式的上传结果

### 2. 通用上传接口
- **URL**: `/api/upload/image`
- **方法**: POST
- **参数**: `file` (MultipartFile)
- **返回**: JSON格式的上传结果

## 文件大小配置

### 当前配置（已更新为50MB）
```yaml
spring:
  servlet:
    multipart:
      max-file-size: 50MB      # 单个文件最大大小
      max-request-size: 50MB   # 请求最大大小
      enabled: true
```

### 代码中的文件大小检查
- **FileUploadController**: 硬编码50MB限制
- **GalleryServiceImpl**: 已添加50MB限制检查

## 文件类型限制

### 支持的图片格式
- JPEG/JPG
- PNG
- GIF
- BMP
- WebP
- 其他image/*类型

### 验证逻辑
```java
// 检查文件类型
String contentType = file.getContentType();
if (contentType == null || !contentType.startsWith("image/")) {
    throw new RuntimeException("只能上传图片文件");
}
```

## 存储方式

### Gallery图片存储
- **存储格式**: Base64编码
- **存储位置**: 数据库 `gallery_images` 表
- **字段**: `image_data` (LONGTEXT)

### 通用文件存储
- **存储格式**: 文件系统
- **存储位置**: `uploads/images/` 目录
- **文件命名**: UUID + 原扩展名

## 响应格式

### 成功响应
```json
{
  "id": 1,
  "name": "image.jpg",
  "imageData": "data:image/jpeg;base64,/9j/4AAQ...",
  "contentType": "image/jpeg"
}
```

### 错误响应
```json
{
  "error": "错误信息"
}
```

## 使用示例

### 前端调用示例
```javascript
const formData = new FormData();
formData.append('image', file);

fetch('/api/gallery/upload', {
  method: 'POST',
  body: formData
})
.then(response => response.json())
.then(data => {
  if (data.error) {
    console.error('上传失败:', data.error);
  } else {
    console.log('上传成功:', data);
  }
});
```

## 注意事项

1. **文件大小限制**: 单个图片文件最大50MB
2. **存储方式**: Gallery图片以base64格式存储在数据库中
3. **性能考虑**: 大文件上传可能影响性能，建议适当压缩
4. **安全考虑**: 已添加文件类型验证，防止恶意文件上传
5. **错误处理**: 完善的错误处理和用户友好的错误信息

## 相关文件

- `GalleryController.java` - Gallery图片上传控制器
- `FileUploadController.java` - 通用文件上传控制器
- `GalleryServiceImpl.java` - Gallery服务实现
- `application.yml` - Spring Boot配置文件
- `gallery_images` 表 - 数据库存储表 