<template>
  <div class="simple-field-input-container">
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
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'

// 定义组件名称
defineOptions({
  name: 'SimpleFieldInput'
})

// 响应式数据
const currentField = ref('')
const currentValue = ref('')
const showFieldDropdown = ref(false)
const filteredFields = ref([])
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
const emit = defineEmits(['add'])

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
  
  // 触发父组件事件
  emit('add', {
    field: currentField.value,
    value: currentValue.value
  })
  
  // 清空当前输入
  currentField.value = ''
  currentValue.value = ''
  showFieldDropdown.value = false
}

// 生命周期
onMounted(() => {
  // 初始化过滤字段
  filterFields()
})
</script>

<style scoped>
.simple-field-input-container {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-bottom: 20px;
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
  white-space: nowrap;
}

.add-btn:hover:not(:disabled) {
  background: #218838;
}

.add-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}
</style> 