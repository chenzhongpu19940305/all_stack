package com.tool;

import com.tool.mapper.GalleryRecordMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MyBatis配置测试类
 * 
 * @author tool-service
 * @version 1.0.0
 */
@SpringBootTest
public class MyBatisTest {
    
    @Autowired
    private GalleryRecordMapper galleryRecordMapper;
    
    @Test
    public void testMapperInjection() {
        assertNotNull(galleryRecordMapper, "GalleryRecordMapper should be injected");
    }
    
    @Test
    public void testSelectAll() {
        try {
            java.util.List<com.tool.entity.GalleryRecord> records = galleryRecordMapper.selectAll();
            assertNotNull(records, "Should return a list (even if empty)");
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
} 