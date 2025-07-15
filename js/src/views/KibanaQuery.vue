<template>
  <div class="kibana-query">
    <h1 class="title">Elasticsearch 查询工具</h1>
    
    <div class="query-container">
      <!-- 左侧查询配置面板 -->
      <div class="query-panel">
        <div class="panel-section">
          <h3>索引配置</h3>
          <div class="form-group">
            <label>索引名称：</label>
            <input 
              type="text" 
              v-model="queryConfig.indexName" 
              placeholder="例如: logs-*"
              class="form-input"
            >
          </div>
        </div>

        <div class="panel-section">
          <h3>过滤条件</h3>
          <div class="filter-list">
            <div 
              v-for="(filter, index) in queryConfig.filters" 
              :key="index" 
              class="filter-item"
            >
              <div class="filter-header">
                <span>过滤条件 {{ index + 1 }}</span>
                <button @click="removeFilter(index)" class="remove-btn">删除</button>
              </div>
              <div class="filter-content">
                <select 
                  v-model="filter.field" 
                  class="form-select small"
                  placeholder="选择字段"
                >
                  <option value="">请选择字段</option>
                  <option 
                    v-for="field in availableFilterFields" 
                    :key="field" 
                    :value="field"
                  >
                    {{ field }}
                  </option>
                </select>
                <select v-model="filter.operator" class="form-select small">
                  <option value="equals">等于</option>
                  <option value="contains">包含</option>
                  <option value="gt">大于</option>
                  <option value="lt">小于</option>
                  <option value="gte">大于等于</option>
                  <option value="lte">小于等于</option>
                </select>
                <input 
                  v-model="filter.value" 
                  placeholder="值"
                  class="form-input small"
                >
              </div>
            </div>
            <button @click="addFilter" class="add-btn">添加过滤条件</button>
          </div>
        </div>

        <div class="panel-section">
          <h3>聚合配置</h3>
          <div class="aggregation-list">
            <div 
              v-for="(agg, index) in queryConfig.aggregations" 
              :key="index" 
              class="aggregation-item"
            >
              <div class="agg-header">
                <span>聚合 {{ index + 1 }}</span>
                <button @click="removeAggregation(index)" class="remove-btn">删除</button>
              </div>
              <div class="agg-content">
                <input 
                  v-model="agg.name" 
                  placeholder="聚合名称"
                  class="form-input small"
                >
                <select v-model="agg.type" class="form-select small">
                  <option value="terms">Terms</option>
                  <option value="avg">Average</option>
                  <option value="sum">Sum</option>
                  <option value="count">Count</option>
                  <option value="max">Max</option>
                  <option value="min">Min</option>
                </select>
                <div class="field-checkboxes">
                  <label class="checkbox-label">选择聚合字段：</label>
                  <div class="checkbox-group">
                    <label 
                      v-for="field in availableAggregationFields" 
                      :key="field" 
                      class="checkbox-item"
                    >
                      <input 
                        type="checkbox" 
                        :value="field"
                        v-model="agg.fields"
                        @change="updateAggregationField(agg)"
                      >
                      <span>{{ field }}</span>
                    </label>
                  </div>
                </div>
              </div>
            </div>
            <button @click="addAggregation" class="add-btn">添加聚合</button>
          </div>
        </div>

        <div class="query-actions">
          <button @click="executeQuery" class="execute-btn" :disabled="loading">
            {{ loading ? '查询中...' : '执行查询' }}
          </button>
          <button @click="clearQuery" class="clear-btn">清空查询</button>
        </div>
      </div>

      <!-- 右侧结果展示区域 -->
      <div class="result-panel">
        <div class="result-header">
          <h3>查询结果</h3>
          <div class="result-info">
            <span v-if="queryResult.total">总数: {{ queryResult.total }}</span>
            <span v-if="queryResult.took">耗时: {{ queryResult.took }}ms</span>
          </div>
        </div>
        
        <div class="result-tabs">
          <button 
            @click="activeTab = 'json'" 
            :class="['tab-btn', { active: activeTab === 'json' }]"
          >
            JSON结果
          </button>
          <button 
            @click="activeTab = 'table'" 
            :class="['tab-btn', { active: activeTab === 'table' }]"
          >
            表格视图
          </button>
        </div>

        <div class="result-content">
          <!-- JSON结果视图 -->
          <div v-if="activeTab === 'json'" class="json-view">
            <pre v-if="queryResult.data" class="json-display">{{ formatJson(queryResult.data) }}</pre>
            <div v-else class="no-data">暂无数据，请执行查询</div>
          </div>

          <!-- 表格视图 -->
          <div v-if="activeTab === 'table'" class="table-view">
            <table v-if="queryResult.hits && queryResult.hits.length > 0" class="data-table">
              <thead>
                <tr>
                  <th v-for="field in tableFields" :key="field">{{ field }}</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(hit, index) in queryResult.hits" :key="index">
                  <td v-for="field in tableFields" :key="field">
                    {{ formatFieldValue(hit._source[field]) }}
                  </td>
                </tr>
              </tbody>
            </table>
            <div v-else class="no-data">暂无数据，请执行查询</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, watch } from 'vue'

export default {
  name: 'KibanaQuery',
  setup() {
    var activeTab = ref('json');
    var loading = ref(false);
    var availableFilterFields = ref([]);
    var availableAggregationFields = ref([]);
    var tableFields = ref([]);
    
    var queryConfig = reactive({
      indexName: '',
      filters: [],
      aggregations: []
    });
    
    var queryResult = reactive({
      data: null,
      total: 0,
      took: 0,
      hits: []
    });

    // 获取可用字段
    async function getAvailableFields() {
      if (!queryConfig.indexName) {
        return;
      }
      
      try {
        var response = await fetch('/api/elasticsearch/fields?index=' + queryConfig.indexName);
        if (response.ok) {
          var data = await response.json();
          availableFilterFields.value = data.filterFields || [];
          availableAggregationFields.value = data.aggregationFields || [];
        } else {
          // 如果API返回错误，使用模拟数据
          loadMockFields();
        }
      } catch (error) {
        console.error('获取字段失败:', error);
        // 如果API不可用，使用模拟数据
        loadMockFields();
      }
    }
    
    // 加载模拟字段数据
    function loadMockFields() {
      availableFilterFields.value = [
        'timestamp',
        'level',
        'message',
        'service',
        'user_id',
        'ip',
        'status_code',
        'response_time'
      ];
      availableAggregationFields.value = [
        'level',
        'service',
        'status_code',
        'user_id'
      ];
    }
    
    function addFilter() {
      queryConfig.filters.push({
        field: '',
        operator: 'equals',
        value: ''
      });
    }
    
    function removeFilter(index) {
      queryConfig.filters.splice(index, 1);
    }
    
    function addAggregation() {
      queryConfig.aggregations.push({
        name: '',
        type: 'terms',
        fields: [],
        field: '' // 用于存储选中的字段
      });
    }
    
    function removeAggregation(index) {
      queryConfig.aggregations.splice(index, 1);
    }
    
    function updateAggregationField(agg) {
      // 当复选框改变时，更新聚合的字段
      if (agg.fields && agg.fields.length > 0) {
        agg.field = agg.fields[0]; // 取第一个选中的字段
      } else {
        agg.field = '';
      }
    }
    
    async function executeQuery() {
      if (!queryConfig.indexName) {
        alert('请输入索引名称');
        return;
      }

      loading.value = true;
      
      try {
        // 构建请求数据
        var requestData = {
          index: queryConfig.indexName,
          filters: queryConfig.filters.filter(function(f) { return f.field && f.value; }),
          aggregations: queryConfig.aggregations.filter(function(a) { return a.name && a.field; })
        };

        console.log('发送请求数据:', requestData);
        
        // 调用后端接口
        var response = await callBackendAPI(requestData);
        
        // 处理返回结果
        handleQueryResult(response);
        
      } catch (error) {
        console.error('查询失败:', error);
        alert('查询失败: ' + error.message);
      } finally {
        loading.value = false;
      }
    }
    
    async function callBackendAPI(requestData) {
      // 调用真实的后端API
      console.log('调用后端API，请求数据:', requestData);
      
      var apiUrl = '/api/elasticsearch/query';
      
      var response = await fetch(apiUrl, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestData)
      });
      
      if (!response.ok) {
        throw new Error('HTTP error! status: ' + response.status);
      }
      
      return await response.json();
    }
    
    function handleQueryResult(response) {
      // 处理后端返回的数据
      Object.assign(queryResult, {
        data: response,
        total: (response.hits && response.hits.total && response.hits.total.value) || 0,
        took: response.took || 0,
        hits: (response.hits && response.hits.hits) || []
      });
      
      // 更新表格字段
      if (queryResult.hits.length > 0) {
        tableFields.value = Object.keys(queryResult.hits[0]._source || {});
      }
    }
    
    function clearQuery() {
      Object.assign(queryConfig, {
        indexName: '',
        filters: [],
        aggregations: []
      });
      Object.assign(queryResult, {
        data: null,
        total: 0,
        took: 0,
        hits: []
      });
      tableFields.value = [];
    }
    
    function formatJson(obj) {
      return JSON.stringify(obj, null, 2);
    }
    
    function formatFieldValue(value) {
      if (value === null || value === undefined) {
        return '-';
      }
      if (typeof value === 'object') {
        return JSON.stringify(value);
      }
      return String(value);
    }

    // 监听索引名称变化
    watch(function() { return queryConfig.indexName; }, function(newIndexName) {
      if (newIndexName) {
        getAvailableFields();
      } else {
        availableFilterFields.value = [];
        availableAggregationFields.value = [];
      }
    });

    return {
      activeTab: activeTab,
      loading: loading,
      availableFilterFields: availableFilterFields,
      availableAggregationFields: availableAggregationFields,
      queryConfig: queryConfig,
      queryResult: queryResult,
      tableFields: tableFields,
      getAvailableFields: getAvailableFields,
      loadMockFields: loadMockFields,
      addFilter: addFilter,
      removeFilter: removeFilter,
      addAggregation: addAggregation,
      removeAggregation: removeAggregation,
      updateAggregationField: updateAggregationField,
      executeQuery: executeQuery,
      callBackendAPI: callBackendAPI,
      handleQueryResult: handleQueryResult,
      clearQuery: clearQuery,
      formatJson: formatJson,
      formatFieldValue: formatFieldValue
    }
  }
}
</script>

<style scoped>
.kibana-query {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.title {
  text-align: center;
  font-size: 2rem;
  color: #2c3e50;
  margin: 1rem 0;
  padding: 0 2rem;
}

.query-container {
  display: flex;
  flex: 1;
  gap: 1rem;
  padding: 0 2rem 2rem;
  min-height: 0;
}

.query-panel {
  width: 400px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  padding: 1.5rem;
  overflow-y: auto;
}

.result-panel {
  flex: 1;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  display: flex;
  flex-direction: column;
}

.panel-section {
  margin-bottom: 2rem;
  padding-bottom: 1.5rem;
  border-bottom: 1px solid #eee;
}

.panel-section:last-child {
  border-bottom: none;
}

.panel-section h3 {
  color: #42b883;
  margin-bottom: 1rem;
  font-size: 1.1rem;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #2c3e50;
  font-weight: 500;
}

.form-input, .form-select {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.9rem;
}

.form-input.small, .form-select.small {
  width: calc(50% - 0.25rem);
  margin-right: 0.5rem;
}

.filter-item, .aggregation-item {
  background: #f8f9fa;
  padding: 1rem;
  border-radius: 4px;
  margin-bottom: 1rem;
}

.filter-header, .agg-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #2c3e50;
}

.filter-content, .agg-content {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.field-checkboxes {
  width: 100%;
  margin-top: 0.5rem;
}

.checkbox-label {
  display: block;
  margin-bottom: 0.5rem;
  color: #2c3e50;
  font-weight: 500;
  font-size: 0.9rem;
}

.checkbox-group {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.checkbox-item {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.85rem;
  color: #2c3e50;
  cursor: pointer;
}

.checkbox-item input[type="checkbox"] {
  margin: 0;
  cursor: pointer;
}

.remove-btn {
  background: #e74c3c;
  color: white;
  border: none;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.8rem;
  cursor: pointer;
}

.add-btn {
  background: #42b883;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
}

.query-actions {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
}

.execute-btn, .clear-btn {
  flex: 1;
  padding: 0.75rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
}

.execute-btn {
  background: #42b883;
  color: white;
}

.execute-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.clear-btn {
  background: #6c757d;
  color: white;
}

.result-header {
  padding: 1.5rem;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.result-header h3 {
  color: #2c3e50;
  margin: 0;
}

.result-info {
  display: flex;
  gap: 1rem;
  font-size: 0.9rem;
  color: #7f8c8d;
}

.result-tabs {
  display: flex;
  border-bottom: 1px solid #eee;
}

.tab-btn {
  padding: 1rem 2rem;
  border: none;
  background: none;
  cursor: pointer;
  border-bottom: 2px solid transparent;
  color: #7f8c8d;
  font-weight: 500;
}

.tab-btn.active {
  color: #42b883;
  border-bottom-color: #42b883;
}

.result-content {
  flex: 1;
  overflow: auto;
  padding: 1.5rem;
}

.json-display {
  background: #f8f9fa;
  padding: 1rem;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 0.9rem;
  line-height: 1.4;
  overflow-x: auto;
  white-space: pre-wrap;
}

.no-data {
  text-align: center;
  color: #7f8c8d;
  padding: 2rem;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.9rem;
}

.data-table th,
.data-table td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.data-table th {
  background: #f8f9fa;
  font-weight: 600;
  color: #2c3e50;
}

.data-table tr:hover {
  background: #f8f9fa;
}

@media (max-width: 1200px) {
  .query-container {
    flex-direction: column;
  }
  
  .query-panel {
    width: 100%;
  }
}
</style> 