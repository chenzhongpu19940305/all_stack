# SearchInput 搜索组件

一个基于Vue3组合式API的现代化搜索输入组件，具有放大镜图标、防抖搜索、清空功能等特性。

## 特性

- 🎯 **Vue3组合式API** - 使用最新的Vue3语法
- 🔍 **放大镜图标** - 左侧内置搜索图标
- ⚡ **防抖搜索** - 可配置的防抖时间，避免频繁请求
- 🧹 **清空功能** - 一键清空搜索内容
- 📱 **响应式设计** - 支持移动端适配
- 🌙 **深色模式** - 自动适配系统深色模式
- 🎨 **现代化UI** - 美观的界面设计
- ⌨️ **键盘支持** - 支持回车键搜索

## 基础用法

```vue
<template>
  <SearchInput 
    v-model="searchValue"
    placeholder="请输入搜索内容..."
    @search="handleSearch"
  />
</template>

<script setup>
import { ref } from 'vue'
import SearchInput from './SearchInput.vue'

const searchValue = ref('')

const handleSearch = (value) => {
  console.log('搜索:', value)
  // 执行搜索逻辑
}
</script>
```

## Props

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `modelValue` | String | `''` | 搜索框的值，支持v-model |
| `placeholder` | String | `'搜索...'` | 占位符文本 |
| `debounceTime` | Number | `300` | 防抖时间（毫秒） |
| `autoSearch` | Boolean | `true` | 是否自动搜索（输入时触发） |

## Events

| 事件名 | 参数 | 说明 |
|--------|------|------|
| `update:modelValue` | `(value: string)` | 值更新时触发 |
| `search` | `(value: string)` | 搜索时触发 |
| `input` | `(value: string)` | 输入时触发 |
| `focus` | - | 获得焦点时触发 |
| `blur` | - | 失去焦点时触发 |
| `clear` | - | 清空时触发 |

## 方法

通过ref可以调用以下方法：

| 方法名 | 说明 |
|--------|------|
| `focus()` | 聚焦到输入框 |
| `blur()` | 失焦 |
| `clearSearch()` | 清空搜索内容 |

```vue
<template>
  <SearchInput ref="searchRef" v-model="searchValue" />
  <button @click="focusSearch">聚焦</button>
  <button @click="clearSearch">清空</button>
</template>

<script setup>
import { ref } from 'vue'
import SearchInput from './SearchInput.vue'

const searchRef = ref(null)
const searchValue = ref('')

const focusSearch = () => {
  searchRef.value?.focus()
}

const clearSearch = () => {
  searchRef.value?.clearSearch()
}
</script>
```

## 高级用法

### 自定义防抖时间

```vue
<SearchInput 
  v-model="searchValue"
  :debounce-time="500"
  @search="handleSearch"
/>
```

### 手动触发搜索

```vue
<SearchInput 
  v-model="searchValue"
  :auto-search="false"
  @search="handleSearch"
/>
```

### 监听所有事件

```vue
<SearchInput 
  v-model="searchValue"
  @input="handleInput"
  @focus="handleFocus"
  @blur="handleBlur"
  @clear="handleClear"
  @search="handleSearch"
/>
```

## 样式定制

组件使用scoped样式，可以通过CSS变量或覆盖样式来自定义外观：

```css
/* 自定义搜索框样式 */
.search-input-wrapper {
  border-radius: 20px; /* 圆角 */
  background: #f8f9fa; /* 背景色 */
}

/* 自定义图标颜色 */
.search-icon {
  color: #007bff;
}

/* 自定义输入框样式 */
.search-input {
  font-size: 16px;
  color: #333;
}
```

## 深色模式

组件自动支持深色模式，会根据系统设置自动切换样式。

## 浏览器兼容性

- Chrome >= 88
- Firefox >= 78
- Safari >= 14
- Edge >= 88

## 注意事项

1. 组件依赖Vue3，请确保项目使用Vue3版本
2. 防抖功能使用setTimeout实现，在组件销毁时会自动清理
3. 移动端会自动调整字体大小，防止iOS缩放
4. 组件使用flexbox布局，确保父容器有足够的宽度

## 示例

查看 `SearchInputExample.vue` 文件获取完整的使用示例。 