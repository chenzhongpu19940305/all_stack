package com.example.demo.entity;

public class GalleryImage {
    private Long id;
    private String name;
    private String contentType;
    private byte[] imageData;
    private Long recordId;

    public GalleryImage() {
    }

    public GalleryImage(Long id, String name, String contentType, byte[] imageData, Long recordId) {
        this.id = id;
        this.name = name;
        this.contentType = contentType;
        this.imageData = imageData;
        this.recordId = recordId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }
} 