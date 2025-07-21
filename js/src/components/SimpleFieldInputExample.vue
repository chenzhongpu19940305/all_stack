<template>
  <div class="example-container">
    <h2>字段输入组件示例</h2>
    
    <!-- 使用简化的字段输入组件 -->
    <SimpleFieldInput @add="handleAddField" />
    
    <!-- 显示已添加的字段列表 -->
    <div v-if="fieldList.length > 0" class="field-list">
      <h3>已添加的字段:</h3>
      <div 
        v-for="(item, index) in fieldList" 
        :key="index" 
        class="field-item"
      >
        <span class="field-name">{{ item.field }}</span>
        <span class="separator">:</span>
        <span class="field-value">{{ item.value }}</span>
        <button @click="removeField(index)" class="remove-btn">删除</button>
      </div>
    </div>
    
    <!-- 清空按钮 -->
    <div v-if="fieldList.length > 0" class="button-group">
      <button @click="clearAll" class="clear-btn">清空所有</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import SimpleFieldInput from './SimpleFieldInput.vue'

// 响应式数据
const fieldList = ref([])

// 方法
const handleAddField = (fieldData) => {
  fieldList.value.push(fieldData)
  console.log('添加字段:', fieldData)
}

const removeField = (index) => {
  fieldList.value.splice(index, 1)
}

const clearAll = () => {
  fieldList.value = []
}
</script>

<style scoped>
.example-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

h2 {
  color: #333;
  margin-bottom: 20px;
  text-align: center;
}

.field-list {
  margin-top: 30px;
}

.field-list h3 {
  color: #333;
  margin-bottom: 15px;
}

.field-item {
  display: flex;
  align-items: center;
  background: white;
  padding: 10px 15px;
  border-radius: 4px;
  margin-bottom: 10px;
  border: 1px solid #e9ecef;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}

.field-name {
  font-weight: bold;
  color: #007bff;
  margin-right: 8px;
}

.separator {
  color: #6c757d;
  margin-right: 8px;
}

.field-value {
  flex: 1;
  color: #495057;
}

.remove-btn {
  padding: 4px 8px;
  background: #dc3545;
  color: white;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  font-size: 12px;
  margin-left: 10px;
}

.remove-btn:hover {
  background: #c82333;
}

.button-group {
  margin-top: 20px;
  text-align: center;
}

.clear-btn {
  padding: 10px 20px;
  background: #6c757d;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.clear-btn:hover {
  background: #5a6268;
}
</style> 