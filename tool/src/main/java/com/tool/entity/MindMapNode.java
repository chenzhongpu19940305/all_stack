package com.tool.entity;

/**
 * 思维导图-节点
 */
public class MindMapNode {
    private Long id;
    private Long mapId;      // 所属思维导图
    private Long parentId;   // 父节点ID，根节点为null

    private String text;
    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;

    private String shape;            // rectangle/rounded/circle/diamond/cloud
    private String backgroundColor;
    private String borderColor;
    private Integer fontSize;

    private Boolean isRoot;
    private Boolean collapsed;

    // 详细说明关联（来自AI问答记录）
    private Long detailRecordId;
    private String detailRecordTitle;
    
    // 关联代码片段
    private Long codeRecordId;
    private String codeRecordTitle;
    
    // 关联文档
    private String docRecordId;
    private String docRecordTitle;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMapId() { return mapId; }
    public void setMapId(Long mapId) { this.mapId = mapId; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Integer getX() { return x; }
    public void setX(Integer x) { this.x = x; }

    public Integer getY() { return y; }
    public void setY(Integer y) { this.y = y; }

    public Integer getWidth() { return width; }
    public void setWidth(Integer width) { this.width = width; }

    public Integer getHeight() { return height; }
    public void setHeight(Integer height) { this.height = height; }

    public String getShape() { return shape; }
    public void setShape(String shape) { this.shape = shape; }

    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }

    public String getBorderColor() { return borderColor; }
    public void setBorderColor(String borderColor) { this.borderColor = borderColor; }

    public Integer getFontSize() { return fontSize; }
    public void setFontSize(Integer fontSize) { this.fontSize = fontSize; }

    public Boolean getIsRoot() { return isRoot; }
    public void setIsRoot(Boolean isRoot) { this.isRoot = isRoot; }

    public Boolean getCollapsed() { return collapsed; }
    public void setCollapsed(Boolean collapsed) { this.collapsed = collapsed; }

    public Long getDetailRecordId() { return detailRecordId; }
    public void setDetailRecordId(Long detailRecordId) { this.detailRecordId = detailRecordId; }

    public String getDetailRecordTitle() { return detailRecordTitle; }
    public void setDetailRecordTitle(String detailRecordTitle) { this.detailRecordTitle = detailRecordTitle; }
    
    public Long getCodeRecordId() { return codeRecordId; }
    public void setCodeRecordId(Long codeRecordId) { this.codeRecordId = codeRecordId; }
    
    public String getCodeRecordTitle() { return codeRecordTitle; }
    public void setCodeRecordTitle(String codeRecordTitle) { this.codeRecordTitle = codeRecordTitle; }
    
    public String getDocRecordId() { return docRecordId; }
    public void setDocRecordId(String docRecordId) { this.docRecordId = docRecordId; }
    
    public String getDocRecordTitle() { return docRecordTitle; }
    public void setDocRecordTitle(String docRecordTitle) { this.docRecordTitle = docRecordTitle; }
} 