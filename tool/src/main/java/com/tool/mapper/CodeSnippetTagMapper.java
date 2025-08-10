package com.tool.mapper;

import com.tool.entity.CodeSnippetTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 代码片段标签Mapper接口
 * 
 * @author tool-service
 * @version 1.0.0
 */
@Mapper
public interface CodeSnippetTagMapper {
    
    /**
     * 根据ID查询标签
     */
    CodeSnippetTag selectById(@Param("id") Long id);
    
    /**
     * 根据记录ID查询标签列表
     */
    List<CodeSnippetTag> selectByRecordId(@Param("recordId") Long recordId);
    
    /**
     * 查询所有标签
     */
    List<CodeSnippetTag> selectAll();
    
    /**
     * 根据标签名查询标签
     */
    List<CodeSnippetTag> selectByTagName(@Param("tagName") String tagName);
    
    /**
     * 插入标签
     */
    int insert(CodeSnippetTag tag);
    
    /**
     * 更新标签
     */
    int update(CodeSnippetTag tag);
    
    /**
     * 根据ID删除标签
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据记录ID删除标签
     */
    int deleteByRecordId(@Param("recordId") Long recordId);
    
    /**
     * 批量插入标签
     */
    int insertBatch(@Param("tags") List<CodeSnippetTag> tags);
    
    /**
     * 查询孤立标签（没有关联记录的标签）
     */
    List<CodeSnippetTag> selectOrphanTags();
    
    /**
     * 删除孤立标签
     */
    int deleteOrphanTags();
    
    /**
     * 获取所有不重复的标签名
     */
    List<String> selectDistinctTagNames();
}