<template>
  <div class="gallery-container">
    <div class="gallery-header">
      <div class="header-left">
        <div class="logo">
          <span class="logo-icon">🖼️</span>
          <span class="logo-text">AI问答记录</span>
        </div>
        <div class="search-container">
          <input 
            v-model="searchQuery" 
            @input="handleSearch"
            placeholder="搜索AI问答记录..." 
            class="search-input"
          >
          <span class="search-icon">🔍</span>
        </div>
      </div>
      <div class="header-right">
        <button @click="showUploadModal = true" class="upload-btn">新增记录</button>
      </div>
    </div>

    <div class="main-content">
      <div class="image-list">
        <div class="images-grid">
          <div v-if="loading" class="loading-container">
            <div class="loading-spinner"></div>
            <p>加载中...</p>
          </div>
          <div v-else-if="filteredImages.length === 0" class="no-images">
            <p>暂无AI问答记录</p>
          </div>
          <div v-else v-for="item in filteredImages" :key="item.id" @click="viewImage(item)" class="image-card">
            <div class="image-title">{{ item.title }}</div>
            <div class="image-preview">
              <img v-if="item.images && item.images.length > 0" :src="item.images[0].imageData" :alt="item.images[0].name" class="preview-image">
              <div v-else class="no-image-placeholder">
                <span>📷</span>
                <p>暂无图片</p>
              </div>
            </div>
            <div class="image-count" v-if="item.images && item.images.length > 1">
              <span>+{{ item.images.length - 1 }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 全屏图片展示模态框 -->
    <div v-if="showImageModal" class="fullscreen-modal" @click="closeImageModal">
      <div class="fullscreen-content" @click.stop>
        <div class="fullscreen-header">
          <h3>{{ currentImage.title }}</h3>
          <button @click="closeImageModal" class="close-btn">✕</button>
        </div>
        <div class="fullscreen-image-container">
          <div v-for="(img, idx) in currentImage.images" :key="idx" class="fullscreen-image-wrapper">
            <img :src="img.imageData" :alt="img.name" class="fullscreen-image">
          </div>
        </div>
      </div>
    </div>

    <div v-if="showUploadModal" class="upload-modal" @click="closeUploadModal">
      <div class="upload-modal-content" @click.stop>
        <div class="modal-header">
          <h3>新增AI问答记录</h3>
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
            <label>图片：</label>
            <div class="answer-editor" 
                 @paste="handlePaste" 
                 @drop="handleDrop" 
                 @dragover.prevent>
              <textarea style="display:none"></textarea>
              <div class="paste-tip">
                <span>💡 粘贴或拖拽图片到此区域</span>
              </div>
              <div v-if="uploadForm.images && uploadForm.images.length > 0" class="answer-images">
                <div class="image-list">
                  <div v-for="(image, index) in uploadForm.images" :key="index" class="image-item">
                    <img :src="image.imageData" :alt="image.name" class="preview-image">
                    <div class="image-actions">
                      <button @click="removeImage(index)" class="remove-btn">删除</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="form-actions">
            <button @click="uploadImage" class="upload-submit-btn">新增</button>
            <button @click="closeUploadModal" class="cancel-btn">取消</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'

export default {
  name: 'Gallery',
  setup() {
    const showImageModal = ref(false)
    const showUploadModal = ref(false)
    const currentImage = ref({})
    const loading = ref(false)
    const images = ref([])
    const searchQuery = ref('')
    const filteredImages = ref([])
    const uploadForm = reactive({
      title: '',
      description: '',
      images: []
    })

    // API接口配置
    const API_BASE_URL = '/api/gallery'
    const API_ENDPOINTS = {
      // 获取AI问答记录列表
      GET_RECORDS: `${API_BASE_URL}/records`,
      // 搜索AI问答记录
      SEARCH_RECORDS: `${API_BASE_URL}/records/search`,
      // 新增AI问答记录
      CREATE_RECORD: `${API_BASE_URL}/records`,
      // 删除AI问答记录
      DELETE_RECORD: `${API_BASE_URL}/records/:id`,
      // 获取单个记录详情
      GET_RECORD: `${API_BASE_URL}/records/:id`,
      // 上传图片
      UPLOAD_IMAGE: `${API_BASE_URL}/upload`
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

    // 获取AI问答记录列表
    const loadRecords = async (params = {}) => {
      loading.value = true
      try {
        const queryParams = new URLSearchParams(params)
        const url = `${API_ENDPOINTS.GET_RECORDS}?${queryParams}`
        const data = await apiRequest(url)
        images.value = data.records || []
        filteredImages.value = [...images.value]
      } catch (error) {
        console.error('加载记录失败:', error)
        alert('加载记录失败，请重试')
      } finally {
        loading.value = false
      }
    }

    // 搜索AI问答记录
    const searchRecords = async (query) => {
      if (!query.trim()) {
        filteredImages.value = [...images.value]
        return
      }
      
      try {
        const response = await apiRequest(API_ENDPOINTS.SEARCH_RECORDS, {
          method: 'POST',
          body: JSON.stringify({ query })
        })
        filteredImages.value = response.records || []
      } catch (error) {
        console.error('搜索失败:', error)
        // 如果搜索失败，使用本地过滤
        const localQuery = query.toLowerCase()
        filteredImages.value = images.value.filter(item => 
          item.title.toLowerCase().includes(localQuery)
        )
      }
    }

    // 上传图片到服务器
    const uploadImageToServer = async (file) => {
      const formData = new FormData()
      formData.append('image', file)
      
      try {
        const response = await fetch(API_ENDPOINTS.UPLOAD_IMAGE, {
          method: 'POST',
          body: formData
        })
        
        if (!response.ok) {
          throw new Error(`上传失败: ${response.status}`)
        }
        
        const result = await response.json()
        return {
          imageData: result.imageData,
          name: file.name,
          id: result.id
        }
      } catch (error) {
        console.error('图片上传失败:', error)
        throw error
      }
    }

    // 新增AI问答记录
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

    // 删除AI问答记录
    const deleteRecord = async (recordId) => {
      try {
        const url = API_ENDPOINTS.DELETE_RECORD.replace(':id', recordId)
        await apiRequest(url, { method: 'DELETE' })
      } catch (error) {
        console.error('删除记录失败:', error)
        throw error
      }
    }

    const viewImage = (item) => {
      currentImage.value = item
      showImageModal.value = true
    }
    const closeImageModal = () => {
      showImageModal.value = false
      currentImage.value = {}
    }
    const closeUploadModal = () => {
      showUploadModal.value = false
      uploadForm.title = ''
      uploadForm.description = ''
      uploadForm.images = []
    }
    // 粘贴图片
    const handlePaste = async (event) => {
      const items = (event.clipboardData || event.originalEvent.clipboardData).items;
      for (let i = 0; i < items.length; i++) {
        if (items[i].kind === 'file') {
          const file = items[i].getAsFile();
          if (file && file.type.startsWith('image/')) {
            try {
              const uploadedImage = await uploadImageToServer(file);
              uploadForm.images.push(uploadedImage);
            } catch (error) {
              alert('图片上传失败，请重试');
            }
          }
        }
      }
    };
    
    // 拖拽图片
    const handleDrop = async (event) => {
      event.preventDefault();
      const files = event.dataTransfer.files;
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        if (file.type.startsWith('image/')) {
          try {
            const uploadedImage = await uploadImageToServer(file);
            uploadForm.images.push(uploadedImage);
          } catch (error) {
            alert('图片上传失败，请重试');
          }
        }
      }
    };
    
    // 移除图片
    const removeImage = (index) => {
      uploadForm.images.splice(index, 1);
    };
    
    // 搜索处理函数
    const handleSearch = () => {
      searchRecords(searchQuery.value);
    };
    
    // 新增AI问答记录
    const uploadImage = async () => {
      if (!uploadForm.title || uploadForm.images.length === 0) {
        alert('请填写标题并添加图片')
        return
      }
      
      try {
        const recordData = {
          title: uploadForm.title,
          description: uploadForm.description,
          images: uploadForm.images
        }
        
        await createRecord(recordData);
        await loadRecords(); // 重新加载列表
        closeUploadModal();
        alert('AI问答记录新增成功！');
      } catch (error) {
        alert('新增记录失败，请重试');
      }
    }
    onMounted(() => {
      loadRecords()
    })
    return {
      showImageModal,
      showUploadModal,
      currentImage,
      loading,
      images,
      searchQuery,
      filteredImages,
      uploadForm,
      viewImage,
      closeImageModal,
      closeUploadModal,
      handlePaste,
      handleDrop,
      removeImage,
      uploadImage,
      handleSearch,
      loadRecords,
      searchRecords,
      createRecord,
      deleteRecord,
      uploadImageToServer
    }
  }
}
</script>

<style scoped>
.gallery-container {
  min-height: 100vh;
  background-color: #f4f5f7;
}
.gallery-header {
  background: linear-gradient(135deg, #4CAF50 0%, #2E7D32 100%);
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
.image-list {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  padding: 2rem;
  width: 100%;
  max-width: 1400px;
}
.images-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.5rem;
  max-width: 1200px;
  margin: 0 auto;
}
.image-card {
  background: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  padding: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  height: 200px;
  display: flex;
  flex-direction: column;
}
.image-card:hover {
  box-shadow: 0 8px 25px rgba(0,0,0,0.12);
  transform: translateY(-2px);
}
.image-title {
  font-size: 0.9rem;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 0.5rem;
  text-align: center;
  line-height: 1.2;
  flex-shrink: 0;
}
.image-preview {
  flex: 1;
  position: relative;
  overflow: hidden;
  border-radius: 6px;
  background: #fff;
  border: 1px solid #e0e0e0;
}
.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.no-image-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #999;
  font-size: 0.8rem;
}
.no-image-placeholder span {
  font-size: 2rem;
  margin-bottom: 0.5rem;
}
.image-count {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  background: rgba(0,0,0,0.7);
  color: white;
  padding: 0.2rem 0.5rem;
  border-radius: 12px;
  font-size: 0.7rem;
  font-weight: bold;
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
  background: #000;
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
.fullscreen-image-container {
  flex: 1;
  overflow-y: auto;
  padding: 80px 2rem 2rem 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
}
.fullscreen-image-wrapper {
  width: 100%;
  max-width: 800px;
  display: flex;
  justify-content: center;
}
.fullscreen-image {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.3);
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
  border-top: 4px solid #4CAF50;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}
@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
.no-images {
  text-align: center;
  padding: 3rem;
  color: #666;
}

.image-modal {
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
.image-modal-content {
  background: white;
  border-radius: 12px;
  max-width: 90vw;
  max-height: 90vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
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
.image-viewer {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1rem;
  overflow: auto;
}
.image-simple-list-modal {
  display: flex;
  gap: 1.5rem;
  flex-wrap: wrap;
  justify-content: center;
}
.simple-image-modal {
  width: 320px;
  height: 220px;
  object-fit: contain;
  border-radius: 8px;
  background: #fff;
  border: 1px solid #e0e0e0;
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
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
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
  border-color: #4CAF50;
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
  border-color: #4CAF50;
}
.answer-editor {
  position: relative;
  min-height: 120px;
  border: 2px dashed #e0e0e0;
  border-radius: 6px;
  padding: 1rem;
  background: #fafbfc;
  margin-bottom: 1rem;
}
.paste-tip {
  font-size: 0.9rem;
  color: #888;
  margin-bottom: 0.5rem;
}
.answer-images {
  margin-top: 1rem;
}
.image-item {
  position: relative;
  background: white;
  border-radius: 6px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  margin-bottom: 1rem;
}
.preview-image {
  width: 100%;
  height: 100px;
  object-fit: cover;
}
.image-actions {
  padding: 0.5rem;
  display: flex;
  justify-content: center;
}
.remove-btn {
  background: #ff4757;
  color: white;
  border: none;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.8rem;
  transition: background-color 0.3s ease;
}
.remove-btn:hover {
  background: #ff3742;
}
.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
}
.upload-submit-btn {
  background: #4CAF50;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.3s ease;
}
.upload-submit-btn:hover {
  background: #3aa876;
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
  .images-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 900px) {
  .images-grid {
    grid-template-columns: repeat(2, 1fr);
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
  .image-list {
    padding: 1rem;
    width: 100%;
    max-width: 100%;
  }
  .images-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 1rem;
  }
  .image-card {
    height: 180px;
  }
  .fullscreen-header {
    padding: 1rem;
  }
  .fullscreen-header h3 {
    font-size: 1rem;
  }
  .fullscreen-image-container {
    padding: 70px 1rem 1rem 1rem;
  }
}

@media (max-width: 480px) {
  .images-grid {
    grid-template-columns: 1fr;
  }
  .image-card {
    height: 160px;
  }
}
</style>