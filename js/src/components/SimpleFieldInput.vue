<template>
  <div class="simple-field-input-container">
    <div class="field-input-wrapper">
      <input 
        type="text" 
        v-model="searchText" 
        @focus="showDropdown = true"
        @blur="handleInputBlur"
        @input="filterOptions"
        placeholder="选择字段，然后输入值"
        class="field-input"
        ref="fieldInput"
        readonly
      />
      
      <!-- 下拉选项 -->
      <div v-if="showDropdown && filteredOptions.length > 0" class="field-dropdown">
        <div 
          v-for="option in filteredOptions" 
          :key="option.id"
          @click="selectField(option)"
          class="field-option"
        >
          <span class="option-field">{{ option.field }}</span>
          <span class="option-separator">:</span>
          <span class="option-value">{{ option.value }}</span>
        </div>
      </div>
    </div>
    
    <!-- 值输入框 -->
    <div v-if="selectedField" class="value-input-wrapper">
      <input 
        type="text" 
        v-model="customValue" 
        placeholder="请输入值"
        class="value-input"
        @keyup.enter="addFieldValue"
        ref="valueInput"
      />
      <button 
        @click="addFieldValue" 
        class="add-btn"
        :disabled="!customValue"
      >
        添加
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'

// 定义组件名称
defineOptions({
  name: 'SimpleFieldInput'
})

// 响应式数据
const searchText = ref('')
const showDropdown = ref(false)
const filteredOptions = ref([])
const fieldInput = ref(null)
const selectedField = ref(null)
const customValue = ref('')
const valueInput = ref(null)

// 预定义的字段列表
const availableOptions = [
  { id: 1, field: 'title', value: '标题' },
  { id: 2, field: 'content', value: '内容' },
  { id: 3, field: 'author', value: '作者' },
  { id: 4, field: 'category', value: '分类' },
  { id: 5, field: 'tags', value: '标签' },
  { id: 6, field: 'status', value: '状态' },
  { id: 7, field: 'priority', value: '优先级' },
  { id: 8, field: 'created_at', value: '创建时间' },
  { id: 9, field: 'updated_at', value: '更新时间' },
  { id: 10, field: 'user_id', value: '用户ID' },
  { id: 11, field: 'email', value: '邮箱' },
  { id: 12, field: 'phone', value: '电话' },
  { id: 13, field: 'address', value: '地址' },
  { id: 14, field: 'company', value: '公司' },
  { id: 15, field: 'department', value: '部门' },
  { id: 16, field: 'level', value: '级别' },
  { id: 17, field: 'message', value: '消息' },
  { id: 18, field: 'service', value: '服务' },
  { id: 19, field: 'ip', value: 'IP地址' },
  { id: 20, field: 'status_code', value: '状态码' },
  { id: 21, field: 'response_time', value: '响应时间' },
  { id: 22, field: 'timestamp', value: '时间戳' },
  { id: 23, field: 'log_level', value: '日志级别' },
  { id: 24, field: 'error_code', value: '错误码' },
  { id: 25, field: 'session_id', value: '会话ID' },
  { id: 26, field: 'request_id', value: '请求ID' },
  { id: 27, field: 'method', value: '方法' },
  { id: 28, field: 'url', value: 'URL' },
  { id: 29, field: 'user_agent', value: '用户代理' },
  { id: 30, field: 'referer', value: '来源' },
  { id: 31, field: 'client_ip', value: '客户端IP' }
]

// 定义事件
const emit = defineEmits(['add'])

// 方法
const filterOptions = () => {
  const input = searchText.value.toLowerCase()
  
  if (!input) {
    filteredOptions.value = availableOptions.slice(0, 15)
  } else {
    filteredOptions.value = availableOptions.filter(option => 
      option.field.toLowerCase().includes(input) || 
      option.value.toLowerCase().includes(input)
    ).slice(0, 15)
  }
}

const selectField = (option) => {
  selectedField.value = option.field
  searchText.value = option.field
  showDropdown.value = false
  
  // 聚焦到值输入框
  nextTick(() => {
    if (valueInput.value) {
      valueInput.value.focus()
    }
  })
}

const handleInputBlur = () => {
  // 延迟隐藏下拉框，让点击事件先执行
  setTimeout(() => {
    showDropdown.value = false
  }, 200)
}

const addFieldValue = () => {
  if (!selectedField.value || !customValue.value) {
    return
  }
  
  // 触发父组件事件
  emit('add', {
    field: selectedField.value,
    value: customValue.value
  })
  
  // 清空当前输入
  selectedField.value = null
  searchText.value = ''
  customValue.value = ''
  
  // 聚焦回字段输入框
  nextTick(() => {
    if (fieldInput.value) {
      fieldInput.value.focus()
    }
  })
}

// 生命周期
onMounted(() => {
  // 初始化过滤选项
  filterOptions()
})
</script>

<style scoped>
.simple-field-input-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
  width: 100%;
}

.field-input-wrapper {
  position: relative;
  flex: 1;
}

.field-input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  box-sizing: border-box;
  background-color: #ffffff;
  cursor: pointer;
  transition: all 0.2s ease;
}

.field-input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
}

.field-input:hover {
  border-color: #007bff;
}

.field-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border: 1px solid #ddd;
  border-top: none;
  border-radius: 0 0 6px 6px;
  max-height: 300px;
  overflow-y: auto;
  z-index: 1000;
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

.field-option {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  cursor: pointer;
  border-bottom: 1px solid #eee;
  font-size: 14px;
  transition: background-color 0.2s ease;
}

.field-option:hover {
  background: #f8f9fa;
}

.field-option:last-child {
  border-bottom: none;
}

.option-field {
  font-weight: 500;
  color: #007bff;
  margin-right: 8px;
}

.option-separator {
  color: #6c757d;
  margin-right: 8px;
}

.option-value {
  color: #495057;
  flex: 1;
}

.value-input-wrapper {
  display: flex;
  gap: 10px;
  align-items: center;
}

.value-input {
  flex: 1;
  padding: 12px 16px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  box-sizing: border-box;
  transition: all 0.2s ease;
}

.value-input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
}

.add-btn {
  padding: 12px 20px;
  background: #28a745;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
  white-space: nowrap;
  transition: background-color 0.2s ease;
}

.add-btn:hover:not(:disabled) {
  background: #218838;
}

.add-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

/* 滚动条样式 */
.field-dropdown::-webkit-scrollbar {
  width: 6px;
}

.field-dropdown::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.field-dropdown::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.field-dropdown::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style> 