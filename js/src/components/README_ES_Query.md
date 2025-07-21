# Elasticsearch查询组件使用说明

## 组件功能
`ElasticsearchQuery.vue` 是一个用于查询Elasticsearch多字段的Vue3组件，支持动态添加多个字段-值对，并将查询条件传递给后端。

## 技术特性
- 使用Vue3 Composition API和`<script setup>`语法糖
- 支持多字段查询条件
- 智能字段下拉提示
- 响应式数据管理
- 现代化UI设计

## 使用方法

### 1. 导入组件
```javascript
import ElasticsearchQuery from './ElasticsearchQuery.vue';

// Vue3 Composition API
export default {
  components: {
    ElasticsearchQuery
  }
}

// 或者使用<script setup>
import ElasticsearchQuery from './ElasticsearchQuery.vue';
```

### 2. 在模板中使用
```html
<template>
  <ElasticsearchQuery 
    @query="handleQuery"
    @clear="handleClear"
    ref="esQueryComponent"
  />
</template>
```

### 3. 处理事件 (Vue3 Composition API)
```javascript
import { ref } from 'vue'

// 在setup中定义
const esQueryComponent = ref(null)

const handleQuery = (queryData) => {
  console.log('查询数据:', queryData);
  // queryData 格式: { conditions: [{ field: "字段名", value: "字段值" }] }
  
  // 发送到后端
  sendToBackend(queryData);
}

const handleClear = () => {
  console.log('查询已清空');
}

const sendToBackend = async (data) => {
  try {
    const response = await fetch('/api/es-query', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    });
    const result = await response.json();
    console.log('后端响应:', result);
  } catch (error) {
    console.error('发送失败:', error);
  }
}
```

### 4. 传统Options API使用方式
```javascript
export default {
  methods: {
    // 处理查询事件
    handleQuery(queryData) {
      console.log('查询数据:', queryData);
      // queryData 格式: { conditions: [{ field: "字段名", value: "字段值" }] }
      
      // 发送到后端
      this.sendToBackend(queryData);
    },
    
    // 处理清空事件
    handleClear() {
      console.log('查询已清空');
    },
    
    // 发送到后端的方法
    async sendToBackend(data) {
      try {
        const response = await fetch('/api/es-query', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(data)
        });
        const result = await response.json();
        console.log('后端响应:', result);
      } catch (error) {
        console.error('发送失败:', error);
      }
    }
  }
}
```

## 组件API

### 事件 (Events)
- `@query`: 当用户点击查询按钮时触发
  - 参数: `{ conditions: [{ field: "字段名", value: "字段值" }] }`
- `@clear`: 当用户点击清空按钮时触发

### 方法 (Methods)
通过 `ref` 可以调用以下方法：

```javascript
// Vue3 Composition API
const esQueryComponent = ref(null)

// 获取当前查询数据
const queryData = esQueryComponent.value.getQueryData();

// 设置查询数据
esQueryComponent.value.setQueryData([
  { field: 'title', value: '示例值' },
  { field: 'author', value: '作者名' }
]);
```

```javascript
// 传统Options API
// 获取当前查询数据
const queryData = this.$refs.esQueryComponent.getQueryData();

// 设置查询数据
this.$refs.esQueryComponent.setQueryData([
  { field: 'title', value: '示例值' },
  { field: 'author', value: '作者名' }
]);
```

### 数据格式
组件输出的数据格式为：
```javascript
{
  conditions: [
    { field: "字段名1", value: "字段值1" },
    { field: "字段名2", value: "字段值2" },
    // ... 更多字段-值对
  ]
}
```

## 预定义字段列表
组件内置了常用的Elasticsearch字段，包括：
- `title`, `content`, `author`, `category`, `tags`
- `status`, `priority`, `created_at`, `updated_at`
- `user_id`, `email`, `phone`, `address`
- `company`, `department`, `level`, `message`
- `service`, `ip`, `status_code`, `response_time`
- `timestamp`, `log_level`, `error_code`
- `session_id`, `request_id`, `method`, `url`
- `user_agent`, `referer`, `client_ip`

## 功能特性

### 1. 智能字段提示
- 输入字段名时自动显示下拉提示
- 支持模糊匹配和筛选
- 最多显示10个匹配结果

### 2. 多字段管理
- 支持添加多个字段-值对
- 相同字段名会自动更新值
- 可以单独删除任意字段

### 3. 数据验证
- 字段名和值都不能为空
- 查询按钮只有在有查询条件时才能点击
- 自动验证数据完整性

### 4. 响应式设计
- 现代化的UI设计
- 良好的用户体验
- 支持键盘操作（回车键添加）

## 样式定制
组件使用scoped样式，可以通过CSS变量或覆盖样式来自定义外观。主要样式类：
- `.es-query-container`: 主容器
- `.query-form`: 查询表单
- `.field-input-container`: 字段输入容器
- `.field-dropdown`: 字段下拉框
- `.field-values-list`: 字段值列表
- `.button-group`: 按钮组

## 注意事项
1. 组件使用Vue3 Composition API编写，需要Vue3环境
2. 查询按钮只有在添加了查询条件时才能点击
3. 组件会自动验证输入，确保数据完整性
4. 实际使用时需要根据后端API调整数据发送方式
5. 支持ES6+语法，现代浏览器兼容性良好
6. 组件内部使用`ref`和`reactive`进行响应式数据管理 