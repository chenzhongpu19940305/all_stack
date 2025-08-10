package com.tool.controller;

import com.tool.entity.DocumentRecord;
import com.tool.entity.DocumentFile;
import com.tool.mapper.DocumentMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/docs")
@CrossOrigin(origins = "*")
public class DocumentController {

    private final DocumentMapper documentMapper;

    public DocumentController(DocumentMapper documentMapper) {
        this.documentMapper = documentMapper;
    }

    @PostMapping("/records")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestParam("title") String title,
                                                            @RequestParam(value = "description", required = false) String description,
                                                            @RequestParam("files") List<MultipartFile> files) throws Exception {
        Map<String, Object> res = new HashMap<>();
        DocumentRecord record = new DocumentRecord();
        record.setTitle(title);
        record.setDescription(description);
        documentMapper.insertRecord(record);
        Long recordId = record.getId();

        String baseDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "docs" + File.separator + recordId;
        Files.createDirectories(Paths.get(baseDir));

        for (MultipartFile f : files) {
            String filename = System.currentTimeMillis() + "_" + f.getOriginalFilename();
            String storagePath = baseDir + File.separator + filename;
            Files.copy(f.getInputStream(), Paths.get(storagePath));

            DocumentFile df = new DocumentFile();
            df.setRecordId(recordId);
            df.setName(f.getOriginalFilename());
            df.setMimeType(f.getContentType());
            df.setSize(f.getSize());
            df.setStoragePath(storagePath);
            df.setSha256(sha256(f.getBytes()));
            documentMapper.insertFile(df);
        }

        res.put("success", true);
        res.put("id", recordId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/records")
    public ResponseEntity<Map<String, Object>> listRecords(@RequestParam(defaultValue = "1") int page,
                                                           @RequestParam(defaultValue = "20") int size,
                                                           @RequestParam(value = "q", required = false) String q) {
        Map<String, Object> res = new HashMap<>();
        int offset = (page - 1) * size;
        List<DocumentRecord> records;
        int total;
        if (q != null && !q.trim().isEmpty()) {
            records = documentMapper.searchByTitle(q.trim(), size, offset);
            total = documentMapper.countByTitle(q.trim());
        } else {
            records = documentMapper.listRecords(size, offset);
            total = documentMapper.countRecords();
        }
        res.put("success", true);
        res.put("records", records);
        res.put("total", total);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/records/{id}/files")
    public ResponseEntity<Map<String, Object>> listFiles(@PathVariable("id") Long id) {
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("files", documentMapper.listFiles(id));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<FileSystemResource> download(@PathVariable("fileId") Long fileId) throws IOException {
        DocumentFile f = documentMapper.findFileById(fileId);
        if (f == null) {
            return ResponseEntity.notFound().build();
        }
        File file = new File(f.getStoragePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        FileSystemResource resource = new FileSystemResource(file);
        String contentType = f.getMimeType() != null ? f.getMimeType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;
        String filename = f.getName() != null ? f.getName() : file.getName();
        String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"; filename*=UTF-8''" + encoded)
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(file.length())
                .body(resource);
    }

    @GetMapping("/files/{fileId}/download")
    public ResponseEntity<FileSystemResource> forceDownload(@PathVariable("fileId") Long fileId) throws IOException {
        DocumentFile f = documentMapper.findFileById(fileId);
        if (f == null) {
            return ResponseEntity.notFound().build();
        }
        File file = new File(f.getStoragePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        FileSystemResource resource = new FileSystemResource(file);
        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        String filename = f.getName() != null ? f.getName() : file.getName();
        String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + encoded)
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(file.length())
                .body(resource);
    }

    @DeleteMapping("/records/{id}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable("id") Long id) {
        Map<String, Object> res = new HashMap<>();
        try {
            // 先获取记录关联的文件，删除物理文件
            List<DocumentFile> files = documentMapper.listFiles(id);
            for (DocumentFile file : files) {
                File physicalFile = new File(file.getStoragePath());
                if (physicalFile.exists()) {
                    physicalFile.delete();
                }
            }
            
            // 删除数据库中的文件记录
            documentMapper.deleteFilesByRecordId(id);
            
            // 删除记录
            int deleted = documentMapper.deleteRecord(id);
            
            if (deleted > 0) {
                res.put("success", true);
                res.put("message", "删除成功");
            } else {
                res.put("success", false);
                res.put("message", "记录不存在");
            }
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", "删除失败: " + e.getMessage());
        }
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<Map<String, Object>> deleteFile(@PathVariable("fileId") Long fileId) {
        Map<String, Object> res = new HashMap<>();
        try {
            // 先获取文件信息
            DocumentFile file = documentMapper.findFileById(fileId);
            if (file == null) {
                res.put("success", false);
                res.put("message", "文件不存在");
                return ResponseEntity.ok(res);
            }
            
            // 删除物理文件
            File physicalFile = new File(file.getStoragePath());
            if (physicalFile.exists()) {
                physicalFile.delete();
            }
            
            // 删除数据库记录
            int deleted = documentMapper.deleteFile(fileId);
            
            if (deleted > 0) {
                res.put("success", true);
                res.put("message", "文件删除成功");
            } else {
                res.put("success", false);
                res.put("message", "删除失败");
            }
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", "删除失败: " + e.getMessage());
        }
        return ResponseEntity.ok(res);
    }

    private static String sha256(byte[] data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data);
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}