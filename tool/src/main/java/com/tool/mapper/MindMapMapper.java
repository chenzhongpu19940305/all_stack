package com.tool.mapper;

import com.tool.entity.MindMap;
import com.tool.entity.MindMapNode;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MindMapMapper {

    // MindMap CRUD
    @Insert("INSERT INTO mind_map(title, layout, created_at, updated_at) VALUES(#{title}, #{layout}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertMap(MindMap map);

    @Update("UPDATE mind_map SET title=#{title}, layout=#{layout}, updated_at=NOW() WHERE id=#{id}")
    int updateMap(MindMap map);

    @Select("SELECT id, title, layout, created_at as createdAt, updated_at as updatedAt FROM mind_map WHERE id=#{id}")
    MindMap findMapById(@Param("id") Long id);

    @Delete("DELETE FROM mind_map WHERE id=#{id}")
    int deleteMap(@Param("id") Long id);

    // MindMapNode CRUD
    @Insert("INSERT INTO mind_map_node(map_id, parent_id, text, x, y, width, height, shape, background_color, border_color, font_size, is_root, collapsed) " +
            "VALUES(#{mapId}, #{parentId}, #{text}, #{x}, #{y}, #{width}, #{height}, #{shape}, #{backgroundColor}, #{borderColor}, #{fontSize}, #{isRoot}, #{collapsed})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertNode(MindMapNode node);

    @Update("UPDATE mind_map_node SET parent_id=#{parentId}, text=#{text}, x=#{x}, y=#{y}, width=#{width}, height=#{height}, shape=#{shape}, background_color=#{backgroundColor}, border_color=#{borderColor}, font_size=#{fontSize}, is_root=#{isRoot}, collapsed=#{collapsed} WHERE id=#{id}")
    int updateNode(MindMapNode node);

    @Select("SELECT id, map_id as mapId, parent_id as parentId, text, x, y, width, height, shape, background_color as backgroundColor, border_color as borderColor, font_size as fontSize, is_root as isRoot, collapsed FROM mind_map_node WHERE map_id=#{mapId}")
    List<MindMapNode> findNodesByMapId(@Param("mapId") Long mapId);

    @Delete("DELETE FROM mind_map_node WHERE map_id=#{mapId}")
    int deleteNodesByMapId(@Param("mapId") Long mapId);
} 