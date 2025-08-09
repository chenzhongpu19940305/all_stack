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
} 