<template>
  <div class="video-gallery-container">
    <div class="gallery-header">
      <div class="header-left">
        <div class="logo">
          <span class="logo-icon">🎬</span>
          <span class="logo-text">哔哩哔哩</span>
        </div>
        <div class="search-container">
          <input 
            v-model="searchQuery" 
            @input="handleSearch"
            placeholder="搜索哔哩哔哩视频..." 
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
      <div class="video-list">
        <div class="videos-grid">
          <div v-if="loading" class="loading-container">
            <div class="loading-spinner"></div>
            <p>加载中...</p>
          </div>
          <div v-else-if="filteredVideos.length === 0" class="no-videos">
            <p>暂无哔哩哔哩视频</p>
          </div>
          <div v-else v-for="item in filteredVideos" :key="item.id" class="video-card" @mouseenter="showCardActions(item.id)" @mouseleave="hideCardActions(item.id)">
            <div class="video-title">{{ item.title }}</div>
            <div class="video-preview" @click="viewVideo(item)">
              <video v-if="item.videos && item.videos.length > 0" :src="item.videos[0].videoData" class="preview-video" preload="metadata">
                <source :src="item.videos[0].videoData" type="video/mp4">
                您的浏览器不支持视频播放
              </video>
              <div v-else class="no-video-placeholder">
                <span>🎬</span>
                <p>暂无视频</p>
              </div>
            </div>
            <div class="video-count" v-if="item.videos && item.videos.length > 1">
              <span>+{{ item.videos.length - 1 }}</span>
            </div>
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

    <!-- 全屏视频展示模态框 -->
    <div v-if="showVideoModal" class="fullscreen-modal" @click="closeVideoModal">
      <div class="fullscreen-content" @click.stop>
        <div class="fullscreen-header">
          <h3>{{ currentVideo.title }}</h3>
          <button @click="closeVideoModal" class="close-btn">✕</button>
        </div>
        <div class="fullscreen-video-container">
          <div v-for="(video, idx) in currentVideo.videos" :key="idx" class="fullscreen-video-wrapper">
            <video :src="video.videoData" :alt="video.name" class="fullscreen-video" controls>
              <source :src="video.videoData" type="video/mp4">
              您的浏览器不支持视频播放
            </video>
          </div>
        </div>
      </div>
    </div>

    <div v-if="showUploadModal" class="upload-modal" @click="closeUploadModal">
      <div class="upload-modal-content" @click.stop>
        <div class="modal-header">
          <h3>新增哔哩哔哩视频</h3>
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
            <label>视频：</label>
            <div class="video-editor" 
                 @paste="handlePaste" 
                 @drop="handleDrop" 
                 @dragover.prevent>
              <textarea style="display:none"></textarea>
              <div class="paste-tip">
                <span>💡 拖拽视频文件到此区域</span>
              </div>
              <div v-if="uploadForm.videos && uploadForm.videos.length > 0" class="video-list">
                <div v-for="(video, index) in uploadForm.videos" :key="index" class="video-item">
                  <video :src="video.videoData" :alt="video.name" class="preview-video" preload="metadata">
                    <source :src="video.videoData" type="video/mp4">
                  </video>
                  <div class="video-actions">
                    <button @click="removeVideo(index)" class="remove-btn">删除</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="form-actions">
            <button @click="uploadVideo" class="upload-submit-btn">新增</button>
            <button @click="closeUploadModal" class="cancel-btn">取消</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 更新模态框 -->
    <div v-if="showUpdateModal" class="upload-modal" @click="closeUpdateModal">
      <div class="upload-modal-content" @click.stop>
        <div class="modal-header">
          <h3>更新哔哩哔哩视频</h3>
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
            <label>视频：</label>
            <div class="video-editor" 
                 @paste="handleUpdatePaste" 
                 @drop="handleUpdateDrop" 
                 @dragover.prevent>
              <textarea style="display:none"></textarea>
              <div class="paste-tip">
                <span>💡 拖拽视频文件到此区域</span>
              </div>
              <div v-if="updateForm.videos && updateForm.videos.length > 0" class="video-list">
                <div v-for="(video, index) in updateForm.videos" :key="index" class="video-item">
                  <video :src="video.videoData" :alt="video.name" class="preview-video" preload="metadata">
                    <source :src="video.videoData" type="video/mp4">
                  </video>
                  <div class="video-actions">
                    <button @click="removeUpdateVideo(index)" class="remove-btn">删除</button>
                  </div>
                </div>
              </div>
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
  name: 'VideoGallery',
  setup() {
    const showVideoModal = ref(false)
    const showUploadModal = ref(false)
    const showUpdateModal = ref(false)
    const currentVideo = ref({})
    const loading = ref(false)
    const videos = ref([])
    const searchQuery = ref('')
    const filteredVideos = ref([])
    const hoveredCardId = ref(null)
    const uploadForm = reactive({
      title: '',
      description: '',
      videos: []
    })
    const updateForm = reactive({
      id: null,
      title: '',
      description: '',
      videos: []
    })

    // API接口配置
    const API_BASE_URL = '/api/videogallery'
    const API_ENDPOINTS = {
      // 获取AI视频记录列表
      GET_RECORDS: `${API_BASE_URL}/records`,
      // 搜索AI视频记录
      SEARCH_RECORDS: `${API_BASE_URL}/records/search`,
      // 新增AI视频记录
      CREATE_RECORD: `${API_BASE_URL}/records`,
      // 删除AI视频记录
      DELETE_RECORD: `${API_BASE_URL}/records/:id`,
      // 获取单个记录详情
      GET_RECORD: `${API_BASE_URL}/records/:id`,
      // 上传视频
      UPLOAD_VIDEO: `${API_BASE_URL}/upload`
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

    // 获取AI视频记录列表
    const loadRecords = async (params = {}) => {
      loading.value = true
      try {
        const queryParams = new URLSearchParams(params)
        const url = `${API_ENDPOINTS.GET_RECORDS}?${queryParams}`
        const data = await apiRequest(url)
        videos.value = data.records || []
        filteredVideos.value = [...videos.value]
      } catch (error) {
        console.error('加载记录失败:', error)
        alert('加载记录失败，请重试')
      } finally {
        loading.value = false
      }
    }

    // 搜索AI视频记录
    const searchRecords = async (query) => {
      if (!query.trim()) {
        filteredVideos.value = [...videos.value]
        return
      }
      
      try {
        const response = await apiRequest(API_ENDPOINTS.SEARCH_RECORDS, {
          method: 'POST',
          body: JSON.stringify({ query })
        })
        filteredVideos.value = response.records || []
      } catch (error) {
        console.error('搜索失败:', error)
        // 如果搜索失败，使用本地过滤
        const localQuery = query.toLowerCase()
        filteredVideos.value = videos.value.filter(item => 
          item.title.toLowerCase().includes(localQuery)
        )
      }
    }

    // 上传视频到服务器
    const uploadVideoToServer = async (file) => {
      const formData = new FormData()
      formData.append('video', file)
      
      try {
        const response = await fetch(API_ENDPOINTS.UPLOAD_VIDEO, {
          method: 'POST',
          body: formData
        })
        
        if (!response.ok) {
          throw new Error(`上传失败: ${response.status}`)
        }
        
        const result = await response.json()
        return {
          videoData: result.videoData,
          name: file.name,
          id: result.id
        }
      } catch (error) {
        console.error('视频上传失败:', error)
        throw error
      }
    }

    // 新增AI视频记录
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

    // 删除AI视频记录
    const deleteRecord = async (recordId) => {
      if (!confirm('确定要删除这条记录吗？')) {
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

    const viewVideo = (item) => {
      currentVideo.value = item
      showVideoModal.value = true
    }
    const closeVideoModal = () => {
      showVideoModal.value = false
      currentVideo.value = {}
    }
    const closeUploadModal = () => {
      showUploadModal.value = false
      uploadForm.title = ''
      uploadForm.description = ''
      uploadForm.videos = []
    }
    const closeUpdateModal = () => {
      showUpdateModal.value = false
      updateForm.id = null
      updateForm.title = ''
      updateForm.description = ''
      updateForm.videos = []
    }
    // 显示卡片操作按钮
    const showCardActions = (cardId) => {
      hoveredCardId.value = cardId
    }
    // 隐藏卡片操作按钮
    const hideCardActions = (cardId) => {
      hoveredCardId.value = null
    }
    
    // 拖拽视频
    const handleDrop = async (event) => {
      event.preventDefault();
      const files = event.dataTransfer.files;
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        if (file.type.startsWith('video/')) {
          try {
            const uploadedVideo = await uploadVideoToServer(file);
            uploadForm.videos.push(uploadedVideo);
          } catch (error) {
            alert('视频上传失败，请重试');
          }
        }
      }
    };
    
    // 移除视频
    const removeVideo = (index) => {
      uploadForm.videos.splice(index, 1);
    };
    
    // 移除更新模态框中的视频
    const removeUpdateVideo = (index) => {
      updateForm.videos.splice(index, 1);
    };
    
    // 更新模态框拖拽视频
    const handleUpdateDrop = async (event) => {
      event.preventDefault();
      const files = event.dataTransfer.files;
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        if (file.type.startsWith('video/')) {
          try {
            const uploadedVideo = await uploadVideoToServer(file);
            updateForm.videos.push(uploadedVideo);
          } catch (error) {
            alert('视频上传失败，请重试');
          }
        }
      }
    };
    
    // 搜索处理函数
    const handleSearch = () => {
      searchRecords(searchQuery.value);
    };
    
    // 新增AI视频记录
    const uploadVideo = async () => {
      if (!uploadForm.title || uploadForm.videos.length === 0) {
        alert('请填写标题并添加视频')
        return
      }
      
      try {
        const recordData = {
          title: uploadForm.title,
          description: uploadForm.description,
          videos: uploadForm.videos
        }
        
        await createRecord(recordData);
        await loadRecords(); // 重新加载列表
        closeUploadModal();
        alert('哔哩哔哩视频新增成功！');
      } catch (error) {
        alert('新增记录失败，请重试');
      }
    }

    // 更新记录
    const updateRecord = (item) => {
      updateForm.id = item.id
      updateForm.title = item.title
      updateForm.description = item.description || ''
      updateForm.videos = [...(item.videos || [])]
      showUpdateModal.value = true
    }

    // 提交更新
    const submitUpdate = async () => {
      if (!updateForm.title || updateForm.videos.length === 0) {
        alert('请填写标题并添加视频')
        return
      }
      
      try {
        const url = API_ENDPOINTS.CREATE_RECORD.replace('/records', `/records/${updateForm.id}`)
        const recordData = {
          title: updateForm.title,
          description: updateForm.description,
          videos: updateForm.videos
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

    onMounted(() => {
      loadRecords()
    })
    return {
      showVideoModal,
      showUploadModal,
      showUpdateModal,
      currentVideo,
      loading,
      videos,
      searchQuery,
      filteredVideos,
      hoveredCardId,
      uploadForm,
      updateForm,
      viewVideo,
      closeVideoModal,
      closeUploadModal,
      closeUpdateModal,
      showCardActions,
      hideCardActions,
      updateRecord,
      submitUpdate,
      handleDrop,
      handleUpdateDrop,
      removeVideo,
      removeUpdateVideo,
      uploadVideo,
      handleSearch,
      loadRecords,
      searchRecords,
      createRecord,
      deleteRecord,
      uploadVideoToServer
    }
  }
}
</script>

<style scoped>
.video-gallery-container {
  min-height: 100vh;
  background-color: #f4f5f7;
}
.gallery-header {
  background: linear-gradient(135deg, #9C27B0 0%, #673AB7 100%);
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
.video-list {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  padding: 2rem;
  width: 100%;
  max-width: 1400px;
}
.videos-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.5rem;
  max-width: 1200px;
  margin: 0 auto;
}
.video-card {
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
  overflow: hidden;
}
.video-card:hover {
  box-shadow: 0 8px 25px rgba(0,0,0,0.12);
  transform: translateY(-2px);
}
.video-title {
  font-size: 0.9rem;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 0.5rem;
  text-align: center;
  line-height: 1.2;
  flex-shrink: 0;
}
.video-preview {
  flex: 1;
  position: relative;
  overflow: hidden;
  border-radius: 6px;
  background: #fff;
  border: 1px solid #e0e0e0;
}
.preview-video {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 6px;
}
.no-video-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #999;
  font-size: 0.8rem;
}
.no-video-placeholder span {
  font-size: 2rem;
  margin-bottom: 0.5rem;
}
.video-count {
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
  background: rgba(156, 39, 176, 0.9);
  color: white;
}

.update-btn:hover {
  background: rgba(156, 39, 176, 1);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(156, 39, 176, 0.3);
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
.fullscreen-video-container {
  flex: 1;
  overflow-y: auto;
  padding: 80px 2rem 2rem 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
}
.fullscreen-video-wrapper {
  width: 100%;
  max-width: 800px;
  display: flex;
  justify-content: center;
}
.fullscreen-video {
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
  border-top: 4px solid #9C27B0;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}
@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
.no-videos {
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
  border-color: #9C27B0;
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
  border-color: #9C27B0;
}
.video-editor {
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
.video-list {
  margin-top: 1rem;
}
.video-item {
  position: relative;
  background: white;
  border-radius: 6px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  margin-bottom: 1rem;
}
.preview-video {
  width: 100%;
  height: 100px;
  object-fit: cover;
}
.video-actions {
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
  background: #9C27B0;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.3s ease;
}
.upload-submit-btn:hover {
  background: #7B1FA2;
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
  .videos-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 900px) {
  .videos-grid {
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
  .video-list {
    padding: 1rem;
    width: 100%;
    max-width: 100%;
  }
  .videos-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 1rem;
  }
  .video-card {
    height: 180px;
  }
  .fullscreen-header {
    padding: 1rem;
  }
  .fullscreen-header h3 {
    font-size: 1rem;
  }
  .fullscreen-video-container {
    padding: 70px 1rem 1rem 1rem;
  }
}

@media (max-width: 480px) {
  .videos-grid {
    grid-template-columns: 1fr;
  }
  .video-card {
    height: 160px;
  }
}
</style> 