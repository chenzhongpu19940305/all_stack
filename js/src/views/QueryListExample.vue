<template>
  <div class="example-container">
    <div class="example-header">
      <h1>查询列表功能示例</h1>
      <p>这是一个展示查询列表功能的示例页面，包含列表展示和详情查看功能。</p>
    </div>

    <div class="feature-demo">
      <h2>功能特性</h2>
      <ul class="feature-list">
        <li>📋 展示查询记录列表，包含应用ID、请求路径、日期等信息</li>
        <li>🔍 支持搜索过滤功能</li>
        <li>👁️ 点击"查看详情"按钮打开详情模态框</li>
        <li>📊 详情页面自动将appId、reqPath、date等字段传递给ESFieldSearch组件</li>
        <li>🔎 ESFieldSearch组件支持字段搜索和值选择</li>
        <li>📈 展示搜索结果和性能指标</li>
      </ul>
    </div>

    <div class="usage-guide">
      <h2>使用指南</h2>
      <div class="guide-steps">
        <div class="step">
          <div class="step-number">1</div>
          <div class="step-content">
            <h3>访问查询列表</h3>
            <p>点击导航栏中的"查询列表"菜单项，或直接访问 <code>/query-list</code> 路径</p>
          </div>
        </div>
        
        <div class="step">
          <div class="step-number">2</div>
          <div class="step-content">
            <h3>浏览记录</h3>
            <p>在列表中查看查询记录，可以使用搜索框过滤特定记录</p>
          </div>
        </div>
        
        <div class="step">
          <div class="step-number">3</div>
          <div class="step-content">
            <h3>查看详情</h3>
            <p>点击任意记录行的"查看详情"按钮，打开详情模态框</p>
          </div>
        </div>
        
        <div class="step">
          <div class="step-number">4</div>
          <div class="step-content">
            <h3>ES字段搜索</h3>
            <p>在详情页面中，可以看到ESFieldSearch组件已经预填充了当前记录的信息</p>
          </div>
        </div>
        
        <div class="step">
          <div class="step-number">5</div>
          <div class="step-content">
            <h3>执行搜索</h3>
            <p>使用ESFieldSearch组件进行字段搜索，查看搜索结果和性能指标</p>
          </div>
        </div>
      </div>
    </div>

    <div class="code-example">
      <h2>代码示例</h2>
      <div class="code-section">
        <h3>列表页面组件</h3>
        <pre><code>// QueryList.vue
&lt;template&gt;
  &lt;div class="query-list-container"&gt;
    &lt;table class="query-table"&gt;
      &lt;tbody&gt;
        &lt;tr v-for="record in records" :key="record.id"&gt;
          &lt;td&gt;{{ record.appId }}&lt;/td&gt;
          &lt;td&gt;{{ record.reqPath }}&lt;/td&gt;
          &lt;td&gt;{{ record.date }}&lt;/td&gt;
          &lt;td&gt;
            &lt;button @click="viewDetail(record)"&gt;查看详情&lt;/button&gt;
          &lt;/td&gt;
        &lt;/tr&gt;
      &lt;/tbody&gt;
    &lt;/table&gt;
    
    &lt;!-- 详情模态框 --&gt;
    &lt;QueryDetail 
      :app-id="selectedRecord.appId"
      :req-path="selectedRecord.reqPath"
      :date="selectedRecord.date"
    /&gt;
  &lt;/div&gt;
&lt;/template&gt;</code></pre>
      </div>
      
      <div class="code-section">
        <h3>详情组件</h3>
        <pre><code>// QueryDetail.vue
&lt;template&gt;
  &lt;div class="query-detail-container"&gt;
    &lt;div class="detail-info"&gt;
      &lt;div&gt;应用ID: {{ appId }}&lt;/div&gt;
      &lt;div&gt;请求路径: {{ reqPath }}&lt;/div&gt;
      &lt;div&gt;日期: {{ date }}&lt;/div&gt;
    &lt;/div&gt;
    
    &lt;ESFieldSearchInput
      v-model="selectedValues"
      :available-fields="availableFields"
      :field-values="fieldValues"
      @search="handleSearch"
    /&gt;
  &lt;/div&gt;
&lt;/template&gt;

&lt;script setup&gt;
const props = defineProps({
  appId: String,
  reqPath: String,
  date: String
})

// 组件挂载时自动设置ES搜索条件
onMounted(() => {
  if (props.appId) {
    selectedValues.value.push({
      field: 'app_id',
      value: props.appId
    })
  }
})
&lt;/script&gt;</code></pre>
      </div>
    </div>

    <div class="navigation-hint">
      <h2>开始体验</h2>
      <p>点击下面的按钮开始体验查询列表功能：</p>
      <router-link to="/query-list" class="start-btn">
        🚀 开始体验查询列表
      </router-link>
    </div>
  </div>
</template>

<script setup>
// 定义组件名称
defineOptions({
  name: 'QueryListExample'
})
</script>

<style scoped>
.example-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.example-header {
  text-align: center;
  margin-bottom: 40px;
  padding: 30px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 12px;
}

.example-header h1 {
  margin: 0 0 15px 0;
  font-size: 32px;
  font-weight: 700;
}

.example-header p {
  margin: 0;
  font-size: 16px;
  opacity: 0.9;
}

.feature-demo {
  margin-bottom: 40px;
}

.feature-demo h2 {
  color: #1f2937;
  font-size: 24px;
  margin-bottom: 20px;
  border-bottom: 2px solid #e5e7eb;
  padding-bottom: 10px;
}

.feature-list {
  list-style: none;
  padding: 0;
}

.feature-list li {
  padding: 12px 0;
  border-bottom: 1px solid #f3f4f6;
  font-size: 16px;
  color: #374151;
}

.feature-list li:last-child {
  border-bottom: none;
}

.usage-guide {
  margin-bottom: 40px;
}

.usage-guide h2 {
  color: #1f2937;
  font-size: 24px;
  margin-bottom: 20px;
  border-bottom: 2px solid #e5e7eb;
  padding-bottom: 10px;
}

.guide-steps {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.step {
  display: flex;
  align-items: flex-start;
  gap: 15px;
  padding: 20px;
  background: #f9fafb;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
}

.step-number {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: #3b82f6;
  color: white;
  border-radius: 50%;
  font-weight: 600;
  font-size: 18px;
  flex-shrink: 0;
}

.step-content h3 {
  margin: 0 0 8px 0;
  color: #1f2937;
  font-size: 18px;
  font-weight: 600;
}

.step-content p {
  margin: 0;
  color: #6b7280;
  line-height: 1.6;
}

.code-example {
  margin-bottom: 40px;
}

.code-example h2 {
  color: #1f2937;
  font-size: 24px;
  margin-bottom: 20px;
  border-bottom: 2px solid #e5e7eb;
  padding-bottom: 10px;
}

.code-section {
  margin-bottom: 30px;
}

.code-section h3 {
  color: #374151;
  font-size: 18px;
  margin-bottom: 15px;
  font-weight: 600;
}

.code-section pre {
  background: #1f2937;
  color: #f9fafb;
  padding: 20px;
  border-radius: 8px;
  overflow-x: auto;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.5;
}

.code-section code {
  font-family: 'Courier New', monospace;
}

.navigation-hint {
  text-align: center;
  padding: 30px;
  background: #f0f9ff;
  border: 2px solid #bae6fd;
  border-radius: 12px;
}

.navigation-hint h2 {
  color: #1f2937;
  font-size: 24px;
  margin-bottom: 15px;
}

.navigation-hint p {
  color: #6b7280;
  margin-bottom: 20px;
  font-size: 16px;
}

.start-btn {
  display: inline-block;
  padding: 15px 30px;
  background: #3b82f6;
  color: white;
  text-decoration: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  transition: all 0.2s;
  box-shadow: 0 4px 6px rgba(59, 130, 246, 0.2);
}

.start-btn:hover {
  background: #2563eb;
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(59, 130, 246, 0.3);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .example-header h1 {
    font-size: 24px;
  }
  
  .step {
    flex-direction: column;
    text-align: center;
  }
  
  .step-number {
    margin: 0 auto 15px auto;
  }
  
  .code-section pre {
    font-size: 12px;
    padding: 15px;
  }
}
</style> 