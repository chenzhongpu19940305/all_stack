<template>
  <div class="es-field-search-page">
    <div class="page-header">
      <h1 class="title">ES字段搜索组件</h1>
      <p class="subtitle">一个支持多选的Elasticsearch字段搜索组件</p>
    </div>
    
    <div class="content-container">
      <ESFieldSearchExample />
    </div>
  </div>
</template>

<script setup>
import ESFieldSearchExample from '../components/ESFieldSearchExample.vue'
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
</style>