<template>
  <div class="es-field-search-page">
    <div class="page-header">
      <h1 class="title">ES字段搜索组件</h1>
      <p class="subtitle">一个支持多选的Elasticsearch字段搜索组件</p>
    </div>
    
    <div class="content-container">
      <ESFieldSearchInput 
        v-model="selectedValues"
        :available-fields="availableFields"
        :field-values="fieldValues"
        placeholder="搜索ES字段..."
        @search="handleSearch"
      />
      
      <div v-if="selectedValues.length > 0" class="search-results">
        <h3>已选择的字段值:</h3>
        <pre>{{ JSON.stringify(selectedValues, null, 2) }}</pre>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import ESFieldSearchInput from '../components/ESFieldSearchInput.vue'

// 示例数据
const availableFields = ref([
  'user_id',
  'username', 
  'email',
  'created_at',
  'updated_at',
  'status',
  'category',
  'tags',
  'score',
  'title',
  'content',
  'author',
  'publish_date',
  'view_count',
  'like_count'
])

const fieldValues = ref({
  'status': ['active', 'inactive', 'pending', 'deleted'],
  'category': ['技术', '产品', '设计', '运营', '市场'],
  'tags': ['Vue', 'React', 'JavaScript', 'Python', 'Java', 'Go'],
  'author': ['张三', '李四', '王五', '赵六'],
  'score': ['1', '2', '3', '4', '5']
})

const selectedValues = ref([])

const handleSearch = (searchValue) => {
  console.log('搜索值:', searchValue)
}
</script>

<style scoped>
.es-field-search-page {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
}

.title {
  font-size: 2rem;
  color: #1f2937;
  margin-bottom: 8px;
}

.subtitle {
  font-size: 1.1rem;
  color: #6b7280;
  max-width: 600px;
  margin: 0 auto;
}

.content-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05), 0 10px 15px rgba(0, 0, 0, 0.03);
  padding: 32px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .es-field-search-page {
    padding: 16px;
  }
  
  .page-header {
    margin-bottom: 24px;
  }
  
  .title {
    font-size: 1.5rem;
  }
  
  .subtitle {
    font-size: 1rem;
  }
  
  .content-container {
    padding: 16px;
    border-radius: 8px;
  }
}

/* 深色模式支持 */
@media (prefers-color-scheme: dark) {
  .es-field-search-page {
    background: #111827;
    color: #f9fafb;
  }
  
  .title {
    color: #f9fafb;
  }
  
  .subtitle {
    color: #9ca3af;
  }
  
  .content-container {
    background: #1f2937;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2), 0 10px 15px rgba(0, 0, 0, 0.1);
  }
}

/* 搜索结果样式 */
.search-results {
  margin-top: 32px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.search-results h3 {
  margin-bottom: 16px;
  color: #495057;
  font-size: 1.1rem;
}

.search-results pre {
  background: #ffffff;
  padding: 16px;
  border-radius: 6px;
  border: 1px solid #dee2e6;
  overflow-x: auto;
  font-size: 14px;
  line-height: 1.5;
  color: #212529;
}

/* 深色模式下的搜索结果样式 */
@media (prefers-color-scheme: dark) {
  .search-results {
    background: #2d3748;
    border-color: #4a5568;
  }
  
  .search-results h3 {
    color: #e2e8f0;
  }
  
  .search-results pre {
    background: #1a202c;
    border-color: #4a5568;
    color: #e2e8f0;
  }
}
</style>