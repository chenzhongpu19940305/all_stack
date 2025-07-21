<template>
  <div class="example-container">
    <h2>ES查询组件使用示例</h2>
    
    <!-- 使用ES查询组件 -->
    <ElasticsearchQuery 
      @query="handleEsQuery"
      @clear="handleClear"
      ref="esQueryComponent"
    />
    
    <!-- 显示接收到的数据 -->
    <div v-if="receivedData" class="received-data">
      <h3>接收到的查询数据:</h3>
      <div class="data-display">
        <p><strong>查询条件数量:</strong> {{ receivedData.conditions.length }}</p>
        <div v-for="(condition, index) in receivedData.conditions" :key="index" class="condition-item">
          <p><strong>字段 {{ index + 1 }}:</strong> {{ condition.field }}</p>
          <p><strong>值 {{ index + 1 }}:</strong> {{ condition.value }}</p>
        </div>
      </div>
      
      <div class="action-buttons">
        <button @click="sendToBackend" class="send-btn">
          发送到后端
        </button>
        <button @click="setExampleData" class="example-btn">
          设置示例数据
        </button>
        <button @click="getCurrentData" class="get-btn">
          获取当前数据
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import ElasticsearchQuery from './ElasticsearchQuery.vue'

// 定义组件名称
defineOptions({
  name: 'EsQueryExample'
})

// 响应式数据
const receivedData = ref(null)
const esQueryComponent = ref(null)

// 方法
const handleEsQuery = (queryData) => {
  console.log('接收到查询数据:', queryData)
  receivedData.value = queryData
  
  // 这里可以调用API发送到后端
  sendToBackend(queryData)
}

const handleClear = () => {
  receivedData.value = null
  console.log('查询已清空')
}

const sendToBackend = async (data) => {
  // 模拟发送到后端
  console.log('发送到后端的数据:', data)
  
  // 实际使用时，这里应该调用API
  // 例如：
  /*
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
  */
  
  const conditionsText = data.conditions.map(condition => 
    `${condition.field}: ${condition.value}`
  ).join('\n')
  
  alert('数据已发送到后端:\n' + conditionsText)
}

const setExampleData = () => {
  // 通过ref调用子组件方法，设置多个示例字段
  if (esQueryComponent.value) {
    esQueryComponent.value.setQueryData([
      { field: 'title', value: '示例标题' },
      { field: 'author', value: '示例作者' },
      { field: 'category', value: '技术文档' }
    ])
  }
}

const getCurrentData = () => {
  // 获取当前查询数据
  if (esQueryComponent.value) {
    const currentData = esQueryComponent.value.getQueryData()
    console.log('当前查询数据:', currentData)
    alert('当前查询数据:\n' + JSON.stringify(currentData, null, 2))
  }
}
</script>

<style scoped>
.example-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.example-container h2 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
}

.received-data {
  margin-top: 30px;
  padding: 20px;
  background: #e9ecef;
  border-radius: 8px;
}

.received-data h3 {
  margin-top: 0;
  color: #333;
}

.data-display {
  background: white;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 15px;
}

.data-display p {
  margin: 5px 0;
  font-size: 14px;
}

.condition-item {
  border-left: 3px solid #007bff;
  padding-left: 10px;
  margin: 10px 0;
  background: #f8f9fa;
  padding: 10px;
  border-radius: 4px;
}

.condition-item p {
  margin: 3px 0;
  font-size: 13px;
}

.action-buttons {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.send-btn, .example-btn, .get-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
  transition: background-color 0.3s;
}

.send-btn {
  background-color: #28a745;
  color: white;
}

.send-btn:hover {
  background-color: #218838;
}

.example-btn {
  background-color: #17a2b8;
  color: white;
}

.example-btn:hover {
  background-color: #138496;
}

.get-btn {
  background-color: #6c757d;
  color: white;
}

.get-btn:hover {
  background-color: #545b62;
}
</style> 