package com.tool.controller;

import com.tool.entity.MindMap;
import com.tool.entity.MindMapNode;
import com.tool.service.MindMapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mindmap")
@CrossOrigin(origins = "*")
public class MindMapController {

    private final MindMapService mindMapService;

    public MindMapController(MindMapService mindMapService) {
        this.mindMapService = mindMapService;
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> save(@RequestBody Map<String, Object> payload) {
        // 预期 payload: { map: {id?, title, layout}, nodes: [ {id?, parentId, text, x,y,width,height,shape,backgroundColor,borderColor,fontSize,isRoot,collapsed} ] }
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> mapObj = (Map<String, Object>) payload.get("map");
            List<Map<String, Object>> nodesList = (List<Map<String, Object>>) payload.get("nodes");

            MindMap map = new MindMap();
            if (mapObj.get("id") != null) map.setId(((Number) mapObj.get("id")).longValue());
            map.setTitle((String) mapObj.getOrDefault("title", "未命名导图"));
            map.setLayout((String) mapObj.getOrDefault("layout", "mindmap"));

            List<MindMapNode> nodes = nodesList.stream().map(n -> {
                MindMapNode node = new MindMapNode();
                if (n.get("id") != null) node.setId(((Number) n.get("id")).longValue());
                if (n.get("parentId") != null) node.setParentId(n.get("parentId") == null ? null : ((Number) n.get("parentId")).longValue());
                node.setText((String) n.getOrDefault("text", "节点"));
                node.setX(((Number) n.getOrDefault("x", 0)).intValue());
                node.setY(((Number) n.getOrDefault("y", 0)).intValue());
                node.setWidth(((Number) n.getOrDefault("width", 100)).intValue());
                node.setHeight(((Number) n.getOrDefault("height", 50)).intValue());
                node.setShape((String) n.getOrDefault("shape", "rectangle"));
                node.setBackgroundColor((String) n.getOrDefault("backgroundColor", "#2196F3"));
                node.setBorderColor((String) n.getOrDefault("borderColor", "#1976D2"));
                node.setFontSize(((Number) n.getOrDefault("fontSize", 14)).intValue());
                node.setIsRoot((Boolean) n.getOrDefault("isRoot", false));
                node.setCollapsed((Boolean) n.getOrDefault("collapsed", false));
                if (n.get("detailRecordId") != null) {
                    Object v = n.get("detailRecordId");
                    if (v instanceof Number) node.setDetailRecordId(((Number) v).longValue());
                    else if (v instanceof String && !((String) v).isEmpty()) node.setDetailRecordId(Long.parseLong((String) v));
                }
                if (n.get("detailRecordTitle") != null) {
                    node.setDetailRecordTitle((String) n.get("detailRecordTitle"));
                }
                
                // 处理文档关联字段
                if (n.get("docRecordId") != null) {
                    Object v = n.get("docRecordId");
                    if (v instanceof String && !((String) v).isEmpty()) {
                        // 验证文档ID格式
                        String docId = (String) v;
                        if (isValidUUID(docId) || isValidLong(docId)) {
                            node.setDocRecordId(docId);
                        } else {
                            System.out.println("Warning: Invalid docRecordId format: " + docId);
                            node.setDocRecordId(null);
                        }
                    } else if (v instanceof Number) {
                        node.setDocRecordId(String.valueOf(v));
                    } else {
                        System.out.println("Warning: Invalid docRecordId type: " + v.getClass().getSimpleName());
                        node.setDocRecordId(null);
                    }
                }
                if (n.get("docRecordTitle") != null) {
                    node.setDocRecordTitle((String) n.get("docRecordTitle"));
                }
                
                // 处理代码片段关联字段
                if (n.get("codeRecordId") != null) {
                    Object v = n.get("codeRecordId");
                    if (v instanceof Number) {
                        node.setCodeRecordId(((Number) v).longValue());
                    } else if (v instanceof String && !((String) v).isEmpty()) {
                        try {
                            // 尝试解析为数字ID
                            node.setCodeRecordId(Long.parseLong((String) v));
                        } catch (NumberFormatException e) {
                            // 如果是UUID格式，暂时跳过，或者可以记录日志
                            System.out.println("Warning: codeRecordId is UUID format, skipping: " + v);
                            node.setCodeRecordId(null);
                        }
                    }
                }
                if (n.get("codeRecordTitle") != null) {
                    node.setCodeRecordTitle((String) n.get("codeRecordTitle"));
                }
                
                // 处理超链接字段
                if (n.get("hyperlink") != null) {
                    String hyperlink = (String) n.get("hyperlink");
                    if (hyperlink != null && !hyperlink.trim().isEmpty()) {
                        // 验证URL格式（简单验证）
                        if (hyperlink.startsWith("http://") || hyperlink.startsWith("https://") || 
                            hyperlink.startsWith("ftp://") || hyperlink.startsWith("mailto:") ||
                            hyperlink.startsWith("tel:") || hyperlink.startsWith("#")) {
                            node.setHyperlink(hyperlink.trim());
                        } else {
                            System.out.println("Warning: Invalid hyperlink format: " + hyperlink);
                            node.setHyperlink(null);
                        }
                    } else {
                        node.setHyperlink(null);
                    }
                }
                
                return node;
            }).collect(Collectors.toList());

            Long mapId = mindMapService.saveMindMap(map, nodes);
            response.put("success", true);
            response.put("mapId", mapId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> load(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<>();
        MindMap map = mindMapService.loadMindMap(id);
        if (map == null) {
            response.put("success", false);
            response.put("error", "not_found");
            return ResponseEntity.status(404).body(response);
        }
        List<MindMapNode> nodes = mindMapService.loadNodes(id);
        response.put("success", true);
        response.put("map", map);
        response.put("nodes", nodes);
        return ResponseEntity.ok(response);
    }

    // EdrawMind首页：列出所有导图（仅元信息）
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> listMaps() {
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("roots", mindMapService.listAllRootNodes());
        return ResponseEntity.ok(res);
    }

    // 获取某导图根节点（首页展示）
    @GetMapping("/{id}/root")
    public ResponseEntity<Map<String, Object>> loadRoot(@PathVariable("id") Long id) {
        Map<String, Object> res = new HashMap<>();
        MindMap map = mindMapService.loadMindMap(id);
        if (map == null) {
            res.put("success", false);
            res.put("error", "not_found");
            return ResponseEntity.status(404).body(res);
        }
        res.put("success", true);
        res.put("map", map);
        res.put("root", mindMapService.loadRootNode(id));
        return ResponseEntity.ok(res);
    }
    
    // 删除思维导图
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteMindMap(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 先检查思维导图是否存在
            MindMap existingMap = mindMapService.loadMindMap(id);
            if (existingMap == null) {
                response.put("success", false);
                response.put("error", "思维导图不存在");
                return ResponseEntity.status(404).body(response);
            }
            
            // 删除思维导图及其所有节点
            boolean deleted = mindMapService.deleteMindMap(id);
            if (deleted) {
                response.put("success", true);
                response.put("message", "思维导图删除成功");
                response.put("deletedId", id);
                System.out.println("思维导图删除成功，ID: " + id);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "删除失败，请稍后重试");
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            System.err.println("删除思维导图时发生错误，ID: " + id + ", 错误: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("error", "删除过程中发生错误: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // 验证UUID格式的辅助方法
    private boolean isValidUUID(String uuid) {
        try {
            java.util.UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    // 验证Long格式的辅助方法
    private boolean isValidLong(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
} 