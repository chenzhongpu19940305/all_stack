# 查询详情抽屉柜功能

## 功能概述

已将QueryDetail组件从模态框改为抽屉柜形式，提供更好的用户体验。

## 主要改进

### 1. 界面布局优化
- **抽屉柜设计**: 从右侧滑入的抽屉柜，占据屏幕90%宽度，最大600px
- **全屏高度**: 抽屉柜占据整个屏幕高度，提供更大的显示空间
- **平滑动画**: 添加了淡入和滑入动画效果

### 2. 交互体验提升
- **点击遮罩关闭**: 点击抽屉柜外的遮罩区域可关闭抽屉柜
- **ESC键关闭**: 支持键盘ESC键关闭抽屉柜
- **响应式设计**: 在移动设备上抽屉柜占据全屏宽度

### 3. 内容展示优化
- **信息展示**: 在抽屉柜顶部显示查询的基本信息（应用ID、请求路径、日期、状态）
- **ES搜索**: 集成Elasticsearch字段搜索功能
- **结果展示**: 搜索结果以卡片形式展示，支持滚动查看

## 技术实现

### Vue3组合式API
```javascript
// 响应式数据
const showDrawer = ref(false)
const selectedRecord = ref({})

// 方法
const openDrawer = (record) => {
  selectedRecord.value = record
  showDrawer.value = true
}

const closeDrawer = () => {
  showDrawer.value = false
  selectedRecord.value = {}
}
```

### CSS动画效果
```css
/* 淡入动画 */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

/* 滑入动画 */
@keyframes slideIn {
  from { transform: translateX(100%); }
  to { transform: translateX(0); }
}
```

## 使用方法

1. 在查询列表页面点击"查看详情"按钮
2. 抽屉柜从右侧滑入，显示查询详情
3. 可以查看基本信息、进行ES字段搜索
4. 点击关闭按钮或遮罩区域关闭抽屉柜

## 文件结构

- `js/src/views/QueryList.vue` - 主列表页面，包含抽屉柜逻辑
- `js/src/components/QueryDetail.vue` - 详情组件，优化了抽屉柜内的显示效果
- `js/src/components/ESFieldSearchInput.vue` - ES字段搜索组件

## 样式特点

- 现代化的UI设计
- 响应式布局
- 平滑的动画过渡
- 良好的可访问性
- 移动端友好的交互

## 浏览器兼容性

- Chrome 60+
- Firefox 55+
- Safari 12+
- Edge 79+

## 性能优化

- 使用Vue3的响应式系统
- CSS动画使用transform和opacity
- 组件懒加载
- 内存管理优化 