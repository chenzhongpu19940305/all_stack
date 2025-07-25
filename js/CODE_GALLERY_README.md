# 代码片段管理功能说明

## 功能概述

代码片段管理功能是基于现有的Gallery模块扩展而来，允许用户存储、管理和分享代码片段。该功能复用了Gallery的后端接口和数据库结构，通过添加代码相关字段来支持代码片段的存储。

## 主要特性

### 1. 代码片段存储
- 支持多种编程语言（JavaScript、Java、Python、C++、C#、PHP、Go、Rust、SQL、HTML、CSS、Vue、React、TypeScript等）
- 代码语法高亮显示
- 支持长代码片段（LONGTEXT类型）

### 2. 代码片段管理
- 新增代码片段
- 编辑现有代码片段
- 删除代码片段
- 搜索代码片段（支持标题、描述、代码内容搜索）

### 3. 代码展示
- 卡片式布局展示
- 代码预览（显示前5行）
- 全屏代码查看
- 一键复制代码功能

### 4. 用户体验
- 支持粘贴代码
- 支持拖拽代码文件
- 响应式设计，支持移动端
- 现代化的UI界面

## 技术实现

### 前端技术栈
- Vue 3 + Composition API
- 响应式设计
- 现代化UI组件

### 后端技术栈
- Spring Boot
- MyBatis
- MySQL数据库

### 数据库结构
```sql
-- gallery_records表新增字段
ALTER TABLE gallery_records 
ADD COLUMN description TEXT COMMENT '记录描述',
ADD COLUMN code LONGTEXT COMMENT '代码内容',
ADD COLUMN language VARCHAR(50) COMMENT '编程语言';
```

## 使用方法

### 1. 访问代码片段管理页面
- 在主页点击"代码片段管理"卡片
- 或直接访问 `/code-gallery` 路径

### 2. 新增代码片段
1. 点击"新增代码"按钮
2. 填写标题和描述
3. 选择编程语言
4. 粘贴或输入代码内容
5. 点击"新增"保存

### 3. 查看代码片段
- 点击代码卡片查看详情
- 在全屏模式下可以复制代码
- 支持搜索功能

### 4. 编辑代码片段
- 鼠标悬停在卡片上显示操作按钮
- 点击"更新"按钮进行编辑
- 修改后保存更改

### 5. 删除代码片段
- 鼠标悬停在卡片上显示操作按钮
- 点击"删除"按钮
- 确认删除操作

## API接口

代码片段管理功能复用了Gallery的API接口：

### 获取代码片段列表
```
GET /api/gallery/records
```

### 搜索代码片段
```
POST /api/gallery/records/search
Content-Type: application/json

{
  "query": "搜索关键词"
}
```

### 新增代码片段
```
POST /api/gallery/records
Content-Type: application/json

{
  "title": "代码标题",
  "description": "代码描述",
  "code": "代码内容",
  "language": "javascript",
  "images": []
}
```

### 更新代码片段
```
PUT /api/gallery/records/{id}
Content-Type: application/json

{
  "title": "更新后的标题",
  "description": "更新后的描述",
  "code": "更新后的代码",
  "language": "java",
  "images": []
}
```

### 删除代码片段
```
DELETE /api/gallery/records/{id}
```

## 数据库迁移

如果您的数据库中没有代码相关字段，请执行以下迁移脚本：

```sql
-- 添加代码相关字段
ALTER TABLE gallery_records 
ADD COLUMN description TEXT COMMENT '记录描述' AFTER title,
ADD COLUMN code LONGTEXT COMMENT '代码内容' AFTER description,
ADD COLUMN language VARCHAR(50) COMMENT '编程语言' AFTER code;

-- 添加索引
ALTER TABLE gallery_records 
ADD INDEX idx_language (language);
```

## 支持的编程语言

- JavaScript
- Java
- Python
- C++
- C#
- PHP
- Go
- Rust
- SQL
- HTML
- CSS
- Vue
- React
- TypeScript
- 文本

## 注意事项

1. 代码内容使用LONGTEXT类型存储，支持大段代码
2. 代码片段与图片可以共存，支持混合内容
3. 搜索功能支持在代码内容中搜索
4. 代码复制功能使用现代浏览器的Clipboard API
5. 响应式设计确保在不同设备上都有良好的体验

## 扩展功能

未来可以考虑添加的功能：
- 代码语法高亮
- 代码格式化
- 代码版本管理
- 代码分享链接
- 代码评论功能
- 代码标签分类 