<template>
  <div class="es-query-container">
    <div class="query-form">
      <h3 class="form-title">Elasticsearch 多字段查询</h3>
      
      <!-- 字段选择输入框 -->
      <div class="field-input-container">
        <div class="field-input-wrapper">
          <input 
            type="text" 
            v-model="currentField" 
            @focus="showFieldDropdown = true"
            @blur="handleFieldBlur"
            @input="filterFields"
            placeholder="选择或输入字段名"
            class="field-input"
            ref="fieldInput"
          />
          
          <!-- 字段下拉框 -->
          <div v-if="showFieldDropdown && filteredFields.length > 0" class="field-dropdown">
            <div 
              v-for="field in filteredFields" 
              :key="field"
              @click="selectField(field)"
              class="field-option"
            >
              {{ field }}
            </div>
          </div>
        </div>
        
        <input 
          type="text" 
          v-model="currentValue" 
          placeholder="输入字段值"
          class="value-input"
          @keyup.enter="addFieldValue"
        />
        
        <button 
          @click="addFieldValue" 
          class="add-btn"
          :disabled="!currentField || !currentValue"
        >
          添加
        </button>
      </div>
      
      <!-- 已添加的字段-值对列表 -->
      <div v-if="fieldValues.length > 0" class="field-values-list">
        <h4>查询条件:</h4>
        <div 
          v-for="(item, index) in fieldValues" 
          :key="index" 
          class="field-value-item"
        >
          <span class="field-name">{{ item.field }}</span>
          <span class="separator">:</span>
          <span class="field-value">{{ item.value }}</span>
          <button @click="removeFieldValue(index)" class="remove-btn">×</button>
        </div>
      </div>
      
      <!-- 操作按钮 -->
      <div class="button-group">
        <button 
          @click="handleQuery" 
          class="query-btn"
          :disabled="fieldValues.length === 0"
        >
          执行查询
        </button>
        <button @click="handleClear" class="clear-btn">
          清空所有
        </button>
      </div>
    </div>
    
    <!-- 查询结果 -->
    <div v-if="queryResult" class="result-section">
      <h3>查询结果:</h3>
      <pre class="result-display">{{ JSON.stringify(queryResult, null, 2) }}</pre>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'

// 定义组件名称
defineOptions({
  name: 'ElasticsearchQuery'
})

// 响应式数据
const currentField = ref('')
const currentValue = ref('')
const fieldValues = ref([])
const showFieldDropdown = ref(false)
const filteredFields = ref([])
const queryResult = ref(null)
const fieldInput = ref(null)

// 预定义的字段列表
const availableFields = [
  'title',
  'content',
  'author',
  'category',
  'tags',
  'status',
  'priority',
  'created_at',
  'updated_at',
  'user_id',
  'email',
  'phone',
  'address',
  'company',
  'department',
  'level',
  'message',
  'service',
  'ip',
  'status_code',
  'response_time',
  'timestamp',
  'log_level',
  'error_code',
  'session_id',
  'request_id',
  'method',
  'url',
  'user_agent',
  'referer',
  'client_ip'
]

// 定义事件
const emit = defineEmits(['query', 'clear'])

// 方法
const filterFields = () => {
  const input = currentField.value.toLowerCase()
  
  if (!input) {
    filteredFields.value = availableFields.slice(0, 10)
  } else {
    filteredFields.value = availableFields.filter(field => 
      field.toLowerCase().includes(input)
    ).slice(0, 10)
  }
}

const selectField = (field) => {
  currentField.value = field
  showFieldDropdown.value = false
  nextTick(() => {
    if (fieldInput.value) {
      fieldInput.value.focus()
    }
  })
}

const handleFieldBlur = () => {
  // 延迟隐藏下拉框，让点击事件先执行
  setTimeout(() => {
    showFieldDropdown.value = false
  }, 200)
}

const addFieldValue = () => {
  if (!currentField.value || !currentValue.value) {
    return
  }
  
  // 检查是否已存在相同字段
  const existingIndex = fieldValues.value.findIndex(item => 
    item.field === currentField.value
  )
  
  if (existingIndex !== -1) {
    // 更新已存在的字段值
    fieldValues.value[existingIndex].value = currentValue.value
  } else {
    // 添加新的字段值对
    fieldValues.value.push({
      field: currentField.value,
      value: currentValue.value
    })
  }
  
  // 清空当前输入
  currentField.value = ''
  currentValue.value = ''
  showFieldDropdown.value = false
}

const removeFieldValue = (index) => {
  fieldValues.value.splice(index, 1)
}

const handleQuery = () => {
  if (fieldValues.value.length === 0) {
    alert('请至少添加一个查询条件')
    return
  }
  
  // 构建查询参数
  const queryParams = {
    conditions: fieldValues.value
  }
  
  // 触发父组件事件
  emit('query', queryParams)
  
  // 模拟查询结果
  queryResult.value = {
    success: true,
    data: {
      conditions: fieldValues.value,
      timestamp: new Date().toISOString(),
      message: '多字段查询参数已发送到后端'
    }
  }
}

const handleClear = () => {
  currentField.value = ''
  currentValue.value = ''
  fieldValues.value = []
  queryResult.value = null
  showFieldDropdown.value = false
  emit('clear')
}

// 提供给父组件调用的方法
const getQueryData = () => {
  return {
    conditions: fieldValues.value
  }
}

const setQueryData = (conditions) => {
  fieldValues.value = conditions || []
}

// 暴露方法给父组件
defineExpose({
  getQueryData,
  setQueryData
})

// 生命周期
onMounted(() => {
  // 初始化过滤字段
  filterFields()
})
</script>

<style scoped>
.es-query-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.query-form {
  background: #f5f5f5;
  padding: 25px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.form-title {
  margin-top: 0;
  margin-bottom: 20px;
  color: #333;
  font-size: 1.2rem;
  text-align: center;
}

.field-input-container {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  align-items: center;
}

.field-input-wrapper {
  position: relative;
  flex: 2;
}

.field-input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  box-sizing: border-box;
}

.field-input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
}

.field-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border: 1px solid #ddd;
  border-top: none;
  border-radius: 0 0 4px 4px;
  max-height: 200px;
  overflow-y: auto;
  z-index: 1000;
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

.field-option {
  padding: 8px 12px;
  cursor: pointer;
  border-bottom: 1px solid #eee;
  font-size: 14px;
}

.field-option:hover {
  background: #f8f9fa;
}

.field-option:last-child {
  border-bottom: none;
}

.value-input {
  flex: 2;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  box-sizing: border-box;
}

.value-input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
}

.add-btn {
  padding: 10px 15px;
  background: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
}

.add-btn:hover:not(:disabled) {
  background: #218838;
}

.add-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.field-values-list {
  margin-bottom: 20px;
}

.field-values-list h4 {
  margin-bottom: 10px;
  color: #333;
  font-size: 1rem;
}

.field-value-item {
  display: flex;
  align-items: center;
  background: white;
  padding: 8px 12px;
  border-radius: 4px;
  margin-bottom: 8px;
  border: 1px solid #e9ecef;
}

.field-name {
  font-weight: bold;
  color: #007bff;
  margin-right: 5px;
}

.separator {
  color: #666;
  margin: 0 5px;
}

.field-value {
  color: #333;
  flex: 1;
}

.remove-btn {
  background: #dc3545;
  color: white;
  border: none;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  cursor: pointer;
  font-size: 12px;
  font-weight: bold;
  margin-left: 10px;
}

.remove-btn:hover {
  background: #c82333;
}

.button-group {
  display: flex;
  gap: 10px;
}

.query-btn, .clear-btn {
  flex: 1;
  padding: 12px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
  transition: background-color 0.3s;
}

.query-btn {
  background-color: #007bff;
  color: white;
}

.query-btn:hover:not(:disabled) {
  background-color: #0056b3;
}

.query-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.clear-btn {
  background-color: #6c757d;
  color: white;
}

.clear-btn:hover {
  background-color: #545b62;
}

.result-section {
  background: #f8f9fa;
  padding: 15px;
  border-radius: 8px;
  border: 1px solid #dee2e6;
}

.result-section h3 {
  margin-top: 0;
  color: #333;
}

.result-display {
  background: #fff;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #ddd;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  overflow-x: auto;
  white-space: pre-wrap;
  word-break: break-all;
}
</style> 