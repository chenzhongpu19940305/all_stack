<template>
  <div class="es-field-search-example">
    <h2>ES字段搜索组件示例</h2>
    
    <div class="example-section">
      <h3>基础用法</h3>
      <p class="description">支持搜索ES模板字段，选择字段后可以选择该字段的多个可选值</p>
      
      <ESFieldSearchInput 
        v-model="selectedConditions"
        placeholder="搜索ES字段..."
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
    
    <div class="usage-section">
      <h3>使用说明</h3>
      <div class="usage-steps">
        <div class="step">
          <div class="step-number">1</div>
          <div class="step-content">
            <h4>点击搜索框</h4>
            <p>点击搜索框会显示ES模板中的可用字段列表</p>
          </div>
        </div>
        
        <div class="step">
          <div class="step-number">2</div>
          <div class="step-content">
            <h4>选择字段</h4>
            <p>从下拉列表中选择一个字段，会显示该字段的可选值</p>
          </div>
        </div>
        
        <div class="step">
          <div class="step-number">3</div>
          <div class="step-content">
            <h4>选择多个值</h4>
            <p>可以为每个字段选择多个值，点击"应用"按钮确认选择</p>
          </div>
        </div>
        
        <div class="step">
          <div class="step-number">4</div>
          <div class="step-content">
            <h4>查看已选条件</h4>
            <p>已选择的条件会显示在搜索框下方，可以随时删除不需要的条件</p>
          </div>
        </div>
      </div>
    </div>
    
    <div class="code-section">
      <h3>组件代码示例</h3>
      <pre class="code-block">
&lt;template&gt;
  &lt;div&gt;
    &lt;ESFieldSearchInput 
      v-model="selectedConditions"
      placeholder="搜索ES字段..."
      @search="handleSearch"
    /&gt;
  &lt;/div&gt;
&lt;/template&gt;

&lt;script setup&gt;
import { ref } from 'vue'
import ESFieldSearchInput from './ESFieldSearchInput.vue'

const selectedConditions = ref([])
const searchResult = ref(null)

const handleSearch = (conditions) => {
  // 处理搜索逻辑
  searchResult.value = {
    query: {
      bool: {
        must: conditions.map(item => ({
          match: { [item.field]: item.value }
        }))
      }
    }
  }
}
&lt;/script&gt;
      </pre>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import ESFieldSearchInput from './ESFieldSearchInput.vue'

// 响应式数据
const selectedConditions = ref([])
const searchResult = ref(null)

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
</script>

<style scoped>
.es-field-search-example {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

h2 {
  color: #1f2937;
  margin-bottom: 24px;
  text-align: center;
}

.example-section {
  background: #f9fafb;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 32px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.description {
  color: #6b7280;
  margin-bottom: 16px;
}

h3 {
  color: #1f2937;
  margin-top: 0;
  margin-bottom: 16px;
  font-size: 1.25rem;
}

h4 {
  color: #374151;
  margin-top: 0;
  margin-bottom: 8px;
  font-size: 1rem;
}

.result-section {
  margin-top: 20px;
  background: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  padding: 16px;
}

.result-display {
  background: #f3f4f6;
  padding: 12px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  overflow-x: auto;
  white-space: pre-wrap;
  word-break: break-all;
  margin: 0;
}

.usage-section {
  background: #f0f9ff;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 32px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.usage-steps {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.step {
  display: flex;
  gap: 16px;
  align-items: flex-start;
}

.step-number {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  background: #0ea5e9;
  color: white;
  border-radius: 50%;
  font-weight: bold;
  flex-shrink: 0;
}

.step-content {
  flex: 1;
}

.step-content h4 {
  color: #0c4a6e;
  margin-bottom: 4px;
}

.step-content p {
  color: #334155;
  margin: 0;
}

.code-section {
  background: #1e293b;
  border-radius: 8px;
  padding: 24px;
  color: #f8fafc;
}

.code-block {
  background: #0f172a;
  padding: 16px;
  border-radius: 6px;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  overflow-x: auto;
  white-space: pre-wrap;
  margin: 0;
  color: #e2e8f0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .es-field-search-example {
    padding: 16px;
  }
  
  .example-section,
  .usage-section,
  .code-section {
    padding: 16px;
    margin-bottom: 24px;
  }
  
  .step {
    flex-direction: column;
    gap: 8px;
  }
  
  .step-number {
    margin-bottom: 4px;
  }
}

/* 深色模式支持 */
@media (prefers-color-scheme: dark) {
  .es-field-search-example {
    background: #111827;
    color: #f9fafb;
  }
  
  h2, h3 {
    color: #f9fafb;
  }
  
  h4 {
    color: #e5e7eb;
  }
  
  .description {
    color: #9ca3af;
  }
  
  .example-section {
    background: #1f2937;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
  }
  
  .result-section {
    background: #111827;
    border-color: #374151;
  }
  
  .result-display {
    background: #1e293b;
    color: #e5e7eb;
  }
  
  .usage-section {
    background: #0c4a6e;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
  }
  
  .step-content h4 {
    color: #7dd3fc;
  }
  
  .step-content p {
    color: #e0f2fe;
  }
}
</style>