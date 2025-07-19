package com.tool.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class FileUploadController {

    private static final String UPLOAD_DIR = "uploads";
    private static final String VIDEO_DIR = "videos";
    private static final String IMAGE_DIR = "images";

    public FileUploadController() {
        // 创建上传目录
        createUploadDirectories();
    }

    /**
     * 上传视频文件
     */
    @PostMapping("/video")
    public ResponseEntity<Map<String, Object>> uploadVideo(@RequestParam("file") MultipartFile file) {
        return uploadFile(file, VIDEO_DIR, "video");
    }

    /**
     * 上传图片文件
     */
    @PostMapping("/image")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) {
        return uploadFile(file, IMAGE_DIR, "image");
    }

    /**
     * 通用文件上传方法
     */
    private ResponseEntity<Map<String, Object>> uploadFile(MultipartFile file, String subDir, String fileType) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "文件为空");
                return ResponseEntity.badRequest().body(response);
            }

            // 检查文件大小（限制为50MB）
            if (file.getSize() > 50 * 1024 * 1024) {
                response.put("success", false);
                response.put("message", "文件大小超过限制（50MB）");
                return ResponseEntity.badRequest().body(response);
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            // 创建目标目录
            Path uploadPath = Paths.get(UPLOAD_DIR, subDir);
            Files.createDirectories(uploadPath);

            // 保存文件
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath);

            // 构建文件URL
            String fileUrl = "/" + UPLOAD_DIR + "/" + subDir + "/" + uniqueFilename;

            response.put("success", true);
            response.put("message", fileType + "上传成功");
            response.put("filename", uniqueFilename);
            response.put("originalName", originalFilename);
            response.put("fileUrl", fileUrl);
            response.put("fileSize", file.getSize());

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "文件上传失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    /**
     * 创建上传目录
     */
    private void createUploadDirectories() {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            Path videoPath = uploadPath.resolve(VIDEO_DIR);
            Path imagePath = uploadPath.resolve(IMAGE_DIR);

            Files.createDirectories(videoPath);
            Files.createDirectories(imagePath);

            System.out.println("上传目录创建成功: " + uploadPath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("创建上传目录失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/file")
    public ResponseEntity<Map<String, Object>> deleteFile(@RequestParam String fileUrl) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 移除开头的斜杠
            String filePath = fileUrl.startsWith("/") ? fileUrl.substring(1) : fileUrl;
            Path path = Paths.get(filePath);

            if (Files.exists(path)) {
                Files.delete(path);
                response.put("success", true);
                response.put("message", "文件删除成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "文件不存在");
                return ResponseEntity.notFound().build();
            }

        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "文件删除失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 获取上传目录信息
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getUploadInfo() {
        Map<String, Object> response = new HashMap<>();

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            Path videoPath = uploadPath.resolve(VIDEO_DIR);
            Path imagePath = uploadPath.resolve(IMAGE_DIR);

            response.put("uploadDir", uploadPath.toAbsolutePath().toString());
            response.put("videoDir", videoPath.toAbsolutePath().toString());
            response.put("imageDir", imagePath.toAbsolutePath().toString());
            response.put("uploadDirExists", Files.exists(uploadPath));
            response.put("videoDirExists", Files.exists(videoPath));
            response.put("imageDirExists", Files.exists(imagePath));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
} 