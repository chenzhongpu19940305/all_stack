package com.tool.mapper;

import com.tool.entity.DocumentRecord;
import com.tool.entity.DocumentFile;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface DocumentMapper {

    @Insert("INSERT INTO document_record(title, description, created_at, updated_at) VALUES(#{title}, #{description}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertRecord(DocumentRecord record);

    @Update("UPDATE document_record SET title=#{title}, description=#{description}, updated_at=NOW() WHERE id=#{id}")
    int updateRecord(DocumentRecord record);

    @Select("SELECT id, title, description, created_at as createdAt, updated_at as updatedAt FROM document_record ORDER BY updated_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<DocumentRecord> listRecords(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT COUNT(1) FROM document_record")
    int countRecords();

    // Fuzzy search by title
    @Select("SELECT id, title, description, created_at as createdAt, updated_at as updatedAt FROM document_record WHERE title LIKE CONCAT('%', #{kw}, '%') ORDER BY updated_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<DocumentRecord> searchByTitle(@Param("kw") String keyword, @Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT COUNT(1) FROM document_record WHERE title LIKE CONCAT('%', #{kw}, '%')")
    int countByTitle(@Param("kw") String keyword);

    @Insert("INSERT INTO document_file(record_id, name, mime_type, size, storage_path, preview_path, sha256, created_at) VALUES(#{recordId}, #{name}, #{mimeType}, #{size}, #{storagePath}, #{previewPath}, #{sha256}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertFile(DocumentFile file);

    @Select("SELECT id, record_id as recordId, name, mime_type as mimeType, size, storage_path as storagePath, preview_path as previewPath, sha256, created_at as createdAt FROM document_file WHERE record_id=#{recordId}")
    List<DocumentFile> listFiles(@Param("recordId") Long recordId);

    @Select("SELECT id, record_id as recordId, name, mime_type as mimeType, size, storage_path as storagePath, preview_path as previewPath, sha256, created_at as createdAt FROM document_file WHERE id=#{id}")
    DocumentFile findFileById(@Param("id") Long id);

    @Delete("DELETE FROM document_file WHERE id=#{id}")
    int deleteFile(@Param("id") Long id);

    @Delete("DELETE FROM document_record WHERE id=#{id}")
    int deleteRecord(@Param("id") Long id);

    @Delete("DELETE FROM document_file WHERE record_id=#{recordId}")
    int deleteFilesByRecordId(@Param("recordId") Long recordId);
}