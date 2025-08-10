<template>
  <div class="code-snippets-container">
    <div class="gallery-header">
      <div class="header-left">
        <div class="logo">
          <span class="logo-icon">💻</span>
          <span class="logo-text">代码片段库</span>
        </div>
        <div class="search-container">
          <input 
            v-model="searchQuery" 
            @input="handleSearch"
            placeholder="搜索代码片段..." 
            class="search-input"
          >
          <span class="search-icon">🔍</span>
        </div>
      </div>
      <div class="header-right">
        <button @click="showUploadModal = true" class="upload-btn">新增代码片段</button>
      </div>
    </div>

    <div class="main-content">
      <div class="code-list">
        <div class="codes-grid">
          <div v-if="loading" class="loading-container">
            <div class="loading-spinner"></div>
            <p>加载中...</p>
          </div>
          <div v-else-if="filteredCodes.length === 0" class="no-codes">
            <p>暂无代码片段</p>
          </div>
          <div v-else v-for="item in filteredCodes" :key="item.id" :id="'record-' + item.id" :class="['code-card', { highlight: item.id === highlightedId }]" @mouseenter="showCardActions(item.id)" @mouseleave="hideCardActions(item.id)">
            <div class="card-header">
              <h3 class="card-title">{{ item.title }}</h3>
              <div class="language-tag" v-if="item.language">
                <span class="language-icon">🏷️</span>
                <span>{{ item.language }}</span>
              </div>
            </div>
            <div class="card-content">
              <div class="code-preview">
                <pre><code>{{ getCodePreview(item.code) }}</code></pre>
              </div>
              <p class="card-description" v-if="item.description">{{ item.description }}</p>
            </div>
            <div class="card-footer">
              <span class="card-date">{{ formatDate(item.createdAt) }}</span>
              <div :class="['card-actions', { visible: hoveredCardId === item.id }]">
                <button @click="viewCode(item)" class="action-btn view-btn">查看</button>
                <button @click="copyCode(item.code)" class="action-btn copy-btn">复制</button>
                <button @click="updateRecord(item)" class="action-btn edit-btn">编辑</button>
                <button @click="deleteRecord(item.id)" class="action-btn delete-btn">删除</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 代码查看模态框 -->
    <div v-if="showCodeModal" class="code-modal" @click="closeCodeModal">
      <div class="code-modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ currentCode.title }}</h3>
          <div class="header-actions">
            <button @click="copyCode(currentCode.code)" class="copy-btn">复制代码</button>
            <button @click="closeCodeModal" class="close-btn">✕</button>
          </div>
        </div>
        <div class="code-content">
          <div class="code-info">
            <span class="language-tag" v-if="currentCode.language">
              <span class="language-icon">🏷️</span>
              <span>{{ currentCode.language }}</span>
            </span>
            <span class="created-date">{{ formatDate(currentCode.createdAt) }}</span>
          </div>
          <div class="code-block">
            <pre><code>{{ currentCode.code }}</code></pre>
          </div>
          <div class="description" v-if="currentCode.description">
            <h4>描述</h4>
            <p>{{ currentCode.description }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 新增代码片段模态框 -->
    <div v-if="showUploadModal" class="upload-modal" @click="closeUploadModal">
      <div class="upload-modal-content" @click.stop>
        <div class="modal-header">
          <h3>新增代码片段</h3>
          <button @click="closeUploadModal" class="close-btn">✕</button>
        </div>
        <div class="upload-form">
          <div class="form-group">
            <label>标题：</label>
            <input v-model="uploadForm.title" placeholder="请输入标题" class="form-input">
          </div>
          <div class="form-group">
            <label>编程语言：</label>
            <select v-model="uploadForm.language" class="form-select">
              <option value="">请选择语言</option>
              <option value="javascript">JavaScript</option>
              <option value="python">Python</option>
              <option value="java">Java</option>
              <option value="html">HTML</option>
              <option value="css">CSS</option>
              <option value="vue">Vue</option>
              <option value="react">React</option>
              <option value="sql">SQL</option>
              <option value="bash">Bash</option>
              <option value="json">JSON</option>
              <option value="xml">XML</option>
              <option value="other">其他</option>
            </select>
          </div>
          <div class="form-group">
            <label>代码：</label>
            <textarea v-model="uploadForm.code" placeholder="请输入代码" class="form-code-textarea" rows="15"></textarea>
          </div>
          <div class="form-group">
            <label>描述：</label>
            <textarea v-model="uploadForm.description" placeholder="请输入描述" class="form-textarea" rows="3"></textarea>
          </div>
          <div class="form-actions">
            <button @click="uploadCode" class="upload-submit-btn">保存</button>
            <button @click="closeUploadModal" class="cancel-btn">取消</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 编辑代码片段模态框 -->
    <div v-if="showUpdateModal" class="upload-modal" @click="closeUpdateModal">
      <div class="upload-modal-content" @click.stop>
        <div class="modal-header">
          <h3>编辑代码片段</h3>
          <button @click="closeUpdateModal" class="close-btn">✕</button>
        </div>
        <div class="upload-form">
          <div class="form-group">
            <label>标题：</label>
            <input v-model="updateForm.title" placeholder="请输入标题" class="form-input">
          </div>
          <div class="form-group">
            <label>编程语言：</label>
            <select v-model="updateForm.language" class="form-select">
              <option value="">请选择语言</option>
              <option value="javascript">JavaScript</option>
              <option value="python">Python</option>
              <option value="java">Java</option>
              <option value="html">HTML</option>
              <option value="css">CSS</option>
              <option value="vue">Vue</option>
              <option value="react">React</option>
              <option value="sql">SQL</option>
              <option value="bash">Bash</option>
              <option value="json">JSON</option>
              <option value="xml">XML</option>
              <option value="other">其他</option>
            </select>
          </div>
          <div class="form-group">
            <label>代码：</label>
            <textarea v-model="updateForm.code" placeholder="请输入代码" class="form-code-textarea" rows="15"></textarea>
          </div>
          <div class="form-group">
            <label>描述：</label>
            <textarea v-model="updateForm.description" placeholder="请输入描述" class="form-textarea" rows="3"></textarea>
          </div>
          <div class="form-actions">
            <button @click="submitUpdate" class="upload-submit-btn">更新</button>
            <button @click="closeUpdateModal" class="cancel-btn">取消</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'

export default {
  name: 'CodeSnippets',
  setup() {
    const showCodeModal = ref(false)
    const showUploadModal = ref(false)
    const showUpdateModal = ref(false)
    const currentCode = ref({})
    const loading = ref(false)
    const codes = ref([])
    const searchQuery = ref('')
    const filteredCodes = ref([])
    const hoveredCardId = ref(null)
    const highlightedId = ref(null)
    const uploadForm = reactive({
      title: '',
      description: '',
      code: '',
      language: ''
    })
    const updateForm = reactive({
      id: null,
      title: '',
      description: '',
      code: '',
      language: ''
    })

    // API接口配置
    const API_BASE_URL = 'http://localhost:8080/czp/tool/api/code-snippets'
    const API_ENDPOINTS = {
      // 获取代码片段列表
      GET_RECORDS: `${API_BASE_URL}/records`,
      // 搜索代码片段
      SEARCH_RECORDS: `${API_BASE_URL}/records/search`,
      // 新增代码片段
      CREATE_RECORD: `${API_BASE_URL}/records`,
      // 删除代码片段
      DELETE_RECORD: `${API_BASE_URL}/records/:id`,
      // 获取单个记录详情
      GET_RECORD: `${API_BASE_URL}/records/:id`
    }

    // 通用请求方法
    const apiRequest = async (url, options = {}) => {
      try {
        const response = await fetch(url, {
          headers: {
            'Content-Type': 'application/json',
            ...options.headers
          },
          ...options
        })
        
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }
        
        return await response.json()
      } catch (error) {
        console.error('API请求失败:', error)
        throw error
      }
    }

    const loadRecords = async (params = {}) => {
      loading.value = true
      try {
        const queryParams = new URLSearchParams(params)
        const url = `${API_ENDPOINTS.GET_RECORDS}?${queryParams}`
        const response = await apiRequest(url)
        console.log('API响应:', response)
        codes.value = response.data?.records || []
        filteredCodes.value = [...codes.value]
      } catch (error) {
        console.error('加载记录失败:', error)
        alert('加载记录失败，请重试')
      } finally {
        loading.value = false
      }
    }

    const searchRecords = async (query) => {
      if (!query.trim()) {
        filteredCodes.value = [...codes.value]
        return
      }
      
      try {
        const response = await apiRequest(API_ENDPOINTS.SEARCH_RECORDS, {
          method: 'POST',
          body: JSON.stringify({ query })
        })
        console.log('搜索响应:', response)
        filteredCodes.value = response.data?.records || []
      } catch (error) {
        console.error('搜索失败:', error)
        // 如果搜索失败，使用本地过滤
        const localQuery = query.toLowerCase()
        filteredCodes.value = codes.value.filter(item => 
          item.title.toLowerCase().includes(localQuery) ||
          (item.code && item.code.toLowerCase().includes(localQuery)) ||
          (item.language && item.language.toLowerCase().includes(localQuery))
        )
      }
    }

    const createRecord = async (recordData) => {
      try {
        const response = await apiRequest(API_ENDPOINTS.CREATE_RECORD, {
          method: 'POST',
          body: JSON.stringify(recordData)
        })
        return response
      } catch (error) {
        console.error('创建记录失败:', error)
        throw error
      }
    }

    const deleteRecord = async (id) => {
      if (!confirm('确定要删除这个代码片段吗？')) {
        return
      }
      
      try {
        const url = API_ENDPOINTS.DELETE_RECORD.replace(':id', id)
        await apiRequest(url, {
          method: 'DELETE'
        })
        await loadRecords()
        alert('删除成功！')
      } catch (error) {
        console.error('删除失败:', error)
        alert('删除失败，请重试')
      }
    }

    // 格式化日期
    const formatDate = (dateString) => {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    }

    // 获取代码预览（前几行）
    const getCodePreview = (code) => {
      if (!code) return ''
      const lines = code.split('\n')
      return lines.slice(0, 5).join('\n') + (lines.length > 5 ? '\n...' : '')
    }

    // 查看代码
    const viewCode = (item) => {
      currentCode.value = item
      showCodeModal.value = true
    }

    // 复制代码
    const copyCode = async (code) => {
      try {
        await navigator.clipboard.writeText(code)
        alert('代码已复制到剪贴板！')
      } catch (error) {
        console.error('复制失败:', error)
        alert('复制失败，请手动复制')
      }
    }

    // 关闭代码查看模态框
    const closeCodeModal = () => {
      showCodeModal.value = false
      currentCode.value = {}
    }

    // 关闭上传模态框
    const closeUploadModal = () => {
      showUploadModal.value = false
      uploadForm.title = ''
      uploadForm.description = ''
      uploadForm.code = ''
      uploadForm.language = ''
    }

    // 关闭更新模态框
    const closeUpdateModal = () => {
      showUpdateModal.value = false
      updateForm.id = null
      updateForm.title = ''
      updateForm.description = ''
      updateForm.code = ''
      updateForm.language = ''
    }

    // 显示卡片操作按钮
    const showCardActions = (id) => {
      hoveredCardId.value = id
    }

    // 隐藏卡片操作按钮
    const hideCardActions = () => {
      hoveredCardId.value = null
    }

    // 搜索处理
    const handleSearch = () => {
      searchRecords(searchQuery.value)
    }

    // 新增代码片段
    const uploadCode = async () => {
      if (!uploadForm.title || !uploadForm.code) {
        alert('请填写标题和代码')
        return
      }
      
      try {
        const recordData = {
          title: uploadForm.title,
          description: uploadForm.description,
          code: uploadForm.code,
          language: uploadForm.language,
          images: [] // 代码片段不需要图片
        }
        
        await createRecord(recordData)
        await loadRecords()
        closeUploadModal()
        alert('代码片段新增成功！')
      } catch (error) {
        alert('新增记录失败，请重试')
      }
    }

    // 更新记录
    const updateRecord = (item) => {
      updateForm.id = item.id
      updateForm.title = item.title
      updateForm.description = item.description || ''
      updateForm.code = item.code || ''
      updateForm.language = item.language || ''
      showUpdateModal.value = true
    }

    // 提交更新
    const submitUpdate = async () => {
      if (!updateForm.title || !updateForm.code) {
        alert('请填写标题和代码')
        return
      }
      
      try {
        const url = API_ENDPOINTS.CREATE_RECORD.replace('/records', `/records/${updateForm.id}`)
        const recordData = {
          title: updateForm.title,
          description: updateForm.description,
          code: updateForm.code,
          language: updateForm.language,
          images: []
        }
        
        await apiRequest(url, {
          method: 'PUT',
          body: JSON.stringify(recordData)
        })
        
        await loadRecords()
        closeUpdateModal()
        alert('更新成功！')
      } catch (error) {
        console.error('更新记录失败:', error)
        alert('更新失败，请重试')
      }
    }

    onMounted(() => {
      loadRecords()
    })

    return {
      showCodeModal,
      showUploadModal,
      showUpdateModal,
      currentCode,
      loading,
      codes,
      searchQuery,
      filteredCodes,
      hoveredCardId,
      highlightedId,
      uploadForm,
      updateForm,
      viewCode,
      copyCode,
      closeCodeModal,
      closeUploadModal,
      closeUpdateModal,
      showCardActions,
      hideCardActions,
      handleSearch,
      uploadCode,
      updateRecord,
      submitUpdate,
      deleteRecord,
      formatDate,
      getCodePreview
    }
  }
}
</script>

<style scoped>
.code-snippets-container {
  min-height: 100vh;
  background-color: #f4f5f7;
}

.gallery-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
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
  color: white;
  font-weight: bold;
  font-size: 1.2rem;
}

.logo-icon {
  font-size: 1.5rem;
}

.search-container {
  position: relative;
  display: flex;
  align-items: center;
}

.search-input {
  padding: 0.5rem 2.5rem 0.5rem 1rem;
  border: none;
  border-radius: 25px;
  width: 300px;
  font-size: 0.9rem;
  outline: none;
}

.search-icon {
  position: absolute;
  right: 1rem;
  color: #666;
}

.header-right {
  display: flex;
  gap: 1rem;
}

.upload-btn {
  background: #4CAF50;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.3s ease;
}

.upload-btn:hover {
  background: #45a049;
}

.main-content {
  padding: 2rem;
}

.codes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 1.5rem;
}

.loading-container {
  grid-column: 1 / -1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem;
  color: #666;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.no-codes {
  grid-column: 1 / -1;
  text-align: center;
  padding: 3rem;
  color: #666;
  font-size: 1.1rem;
}

.code-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  overflow: hidden;
  transition: all 0.3s ease;
  cursor: pointer;
}

.code-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
}

.code-card.highlight {
  border: 2px solid #667eea;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.3);
}

.card-header {
  padding: 1rem;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  margin: 0;
  font-size: 1.1rem;
  color: #333;
  font-weight: 600;
}

.language-tag {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  background: #667eea;
  color: white;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.8rem;
}

.language-icon {
  font-size: 0.7rem;
}

.card-content {
  padding: 1rem;
}

.code-preview {
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  padding: 0.75rem;
  margin-bottom: 0.75rem;
  overflow: hidden;
}

.code-preview pre {
  margin: 0;
  font-family: 'Courier New', monospace;
  font-size: 0.85rem;
  line-height: 1.4;
  color: #333;
  white-space: pre-wrap;
  word-break: break-all;
}

.card-description {
  margin: 0;
  color: #666;
  font-size: 0.9rem;
  line-height: 1.4;
}

.card-footer {
  padding: 1rem;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-date {
  color: #999;
  font-size: 0.8rem;
}

.card-actions {
  display: flex;
  gap: 0.5rem;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.card-actions.visible {
  opacity: 1;
}

.action-btn {
  padding: 0.4rem 0.8rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.8rem;
  font-weight: 500;
  transition: all 0.3s ease;
}

.view-btn {
  background: #2196F3;
  color: white;
}

.view-btn:hover {
  background: #1976D2;
}

.copy-btn {
  background: #FF9800;
  color: white;
}

.copy-btn:hover {
  background: #F57C00;
}

.edit-btn {
  background: #4CAF50;
  color: white;
}

.edit-btn:hover {
  background: #45a049;
}

.delete-btn {
  background: #f44336;
  color: white;
}

.delete-btn:hover {
  background: #d32f2f;
}

/* 模态框样式 */
.code-modal, .upload-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.code-modal-content, .upload-modal-content {
  background: white;
  border-radius: 12px;
  max-width: 90vw;
  max-height: 90vh;
  overflow: auto;
  box-shadow: 0 10px 30px rgba(0,0,0,0.3);
}

.code-modal-content {
  width: 80vw;
}

.upload-modal-content {
  width: 600px;
}

.modal-header {
  padding: 1.5rem;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #f8f9fa;
  border-radius: 12px 12px 0 0;
}

.modal-header h3 {
  margin: 0;
  color: #333;
  font-size: 1.3rem;
}

.header-actions {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.close-btn {
  background: #f44336;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
}

.close-btn:hover {
  background: #d32f2f;
}

.code-content {
  padding: 1.5rem;
}

.code-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #eee;
}

.created-date {
  color: #666;
  font-size: 0.9rem;
}

.code-block {
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  padding: 1rem;
  margin-bottom: 1rem;
  overflow: auto;
}

.code-block pre {
  margin: 0;
  font-family: 'Courier New', monospace;
  font-size: 0.9rem;
  line-height: 1.5;
  color: #333;
  white-space: pre-wrap;
}

.description h4 {
  margin: 0 0 0.5rem 0;
  color: #333;
  font-size: 1rem;
}

.description p {
  margin: 0;
  color: #666;
  line-height: 1.5;
}

/* 表单样式 */
.upload-form {
  padding: 1.5rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
  color: #333;
}

.form-input, .form-select, .form-textarea, .form-code-textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 0.9rem;
  outline: none;
  transition: border-color 0.3s ease;
  box-sizing: border-box;
}

.form-input:focus, .form-select:focus, .form-textarea:focus, .form-code-textarea:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
}

.form-code-textarea {
  font-family: 'Courier New', monospace;
  font-size: 0.85rem;
  line-height: 1.4;
  resize: vertical;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 2rem;
  padding-top: 1rem;
  border-top: 1px solid #eee;
}

.upload-submit-btn {
  background: #667eea;
  color: white;
  border: none;
  padding: 0.75rem 2rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.3s ease;
}

.upload-submit-btn:hover {
  background: #5a6fd8;
}

.cancel-btn {
  background: #6c757d;
  color: white;
  border: none;
  padding: 0.75rem 2rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.3s ease;
}

.cancel-btn:hover {
  background: #5a6268;
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
  }
  
  .search-input {
    width: 250px;
  }
  
  .codes-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .main-content {
    padding: 1rem;
  }
  
  .upload-modal-content {
    width: 95vw;
    margin: 1rem;
  }
  
  .code-modal-content {
    width: 95vw;
    margin: 1rem;
  }
}
</style>