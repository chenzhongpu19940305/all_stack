<template>
  <div class="query-detail-container">
    <div class="detail-header">
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
      <p class="section-desc">使用Elasticsearch字段进行精确搜索和过滤</p>
      
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
  return date.toLocaleString('zh-CN')
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
  height: 100%;
  display: flex;
  flex-direction: column;
}

.detail-header {
  margin-bottom: 24px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e5e7eb;
  flex-shrink: 0;
}

.detail-header h3 {
  margin: 0 0 15px 0;
  color: #1f2937;
  font-size: 18px;
  font-weight: 600;
}

.detail-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 12px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.label {
  font-weight: 600;
  color: #374151;
  min-width: 70px;
  font-size: 13px;
}

.value {
  color: #1f2937;
  font-family: 'Courier New', monospace;
  background: #f3f4f6;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  flex: 1;
  word-break: break-all;
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
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.es-search-section h4 {
  margin: 0 0 8px 0;
  color: #1f2937;
  font-size: 16px;
  font-weight: 600;
}

.section-desc {
  margin: 0 0 16px 0;
  color: #6b7280;
  font-size: 13px;
}

.es-search-wrapper {
  margin-bottom: 20px;
  flex-shrink: 0;
}

.search-results {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.search-results h5 {
  margin: 0 0 12px 0;
  color: #1f2937;
  font-size: 14px;
  font-weight: 600;
  flex-shrink: 0;
}

.results-list {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
  overflow-y: auto;
}

.result-item {
  background: #f9fafb;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  overflow: hidden;
  flex-shrink: 0;
}

.result-header {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  background: #f3f4f6;
  border-bottom: 1px solid #e5e7eb;
}

.result-index {
  font-weight: 600;
  color: #374151;
  font-size: 12px;
}

.result-type {
  background: #3b82f6;
  color: white;
  padding: 2px 6px;
  border-radius: 10px;
  font-size: 10px;
  font-weight: 500;
}

.result-content {
  padding: 12px;
}

.result-content pre {
  margin: 0;
  font-size: 11px;
  color: #374151;
  background: white;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #e5e7eb;
  overflow-x: auto;
  white-space: pre-wrap;
  word-break: break-word;
  max-height: 200px;
  overflow-y: auto;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .detail-info {
    grid-template-columns: 1fr;
  }
  
  .info-item {
    flex-direction: column;
    align-items: stretch;
    gap: 4px;
  }
  
  .label {
    min-width: auto;
  }
  
  .result-content pre {
    font-size: 10px;
    max-height: 150px;
  }
}
</style> 