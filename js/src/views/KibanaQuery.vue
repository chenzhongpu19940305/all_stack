<template>
  <div class="kibana-query">
    <h1 class="title">Elasticsearch 多字段查询工具</h1>
    
    <!-- 使用我们新创建的多字段ES查询组件 -->
    <ElasticsearchQuery 
      @query="handleEsQuery"
      @clear="handleClear"
      ref="esQueryComponent"
    />
    
    <!-- 显示查询结果 -->
    <div v-if="queryHistory.length > 0" class="query-history">
      <h3>查询历史记录:</h3>
      <div class="history-list">
        <div 
          v-for="(record, index) in queryHistory" 
          :key="index" 
          class="history-item"
        >
          <div class="history-header">
            <span class="history-time">{{ record.timestamp }}</span>
            <button @click="removeHistory(index)" class="remove-history-btn">删除</button>
          </div>
          <div class="history-data">
            <div v-for="(condition, condIndex) in record.data.conditions" :key="condIndex" class="condition-item">
              <span class="condition-field">{{ condition.field }}</span>
              <span class="condition-separator">:</span>
              <span class="condition-value">{{ condition.value }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ElasticsearchQuery from '../components/ElasticsearchQuery.vue';

export default {
  name: 'KibanaQuery',
  components: {
    ElasticsearchQuery: ElasticsearchQuery
  },
  data: function() {
    return {
      queryHistory: []
    };
  },
  methods: {
    handleEsQuery: function(queryData) {
      console.log('接收到ES多字段查询数据:', queryData);
      
      // 添加到查询历史
      var historyRecord = {
        timestamp: new Date().toLocaleString(),
        data: queryData
      };
      
      this.queryHistory.unshift(historyRecord);
      
      // 限制历史记录数量
      if (this.queryHistory.length > 10) {
        this.queryHistory = this.queryHistory.slice(0, 10);
      }
      
      // 发送到后端
      this.sendToBackend(queryData);
    },
    
    handleClear: function() {
      console.log('查询已清空');
    },
    
    sendToBackend: function(data) {
      var self = this;
      
      // 模拟发送到后端
      console.log('发送到后端的数据:', data);
      
      // 构建查询条件字符串
      var conditionsText = data.conditions.map(function(condition) {
        return condition.field + ': ' + condition.value;
      }).join(', ');
      
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
        alert('查询成功！');
      })
      .catch(function(error) {
        console.error('查询失败:', error);
        alert('查询失败，请重试');
      });
      */
      
      alert('多字段查询数据已发送到后端:\n' + conditionsText);
    },
    
    removeHistory: function(index) {
      this.queryHistory.splice(index, 1);
    },
    
    // 提供给外部调用的方法
    setQueryData: function(conditions) {
      this.$refs.esQueryComponent.setQueryData(conditions);
    },
    
    getQueryData: function() {
      return this.$refs.esQueryComponent.getQueryData();
    }
  }
};
</script>

<style scoped>
.kibana-query {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.title {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
  font-size: 2rem;
  font-weight: bold;
}

.query-history {
  margin-top: 40px;
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  border: 1px solid #dee2e6;
}

.query-history h3 {
  margin-top: 0;
  color: #333;
  margin-bottom: 20px;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.history-item {
  background: white;
  padding: 15px;
  border-radius: 6px;
  border: 1px solid #e9ecef;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding-bottom: 8px;
  border-bottom: 1px solid #eee;
}

.history-time {
  font-size: 12px;
  color: #666;
  font-weight: bold;
}

.remove-history-btn {
  background: #dc3545;
  color: white;
  border: none;
  padding: 4px 8px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  font-weight: bold;
}

.remove-history-btn:hover {
  background: #c82333;
}

.history-data {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.condition-item {
  display: flex;
  align-items: center;
  padding: 4px 0;
}

.condition-field {
  font-weight: bold;
  color: #007bff;
  margin-right: 5px;
}

.condition-separator {
  color: #666;
  margin: 0 5px;
}

.condition-value {
  color: #333;
  flex: 1;
}
</style> 