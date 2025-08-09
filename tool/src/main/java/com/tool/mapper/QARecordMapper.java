package com.tool.mapper;

import com.tool.entity.QARecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface QARecordMapper {

    @Select("SELECT id, title, content, created_at as createdAt, updated_at as updatedAt FROM qa_record WHERE id=#{id}")
    QARecord findById(@Param("id") Long id);

    @Select({
            "<script>",
            "SELECT id, title, content, created_at as createdAt, updated_at as updatedAt",
            "FROM qa_record",
            "<where>",
            "  <if test='kw != null and kw != \"\"'>",
            "    (title LIKE CONCAT('%', #{kw}, '%') OR content LIKE CONCAT('%', #{kw}, '%'))",
            "  </if>",
            "</where>",
            "ORDER BY created_at DESC",
            "LIMIT #{size}",
            "</script>"
    })
    List<QARecord> search(@Param("kw") String keyword, @Param("size") Integer size);
} 