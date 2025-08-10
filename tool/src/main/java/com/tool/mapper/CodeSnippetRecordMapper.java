package com.tool.mapper;

import com.tool.entity.CodeSnippetRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 代码片段记录Mapper接口
 * 
 * @author tool-service
 * @version 1.0.0
 */
@Mapper
public interface CodeSnippetRecordMapper {
    
    /**
     * 根据ID查询记录
     */
    CodeSnippetRecord selectById(@Param("id") Long id);
    
    /**
     * 查询所有记录
     */
    List<CodeSnippetRecord> selectAll();
    
    /**
     * 分页查询记录
     */
    List<CodeSnippetRecord> selectByPage(@Param("offset") int offset, @Param("size") int size);
    
    /**
     * 根据标题搜索记录
     */
    List<CodeSnippetRecord> selectByTitle(@Param("title") String title);
    
    /**
     * 根据语言搜索记录
     */
    List<CodeSnippetRecord> selectByLanguage(@Param("language") String language);
    
    /**
     * 根据关键词搜索记录（标题、描述、代码内容）
     */
    List<CodeSnippetRecord> selectByKeyword(@Param("keyword") String keyword);
    
    /**
     * 统计记录总数
     */
    int count();
    
    /**
     * 根据标题统计记录数
     */
    int countByTitle(@Param("title") String title);
    
    /**
     * 根据关键词统计记录数
     */
    int countByKeyword(@Param("keyword") String keyword);
    
    /**
     * 插入记录
     */
    int insert(CodeSnippetRecord record);
    
    /**
     * 更新记录
     */
    int update(CodeSnippetRecord record);
    
    /**
     * 根据ID删除记录
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 查询记录及其关联的标签
     */
    CodeSnippetRecord selectWithTags(@Param("id") Long id);
    
    /**
     * 分页查询记录及其关联的标签
     */
    List<CodeSnippetRecord> selectWithTagsByPage(@Param("offset") int offset, @Param("size") int size);
}