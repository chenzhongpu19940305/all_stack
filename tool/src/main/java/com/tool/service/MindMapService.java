package com.tool.service;

import com.tool.entity.MindMap;
import com.tool.entity.MindMapNode;
import com.tool.mapper.MindMapMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MindMapService {

    private final MindMapMapper mindMapMapper;

    public MindMapService(MindMapMapper mindMapMapper) {
        this.mindMapMapper = mindMapMapper;
    }

    @Transactional
    public Long saveMindMap(MindMap map, List<MindMapNode> nodes) {
        if (map.getId() == null) {
            mindMapMapper.insertMap(map);
        } else {
            mindMapMapper.updateMap(map);
            // 简化策略：清理旧节点，重建（前端提交完整节点）
            mindMapMapper.deleteNodesByMapId(map.getId());
        }
        Long mapId = map.getId();

        // 第一步：插入节点，记录 前端id -> 数据库新id 映射
        Map<Long, Long> oldIdToNewId = new HashMap<>();
        for (MindMapNode node : nodes) {
            Long clientId = node.getId();
            node.setMapId(mapId);
            // 插入时不包含id列，使用自增id
            node.setId(null);
            mindMapMapper.insertNode(node);
            if (clientId != null) {
                oldIdToNewId.put(clientId, node.getId());
            }
        }

        // 第二步：根据映射更新 parentId，保持父子关系正确
        for (MindMapNode node : nodes) {
            Long dbId = node.getId(); // 插入后被回填的新id
            Long oldParentId = node.getParentId();
            if (oldParentId != null) {
                Long newParentId = oldIdToNewId.get(oldParentId);
                if (newParentId != null) {
                    node.setParentId(newParentId);
                    // 使用全量更新方法写回（也可改成仅更新parentId的SQL）
                    node.setId(dbId);
                    mindMapMapper.updateNode(node);
                }
            }
        }
        return mapId;
    }

    public MindMap loadMindMap(Long id) {
        return mindMapMapper.findMapById(id);
    }

    public List<MindMapNode> loadNodes(Long mapId) {
        return mindMapMapper.findNodesByMapId(mapId);
    }
} 