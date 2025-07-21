<template>
  <div class="search-example">
    <h2>搜索组件示例</h2>
    
    <div class="example-section">
      <h3>基础用法</h3>
      <SearchInput 
        v-model="basicSearch"
        placeholder="请输入搜索内容..."
        @search="handleBasicSearch"
      />
      <p>搜索结果: {{ basicSearchResult }}</p>
    </div>

    <div class="example-section">
      <h3>自定义防抖时间</h3>
      <SearchInput 
        v-model="debounceSearch"
        placeholder="500ms防抖搜索..."
        :debounce-time="500"
        @search="handleDebounceSearch"
      />
      <p>搜索结果: {{ debounceSearchResult }}</p>
    </div>

    <div class="example-section">
      <h3>手动触发搜索</h3>
      <SearchInput 
        ref="manualSearchRef"
        v-model="manualSearch"
        placeholder="按回车键搜索..."
        :auto-search="false"
        @search="handleManualSearch"
      />
      <button @click="triggerManualSearch" class="search-btn">手动搜索</button>
      <p>搜索结果: {{ manualSearchResult }}</p>
    </div>

    <div class="example-section">
      <h3>事件监听</h3>
      <SearchInput 
        v-model="eventSearch"
        placeholder="监听所有事件..."
        @input="handleInput"
        @focus="handleFocus"
        @blur="handleBlur"
        @clear="handleClear"
        @search="handleEventSearch"
      />
      <div class="event-log">
        <h4>事件日志:</h4>
        <ul>
          <li v-for="(log, index) in eventLogs" :key="index">{{ log }}</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import SearchInput from './SearchInput.vue'

// 基础搜索
const basicSearch = ref('')
const basicSearchResult = ref('')

const handleBasicSearch = (value) => {
  basicSearchResult.value = `搜索: "${value}"`
}

// 防抖搜索
const debounceSearch = ref('')
const debounceSearchResult = ref('')

const handleDebounceSearch = (value) => {
  debounceSearchResult.value = `防抖搜索: "${value}"`
}

// 手动搜索
const manualSearch = ref('')
const manualSearchResult = ref('')
const manualSearchRef = ref(null)

const handleManualSearch = (value) => {
  manualSearchResult.value = `手动搜索: "${value}"`
}

const triggerManualSearch = () => {
  manualSearchRef.value?.focus()
}

// 事件监听
const eventSearch = ref('')
const eventLogs = ref([])

const addLog = (message) => {
  const timestamp = new Date().toLocaleTimeString()
  eventLogs.value.unshift(`[${timestamp}] ${message}`)
  if (eventLogs.value.length > 10) {
    eventLogs.value = eventLogs.value.slice(0, 10)
  }
}

const handleInput = (value) => {
  addLog(`输入事件: "${value}"`)
}

const handleFocus = () => {
  addLog('获得焦点')
}

const handleBlur = () => {
  addLog('失去焦点')
}

const handleClear = () => {
  addLog('清空搜索')
}

const handleEventSearch = (value) => {
  addLog(`搜索事件: "${value}"`)
}
</script>

<style scoped>
.search-example {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.example-section {
  margin-bottom: 40px;
  padding: 20px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background: #f9fafb;
}

.example-section h3 {
  margin-top: 0;
  margin-bottom: 16px;
  color: #374151;
  font-size: 18px;
}

.example-section p {
  margin-top: 12px;
  padding: 8px 12px;
  background: #ffffff;
  border-radius: 4px;
  border: 1px solid #e5e7eb;
  font-family: monospace;
  font-size: 14px;
}

.search-btn {
  margin-top: 12px;
  padding: 8px 16px;
  background: #3b82f6;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.2s;
}

.search-btn:hover {
  background: #2563eb;
}

.event-log {
  margin-top: 16px;
}

.event-log h4 {
  margin-bottom: 8px;
  color: #374151;
  font-size: 16px;
}

.event-log ul {
  max-height: 200px;
  overflow-y: auto;
  background: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 4px;
  padding: 12px;
  margin: 0;
}

.event-log li {
  margin-bottom: 4px;
  font-family: monospace;
  font-size: 12px;
  color: #6b7280;
}

.event-log li:last-child {
  margin-bottom: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .search-example {
    padding: 16px;
  }
  
  .example-section {
    padding: 16px;
    margin-bottom: 24px;
  }
}
</style> 