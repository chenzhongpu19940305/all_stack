<template>
  <div class="example-container">
    <h2>字段输入组件示例</h2>
    <p class="description">点击输入框选择字段，然后输入自定义值，点击添加按钮添加到列表中</p>
    
    <!-- 使用row标签包装组件 -->
    <div class="row">
      <div class="col">
        <SimpleFieldInput @add="handleAddField" />
      </div>
    </div>
    
    <!-- 多行布局示例 -->
    <div class="row">
      <div class="col">
        <h3>多行布局示例:</h3>
      </div>
    </div>
    
    <div class="row">
      <div class="col-md-6">
        <SimpleFieldInput @add="handleAddField" />
      </div>
      <div class="col-md-6">
        <SimpleFieldInput @add="handleAddField" />
      </div>
    </div>
    
    <!-- 显示已添加的字段列表 -->
    <div v-if="fieldList.length > 0" class="field-list">
      <h3>已添加的字段:</h3>
      <div class="row">
        <div 
          v-for="(item, index) in fieldList" 
          :key="index" 
          class="col-md-4"
        >
          <div class="field-item">
            <span class="field-name">{{ item.field }}</span>
            <span class="separator">:</span>
            <span class="field-value">{{ item.value }}</span>
            <button @click="removeField(index)" class="remove-btn">删除</button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 空状态 -->
    <div v-else class="empty-state">
      <div class="empty-icon">📝</div>
      <p>还没有添加任何字段，点击上方输入框选择字段并输入值</p>
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
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

h2 {
  color: #333;
  margin-bottom: 10px;
  text-align: center;
}

.description {
  color: #666;
  text-align: center;
  margin-bottom: 30px;
  font-size: 14px;
}

/* Row和Col样式 */
.row {
  display: flex;
  flex-wrap: wrap;
  margin: 0 -15px 20px -15px;
}

.col {
  flex: 1;
  padding: 0 15px;
}

.col-md-4 {
  flex: 0 0 33.333333%;
  max-width: 33.333333%;
  padding: 0 15px;
}

.col-md-6 {
  flex: 0 0 50%;
  max-width: 50%;
  padding: 0 15px;
}

@media (max-width: 768px) {
  .col-md-4,
  .col-md-6 {
    flex: 0 0 100%;
    max-width: 100%;
  }
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
  padding: 12px 16px;
  border-radius: 6px;
  margin-bottom: 10px;
  border: 1px solid #e9ecef;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
  transition: all 0.2s ease;
  height: 100%;
  min-height: 60px;
}

.field-item:hover {
  box-shadow: 0 2px 6px rgba(0,0,0,0.15);
}

.field-name {
  font-weight: bold;
  color: #007bff;
  margin-right: 8px;
  white-space: nowrap;
}

.separator {
  color: #6c757d;
  margin-right: 8px;
}

.field-value {
  flex: 1;
  color: #495057;
  word-break: break-word;
  min-width: 0;
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
  transition: background-color 0.2s ease;
  white-space: nowrap;
}

.remove-btn:hover {
  background: #c82333;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: #6c757d;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.empty-state p {
  font-size: 14px;
  margin: 0;
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
  transition: background-color 0.2s ease;
}

.clear-btn:hover {
  background: #5a6268;
}
</style> 