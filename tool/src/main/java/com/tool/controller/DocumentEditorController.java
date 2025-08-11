package com.tool.controller;

import com.tool.entity.DocumentEditor;
import com.tool.mapper.DocumentEditorMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/document-editor")
@CrossOrigin(origins = "*")
public class DocumentEditorController {

    private final DocumentEditorMapper documentEditorMapper;

    public DocumentEditorController(DocumentEditorMapper documentEditorMapper) {
        this.documentEditorMapper = documentEditorMapper;
    }

    /**
     * 获取文档列表
     */
    @GetMapping("/documents")
    public ResponseEntity<Map<String, Object>> listDocuments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(value = "q", required = false) String q) {
        
        Map<String, Object> res = new HashMap<>();
        int offset = (page - 1) * size;
        
        try {
            List<DocumentEditor> documents;
            int total;
            
            if (q != null && !q.trim().isEmpty()) {
                documents = documentEditorMapper.searchByTitle(q.trim(), size, offset);
                total = documentEditorMapper.countByTitle(q.trim());
            } else {
                documents = documentEditorMapper.listDocuments(size, offset);
                total = documentEditorMapper.countDocuments();
            }
            
            res.put("success", true);
            res.put("documents", documents);
            res.put("total", total);
            res.put("page", page);
            res.put("size", size);
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", "获取文档列表失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(res);
    }

    /**
     * 根据ID获取文档详情
     */
    @GetMapping("/documents/{id}")
    public ResponseEntity<Map<String, Object>> getDocument(@PathVariable("id") String id) {
        Map<String, Object> res = new HashMap<>();
        
        try {
            DocumentEditor document = documentEditorMapper.findById(id);
            if (document != null) {
                res.put("success", true);
                res.put("document", document);
            } else {
                res.put("success", false);
                res.put("message", "文档不存在");
            }
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", "获取文档失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(res);
    }

    /**
     * 创建新文档
     */
    @PostMapping("/documents")
    public ResponseEntity<Map<String, Object>> createDocument(@RequestBody Map<String, String> request) {
        Map<String, Object> res = new HashMap<>();
        
        try {
            String title = request.get("title");
            String content = request.get("content");
            
            if (title == null || title.trim().isEmpty()) {
                res.put("success", false);
                res.put("message", "标题不能为空");
                return ResponseEntity.ok(res);
            }
            
            DocumentEditor document = new DocumentEditor();
            document.setId(UUID.randomUUID().toString());
            document.setTitle(title.trim());
            document.setContent(content != null ? content : "");
            
            int result = documentEditorMapper.insertDocument(document);
            if (result > 0) {
                res.put("success", true);
                res.put("document", document);
                res.put("message", "文档创建成功");
            } else {
                res.put("success", false);
                res.put("message", "文档创建失败");
            }
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", "创建文档失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(res);
    }

    /**
     * 更新文档
     */
    @PutMapping("/documents/{id}")
    public ResponseEntity<Map<String, Object>> updateDocument(
            @PathVariable("id") String id,
            @RequestBody Map<String, String> request) {
        
        Map<String, Object> res = new HashMap<>();
        
        try {
            String title = request.get("title");
            String content = request.get("content");
            
            if (title == null || title.trim().isEmpty()) {
                res.put("success", false);
                res.put("message", "标题不能为空");
                return ResponseEntity.ok(res);
            }
            
            // 检查文档是否存在
            if (!documentEditorMapper.existsById(id)) {
                res.put("success", false);
                res.put("message", "文档不存在");
                return ResponseEntity.ok(res);
            }
            
            DocumentEditor document = new DocumentEditor();
            document.setId(id);
            document.setTitle(title.trim());
            document.setContent(content != null ? content : "");
            
            int result = documentEditorMapper.updateDocument(document);
            if (result > 0) {
                // 返回更新后的文档
                DocumentEditor updatedDocument = documentEditorMapper.findById(id);
                res.put("success", true);
                res.put("document", updatedDocument);
                res.put("message", "文档更新成功");
            } else {
                res.put("success", false);
                res.put("message", "文档更新失败");
            }
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", "更新文档失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(res);
    }

    /**
     * 删除文档
     */
    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Map<String, Object>> deleteDocument(@PathVariable("id") String id) {
        Map<String, Object> res = new HashMap<>();
        
        try {
            // 检查文档是否存在
            if (!documentEditorMapper.existsById(id)) {
                res.put("success", false);
                res.put("message", "文档不存在");
                return ResponseEntity.ok(res);
            }
            
            int result = documentEditorMapper.deleteDocument(id);
            if (result > 0) {
                res.put("success", true);
                res.put("message", "文档删除成功");
            } else {
                res.put("success", false);
                res.put("message", "文档删除失败");
            }
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", "删除文档失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(res);
    }
}