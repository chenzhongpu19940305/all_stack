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
        <p><strong>字段 (Key):</strong> {{ receivedData.key }}</p>
        <p><strong>值 (Value):</strong> {{ receivedData.value }}</p>
      </div>
      
      <div class="action-buttons">
        <button @click="sendToBackend" class="send-btn">
          发送到后端
        </button>
        <button @click="setExampleData" class="example-btn">
          设置示例数据
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import ElasticsearchQuery from './ElasticsearchQuery.vue';

export default {
  name: 'EsQueryExample',
  components: {
    ElasticsearchQuery: ElasticsearchQuery
  },
  data: function() {
    return {
      receivedData: null
    };
  },
  methods: {
    handleEsQuery: function(queryData) {
      console.log('接收到查询数据:', queryData);
      this.receivedData = queryData;
      
      // 这里可以调用API发送到后端
      this.sendToBackend(queryData);
    },
    
    handleClear: function() {
      this.receivedData = null;
      console.log('查询已清空');
    },
    
    sendToBackend: function(data) {
      var self = this;
      
      // 模拟发送到后端
      console.log('发送到后端的数据:', data);
      
      // 实际使用时，这里应该调用API
      // 例如：
      /*
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
      */
      
      alert('数据已发送到后端:\n字段: ' + data.key + '\n值: ' + data.value);
    },
    
    setExampleData: function() {
      // 通过ref调用子组件方法
      this.$refs.esQueryComponent.setQueryData('title', '示例标题');
    }
  }
};
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

.action-buttons {
  display: flex;
  gap: 10px;
}

.send-btn, .example-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
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
</style> 