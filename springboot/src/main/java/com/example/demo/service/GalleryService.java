package com.example.demo.service;

import com.example.demo.dto.GalleryRecordDTO;
import com.example.demo.dto.GalleryImageDTO;
import com.example.demo.entity.GalleryImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GalleryService {
    
    List<GalleryRecordDTO> getAllRecords();
    
    GalleryRecordDTO getRecordById(Long id);
    
    List<GalleryRecordDTO> searchRecords(String query);
    
    GalleryRecordDTO createRecord(GalleryRecordDTO recordDTO);
    
    void deleteRecord(Long id);
    
    GalleryImageDTO uploadImage(MultipartFile file);
    
    GalleryImage getImageById(Long imageId);
} 