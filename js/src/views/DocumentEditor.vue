<template>
  <div class="document-list-container">
    <div class="gallery-header">
      <div class="header-left">
        <div class="logo">
          <span class="logo-icon">📝</span>
          <span class="logo-text">文档管理</span>
        </div>
        <div class="search-container">
          <input 
            v-model="searchQuery" 
            @input="handleSearch"
            placeholder="搜索文档..." 
            class="search-input"
          >
          <span class="search-icon">🔍</span>
        </div>
      </div>
      <div class="header-right">
        <button @click="createNewDocument" class="upload-btn">📄 新建文档</button>
      </div>
    </div>

    <div class="main-content">
      <div class="document-list">
        <div class="documents-grid">
          <div v-if="loading" class="loading-container">
            <div class="loading-spinner"></div>
            <p>加载中...</p>
          </div>
          <div v-else-if="filteredDocuments.length === 0" class="no-documents">
            <p>暂无文档</p>
          </div>
          <div v-else v-for="doc in filteredDocuments" :key="doc.id" :class="['document-card', { highlight: doc.id === highlightedId }]" @mouseenter="showCardActions(doc.id)" @mouseleave="hideCardActions(doc.id)">
            <div class="document-title">{{ doc.title || '无标题文档' }}</div>
            <div class="document-preview" @click="editDocument(doc)">
              <div class="document-content-preview">
                {{ getDocumentPreview(doc.content) }}
              </div>
            </div>
            <div class="document-date">{{ formatDate(doc.updatedAt) }}</div>
            <!-- 操作按钮 -->
            <div class="card-actions" v-show="hoveredCardId === doc.id">
              <button @click.stop="editDocument(doc)" class="action-btn edit-btn">
                <span class="btn-icon">✏️</span>
                <span class="btn-text">编辑</span>
              </button>
              <button @click.stop="deleteDocument(doc.id)" class="action-btn delete-btn">
                <span class="btn-icon">🗑️</span>
                <span class="btn-text">删除</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>


  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getDocuments, deleteDocument as deleteDocumentAPI } from '../api/documentEditor.js'

export default {
  name: 'DocumentEditor',
  setup() {
    const router = useRouter()
    const documents = ref([])
    const searchQuery = ref('')
    const loading = ref(false)
    const hoveredCardId = ref(null)
    const highlightedId = ref(null)
    
    // 搜索过滤 - 改为服务端搜索
    const filteredDocuments = computed(() => {
      return documents.value
    })
    
    // 防抖函数
    const debounce = (func, wait) => {
      let timeout
      return function executedFunction(...args) {
        const later = () => {
          clearTimeout(timeout)
          func(...args)
        }
        clearTimeout(timeout)
        timeout = setTimeout(later, wait)
      }
    }
    
    // 搜索处理 - 改为服务端搜索
    const handleSearch = async () => {
      console.log('搜索被触发，搜索词:', searchQuery.value)
      await loadDocuments(searchQuery.value)
    }
    
    // 防抖搜索
    const debouncedSearch = debounce(handleSearch, 300)
    
    // 卡片悬停效果
    const showCardActions = (id) => {
      hoveredCardId.value = id
    }
    
    const hideCardActions = (id) => {
      hoveredCardId.value = null
    }
    
    // 文档操作
    const createNewDocument = () => {
      // 跳转到编辑页面，创建新文档
      router.push('/document-editor/edit')
    }
    
    const editDocument = (doc) => {
      // 跳转到编辑页面，编辑指定文档
      router.push(`/document-editor/edit/${doc.id}`)
    }
    
    const deleteDocument = async (id) => {
      if (confirm('确定要删除这个文档吗？')) {
        try {
          loading.value = true
          const result = await deleteDocumentAPI(id)
          if (result.success) {
            // 重新加载文档列表
            await loadDocuments()
          } else {
            alert(result.message || '删除失败')
          }
        } catch (error) {
          console.error('删除文档失败:', error)
          alert('删除失败，请稍后重试')
        } finally {
          loading.value = false
        }
      }
    }
    
    // 加载文档列表
    const loadDocuments = async (searchQuery = '') => {
      try {
        loading.value = true
        const params = { page: 1, size: 100 }
        if (searchQuery && searchQuery.trim()) {
          params.q = searchQuery.trim()
        }
        console.log('调用API，参数:', params)
        const result = await getDocuments(params)
        if (result.success) {
          documents.value = result.documents || []
          console.log('API调用成功，返回文档数量:', documents.value.length)
        } else {
          console.error('获取文档列表失败:', result.message)
        }
      } catch (error) {
        console.error('获取文档列表失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    // 工具函数
    const getDocumentPreview = (content) => {
      if (!content) return '暂无内容'
      const div = document.createElement('div')
      div.innerHTML = content
      const text = div.textContent || div.innerText || ''
      return text.substring(0, 150) + (text.length > 150 ? '...' : '')
    }
    
    const formatDate = (dateString) => {
      const date = new Date(dateString)
      return date.toLocaleString('zh-CN')
    }
    
    onMounted(() => {
      loadDocuments('')
    })
    
    return {
      documents,
      searchQuery,
      loading,
      hoveredCardId,
      highlightedId,
      filteredDocuments,
      handleSearch,
      showCardActions,
      hideCardActions,
      createNewDocument,
      editDocument,
      deleteDocument,
      getDocumentPreview,
      formatDate
    }
  }
}
</script>

<style scoped>
.document-list-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 0;
}

/* 头部样式 - 参考Gallery样式 */
.gallery-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  padding: 1.5rem 2rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 2rem;
}

.logo {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-weight: bold;
  color: #2c3e50;
}

.logo-icon {
  font-size: 1.8rem;
}

.logo-text {
  font-size: 1.3rem;
  background: linear-gradient(45deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.search-container {
  position: relative;
  min-width: 300px;
}

.search-input {
  width: 100%;
  padding: 0.75rem 3rem 0.75rem 1rem;
  border: 2px solid #e0e0e0;
  border-radius: 25px;
  font-size: 1rem;
  outline: none;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.9);
}

.search-input:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  background: white;
}

.search-icon {
  position: absolute;
  right: 1rem;
  top: 50%;
  transform: translateY(-50%);
  color: #666;
  font-size: 1.2rem;
}

.header-right {
  display: flex;
  gap: 1rem;
}

.upload-btn {
  padding: 0.75rem 1.5rem;
  background: linear-gradient(45deg, #4CAF50, #45a049);
  color: white;
  border: none;
  border-radius: 25px;
  cursor: pointer;
  font-weight: 600;
  font-size: 1rem;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(76, 175, 80, 0.3);
}

.upload-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(76, 175, 80, 0.4);
}

/* 主要内容区域 */
.main-content {
  padding: 2rem;
  max-width: 1400px;
  margin: 0 auto;
}

.document-list {
  width: 100%;
}

.documents-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 1.5rem;
  padding: 1rem 0;
}

/* 文档卡片样式 */
.document-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 1.5rem;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  min-height: 200px;
  display: flex;
  flex-direction: column;
}

.document-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
  background: rgba(255, 255, 255, 1);
}

.document-card.highlight {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.2);
}

.document-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 1rem;
  text-align: left;
  line-height: 1.3;
  flex-shrink: 0;
}

.document-preview {
  flex: 1;
  position: relative;
  overflow: hidden;
  border-radius: 8px;
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  margin-bottom: 1rem;
}

.document-content-preview {
  padding: 1rem;
  color: #666;
  font-size: 0.9rem;
  line-height: 1.5;
  height: 100px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
}

.document-date {
  color: #999;
  font-size: 0.8rem;
  text-align: right;
  margin-top: auto;
}

/* 卡片操作按钮样式 */
.card-actions {
  position: absolute;
  bottom: 1rem;
  left: 1rem;
  display: flex;
  gap: 0.5rem;
  z-index: 10;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 0.3rem;
  padding: 0.4rem 0.8rem;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-size: 0.8rem;
  font-weight: 500;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
  box-shadow: 0 2px 8px rgba(0,0,0,0.15);
}

.edit-btn {
  background: rgba(76, 175, 80, 0.9);
  color: white;
}

.edit-btn:hover {
  background: rgba(76, 175, 80, 1);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(76, 175, 80, 0.3);
}

.delete-btn {
  background: rgba(244, 67, 54, 0.9);
  color: white;
}

.delete-btn:hover {
  background: rgba(244, 67, 54, 1);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(244, 67, 54, 0.3);
}

.btn-icon {
  font-size: 0.9rem;
}

.btn-text {
  font-size: 0.8rem;
}

/* 加载和空状态样式 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem;
  color: rgba(255, 255, 255, 0.8);
  grid-column: 1 / -1;
}

.loading-spinner {
  width: 50px;
  height: 50px;
  border: 4px solid rgba(255, 255, 255, 0.3);
  border-top: 4px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.no-documents {
  text-align: center;
  padding: 4rem;
  color: rgba(255, 255, 255, 0.8);
  grid-column: 1 / -1;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.no-documents p {
  font-size: 1.2rem;
  margin: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .gallery-header {
    flex-direction: column;
    gap: 1rem;
    padding: 1rem;
  }
  
  .header-left {
    flex-direction: column;
    gap: 1rem;
    width: 100%;
  }
  
  .search-container {
    min-width: auto;
    width: 100%;
  }
  
  .main-content {
    padding: 1rem;
  }
  
  .documents-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .document-card {
    min-height: 180px;
    padding: 1rem;
  }
  
  .document-title {
    font-size: 1.1rem;
  }
  
  .document-content-preview {
    height: 80px;
    padding: 0.8rem;
  }
  
  .card-actions {
    bottom: 0.8rem;
    left: 0.8rem;
  }
  
  .action-btn {
    padding: 0.3rem 0.6rem;
    font-size: 0.7rem;
  }
}

@media (max-width: 480px) {
  .gallery-header {
    padding: 0.8rem;
  }
  
  .logo-text {
    font-size: 1.1rem;
  }
  
  .upload-btn {
    padding: 0.6rem 1rem;
    font-size: 0.9rem;
  }
  
  .main-content {
    padding: 0.8rem;
  }
  
  .documents-grid {
    gap: 0.8rem;
  }
  
  .document-card {
    min-height: 160px;
    padding: 0.8rem;
  }
}
</style>