# ESFieldSearchInput 组件说明文档

## 组件概述

ESFieldSearchInput 是一个功能丰富的 Vue 3 搜索输入组件，专为 Elasticsearch 字段搜索设计。该组件支持字段选择、值过滤、自定义值输入、日期时间范围选择等功能，提供了灵活且用户友好的搜索体验。

## 主要功能

1. **字段搜索与选择**：用户可以输入关键词筛选可用字段，并从下拉列表中选择目标字段。
2. **值选择**：选择字段后，显示该字段的可选值列表，支持多选。
3. **自定义值输入**：允许用户为选定字段添加自定义值。
4. **日期时间范围选择**：对于日期/时间类型的字段，提供专门的日期时间范围选择器。
5. **已选值标签展示**：以标签形式展示已选择的字段和值，支持单个删除。
6. **响应式设计**：适配不同屏幕尺寸。
7. **深色模式支持**：自动适应系统深色模式设置。

## 组件属性 (Props)

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| placeholder | String | '搜索ES字段...' | 搜索输入框的占位文本 |
| modelValue | Array | [] | 组件的值，用于 v-model 双向绑定 |
| debounceTime | Number | 300 | 搜索防抖时间（毫秒） |
| availableFields | Array | [...] | 可用的字段列表 |
| fieldValues | Object | {...} | 字段对应的预定义值映射 |

## 组件事件 (Events)

| 事件名 | 参数 | 说明 |
|--------|------|------|
| update:modelValue | Array | 当选中值变化时触发，用于 v-model 双向绑定 |
| search | Array/String | 当搜索条件变化或用户主动搜索时触发 |
| input | String | 当输入值变化时触发 |
| focus | - | 当输入框获得焦点时触发 |
| blur | - | 当输入框失去焦点时触发 |
| clear | - | 当清空搜索时触发 |

## 组件方法

### 外部可调用方法

以下方法通过 `defineExpose` 暴露给父组件：

| 方法名 | 参数 | 返回值 | 说明 |
|--------|------|--------|------|
| focus | - | - | 使输入框获得焦点 |
| blur | - | - | 使输入框失去焦点 |
| clearSearch | - | - | 清空搜索内容 |

### 内部方法

#### 输入处理相关

| 方法名 | 说明 |
|--------|------|
| handleInput | 处理输入变化，触发字段过滤和搜索事件 |
| handleFocus | 处理输入框获得焦点，显示字段下拉框 |
| handleFieldBlur | 处理输入框失去焦点，延迟隐藏下拉框 |
| filterFields | 根据输入值过滤可用字段列表 |

#### 字段和值选择相关

| 方法名 | 说明 |
|--------|------|
| selectField | 选择字段，显示该字段的值下拉框 |
| isValueSelected | 检查值是否已被选中 |
| toggleValue | 切换值的选中状态 |
| applySelectedValues | 应用选中的值，更新组件值并触发事件 |

#### 自定义值相关

| 方法名 | 说明 |
|--------|------|
| addCustomValue | 添加自定义值到列表并自动选中 |
| removeCustomValue | 从自定义值列表中删除指定值 |
| addDateTimeRange | 添加日期时间范围到自定义值列表 |

#### 日期时间处理

| 方法名 | 说明 |
|--------|------|
| isDateTimeField | 判断字段是否为日期或时间类型 |
| formatDateTime | 格式化日期时间为 yyyy-MM-dd HH:mm:ss 格式 |

#### 其他功能方法

| 方法名 | 说明 |
|--------|------|
| closeValueDropdown | 关闭值下拉框并重置相关状态 |
| removeSelectedValue | 移除已选择的值 |
| clearSearch | 清空搜索内容并重置状态 |

## 使用示例

```vue
<template>
  <div>
    <ESFieldSearchInput
      v-model="searchConditions"
      placeholder="搜索字段..."
      :available-fields="myFields"
      :field-values="myFieldValues"
      @search="handleSearch"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import ESFieldSearchInput from '@/components/ESFieldSearchInput.vue'

const searchConditions = ref([])
const myFields = ['title', 'author', 'category', 'created_at']
const myFieldValues = {
  title: ['首页', '关于我们', '产品介绍'],
  author: ['张三', '李四', '王五'],
  category: ['前端', '后端', '全栈']
}

const handleSearch = (conditions) => {
  console.log('搜索条件:', conditions)
  // 执行搜索逻辑
}
</script>
```

## 样式定制

组件提供了丰富的样式，包括：

1. 搜索输入框样式
2. 下拉菜单样式
3. 字段选项样式
4. 值选择框样式
5. 自定义值输入区域样式
6. 已选值标签样式
7. 响应式设计适配
8. 深色模式支持

可以通过覆盖组件的 CSS 变量或类选择器来自定义样式。

## 注意事项

1. 组件使用 Vue 3 的 Composition API 和 `<script setup>` 语法。
2. 组件内部使用 `defineOptions` 定义组件名称，确保在开发工具中能正确识别。
3. 日期时间字段的识别基于字段名中是否包含 'date'、'time' 或 'at' 关键词。
4. 组件支持键盘操作，如按 Enter 键添加自定义值。
5. 组件使用防抖处理搜索事件，避免频繁触发。