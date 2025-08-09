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
} 