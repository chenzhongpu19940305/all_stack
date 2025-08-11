package com.tool.mapper;

import com.tool.entity.DocumentEditor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DocumentEditorMapper {

    int insertDocument(DocumentEditor document);

    int updateDocument(DocumentEditor document);

    DocumentEditor findById(@Param("id") String id);

    List<DocumentEditor> listDocuments(@Param("limit") int limit, @Param("offset") int offset);

    int countDocuments();

    List<DocumentEditor> searchByTitle(@Param("kw") String keyword, @Param("limit") int limit, @Param("offset") int offset);

    int countByTitle(@Param("kw") String keyword);

    int deleteDocument(@Param("id") String id);

    boolean existsById(@Param("id") String id);
}