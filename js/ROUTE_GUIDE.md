# 路由配置指南

## 概述

本项目使用Vue Router进行路由管理，所有路由配置都集中在 `src/router/routes.js` 文件中，便于维护和管理。

## 路由结构

### 主要路由

| 路径 | 名称 | 图标 | 描述 |
|------|------|------|------|
| `/` | Home | 🏠 | 企业知识管理系统首页 |
| `/gallery` | Gallery | 🤖 | AI问答记录管理 |
| `/code-gallery` | CodeGallery | 💻 | 代码片段管理 |
| `/video-gallery` | VideoGallery | 🎥 | 视频文件管理 |
| `/kibana-query` | KibanaQuery | 🔍 | Elasticsearch查询工具 |
| `/es-field-search` | ESFieldSearch | 📊 | ES字段搜索工具 |
| `/query-list` | QueryList | 📋 | 查询历史记录 |
| `/mind-map` | MindMap | 🧠 | 思维导图工具 |
| `/introduction` | Introduction | 📖 | 系统使用说明 |
| `/route-test` | RouteTest | 🚀 | 路由测试页面 |

## 路由配置

### 路由对象结构

```javascript
{
  path: '/code-gallery',           // 路由路径
  name: 'CodeGallery',             // 路由名称
  component: CodeGallery,          // 组件
  meta: {
    title: '代码片段管理',          // 页面标题
    icon: '💻',                    // 图标
    description: '代码片段存储与分享' // 描述
  }
}
```

### 路由元信息

每个路由都包含 `meta` 信息，用于：
- 页面标题显示
- 导航菜单图标
- 功能描述
- 权限控制（可扩展）

## 导航组件

### 主导航栏 (App.vue)

主应用中的导航栏包含所有主要功能的链接：

```vue
<router-link to="/code-gallery" class="nav-link" active-class="active">
  <span class="nav-icon">💻</span>
  代码片段管理
</router-link>
```

### 主页卡片导航 (Home.vue)

主页使用卡片式布局展示所有功能：

```vue
<div class="action-card" v-for="action in actions">
  <div class="action-icon">{{ action.icon }}</div>
  <h3>{{ action.title }}</h3>
  <p>{{ action.description }}</p>
  <router-link :to="action.path" class="action-btn">开始使用</router-link>
</div>
```

## 路由工具函数

### getNavigationMenu()

获取所有路由的导航菜单数据：

```javascript
import { getNavigationMenu } from '../router/routes'

const menuItems = getNavigationMenu()
// 返回包含所有路由信息的数组
```

### getRouteByPath(path)

根据路径获取路由信息：

```javascript
import { getRouteByPath } from '../router/routes'

const routeInfo = getRouteByPath('/code-gallery')
// 返回路由对象或undefined
```

### getRouteByName(name)

根据名称获取路由信息：

```javascript
import { getRouteByName } from '../router/routes'

const routeInfo = getRouteByName('CodeGallery')
// 返回路由对象或undefined
```

## 添加新路由

### 1. 创建组件

在 `src/views/` 目录下创建新的Vue组件：

```vue
<template>
  <div class="new-page">
    <h1>新页面</h1>
  </div>
</template>

<script>
export default {
  name: 'NewPage'
}
</script>
```

### 2. 导入组件

在 `src/router/routes.js` 中导入新组件：

```javascript
import NewPage from '../views/NewPage.vue'
```

### 3. 添加路由配置

在 `routes` 数组中添加新路由：

```javascript
{
  path: '/new-page',
  name: 'NewPage',
  component: NewPage,
  meta: {
    title: '新页面',
    icon: '🆕',
    description: '新功能页面'
  }
}
```

### 4. 更新导航

新路由会自动出现在：
- 主页的卡片导航中
- 路由测试页面中

## 路由测试

### 访问测试页面

访问 `/route-test` 页面可以：
- 查看所有可用路由
- 测试路由跳转
- 验证路由配置

### 测试功能

- **访问页面**: 直接跳转到对应页面
- **测试路由**: 模拟路由跳转并显示结果
- **导航测试**: 快速切换不同页面

## 路由守卫

### 全局前置守卫

可以在 `src/router/index.js` 中添加全局路由守卫：

```javascript
router.beforeEach((to, from, next) => {
  // 路由跳转前的逻辑
  console.log(`从 ${from.path} 跳转到 ${to.path}`)
  next()
})
```

### 路由元信息守卫

基于路由元信息进行权限控制：

```javascript
router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth && !isAuthenticated()) {
    next('/login')
  } else {
    next()
  }
})
```

## 最佳实践

### 1. 路由命名

- 使用PascalCase命名组件
- 路径使用kebab-case
- 保持命名的一致性

### 2. 组件懒加载

对于大型应用，可以使用懒加载：

```javascript
{
  path: '/code-gallery',
  name: 'CodeGallery',
  component: () => import('../views/CodeGallery.vue'),
  meta: {
    title: '代码片段管理',
    icon: '💻',
    description: '代码片段存储与分享'
  }
}
```

### 3. 嵌套路由

对于复杂页面，可以使用嵌套路由：

```javascript
{
  path: '/admin',
  name: 'Admin',
  component: AdminLayout,
  children: [
    {
      path: 'users',
      name: 'AdminUsers',
      component: AdminUsers
    }
  ]
}
```

### 4. 路由参数

使用动态路由参数：

```javascript
{
  path: '/user/:id',
  name: 'UserDetail',
  component: UserDetail
}
```

## 故障排除

### 常见问题

1. **路由不生效**
   - 检查组件是否正确导入
   - 确认路由配置语法正确
   - 验证路径拼写

2. **组件不显示**
   - 检查组件名称是否正确
   - 确认组件文件存在
   - 验证导入路径

3. **导航链接不工作**
   - 检查 `router-link` 语法
   - 确认路径正确
   - 验证路由已注册

### 调试技巧

1. 使用路由测试页面验证配置
2. 检查浏览器控制台错误信息
3. 使用Vue DevTools查看路由状态
4. 添加路由守卫进行调试

## 扩展功能

### 权限控制

可以基于路由元信息实现权限控制：

```javascript
{
  path: '/admin',
  name: 'Admin',
  component: Admin,
  meta: {
    requiresAuth: true,
    roles: ['admin']
  }
}
```

### 面包屑导航

基于路由信息生成面包屑：

```javascript
function generateBreadcrumbs(route) {
  return route.matched.map(item => ({
    title: item.meta.title,
    path: item.path
  }))
}
```

### 页面标题

自动设置页面标题：

```javascript
router.afterEach((to) => {
  document.title = to.meta.title || '企业知识管理系统'
})
``` 