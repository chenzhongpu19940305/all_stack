<template>
  <div class="code-gallery-container">
    <div class="gallery-header">
      <div class="header-left">
        <div class="logo">
          <span class="logo-icon">💻</span>
          <span class="logo-text">代码片段管理</span>
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
        <button @click="showUploadModal = true" class="upload-btn">新增代码</button>
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
          <div v-else v-for="item in filteredCodes" :key="item.id" class="code-card" @mouseenter="showCardActions(item.id)" @mouseleave="hideCardActions(item.id)">
            <div class="code-title">{{ item.title }}</div>
            <div class="code-preview" @click="viewCode(item)">
              <div class="code-content">
                <pre><code>{{ getCodePreview(item.code) }}</code></pre>
              </div>
              <div class="code-language">{{ item.language || 'text' }}</div>
            </div>
            <div class="code-description">{{ item.description || '暂无描述' }}</div>
            <!-- 操作按钮 -->
            <div class="card-actions" v-show="hoveredCardId === item.id">
              <button @click.stop="updateRecord(item)" class="action-btn update-btn">
                <span class="btn-icon">✏️</span>
                <span class="btn-text">更新</span>
              </button>
              <button @click.stop="deleteRecord(item.id)" class="action-btn delete-btn">
                <span class="btn-icon">🗑️</span>
                <span class="btn-text">删除</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 全屏代码展示模态框 -->
    <div v-if="showCodeModal" class="fullscreen-modal" @click="closeCodeModal">
      <div class="fullscreen-content" @click.stop>
        <div class="fullscreen-header">
          <h3>{{ currentCode.title }}</h3>
          <div class="header-actions">
            <button @click="copyCode" class="copy-btn">复制代码</button>
            <button @click="closeCodeModal" class="close-btn">✕</button>
          </div>
        </div>
        <div class="fullscreen-code-container">
          <div class="code-info">
            <span class="language-tag">{{ currentCode.language || 'text' }}</span>
            <span class="description-text">{{ currentCode.description || '暂无描述' }}</span>
          </div>
          <div class="code-viewer">
            <pre><code class="language-{{ currentCode.language || 'text' }}">{{ currentCode.code }}</code></pre>
          </div>
        </div>
      </div>
    </div>

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
            <label>描述：</label>
            <textarea v-model="uploadForm.description" placeholder="请输入描述" class="form-textarea"></textarea>
          </div>
          <div class="form-group">
            <label>编程语言：</label>
            <select v-model="uploadForm.language" class="form-select">
              <option value="javascript">JavaScript</option>
              <option value="java">Java</option>
              <option value="python">Python</option>
              <option value="cpp">C++</option>
              <option value="csharp">C#</option>
              <option value="php">PHP</option>
              <option value="go">Go</option>
              <option value="rust">Rust</option>
              <option value="sql">SQL</option>
              <option value="html">HTML</option>
              <option value="css">CSS</option>
              <option value="vue">Vue</option>
              <option value="react">React</option>
              <option value="typescript">TypeScript</option>
              <option value="text">文本</option>
            </select>
          </div>
          <div class="form-group">
            <label>代码内容：</label>
            <div class="code-editor" 
                 @paste="handlePaste" 
                 @drop="handleDrop" 
                 @dragover.prevent>
              <textarea 
                v-model="uploadForm.code" 
                placeholder="请粘贴或输入代码内容..."
                class="code-textarea"
                @input="handleCodeInput"
              ></textarea>
              <div class="paste-tip">
                <span>💡 粘贴或输入代码到此区域</span>
                <span class="file-size-tip">📏 支持多种编程语言</span>
              </div>
            </div>
          </div>
          <div class="form-actions">
            <button @click="uploadCode" class="upload-submit-btn">新增</button>
            <button @click="closeUploadModal" class="cancel-btn">取消</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 更新模态框 -->
    <div v-if="showUpdateModal" class="upload-modal" @click="closeUpdateModal">
      <div class="upload-modal-content" @click.stop>
        <div class="modal-header">
          <h3>更新代码片段</h3>
          <button @click="closeUpdateModal" class="close-btn">✕</button>
        </div>
        <div class="upload-form">
          <div class="form-group">
            <label>标题：</label>
            <input v-model="updateForm.title" placeholder="请输入标题" class="form-input">
          </div>
          <div class="form-group">
            <label>描述：</label>
            <textarea v-model="updateForm.description" placeholder="请输入描述" class="form-textarea"></textarea>
          </div>
          <div class="form-group">
            <label>编程语言：</label>
            <select v-model="updateForm.language" class="form-select">
              <option value="javascript">JavaScript</option>
              <option value="java">Java</option>
              <option value="python">Python</option>
              <option value="cpp">C++</option>
              <option value="csharp">C#</option>
              <option value="php">PHP</option>
              <option value="go">Go</option>
              <option value="rust">Rust</option>
              <option value="sql">SQL</option>
              <option value="html">HTML</option>
              <option value="css">CSS</option>
              <option value="vue">Vue</option>
              <option value="react">React</option>
              <option value="typescript">TypeScript</option>
              <option value="text">文本</option>
            </select>
          </div>
          <div class="form-group">
            <label>代码内容：</label>
            <div class="code-editor">
              <textarea 
                v-model="updateForm.code" 
                placeholder="请粘贴或输入代码内容..."
                class="code-textarea"
              ></textarea>
            </div>
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
  name: 'CodeGallery',
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
    const uploadForm = reactive({
      title: '',
      description: '',
      language: 'javascript',
      code: ''
    })
    const updateForm = reactive({
      id: null,
      title: '',
      description: '',
      language: 'javascript',
      code: ''
    })

    // API接口配置 - 复用Gallery的接口
    const API_BASE_URL = '/api/gallery'
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
      GET_RECORD: `${API_BASE_URL}/records/:id`,
      // 上传代码文件
      UPLOAD_CODE: `${API_BASE_URL}/upload`
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

    // 获取代码片段列表
    const loadRecords = async (params = {}) => {
      loading.value = true
      try {
        const queryParams = new URLSearchParams(params)
        const url = `${API_ENDPOINTS.GET_RECORDS}?${queryParams}`
        const data = await apiRequest(url)
        codes.value = data.records || []
        filteredCodes.value = [...codes.value]
      } catch (error) {
        console.error('加载记录失败:', error)
        alert('加载记录失败，请重试')
      } finally {
        loading.value = false
      }
    }

    // 搜索代码片段
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
        filteredCodes.value = response.records || []
      } catch (error) {
        console.error('搜索失败:', error)
        // 如果搜索失败，使用本地过滤
        const localQuery = query.toLowerCase()
        filteredCodes.value = codes.value.filter(item => 
          item.title.toLowerCase().includes(localQuery) ||
          (item.description && item.description.toLowerCase().includes(localQuery)) ||
          (item.code && item.code.toLowerCase().includes(localQuery))
        )
      }
    }

    // 获取代码预览（前几行）
    const getCodePreview = (code) => {
      if (!code) return '暂无代码'
      const lines = code.split('\n')
      if (lines.length <= 5) {
        return code
      }
      return lines.slice(0, 5).join('\n') + '\n...'
    }

    // 新增代码片段
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

    // 删除代码片段
    const deleteRecord = async (recordId) => {
      if (!confirm('确定要删除这条代码片段吗？')) {
        return
      }
      
      try {
        const url = API_ENDPOINTS.DELETE_RECORD.replace(':id', recordId)
        await apiRequest(url, { method: 'DELETE' })
        await loadRecords() // 重新加载列表
        alert('删除成功！')
      } catch (error) {
        console.error('删除记录失败:', error)
        alert('删除失败，请重试')
      }
    }

    // 更新记录
    const updateRecord = (item) => {
      updateForm.id = item.id
      updateForm.title = item.title
      updateForm.description = item.description || ''
      updateForm.language = item.language || 'javascript'
      updateForm.code = item.code || ''
      showUpdateModal.value = true
    }

    // 提交更新
    const submitUpdate = async () => {
      if (!updateForm.title || !updateForm.code.trim()) {
        alert('请填写标题和代码内容')
        return
      }
      
      try {
        const url = API_ENDPOINTS.CREATE_RECORD.replace('/records', `/records/${updateForm.id}`)
        const recordData = {
          title: updateForm.title,
          description: updateForm.description,
          language: updateForm.language,
          code: updateForm.code
        }
        
        await apiRequest(url, {
          method: 'PUT',
          body: JSON.stringify(recordData)
        })
        
        await loadRecords() // 重新加载列表
        closeUpdateModal()
        alert('更新成功！')
      } catch (error) {
        console.error('更新记录失败:', error)
        alert('更新失败，请重试')
      }
    }

    const viewCode = (item) => {
      currentCode.value = item
      showCodeModal.value = true
    }
    
    const closeCodeModal = () => {
      showCodeModal.value = false
      currentCode.value = {}
    }
    
    const closeUploadModal = () => {
      showUploadModal.value = false
      uploadForm.title = ''
      uploadForm.description = ''
      uploadForm.language = 'javascript'
      uploadForm.code = ''
    }
    
    const closeUpdateModal = () => {
      showUpdateModal.value = false
      updateForm.id = null
      updateForm.title = ''
      updateForm.description = ''
      updateForm.language = 'javascript'
      updateForm.code = ''
    }
    
    // 显示卡片操作按钮
    const showCardActions = (cardId) => {
      hoveredCardId.value = cardId
    }
    
    // 隐藏卡片操作按钮
    const hideCardActions = (cardId) => {
      hoveredCardId.value = null
    }
    
    // 粘贴代码
    const handlePaste = (event) => {
      const clipboardData = event.clipboardData || event.originalEvent.clipboardData
      const pastedText = clipboardData.getData('text')
      if (pastedText) {
        uploadForm.code = pastedText
      }
    }
    
    // 拖拽代码文件
    const handleDrop = (event) => {
      event.preventDefault()
      const files = event.dataTransfer.files
      if (files.length > 0) {
        const file = files[0]
        if (file.type === 'text/plain' || file.name.endsWith('.txt') || file.name.endsWith('.js') || 
            file.name.endsWith('.java') || file.name.endsWith('.py') || file.name.endsWith('.cpp') ||
            file.name.endsWith('.cs') || file.name.endsWith('.php') || file.name.endsWith('.go') ||
            file.name.endsWith('.rs') || file.name.endsWith('.sql') || file.name.endsWith('.html') ||
            file.name.endsWith('.css') || file.name.endsWith('.vue') || file.name.endsWith('.tsx') ||
            file.name.endsWith('.ts')) {
          
          const reader = new FileReader()
          reader.onload = (e) => {
            uploadForm.code = e.target.result
            // 根据文件扩展名设置语言
            const ext = file.name.split('.').pop().toLowerCase()
            const languageMap = {
              'js': 'javascript',
              'java': 'java',
              'py': 'python',
              'cpp': 'cpp',
              'cs': 'csharp',
              'php': 'php',
              'go': 'go',
              'rs': 'rust',
              'sql': 'sql',
              'html': 'html',
              'css': 'css',
              'vue': 'vue',
              'tsx': 'react',
              'ts': 'typescript',
              'txt': 'text'
            }
            if (languageMap[ext]) {
              uploadForm.language = languageMap[ext]
            }
          }
          reader.readAsText(file)
        } else {
          alert('请拖拽文本文件或代码文件')
        }
      }
    }
    
    // 代码输入处理
    const handleCodeInput = () => {
      // 可以在这里添加代码高亮或其他处理逻辑
    }
    
    // 复制代码
    const copyCode = async () => {
      try {
        await navigator.clipboard.writeText(currentCode.value.code)
        alert('代码已复制到剪贴板')
      } catch (error) {
        console.error('复制失败:', error)
        // 降级方案
        const textArea = document.createElement('textarea')
        textArea.value = currentCode.value.code
        document.body.appendChild(textArea)
        textArea.select()
        document.execCommand('copy')
        document.body.removeChild(textArea)
        alert('代码已复制到剪贴板')
      }
    }
    
    // 搜索处理函数
    const handleSearch = () => {
      searchRecords(searchQuery.value)
    }
    
    // 新增代码片段
    const uploadCode = async () => {
      if (!uploadForm.title || !uploadForm.code.trim()) {
        alert('请填写标题和代码内容')
        return
      }
      
      try {
        const recordData = {
          title: uploadForm.title,
          description: uploadForm.description,
          language: uploadForm.language,
          code: uploadForm.code
        }
        
        await createRecord(recordData)
        await loadRecords() // 重新加载列表
        closeUploadModal()
        alert('代码片段新增成功！')
      } catch (error) {
        alert('新增记录失败，请重试')
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
      uploadForm,
      updateForm,
      viewCode,
      closeCodeModal,
      closeUploadModal,
      closeUpdateModal,
      showCardActions,
      hideCardActions,
      updateRecord,
      submitUpdate,
      handlePaste,
      handleDrop,
      handleCodeInput,
      copyCode,
      uploadCode,
      handleSearch,
      loadRecords,
      searchRecords,
      createRecord,
      deleteRecord,
      getCodePreview
    }
  }
}
</script>

<style scoped>
.code-gallery-container {
  min-height: 100vh;
  background-color: #f4f5f7;
}

.gallery-header {
  background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
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
  font-size: 1.5rem;
}

.logo-icon {
  font-size: 2rem;
}

.search-container {
  position: relative;
  display: flex;
  align-items: center;
}

.search-input {
  background: rgba(255, 255, 255, 0.9);
  border: none;
  padding: 0.5rem 2.5rem 0.5rem 1rem;
  border-radius: 20px;
  width: 250px;
  font-size: 0.9rem;
  outline: none;
  transition: all 0.3s ease;
}

.search-input:focus {
  background: white;
  box-shadow: 0 0 0 2px rgba(255, 255, 255, 0.3);
}

.search-input::placeholder {
  color: #666;
}

.search-icon {
  position: absolute;
  right: 0.75rem;
  color: #666;
  font-size: 0.9rem;
}

.upload-btn {
  background: #FF9800;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.3s ease;
}

.upload-btn:hover {
  background: #F57C00;
}

.main-content {
  display: flex;
  justify-content: center;
  padding: 2rem;
  width: 100%;
}

.code-list {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  padding: 2rem;
  width: 100%;
  max-width: 1400px;
}

.codes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 1.5rem;
  max-width: 1200px;
  margin: 0 auto;
}

.code-card {
  background: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  padding: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  height: 280px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.code-card:hover {
  box-shadow: 0 8px 25px rgba(0,0,0,0.12);
  transform: translateY(-2px);
}

.code-title {
  font-size: 1rem;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 0.5rem;
  text-align: center;
  line-height: 1.2;
  flex-shrink: 0;
}

.code-preview {
  flex: 1;
  position: relative;
  overflow: hidden;
  border-radius: 6px;
  background: #1e1e1e;
  border: 1px solid #e0e0e0;
  margin-bottom: 0.5rem;
}

.code-content {
  height: 100%;
  overflow: hidden;
  padding: 0.5rem;
}

.code-content pre {
  margin: 0;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  font-size: 0.8rem;
  line-height: 1.4;
  color: #d4d4d4;
  white-space: pre-wrap;
  word-break: break-all;
}

.code-language {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  background: rgba(33, 150, 243, 0.9);
  color: white;
  padding: 0.2rem 0.5rem;
  border-radius: 12px;
  font-size: 0.7rem;
  font-weight: bold;
}

.code-description {
  font-size: 0.8rem;
  color: #666;
  text-align: center;
  line-height: 1.3;
  flex-shrink: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 卡片操作按钮样式 */
.card-actions {
  position: absolute;
  bottom: 0.5rem;
  left: 0.5rem;
  display: flex;
  gap: 0.5rem;
  z-index: 10;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 0.3rem;
  padding: 0.4rem 0.6rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.7rem;
  font-weight: 500;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
  box-shadow: 0 2px 8px rgba(0,0,0,0.15);
}

.update-btn {
  background: rgba(76, 175, 80, 0.9);
  color: white;
}

.update-btn:hover {
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
  font-size: 0.8rem;
}

.btn-text {
  font-size: 0.7rem;
}

/* 全屏模态框样式 */
.fullscreen-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.95);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.fullscreen-content {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #1e1e1e;
}

.fullscreen-header {
  padding: 1rem 2rem;
  background: rgba(0,0,0,0.8);
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1001;
}

.fullscreen-header h3 {
  margin: 0;
  color: white;
  font-size: 1.2rem;
}

.header-actions {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.copy-btn {
  background: #4CAF50;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.3s ease;
}

.copy-btn:hover {
  background: #45a049;
}

.fullscreen-code-container {
  flex: 1;
  overflow-y: auto;
  padding: 80px 2rem 2rem 2rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.code-info {
  display: flex;
  gap: 1rem;
  align-items: center;
  margin-bottom: 1rem;
}

.language-tag {
  background: #2196F3;
  color: white;
  padding: 0.3rem 0.8rem;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: bold;
}

.description-text {
  color: #ccc;
  font-size: 0.9rem;
}

.code-viewer {
  background: #2d2d2d;
  border-radius: 8px;
  padding: 1.5rem;
  overflow-x: auto;
}

.code-viewer pre {
  margin: 0;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  font-size: 0.9rem;
  line-height: 1.5;
  color: #d4d4d4;
  white-space: pre-wrap;
  word-break: break-all;
}

.loading-container {
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
  border-top: 4px solid #2196F3;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.no-codes {
  text-align: center;
  padding: 3rem;
  color: #666;
}

.upload-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.upload-modal-content {
  background: white;
  border-radius: 12px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  margin: 0;
  color: #2c3e50;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #666;
  padding: 0.5rem;
  border-radius: 4px;
  transition: background-color 0.3s ease;
}

.close-btn:hover {
  background: #f0f0f0;
}

.upload-form {
  padding: 1.5rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #2c3e50;
  font-weight: 500;
}

.form-input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  outline: none;
  font-size: 1rem;
  transition: border-color 0.3s ease;
}

.form-input:focus {
  border-color: #2196F3;
}

.form-textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  outline: none;
  font-size: 1rem;
  min-height: 80px;
  resize: vertical;
  font-family: inherit;
  transition: border-color 0.3s ease;
}

.form-textarea:focus {
  border-color: #2196F3;
}

.form-select {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  outline: none;
  font-size: 1rem;
  background: white;
  transition: border-color 0.3s ease;
}

.form-select:focus {
  border-color: #2196F3;
}

.code-editor {
  position: relative;
  min-height: 200px;
  border: 2px dashed #e0e0e0;
  border-radius: 6px;
  padding: 1rem;
  background: #fafbfc;
  margin-bottom: 1rem;
}

.code-textarea {
  width: 100%;
  min-height: 180px;
  padding: 0.75rem;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  outline: none;
  font-size: 0.9rem;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  resize: vertical;
  background: #1e1e1e;
  color: #d4d4d4;
  transition: border-color 0.3s ease;
}

.code-textarea:focus {
  border-color: #2196F3;
}

.paste-tip {
  font-size: 0.9rem;
  color: #888;
  margin-bottom: 0.5rem;
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
}

.file-size-tip {
  font-size: 0.8rem;
  color: #666;
  font-style: italic;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
}

.upload-submit-btn {
  background: #2196F3;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.3s ease;
}

.upload-submit-btn:hover {
  background: #1976D2;
}

.cancel-btn {
  background: #f0f0f0;
  color: #666;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.3s ease;
}

.cancel-btn:hover {
  background: #e0e0e0;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .codes-grid {
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  }
}

@media (max-width: 900px) {
  .codes-grid {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  }
}

@media (max-width: 768px) {
  .gallery-header {
    padding: 1rem;
    flex-direction: column;
    gap: 1rem;
  }
  
  .header-left {
    width: 100%;
    justify-content: space-between;
    gap: 1rem;
  }
  
  .search-input {
    width: 200px;
  }
  
  .main-content {
    padding: 1rem;
  }
  
  .code-list {
    padding: 1rem;
    width: 100%;
    max-width: 100%;
  }
  
  .codes-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .code-card {
    height: 250px;
  }
  
  .fullscreen-header {
    padding: 1rem;
  }
  
  .fullscreen-header h3 {
    font-size: 1rem;
  }
  
  .fullscreen-code-container {
    padding: 70px 1rem 1rem 1rem;
  }
}

@media (max-width: 480px) {
  .code-card {
    height: 220px;
  }
  
  .header-actions {
    flex-direction: column;
    gap: 0.5rem;
  }
}
</style> 