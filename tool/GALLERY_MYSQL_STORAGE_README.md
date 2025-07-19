# Gallery模块图片存储改为MySQL数据库

## 修改概述

将gallery模块的图片存储方式从本地文件系统改为MySQL数据库存储，图片数据以base64格式存储在数据库中。

## 主要修改内容

### 1. 数据库表结构修改

**文件**: `tool/src/main/resources/gallery_schema.sql`

- 修改 `gallery_images` 表结构
- 移除 `url` 和 `file_path` 字段
- 添加 `image_data` 字段（LONGTEXT类型）用于存储base64图片数据

### 2. 实体类修改

**文件**: `tool/src/main/java/com/tool/entity/GalleryImage.java`

- 移除 `url` 和 `filePath` 属性
- 添加 `imageData` 属性用于存储base64图片数据

### 3. DTO类修改

**文件**: `tool/src/main/java/com/tool/dto/GalleryImageDTO.java`

- 移除 `url` 和 `filePath` 属性
- 添加 `imageData` 属性

### 4. MyBatis映射文件修改

**文件**: `tool/src/main/resources/mapper/GalleryImageMapper.xml`

- 更新结果映射，将 `url` 和 `file_path` 字段映射改为 `image_data`
- 更新SQL语句，使用 `image_data` 字段

### 5. 服务层修改

**文件**: `tool/src/main/java/com/tool/service/impl/GalleryServiceImpl.java`

- 修改 `uploadImage` 方法，将图片文件转换为base64格式
- 移除文件系统相关的代码
- 更新图片数据存储逻辑

### 6. 控制器修改

**文件**: `tool/src/main/java/com/tool/controller/GalleryController.java`

- 修改 `uploadImage` 方法返回的数据结构
- 返回 `imageData` 而不是 `url`

### 7. 删除不需要的文件

- 删除 `tool/src/main/java/com/tool/controller/ImageController.java`（不再需要文件访问控制器）

### 8. 配置文件修改

**文件**: `tool/src/main/resources/application.yml`

- 移除gallery相关的文件路径配置

### 9. 前端修改

**文件**: `js/src/views/Gallery.vue`

- 修改图片显示逻辑，使用 `imageData` 而不是 `url`
- 更新图片上传后的数据处理

## 数据库初始化

运行以下SQL脚本来更新数据库结构：

```sql
-- 执行初始化脚本
source tool/src/main/resources/init_gallery_tables.sql
```

## 优势

1. **数据一致性**: 图片数据与业务数据存储在同一个数据库中，保证数据一致性
2. **简化部署**: 不需要管理文件系统路径和权限
3. **事务支持**: 图片上传和记录创建可以在同一个事务中完成
4. **备份恢复**: 数据库备份时自动包含图片数据
5. **跨平台**: 不依赖特定的文件系统结构

## 注意事项

1. **数据库大小**: base64编码会增加约33%的存储空间
2. **性能考虑**: 大图片可能影响查询性能，建议设置合理的图片大小限制
3. **内存使用**: 加载大量图片时需要注意内存使用情况

## 测试

可以使用以下脚本测试修改后的功能：

```sql
-- 执行测试脚本
source tool/src/main/resources/test_gallery.sql
```

## API接口变化

### 上传图片接口

**之前**:
```json
{
  "url": "http://localhost:8080/api/gallery/images/filename.jpg",
  "id": 1,
  "name": "image.jpg"
}
```

**现在**:
```json
{
  "imageData": "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD...",
  "id": 1,
  "name": "image.jpg",
  "contentType": "image/jpeg"
}
```

### 获取记录接口

**之前**:
```json
{
  "images": [
    {
      "id": 1,
      "name": "image.jpg",
      "url": "http://localhost:8080/api/gallery/images/filename.jpg"
    }
  ]
}
```

**现在**:
```json
{
  "images": [
    {
      "id": 1,
      "name": "image.jpg",
      "imageData": "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD..."
    }
  ]
}
``` 