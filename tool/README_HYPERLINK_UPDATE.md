# 思维导图超链接功能更新说明

## 概述
本次更新为思维导图节点添加了超链接功能，允许用户为节点设置URL链接，并在节点下方显示超链接按钮。

## 数据库更改

### 1. 表结构更新
需要为现有的 `mind_map_node` 表添加 `hyperlink` 字段：

```sql
-- 添加超链接字段
ALTER TABLE mind_map_node 
ADD COLUMN IF NOT EXISTS hyperlink VARCHAR(500) NULL 
COMMENT '节点超链接地址';

-- 添加索引以提高查询性能
CREATE INDEX IF NOT EXISTS idx_hyperlink ON mind_map_node(hyperlink);
```

### 2. 执行数据库迁移
运行以下SQL脚本：
```bash
# 在MySQL中执行
source tool/src/main/resources/sql/mindmap_add_hyperlink.sql
```

## 代码更改

### 1. 实体类更新
- `MindMapNode.java` - 添加了 `hyperlink` 字段及其getter/setter方法

### 2. 控制器更新
- `MindMapController.java` - 在保存接口中添加了超链接字段的处理和验证

### 3. 数据访问层更新
- `MindMapMapper.java` - 在所有SQL语句中添加了超链接字段

## 功能特性

### 1. 超链接设置
- 在属性面板中可以设置节点的超链接地址
- 支持多种URL格式：http://, https://, ftp://, mailto:, tel:, #
- 自动验证URL格式，无效格式会被忽略

### 2. 超链接按钮
- 有超链接的节点会在操作按钮区域显示🌐图标
- 点击按钮会在新标签页中打开超链接
- 按钮使用橙色样式，与超链接主题保持一致

### 3. 视觉标识
- 有超链接的节点边框会变成橙色
- 添加了特殊的阴影效果来区分有超链接的节点

## 部署步骤

1. **停止应用服务**
2. **备份数据库**
3. **执行数据库迁移脚本**
4. **更新代码文件**
5. **重启应用服务**

## 注意事项

- 超链接字段为可选字段，不会影响现有数据
- URL验证相对宽松，支持常见的协议格式
- 超链接长度限制为500字符
- 建议在生产环境部署前在测试环境验证功能

## 兼容性

- 完全向后兼容，现有思维导图数据不受影响
- 新增的超链接功能为可选功能
- 前端会自动处理超链接字段的显示和隐藏
