<template>
  <div class="drawer-example">
    <h2>抽屉组件示例</h2>
    
    <!-- 主界面 -->
    <div class="main-content">
      <button @click="openDrawer" class="open-drawer-btn">打开抽屉</button>
      
      <div class="config-section">
        <h3>配置搜索字段和值</h3>
        
        <div class="form-group">
          <label>添加字段：</label>
          <div class="field-input-group">
            <input 
              v-model="newField" 
              type="text" 
              placeholder="输入字段名称"
              class="form-input"
            />
            <button 
              @click="addField" 
              class="add-btn"
              :disabled="!newField.trim()"
            >
              添加
            </button>
          </div>
        </div>
        
        <div class="form-group" v-if="availableFields.length > 0">
          <label>已添加字段：</label>
          <div class="fields-list">
            <div 
              v-for="(field, index) in availableFields" 
              :key="index"
              class="field-item"
            >
              <span>{{ field }}</span>
              <button @click="removeField(index)" class="remove-btn">×</button>
            </div>
          </div>
        </div>
        
        <div class="form-group" v-if="availableFields.length > 0">
          <label>添加字段值：</label>
          <div class="field-value-config">
            <select v-model="selectedFieldForValue" class="form-select">
              <option value="">选择字段</option>
              <option 
                v-for="field in availableFields" 
                :key="field" 
                :value="field"
              >
                {{ field }}
              </option>
            </select>
            
            <div v-if="selectedFieldForValue" class="value-input-group">
              <input 
                v-model="newFieldValue" 
                type="text" 
                placeholder="输入字段值"
                class="form-input"
              />
              <button 
                @click="addFieldValue" 
                class="add-btn"
                :disabled="!newFieldValue.trim()"
              >
                添加值
              </button>
            </div>
          </div>
        </div>
        
        <div class="form-group" v-if="selectedFieldForValue && getFieldValues(selectedFieldForValue).length > 0">
          <label>{{ selectedFieldForValue }}的值：</label>
          <div class="values-list">
            <div 
              v-for="(value, index) in getFieldValues(selectedFieldForValue)" 
              :key="index"
              class="value-item"
            >
              <span>{{ value }}</span>
              <button @click="removeFieldValue(selectedFieldForValue, index)" class="remove-btn">×</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 抽屉组件 -->
    <div v-if="showDrawer" class="drawer-overlay" @click="closeDrawer">
      <div class="drawer-content" @click.stop>
        <div class="drawer-header">
          <h3>ES字段搜索</h3>
          <button @click="closeDrawer" class="close-btn">&times;</button>
        </div>
        
        <div class="drawer-body">
          <p class="drawer-description">使用下面的搜索组件查询ES数据</p>
          
          <!-- 在抽屉中使用ESFieldSearchInput组件 -->
          <ESFieldSearchInput 
            v-model="selectedConditions"
            placeholder="搜索ES字段..."
            :available-fields="availableFields"
            :field-values="fieldValues"
            @search="handleSearch"
          />
          
          <div v-if="selectedConditions.length > 0" class="result-section">
            <h4>已选择的条件:</h4>
            <pre class="result-display">{{ JSON.stringify(selectedConditions, null, 2) }}</pre>
          </div>
          
          <div v-if="searchResult" class="result-section">
            <h4>搜索结果:</h4>
            <pre class="result-display">{{ JSON.stringify(searchResult, null, 2) }}</pre>
          </div>
        </div>
        
        <div class="drawer-footer">
          <button @click="closeDrawer" class="cancel-btn">关闭</button>
          <button @click="applySearch" class="apply-btn" :disabled="selectedConditions.length === 0">应用</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import ESFieldSearchInput from './ESFieldSearchInput.vue'

// 响应式数据
const showDrawer = ref(false)
const selectedConditions = ref([])
const searchResult = ref(null)

// 字段和字段值配置
const newField = ref('')
const availableFields = ref(['title', 'content', 'author', 'created_at'])
const fieldValues = ref({
  title: ['首页', '关于我们', '产品介绍'],
  content: ['Vue3', 'React18', 'TypeScript'],
  author: ['张三', '李四', '王五'],
  created_at: ['今天', '昨天', '本周']
})

// 添加字段值相关
const selectedFieldForValue = ref('')
const newFieldValue = ref('')

// 打开抽屉
const openDrawer = () => {
  showDrawer.value = true
}

// 关闭抽屉
const closeDrawer = () => {
  showDrawer.value = false
}

// 添加字段
const addField = () => {
  if (newField.value.trim() && !availableFields.value.includes(newField.value)) {
    availableFields.value.push(newField.value)
    // 初始化该字段的值为空数组
    fieldValues.value[newField.value] = []
    newField.value = ''
  }
}

// 移除字段
const removeField = (index) => {
  const fieldToRemove = availableFields.value[index]
  availableFields.value.splice(index, 1)
  // 同时移除该字段的值
  if (fieldValues.value[fieldToRemove]) {
    delete fieldValues.value[fieldToRemove]
  }
  // 如果正在编辑该字段的值，重置选择
  if (selectedFieldForValue.value === fieldToRemove) {
    selectedFieldForValue.value = ''
  }
}

// 获取字段的值
const getFieldValues = (field) => {
  return fieldValues.value[field] || []
}

// 添加字段值
const addFieldValue = () => {
  if (selectedFieldForValue.value && newFieldValue.value.trim()) {
    if (!fieldValues.value[selectedFieldForValue.value]) {
      fieldValues.value[selectedFieldForValue.value] = []
    }
    
    if (!fieldValues.value[selectedFieldForValue.value].includes(newFieldValue.value)) {
      fieldValues.value[selectedFieldForValue.value].push(newFieldValue.value)
      newFieldValue.value = ''
    }
  }
}

// 移除字段值
const removeFieldValue = (field, index) => {
  if (fieldValues.value[field]) {
    fieldValues.value[field].splice(index, 1)
  }
}

// 处理搜索事件
const handleSearch = (conditions) => {
  console.log('搜索条件:', conditions)
  
  if (conditions.length === 0) {
    searchResult.value = null
    return
  }
  
  // 模拟构建ES查询
  searchResult.value = {
    query: {
      bool: {
        must: conditions.map(item => ({
          match: { [item.field]: item.value }
        }))
      }
    },
    timestamp: new Date().toISOString(),
    total_conditions: conditions.length
  }
}

// 应用搜索
const applySearch = () => {
  console.log('应用搜索条件:', selectedConditions.value)
  // 这里可以将搜索条件传回主界面或执行其他操作
  closeDrawer()
}
</script>

<style scoped>
.drawer-example {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.main-content {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
}

.open-drawer-btn {
  background: #4CAF50;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin-bottom: 20px;
  transition: background-color 0.3s;
}

.open-drawer-btn:hover {
  background: #45a049;
}

.config-section {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
  color: #333;
}

.form-input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  background-color: white;
}

.field-input-group,
.value-input-group {
  display: flex;
  gap: 10px;
}

.field-input-group .form-input,
.value-input-group .form-input {
  flex: 1;
}

.add-btn {
  background: #2196F3;
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.add-btn:hover:not(:disabled) {
  background: #0b7dda;
}

.add-btn:disabled {
  background: #cccccc;
  cursor: not-allowed;
}

.fields-list,
.values-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 10px;
}

.field-item,
.value-item {
  background: #e9ecef;
  padding: 6px 12px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.remove-btn {
  background: none;
  border: none;
  color: #666;
  cursor: pointer;
  font-size: 16px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  transition: background-color 0.3s;
}

.remove-btn:hover {
  background: #ddd;
  color: #333;
}

.field-value-config {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

/* 抽屉样式 */
.drawer-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: flex-end;
  z-index: 1000;
}

.drawer-content {
  background: white;
  width: 500px;
  height: 100vh;
  overflow-y: auto;
  box-shadow: -2px 0 10px rgba(0,0,0,0.1);
  animation: slideIn 0.3s ease;
  display: flex;
  flex-direction: column;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0);
  }
}

.drawer-header {
  padding: 20px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #f8f9fa;
}

.drawer-header h3 {
  margin: 0;
  color: #333;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
}

.drawer-body {
  padding: 20px;
  flex: 1;
  overflow-y: auto;
}

.drawer-description {
  margin-bottom: 20px;
  color: #666;
}

.drawer-footer {
  padding: 15px 20px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  background: #f8f9fa;
}

.cancel-btn {
  background: #f8f9fa;
  border: 1px solid #ddd;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  color: #333;
}

.apply-btn {
  background: #4CAF50;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.apply-btn:disabled {
  background: #cccccc;
  cursor: not-allowed;
}

.result-section {
  margin-top: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  padding: 15px;
}

.result-section h4 {
  margin-top: 0;
  margin-bottom: 10px;
  color: #333;
}

.result-display {
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 10px;
  overflow-x: auto;
  font-family: monospace;
  margin: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .drawer-content {
    width: 100%;
  }
}
</style>