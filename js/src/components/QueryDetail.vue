<template>
  <div class="query-detail-container">
    <div class="detail-header">
      <h3>查询详情信息</h3>
      <div class="detail-info">
        <div class="info-item">
          <span class="label">应用ID:</span>
          <span class="value">{{ appId }}</span>
        </div>
        <div class="info-item">
          <span class="label">请求路径:</span>
          <span class="value">{{ reqPath }}</span>
        </div>
        <div class="info-item">
          <span class="label">查询日期:</span>
          <span class="value">{{ formatDate(date) }}</span>
        </div>
        <div class="info-item">
          <span class="label">状态:</span>
          <span :class="['status-value', status]">{{ status }}</span>
        </div>
      </div>
    </div>

    <div class="es-search-section">
      <h4>ES字段搜索</h4>
      <p class="section-desc">基于当前查询条件进行ES字段搜索</p>
      
      <div class="search-config">
        <div class="config-item">
          <label>应用ID字段值:</label>
          <input 
            v-model="esAppId" 
            type="text" 
            class="config-input"
            placeholder="应用ID"
          />
        </div>
        <div class="config-item">
          <label>请求路径字段值:</label>
          <input 
            v-model="esReqPath" 
            type="text" 
            class="config-input"
            placeholder="请求路径"
          />
        </div>
        <div class="config-item">
          <label>日期字段值:</label>
          <input 
            v-model="esDate" 
            type="text" 
            class="config-input"
            placeholder="日期 (格式: YYYY-MM-DD HH:mm:ss to YYYY-MM-DD HH:mm:ss)"
          />
          <div class="date-format-hint">
            日期将自动格式化为开始时间到结束时间的范围格式
          </div>
        </div>
      </div>

      <div class="es-search-wrapper">
        <ESFieldSearchInput
          v-model="selectedValues"
          :available-fields="availableFields"
          :field-values="fieldValues"
          @search="handleSearch"
          placeholder="搜索ES字段..."
        />
      </div>

      <div v-if="searchResults.length > 0" class="search-results">
        <h5>搜索结果</h5>
        <div class="results-list">
          <div 
            v-for="(result, index) in searchResults" 
            :key="index"
            class="result-item"
          >
            <div class="result-header">
              <span class="result-index">#{{ index + 1 }}</span>
              <span class="result-type">{{ result.type }}</span>
            </div>
            <div class="result-content">
              <pre>{{ JSON.stringify(result.data, null, 2) }}</pre>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import ESFieldSearchInput from './ESFieldSearchInput.vue'

// 定义组件名称
defineOptions({
  name: 'QueryDetail'
})

// Props
const props = defineProps({
  appId: {
    type: String,
    required: true
  },
  reqPath: {
    type: String,
    required: true
  },
  date: {
    type: String,
    required: true
  },
  status: {
    type: String,
    default: '成功'
  }
})

// 响应式数据
const esAppId = ref('')
const esReqPath = ref('')
const esDate = ref('')
const selectedValues = ref([])
const searchResults = ref([])

// 可用的ES字段
const availableFields = ref([
  'app_id', 'req_path', 'date', 'timestamp', 'status', 'user_id', 
  'ip_address', 'method', 'response_time', 'status_code', 'error_message',
  'session_id', 'request_id', 'user_agent', 'referer', 'content_type',
  'request_size', 'response_size', 'protocol', 'host', 'port'
])

// 字段值配置
const fieldValues = ref({
  app_id: ['app_001', 'app_002', 'app_003', 'app_004'],
  req_path: ['/api/users/search', '/api/orders/list', '/api/products/filter', '/api/logs/query'],
  date: [
  ],
  status: ['成功', '失败', '超时', '错误'],
  method: ['GET', 'POST', 'PUT', 'DELETE'],
  status_code: ['200', '404', '500', '403', '401'],
  user_id: ['user_001', 'user_002', 'user_003'],
  ip_address: ['192.168.1.1', '192.168.1.2', '10.0.0.1'],
  response_time: ['100ms', '200ms', '500ms', '1s', '2s'],
  error_message: ['连接超时', '权限不足', '资源不存在', '服务器错误']
})

// 方法
const formatDate = (dateString) => {
  const date = new Date(dateString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

// 格式化日期为开始时间到结束时间的格式
const formatDateRange = (dateString) => {
  try {
    const date = new Date(dateString)
    const startDate = new Date(date.getTime() - 24 * 60 * 60 * 1000) // 前一天
    const endDate = new Date(date.getTime() + 24 * 60 * 60 * 1000)   // 后一天
    
    const formatDateTime = (date) => {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    }
    
    return `${formatDateTime(startDate)} to ${formatDateTime(endDate)}`
  } catch (e) {
    console.error('日期格式化失败', e)
    return dateString
  }
}

const handleSearch = (searchData) => {
  console.log('ES搜索数据:', searchData)
  
  // 模拟搜索结果
  searchResults.value = [
    {
      type: '日志记录',
      data: {
        app_id: props.appId,
        req_path: props.reqPath,
        date: props.date,
        status: props.status,
        search_criteria: searchData,
        timestamp: new Date().toISOString(),
        log_level: 'INFO',
        message: '查询执行成功'
      }
    },
    {
      type: '性能指标',
      data: {
        response_time: '150ms',
        request_size: '2.5KB',
        response_size: '15.2KB',
        cpu_usage: '45%',
        memory_usage: '60%',
        database_queries: 8
      }
    }
  ]
}

// 监听props变化，自动设置ES搜索字段值
watch(() => props.appId, (newValue) => {
  esAppId.value = newValue
}, { immediate: true })

watch(() => props.reqPath, (newValue) => {
  esReqPath.value = newValue
}, { immediate: true })

watch(() => props.date, (newValue) => {
  if (newValue) {
    // 将日期格式化为范围格式
    esDate.value = formatDateRange(newValue)
  } else {
    esDate.value = ''
  }
}, { immediate: true })

// 组件挂载时初始化
onMounted(() => {
  // 根据props初始化ES搜索条件
  if (props.appId) {
    selectedValues.value.push({
      field: 'app_id',
      value: props.appId
    })
  }
  
  if (props.reqPath) {
    selectedValues.value.push({
      field: 'req_path',
      value: props.reqPath
    })
  }
  
  if (props.date) {
    // 对于日期字段，使用开始时间到结束时间的格式
    const dateRange = formatDateRange(props.date)
    selectedValues.value.push({
      field: 'date',
      value: dateRange
    })
  }
})
</script>

<style scoped>
.query-detail-container {
  padding: 0;
}

.detail-header {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e5e7eb;
}

.detail-header h3 {
  margin: 0 0 15px 0;
  color: #1f2937;
  font-size: 18px;
  font-weight: 600;
}

.detail-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 15px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.label {
  font-weight: 600;
  color: #374151;
  min-width: 80px;
}

.value {
  color: #1f2937;
  font-family: 'Courier New', monospace;
  background: #f3f4f6;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 13px;
}

.status-value {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-value.成功 {
  background: #dcfce7;
  color: #166534;
}

.status-value.失败 {
  background: #fee2e2;
  color: #dc2626;
}

.es-search-section {
  margin-top: 30px;
}

.es-search-section h4 {
  margin: 0 0 10px 0;
  color: #1f2937;
  font-size: 16px;
  font-weight: 600;
}

.section-desc {
  margin: 0 0 20px 0;
  color: #6b7280;
  font-size: 14px;
}

.search-config {
  background: #f9fafb;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  border: 1px solid #e5e7eb;
}

.config-item {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
}

.config-item:last-child {
  margin-bottom: 0;
}

.config-item label {
  font-weight: 500;
  color: #374151;
  min-width: 120px;
  font-size: 14px;
}

.config-input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #d1d5db;
  border-radius: 4px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s;
}

.config-input:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.date-format-hint {
  font-size: 12px;
  color: #6b7280;
  margin-top: 4px;
  font-style: italic;
}

.es-search-wrapper {
  margin-bottom: 20px;
}

.search-results {
  margin-top: 30px;
}

.search-results h5 {
  margin: 0 0 15px 0;
  color: #1f2937;
  font-size: 14px;
  font-weight: 600;
}

.results-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.result-item {
  background: #f9fafb;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  overflow: hidden;
}

.result-header {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  background: #f3f4f6;
  border-bottom: 1px solid #e5e7eb;
}

.result-index {
  font-weight: 600;
  color: #374151;
  font-size: 13px;
}

.result-type {
  background: #3b82f6;
  color: white;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 11px;
  font-weight: 500;
}

.result-content {
  padding: 16px;
}

.result-content pre {
  margin: 0;
  font-size: 12px;
  color: #374151;
  background: white;
  padding: 12px;
  border-radius: 4px;
  border: 1px solid #e5e7eb;
  overflow-x: auto;
  white-space: pre-wrap;
  word-break: break-word;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .detail-info {
    grid-template-columns: 1fr;
  }
  
  .config-item {
    flex-direction: column;
    align-items: stretch;
    gap: 5px;
  }
  
  .config-item label {
    min-width: auto;
  }
  
  .result-content pre {
    font-size: 11px;
  }
}
</style>