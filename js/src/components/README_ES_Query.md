# Elasticsearch查询组件使用说明

## 组件功能
`ElasticsearchQuery.vue` 是一个用于查询Elasticsearch字段和值的Vue组件，支持输入key和value，并将数据传递给后端。

## 使用方法

### 1. 导入组件
```javascript
import ElasticsearchQuery from './ElasticsearchQuery.vue';

export default {
  components: {
    ElasticsearchQuery: ElasticsearchQuery
  }
}
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

### 3. 处理事件
```javascript
methods: {
  // 处理查询事件
  handleQuery: function(queryData) {
    console.log('查询数据:', queryData);
    // queryData 格式: { key: "字段名", value: "字段值" }
    
    // 发送到后端
    this.sendToBackend(queryData);
  },
  
  // 处理清空事件
  handleClear: function() {
    console.log('查询已清空');
  },
  
  // 发送到后端的方法
  sendToBackend: function(data) {
    fetch('/api/es-query', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
    .then(function(response) {
      return response.json();
    })
    .then(function(result) {
      console.log('后端响应:', result);
    })
    .catch(function(error) {
      console.error('发送失败:', error);
    });
  }
}
```

## 组件API

### 事件 (Events)
- `@query`: 当用户点击查询按钮时触发
  - 参数: `{ key: "字段名", value: "字段值" }`
- `@clear`: 当用户点击清空按钮时触发

### 方法 (Methods)
通过 `ref` 可以调用以下方法：

```javascript
// 获取当前查询数据
var queryData = this.$refs.esQueryComponent.getQueryData();

// 设置查询数据
this.$refs.esQueryComponent.setQueryData('title', '示例值');
```

### 数据格式
组件输出的数据格式为：
```javascript
{
  key: "查询字段名",
  value: "查询字段值"
}
```

## 样式定制
组件使用scoped样式，可以通过CSS变量或覆盖样式来自定义外观。

## 注意事项
1. 组件使用ES5语法编写，兼容性良好
2. 查询按钮只有在key和value都填写时才能点击
3. 组件会自动验证输入，确保数据完整性
4. 实际使用时需要根据后端API调整数据发送方式 