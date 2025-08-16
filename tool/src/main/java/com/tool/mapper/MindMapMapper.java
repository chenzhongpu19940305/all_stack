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
    @Insert("INSERT INTO mind_map_node(map_id, parent_id, text, x, y, width, height, shape, background_color, border_color, font_size, is_root, collapsed, detail_record_id, detail_record_title, doc_record_id, doc_record_title, code_record_id, code_record_title, hyperlink) " +
            "VALUES(#{mapId}, #{parentId}, #{text}, #{x}, #{y}, #{width}, #{height}, #{shape}, #{backgroundColor}, #{borderColor}, #{fontSize}, #{isRoot}, #{collapsed}, #{detailRecordId}, #{detailRecordTitle}, #{docRecordId}, #{docRecordTitle}, #{codeRecordId}, #{codeRecordTitle}, #{hyperlink})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertNode(MindMapNode node);

    @Update("UPDATE mind_map_node SET parent_id=#{parentId}, text=#{text}, x=#{x}, y=#{y}, width=#{width}, height=#{height}, shape=#{shape}, background_color=#{backgroundColor}, border_color=#{borderColor}, font_size=#{fontSize}, is_root=#{isRoot}, collapsed=#{collapsed}, detail_record_id=#{detailRecordId}, detail_record_title=#{detailRecordTitle}, doc_record_id=#{docRecordId}, doc_record_title=#{docRecordTitle}, code_record_id=#{codeRecordId}, code_record_title=#{codeRecordTitle}, hyperlink=#{hyperlink} WHERE id=#{id}")
    int updateNode(MindMapNode node);

    @Select("SELECT id, map_id as mapId, parent_id as parentId, text, x, y, width, height, shape, background_color as backgroundColor, border_color as borderColor, font_size as fontSize, is_root as isRoot, collapsed, detail_record_id as detailRecordId, detail_record_title as detailRecordTitle, doc_record_id as docRecordId, doc_record_title as docRecordTitle, code_record_id as codeRecordId, code_record_title as codeRecordTitle, hyperlink FROM mind_map_node WHERE map_id=#{mapId}")
    List<MindMapNode> findNodesByMapId(@Param("mapId") Long mapId);

    @Delete("DELETE FROM mind_map_node WHERE map_id=#{mapId}")
    int deleteNodesByMapId(@Param("mapId") Long mapId);

    // List all maps
    @Select("SELECT id, title, layout, created_at as createdAt, updated_at as updatedAt FROM mind_map ORDER BY updated_at DESC")
    List<MindMap> findAllMaps();

    // Find root node for a map
    @Select("SELECT id, map_id as mapId, parent_id as parentId, text, x, y, width, height, shape, background_color as backgroundColor, border_color as borderColor, font_size as FontSize, is_root as isRoot, collapsed, detail_record_id as detailRecordId, detail_record_title as detailRecordTitle, doc_record_id as docRecordId, doc_record_title as docRecordTitle, code_record_id as codeRecordId, code_record_title as codeRecordTitle, hyperlink FROM mind_map_node WHERE map_id=#{mapId} AND is_root=1 LIMIT 1")
    MindMapNode findRootNodeByMapId(@Param("mapId") Long mapId);

    // List all root nodes across maps (for homepage)
    @Select("SELECT id, map_id as mapId, parent_id as parentId, text, x, y, width, height, shape, background_color as backgroundColor, border_color as borderColor, font_size as fontSize, is_root as isRoot, collapsed, detail_record_id as detailRecordId, detail_record_title as detailRecordTitle, doc_record_id as docRecordId, doc_record_title as docRecordTitle, code_record_id as codeRecordId, code_record_title as codeRecordTitle, hyperlink FROM mind_map_node WHERE (parent_id IS NULL OR is_root=1) ORDER BY id DESC")
    List<MindMapNode> findAllRootNodes();
} 