package com.example.demo.dto;

import java.util.List;

public class GalleryRecordDTO {
    private Long id;
    private String title;
    private List<GalleryImageDTO> images;

    public GalleryRecordDTO() {
    }

    public GalleryRecordDTO(Long id, String title, List<GalleryImageDTO> images) {
        this.id = id;
        this.title = title;
        this.images = images;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<GalleryImageDTO> getImages() {
        return images;
    }

    public void setImages(List<GalleryImageDTO> images) {
        this.images = images;
    }
} 