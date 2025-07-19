# VideoGallery 后端服务实现

## 概述
本项目实现了VideoGallery页面的后端接口，包括视频记录的增删改查、搜索和文件上传功能。

## 已实现的接口

### 1. 视频记录管理
- `GET /api/videogallery/records` - 获取视频记录列表
- `POST /api/videogallery/records/search` - 搜索视频记录
- `POST /api/videogallery/records` - 创建视频记录
- `PUT /api/videogallery/records/{id}` - 更新视频记录
- `DELETE /api/videogallery/records/{id}` - 删除视频记录
- `GET /api/videogallery/records/{id}` - 获取单个视频记录

### 2. 文件上传
- `POST /api/videogallery/upload` - 上传视频文件

## 预留接口说明

### 视频保存逻辑预留
在 `VideoServiceImpl.uploadVideo()` 方法中，视频保存逻辑已预留，您需要根据实际需求实现：

```java
@Override
public ApiResponse uploadVideo(byte[] videoData, String fileName, String contentType) {
    try {
        // TODO: 您需要在这里实现视频保存逻辑
        // 这里只是示例，您可以根据需要实现具体的保存逻辑
        
        // 示例：将视频数据转换为base64字符串
        String videoDataBase64 = java.util.Base64.getEncoder().encodeToString(videoData);
        
        // 创建视频文件DTO
        VideoFileDTO videoFileDTO = new VideoFileDTO();
        videoFileDTO.setVideoData("data:" + contentType + ";base64," + videoDataBase64);
        videoFileDTO.setName(fileName);
        videoFileDTO.setSize((long) videoData.length);
        videoFileDTO.setType(contentType);
        
        return ApiResponse.success("上传视频成功", videoFileDTO);
    } catch (Exception e) {
        return ApiResponse.error("上传视频失败: " + e.getMessage());
    }
}
```

## 建议的视频保存实现方案

### 方案1：本地文件系统
```java
// 保存到本地文件系统
String uploadDir = "uploads/videos/";
File dir = new File(uploadDir);
if (!dir.exists()) {
    dir.mkdirs();
}

String fileName = System.currentTimeMillis() + "_" + originalFileName;
File file = new File(uploadDir + fileName);
Files.write(file.toPath(), videoData);

// 返回文件URL
String fileUrl = "/uploads/videos/" + fileName;
```

### 方案2：云存储（推荐）
```java
// 使用阿里云OSS、腾讯云COS等云存储服务
// 这里以阿里云OSS为例
OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
String objectName = "videos/" + System.currentTimeMillis() + "_" + fileName;
ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(videoData));

String fileUrl = "https://" + bucketName + "." + endpoint + "/" + objectName;
```

### 方案3：数据库存储
```java
// 直接存储到数据库（适用于小文件）
String videoDataBase64 = Base64.getEncoder().encodeToString(videoData);
// 将base64字符串存储到video_data字段
```

## 数据库表结构

### video_records 表
```sql
CREATE TABLE video_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### video_files 表
```sql
CREATE TABLE video_files (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    record_id BIGINT NOT NULL,
    video_data LONGTEXT NOT NULL,  -- 存储base64或URL
    name VARCHAR(255) NOT NULL,
    size BIGINT,
    type VARCHAR(100),
    FOREIGN KEY (record_id) REFERENCES video_records(id) ON DELETE CASCADE
);
```

## 配置说明

### 1. 数据库配置
在 `application.yml` 中配置数据库连接：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 2. 文件上传配置
```yaml
spring:
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
```

### 3. MyBatis配置
```yaml
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.tool.entity
```

## 使用步骤

1. **创建数据库表**
   ```sql
   -- 执行 video_tables.sql 中的建表语句
   ```

2. **配置数据库连接**
   - 修改 `application.yml` 中的数据库配置

3. **实现视频保存逻辑**
   - 在 `VideoServiceImpl.uploadVideo()` 方法中实现具体的视频保存逻辑

4. **启动应用**
   ```bash
   mvn spring-boot:run
   ```

5. **测试接口**
   - 使用Postman或其他工具测试各个接口

## 注意事项

1. **文件大小限制**：默认限制100MB，可根据需要调整
2. **文件类型验证**：只允许视频文件上传
3. **安全性**：建议添加文件类型白名单验证
4. **性能优化**：大文件建议使用分片上传
5. **存储策略**：建议使用云存储服务，避免本地存储空间不足

## 扩展功能建议

1. **视频转码**：集成FFmpeg进行视频格式转换
2. **视频压缩**：自动压缩大文件
3. **CDN加速**：使用CDN提升视频访问速度
4. **视频预览**：生成视频缩略图
5. **断点续传**：支持大文件断点续传功能

## 联系信息
如有问题，请联系开发团队。 