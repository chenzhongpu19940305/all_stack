<template>
  <div class="es-field-search-container">
    <div class="search-input-wrapper">
      <!-- 搜索图标 -->
      <div class="search-icon">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M21 21L16.514 16.506L21 21ZM19 10.5C19 15.194 15.194 19 10.5 19C5.806 19 2 15.194 2 10.5C2 5.806 5.806 2 10.5 2C15.194 2 19 5.806 19 10.5Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </div>
      
      <!-- 输入框 -->
      <input
        ref="searchInput"
        v-model="searchValue"
        type="text"
        class="search-input"
        :placeholder="placeholder"
        @input="handleInput"
        @focus="handleFocus"
        @blur="handleFieldBlur"
      />
      
      <!-- 清除按钮 -->
      <button
        v-if="showClearButton"
        class="clear-button"
        @click="clearSearch"
        type="button"
      >
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M18 6L6 18M6 6L18 18" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </button>
    </div>
    
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
    
    <!-- 字段值下拉框 -->
    <div v-if="showValueDropdown && selectedField" class="value-dropdown">
      <div class="value-dropdown-header">
        <span>{{ selectedField }} 的可选值:</span>
      </div>
      
      <!-- 自定义输入框 -->
      <div class="custom-value-input-container">
        <!-- 日期时间输入框 -->
        <input
          v-if="isDateTimeField(selectedField)"
          v-model="customValue"
          type="datetime-local"
          class="custom-value-input datetime-input"
          @change="addCustomValue"
        />
        <!-- 普通文本输入框 -->
        <input
          v-else
          v-model="customValue"
          type="text"
          class="custom-value-input"
          placeholder="输入自定义值..."
          @keyup.enter="addCustomValue"
        />
        <button 
          @click="addCustomValue" 
          class="add-custom-btn"
          :disabled="!customValue.trim()"
        >
          添加
        </button>
      </div>
      
      <!-- 已添加的自定义值 -->
      <div v-if="customValues.length > 0" class="custom-values-section">
        <div class="custom-values-header">自定义值:</div>
        <div class="custom-values-list">
          <div 
            v-for="(value, index) in customValues" 
            :key="'custom-' + index"
            class="value-option"
          >
            <label class="value-checkbox">
              <input 
                type="checkbox" 
                :checked="isValueSelected(value)"
                @change="toggleValue(value)"
              />
              <span class="value-label">{{ value }}</span>
              <button 
                @click="removeCustomValue(index)" 
                class="remove-custom-btn"
                title="删除自定义值"
              >
                ×
              </button>
            </label>
          </div>
        </div>
      </div>
      
      <!-- 预定义的可选值 -->
      <div v-if="fieldValues[selectedField] && fieldValues[selectedField].length > 0" class="value-options">
        <div class="predefined-values-header">预定义值:</div>
        <div 
          v-for="(value, index) in fieldValues[selectedField]" 
          :key="index"
          class="value-option"
        >
          <label class="value-checkbox">
            <input 
              type="checkbox" 
              :checked="isValueSelected(value)"
              @change="toggleValue(value)"
            />
            <span class="value-label">{{ value }}</span>
          </label>
        </div>
      </div>
      
      <div class="value-dropdown-footer">
        <button @click="applySelectedValues" class="apply-btn">应用</button>
        <button @click="closeValueDropdown" class="cancel-btn">取消</button>
      </div>
    </div>
    
    <!-- 已选择的值标签 -->
    <div v-if="selectedValues.length > 0" class="selected-values">
      <div 
        v-for="(item, index) in selectedValues" 
        :key="index" 
        class="selected-value-tag"
      >
        <span class="field-name">{{ item.field }}</span>
        <span class="separator">:</span>
        <span class="field-value">{{ item.value }}</span>
        <button @click="removeSelectedValue(index)" class="remove-btn">×</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, nextTick } from 'vue'

// 定义组件名称
defineOptions({
  name: 'ESFieldSearchInput'
})

// Props
const props = defineProps({
  placeholder: {
    type: String,
    default: '搜索ES字段...'
  },
  modelValue: {
    type: Array,
    default: () => []
  },
  debounceTime: {
    type: Number,
    default: 300
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'search', 'input', 'focus', 'blur', 'clear'])

// 响应式数据
const searchValue = ref('')
const searchInput = ref(null)
const isFocused = ref(false)
const debounceTimer = ref(null)
const showFieldDropdown = ref(false)
const showValueDropdown = ref(false)
const filteredFields = ref([])
const selectedField = ref('')
const tempSelectedValues = ref([])
const selectedValues = ref(props.modelValue || [])
const customValue = ref('')
const customValues = ref([])

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

// 判断字段是否为日期或时间类型
const isDateTimeField = (field) => {
  return field.toLowerCase().includes('date') || 
         field.toLowerCase().includes('time') || 
         field.toLowerCase().includes('at')
}

// 模拟每个字段的可选值
const fieldValues = {
  title: ['首页', '关于我们', '产品介绍', '联系方式'],
  content: ['Vue', 'React', 'Angular', 'Svelte'],
  author: ['张三', '李四', '王五', '赵六'],
  category: ['前端', '后端', '移动端', '全栈'],
  tags: ['JavaScript', 'TypeScript', 'CSS', 'HTML', 'Vue', 'React'],
  status: ['已发布', '草稿', '审核中', '已删除'],
  priority: ['高', '中', '低'],
  created_at: ['今天', '昨天', '本周', '本月', '今年'],
  updated_at: ['今天', '昨天', '本周', '本月', '今年'],
  user_id: ['1001', '1002', '1003', '1004'],
  email: ['admin@example.com', 'user@example.com', 'test@example.com'],
  phone: ['13800138000', '13900139000', '13700137000'],
  level: ['初级', '中级', '高级', '专家'],
  log_level: ['INFO', 'WARNING', 'ERROR', 'DEBUG', 'CRITICAL'],
  method: ['GET', 'POST', 'PUT', 'DELETE', 'PATCH']
}

// 计算属性
const showClearButton = computed(() => {
  return searchValue.value.length > 0 && isFocused.value
})

// 方法
const handleInput = () => {
  filterFields()
  emit('input', searchValue.value)
  
  clearTimeout(debounceTimer.value)
  debounceTimer.value = setTimeout(() => {
    emit('search', searchValue.value)
  }, props.debounceTime)
}

const handleFocus = () => {
  isFocused.value = true
  showFieldDropdown.value = true
  filterFields()
  emit('focus')
}

const handleFieldBlur = () => {
  isFocused.value = false
  // 延迟隐藏下拉框，让点击事件先执行
  setTimeout(() => {
    if (!showValueDropdown.value) {
      showFieldDropdown.value = false
    }
  }, 200)
  emit('blur')
}

const filterFields = () => {
  const input = searchValue.value.toLowerCase()
  
  if (!input) {
    filteredFields.value = availableFields.slice(0, 10)
  } else {
    filteredFields.value = availableFields.filter(field => 
      field.toLowerCase().includes(input)
    ).slice(0, 10)
  }
}

const selectField = (field) => {
  selectedField.value = field
  searchValue.value = field
  showFieldDropdown.value = false
  
  // 如果该字段有可选值，则显示值下拉框
  if (fieldValues[field] && fieldValues[field].length > 0) {
    // 初始化临时选中值
    tempSelectedValues.value = selectedValues.value
      .filter(item => item.field === field)
      .map(item => item.value)
    
    showValueDropdown.value = true
    
    // 清空自定义值输入框
    customValue.value = ''
    
    // 加载该字段已有的自定义值
    customValues.value = selectedValues.value
      .filter(item => item.field === field && !fieldValues[field]?.includes(item.value))
      .map(item => item.value)
  }
  
  nextTick(() => {
    if (searchInput.value) {
      searchInput.value.focus()
    }
  })
}

const isValueSelected = (value) => {
  return tempSelectedValues.value.includes(value)
}

const toggleValue = (value) => {
  const index = tempSelectedValues.value.indexOf(value)
  if (index === -1) {
    tempSelectedValues.value.push(value)
  } else {
    tempSelectedValues.value.splice(index, 1)
  }
}

const applySelectedValues = () => {
  // 移除当前字段的所有已选值
  selectedValues.value = selectedValues.value.filter(item => item.field !== selectedField.value)
  
  // 添加新选择的值
  tempSelectedValues.value.forEach(value => {
    selectedValues.value.push({
      field: selectedField.value,
      value: value
    })
  })
  
  // 更新v-model
  emit('update:modelValue', selectedValues.value)
  emit('search', selectedValues.value)
  
  // 关闭下拉框
  closeValueDropdown()
  clearSearch()
}

// 添加自定义值
const addCustomValue = () => {
  const trimmedValue = customValue.value.trim()
  if (!trimmedValue) return
  
  let valueToAdd = trimmedValue
  
  // 如果是日期时间字段，格式化显示
  if (isDateTimeField(selectedField.value) && trimmedValue.includes('T')) {
    try {
      // 将ISO格式的日期时间转换为更友好的显示格式
      const date = new Date(trimmedValue)
      valueToAdd = date.toLocaleString()
    } catch (e) {
      // 如果转换失败，使用原始值
      console.error('日期格式化失败', e)
    }
  }
  
  // 检查是否已存在于自定义值列表中
  if (!customValues.value.includes(valueToAdd)) {
    customValues.value.push(valueToAdd)
    
    // 如果还没有选中，自动选中该值
    if (!tempSelectedValues.value.includes(valueToAdd)) {
      tempSelectedValues.value.push(valueToAdd)
    }
  }
  
  // 清空输入框
  customValue.value = ''
}

// 删除自定义值
const removeCustomValue = (index) => {
  const valueToRemove = customValues.value[index]
  
  // 从自定义值列表中移除
  customValues.value.splice(index, 1)
  
  // 如果该值已被选中，也从临时选中列表中移除
  const selectedIndex = tempSelectedValues.value.indexOf(valueToRemove)
  if (selectedIndex !== -1) {
    tempSelectedValues.value.splice(selectedIndex, 1)
  }
}

const closeValueDropdown = () => {
  showValueDropdown.value = false
  tempSelectedValues.value = []
  customValues.value = []
  customValue.value = ''
}

const removeSelectedValue = (index) => {
  selectedValues.value.splice(index, 1)
  emit('update:modelValue', selectedValues.value)
  emit('search', selectedValues.value)
}

const clearSearch = () => {
  searchValue.value = ''
  showFieldDropdown.value = false
  showValueDropdown.value = false
  emit('clear')
  nextTick(() => {
    searchInput.value?.focus()
  })
}

const focus = () => {
  searchInput.value?.focus()
}

const blur = () => {
  searchInput.value?.blur()
}

// 监听props.modelValue的变化
watch(() => props.modelValue, (newValue) => {
  selectedValues.value = newValue || []
})

// 暴露方法给父组件
defineExpose({
  focus,
  blur,
  clearSearch
})
</script>

<style scoped>
.es-field-search-container {
  position: relative;
  width: 100%;
  font-family: Arial, sans-serif;
}

.search-input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
  background: #ffffff;
  border: 1px solid #e1e5e9;
  border-radius: 8px;
  transition: all 0.2s ease;
  overflow: hidden;
}

.search-input-wrapper:hover {
  border-color: #c1c5c9;
}

.search-input-wrapper:focus-within {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.search-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  color: #6b7280;
  flex-shrink: 0;
}

.search-input {
  flex: 1;
  border: none;
  outline: none;
  padding: 8px 12px;
  font-size: 14px;
  background: transparent;
  color: #374151;
}

.search-input::placeholder {
  color: #9ca3af;
}

.clear-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border: none;
  background: transparent;
  color: #6b7280;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.2s ease;
  margin-right: 4px;
}

.clear-button:hover {
  background: #f3f4f6;
  color: #374151;
}

.field-dropdown,
.value-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border: 1px solid #ddd;
  border-radius: 0 0 8px 8px;
  max-height: 300px;
  overflow-y: auto;
  z-index: 1000;
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
  margin-top: 2px;
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

.value-dropdown-header {
  padding: 10px 12px;
  background: #f5f5f5;
  border-bottom: 1px solid #ddd;
  font-weight: bold;
  font-size: 14px;
}

/* 自定义值输入框容器 */
.custom-value-input-container {
  display: flex;
  padding: 10px 12px;
  border-bottom: 1px solid #ddd;
  background: #f9f9f9;
}

.custom-value-input {
  flex: 1;
  padding: 6px 10px;
  border: 1px solid #ddd;
  border-radius: 4px 0 0 4px;
  font-size: 14px;
  outline: none;
}

.custom-value-input:focus {
  border-color: #3b82f6;
}

.datetime-input {
  min-width: 200px;
}

.add-custom-btn {
  padding: 6px 12px;
  background: #3b82f6;
  color: white;
  border: none;
  border-radius: 0 4px 4px 0;
  cursor: pointer;
  font-size: 14px;
}

.add-custom-btn:hover {
  background: #2563eb;
}

.add-custom-btn:disabled {
  background: #93c5fd;
  cursor: not-allowed;
}

/* 自定义值和预定义值的标题 */
.custom-values-header,
.predefined-values-header {
  padding: 8px 12px;
  font-size: 13px;
  font-weight: 600;
  color: #4b5563;
  background: #f3f4f6;
  border-bottom: 1px solid #e5e7eb;
}

.custom-values-list {
  max-height: 150px;
  overflow-y: auto;
}

.remove-custom-btn {
  margin-left: auto;
  background: transparent;
  border: none;
  color: #9ca3af;
  font-size: 16px;
  cursor: pointer;
  padding: 0 4px;
  line-height: 1;
}

.remove-custom-btn:hover {
  color: #ef4444;
}

.value-options {
  max-height: 150px;
  overflow-y: auto;
  margin-bottom: 50px; /* 确保底部有足够空间显示应用按钮 */
}

.value-option {
  padding: 8px 12px;
  border-bottom: 1px solid #eee;
}

.value-checkbox {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.value-checkbox input {
  margin-right: 8px;
}

.value-label {
  font-size: 14px;
}

.value-dropdown-footer {
  display: flex;
  justify-content: flex-end;
  padding: 10px 12px;
  background: #f5f5f5;
  border-top: 1px solid #ddd;
  position: sticky;
  bottom: 0;
  z-index: 10;
}

.apply-btn,
.cancel-btn {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  margin-left: 8px;
}

.apply-btn {
  background: #3b82f6;
  color: white;
}

.apply-btn:hover {
  background: #2563eb;
}

.cancel-btn {
  background: #e5e7eb;
  color: #374151;
}

.cancel-btn:hover {
  background: #d1d5db;
}

.selected-values {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 10px;
}

.selected-value-tag {
  display: flex;
  align-items: center;
  background: #f0f9ff;
  border: 1px solid #bae6fd;
  border-radius: 4px;
  padding: 4px 8px;
  font-size: 12px;
}

.field-name {
  font-weight: bold;
  color: #0369a1;
}

.separator {
  margin: 0 4px;
  color: #64748b;
}

.field-value {
  color: #334155;
}

.remove-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
  border: none;
  background: transparent;
  color: #64748b;
  cursor: pointer;
  margin-left: 4px;
  border-radius: 50%;
  font-size: 12px;
  line-height: 1;
}

.remove-btn:hover {
  background: #e2e8f0;
  color: #334155;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .search-input-wrapper {
    border-radius: 6px;
  }
  
  .search-icon {
    width: 36px;
    height: 36px;
  }
  
  .search-input {
    font-size: 16px; /* 移动端防止缩放 */
  }
  
  .selected-values {
    margin-top: 8px;
  }
}

/* 深色模式支持 */
@media (prefers-color-scheme: dark) {
  .search-input-wrapper {
    background: #1f2937;
    border-color: #374151;
  }
  
  .search-input-wrapper:hover {
    border-color: #4b5563;
  }
  
  .search-input-wrapper:focus-within {
    border-color: #3b82f6;
  }
  
  .search-input {
    color: #f9fafb;
  }
  
  .search-input::placeholder {
    color: #6b7280;
  }
  
  .clear-button:hover {
    background: #374151;
    color: #f9fafb;
  }
  
  .field-dropdown,
  .value-dropdown {
    background: #1f2937;
    border-color: #374151;
  }
  
  .field-option {
    border-color: #374151;
  }
  
  .field-option:hover {
    background: #374151;
  }
  
  .value-dropdown-header,
  .value-dropdown-footer,
  .custom-values-header,
  .predefined-values-header {
    background: #111827;
    border-color: #374151;
    color: #e5e7eb;
  }
  
  .custom-value-input-container {
    background: #1a202c;
    border-color: #374151;
  }
  
  .custom-value-input {
    background: #1f2937;
    border-color: #4b5563;
    color: #f9fafb;
  }
  
  .custom-value-input:focus {
    border-color: #3b82f6;
  }
  
  .add-custom-btn:disabled {
    background: #1e40af;
    color: #93c5fd;
  }
  
  .remove-custom-btn {
    color: #6b7280;
  }
  
  .remove-custom-btn:hover {
    color: #ef4444;
  }
  
  .value-option {
    border-color: #374151;
  }
  
  .cancel-btn {
    background: #374151;
    color: #f9fafb;
  }
  
  .cancel-btn:hover {
    background: #4b5563;
  }
  
  .selected-value-tag {
    background: #0c4a6e;
    border-color: #0369a1;
  }
  
  .field-name {
    color: #38bdf8;
  }
  
  .separator {
    color: #94a3b8;
  }
  
  .field-value {
    color: #e2e8f0;
  }
  
  .remove-btn:hover {
    background: #1e293b;
    color: #f9fafb;
  }
}
</style>