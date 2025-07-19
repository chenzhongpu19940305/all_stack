package com.tool;

import com.tool.controller.GalleryController;
import com.tool.dto.CreateRecordRequest;
import com.tool.dto.GalleryImageDTO;
import com.tool.dto.SearchRequest;
import com.tool.service.GalleryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Gallery控制器测试类
 * 
 * @author tool-service
 * @version 1.0.0
 */
@SpringBootTest
public class GalleryControllerTest {
    
    @Autowired
    private GalleryController galleryController;
    
    @Autowired
    private GalleryService galleryService;
    
    @Test
    public void testGetRecords() {
        ResponseEntity<Map<String, Object>> response = galleryController.getRecords();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("records"));
        assertTrue(body.containsKey("total"));
    }
    
    @Test
    public void testSearchRecords() {
        SearchRequest request = new SearchRequest("Java");
        ResponseEntity<Map<String, Object>> response = galleryController.searchRecords(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("records"));
        assertTrue(body.containsKey("total"));
    }

    
    @Test
    public void testGetAllRecords() {
        List<com.tool.dto.GalleryRecordDTO> records = galleryService.getAllRecords();
        assertNotNull(records);
        // 由于数据库可能为空，所以不检查具体数量
    }
} 