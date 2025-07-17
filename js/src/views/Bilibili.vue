<template>
  <div class="bilibili-container">
    <!-- 顶部导航栏 -->
    <div class="bilibili-header">
      <div class="header-left">
        <div class="logo">
          <span class="logo-icon">📺</span>
          <span class="logo-text">Bilibili</span>
        </div>
        <div class="search-bar">
          <input 
            v-model="searchKeyword" 
            @keyup.enter="searchVideos"
            placeholder="搜索视频..."
            class="search-input"
          >
          <button @click="searchVideos" class="search-btn">🔍</button>
        </div>
      </div>
      <div class="header-right">
        <button @click="showUploadModal = true" class="upload-btn">上传视频</button>
        <div class="user-avatar">👤</div>
      </div>
    </div>

    <!-- 分类标签 -->
    <div class="category-tabs">
      <button 
        v-for="category in categories" 
        :key="category.id"
        @click="selectCategory(category)"
        :class="['category-tab', { active: selectedCategory.id === category.id }]"
      >
        {{ category.name }}
      </button>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧视频列表 -->
      <div class="video-list">
        <div class="list-header">
          <h2>{{ selectedCategory.name }}</h2>
          <div class="sort-options">
            <select v-model="sortBy" @change="sortVideos" class="sort-select">
              <option value="latest">最新</option>
              <option value="popular">最热</option>
              <option value="views">播放量</option>
            </select>
          </div>
        </div>
        
        <div class="videos-grid">
          <div 
            v-for="video in filteredVideos" 
            :key="video.id"
            @click="playVideo(video)"
            class="video-card"
          >
            <div class="video-thumbnail">
              <img :src="video.thumbnail" :alt="video.title" class="thumbnail-img">
              <div class="video-duration">{{ video.duration }}</div>
              <div class="play-overlay">▶</div>
            </div>
            <div class="video-info">
              <h3 class="video-title">{{ video.title }}</h3>
              <div class="video-meta">
                <span class="author">{{ video.author }}</span>
                <span class="views">{{ formatViews(video.views) }}播放</span>
                <span class="time">{{ formatTime(video.uploadTime) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧推荐区域 -->
      <div class="sidebar">
        <div class="recommended-section">
          <h3>推荐视频</h3>
          <div class="recommended-videos">
            <div 
              v-for="video in recommendedVideos" 
              :key="video.id"
              @click="playVideo(video)"
              class="recommended-video"
            >
              <img :src="video.thumbnail" :alt="video.title" class="rec-thumbnail">
              <div class="rec-info">
                <h4>{{ video.title }}</h4>
                <p>{{ video.author }}</p>
                <span>{{ formatViews(video.views) }}播放</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 视频播放模态框 -->
    <div v-if="showVideoModal" class="video-modal" @click="closeVideoModal">
      <div class="video-modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ currentVideo.title }}</h3>
          <button @click="closeVideoModal" class="close-btn">✕</button>
        </div>
        <div class="video-player">
          <div class="player-placeholder">
            <div class="play-icon">▶</div>
            <p>视频播放区域</p>
          </div>
        </div>
        <div class="video-details">
          <div class="video-stats">
            <span>{{ formatViews(currentVideo.views) }}播放</span>
            <span>{{ formatTime(currentVideo.uploadTime) }}</span>
            <button class="like-btn" @click="toggleLike">
              {{ currentVideo.isLiked ? '❤️' : '🤍' }} {{ currentVideo.likes }}
            </button>
            <button class="share-btn">📤 分享</button>
          </div>
          <div class="video-description">
            <h4>视频简介</h4>
            <p>{{ currentVideo.description }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 上传视频模态框 -->
    <div v-if="showUploadModal" class="upload-modal" @click="closeUploadModal">
      <div class="upload-modal-content" @click.stop>
        <div class="modal-header">
          <h3>上传视频</h3>
          <button @click="closeUploadModal" class="close-btn">✕</button>
        </div>
        <div class="upload-form">
          <div class="form-group">
            <label>视频标题：</label>
            <input v-model="uploadForm.title" placeholder="请输入视频标题" class="form-input">
          </div>
          <div class="form-group">
            <label>视频描述：</label>
            <textarea v-model="uploadForm.description" placeholder="请输入视频描述" class="form-textarea"></textarea>
          </div>
          <div class="form-group">
            <label>分类：</label>
            <select v-model="uploadForm.category" class="form-select">
              <option value="">请选择分类</option>
              <option v-for="category in categories" :key="category.id" :value="category.id">
                {{ category.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>上传视频：</label>
            <div class="file-upload">
              <input type="file" @change="handleFileUpload" accept="video/*" class="file-input">
              <div class="upload-placeholder">
                <span>📁 选择视频文件</span>
              </div>
            </div>
          </div>
          <div class="form-actions">
            <button @click="uploadVideo" class="upload-submit-btn">上传视频</button>
            <button @click="closeUploadModal" class="cancel-btn">取消</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'

export default {
  name: 'Bilibili',
  setup() {
    // 响应式数据
    const searchKeyword = ref('')
    const sortBy = ref('latest')
    const showVideoModal = ref(false)
    const showUploadModal = ref(false)
    const currentVideo = ref({})
    
    // 分类数据
    const categories = ref([
      { id: 'all', name: '全部' },
      { id: 'game', name: '游戏' },
      { id: 'music', name: '音乐' },
      { id: 'animation', name: '动画' },
      { id: 'knowledge', name: '知识' },
      { id: 'life', name: '生活' },
      { id: 'tech', name: '科技' },
      { id: 'food', name: '美食' }
    ])
    
    const selectedCategory = ref(categories.value[0])
    
    // 视频数据
    const videos = ref([
      {
        id: 1,
        title: 'Vue.js 3.0 完整教程',
        author: '前端大师',
        views: 125000,
        likes: 3200,
        duration: '15:30',
        uploadTime: '2024-01-15',
        category: 'tech',
        thumbnail: 'https://via.placeholder.com/300x200/42b883/ffffff?text=Vue.js',
        description: '从零开始学习Vue.js 3.0，包含响应式原理、组合式API等核心概念。',
        isLiked: false
      },
      {
        id: 2,
        title: 'Spring Boot 实战开发',
        author: 'Java工程师',
        views: 89000,
        likes: 2100,
        duration: '22:15',
        uploadTime: '2024-01-14',
        category: 'tech',
        thumbnail: 'https://via.placeholder.com/300x200/ff6b6b/ffffff?text=Spring',
        description: '使用Spring Boot快速构建Web应用，包含数据库操作、REST API等。',
        isLiked: true
      },
      {
        id: 3,
        title: '算法可视化演示',
        author: '算法达人',
        views: 156000,
        likes: 4500,
        duration: '18:45',
        uploadTime: '2024-01-13',
        category: 'knowledge',
        thumbnail: 'https://via.placeholder.com/300x200/4ecdc4/ffffff?text=算法',
        description: '通过动画演示各种经典算法的工作原理，让算法学习变得简单有趣。',
        isLiked: false
      },
      {
        id: 4,
        title: '美食制作教程',
        author: '美食博主',
        views: 234000,
        likes: 6700,
        duration: '12:30',
        uploadTime: '2024-01-12',
        category: 'food',
        thumbnail: 'https://via.placeholder.com/300x200/ffa726/ffffff?text=美食',
        description: '教你制作各种美味佳肴，从家常菜到高级料理，应有尽有。',
        isLiked: false
      },
      {
        id: 5,
        title: '游戏实况解说',
        author: '游戏主播',
        views: 345000,
        likes: 8900,
        duration: '25:20',
        uploadTime: '2024-01-11',
        category: 'game',
        thumbnail: 'https://via.placeholder.com/300x200/9c27b0/ffffff?text=游戏',
        description: '最新游戏实况，精彩操作和搞笑解说，让你笑到停不下来。',
        isLiked: true
      },
      {
        id: 6,
        title: '音乐翻唱合集',
        author: '音乐人',
        views: 178000,
        likes: 5200,
        duration: '20:15',
        uploadTime: '2024-01-10',
        category: 'music',
        thumbnail: 'https://via.placeholder.com/300x200/ff5722/ffffff?text=音乐',
        description: '经典歌曲翻唱，独特的嗓音和编曲，带来不一样的音乐体验。',
        isLiked: false
      }
    ])
    
    // 推荐视频
    const recommendedVideos = computed(() => {
      return videos.value.slice(0, 5)
    })
    
    // 过滤后的视频
    const filteredVideos = computed(() => {
      let filtered = videos.value
      
      // 按分类过滤
      if (selectedCategory.value.id !== 'all') {
        filtered = filtered.filter(video => video.category === selectedCategory.value.id)
      }
      
      // 按搜索关键词过滤
      if (searchKeyword.value) {
        filtered = filtered.filter(video => 
          video.title.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
          video.author.toLowerCase().includes(searchKeyword.value.toLowerCase())
        )
      }
      
      // 排序
      switch (sortBy.value) {
        case 'latest':
          filtered.sort((a, b) => new Date(b.uploadTime) - new Date(a.uploadTime))
          break
        case 'popular':
          filtered.sort((a, b) => b.likes - a.likes)
          break
        case 'views':
          filtered.sort((a, b) => b.views - a.views)
          break
      }
      
      return filtered
    })
    
    // 上传表单
    const uploadForm = reactive({
      title: '',
      description: '',
      category: '',
      file: null
    })
    
    // 方法
    const searchVideos = () => {
      // 搜索逻辑已在computed中实现
    }
    
    const selectCategory = (category) => {
      selectedCategory.value = category
    }
    
    const sortVideos = () => {
      // 排序逻辑已在computed中实现
    }
    
    const playVideo = (video) => {
      currentVideo.value = video
      showVideoModal.value = true
    }
    
    const closeVideoModal = () => {
      showVideoModal.value = false
      currentVideo.value = {}
    }
    
    const toggleLike = () => {
      currentVideo.value.isLiked = !currentVideo.value.isLiked
      if (currentVideo.value.isLiked) {
        currentVideo.value.likes++
      } else {
        currentVideo.value.likes--
      }
    }
    
    const closeUploadModal = () => {
      showUploadModal.value = false
      // 重置表单
      uploadForm.title = ''
      uploadForm.description = ''
      uploadForm.category = ''
      uploadForm.file = null
    }
    
    const handleFileUpload = (event) => {
      uploadForm.file = event.target.files[0]
    }
    
    const uploadVideo = () => {
      if (!uploadForm.title || !uploadForm.category || !uploadForm.file) {
        alert('请填写完整信息并选择视频文件')
        return
      }
      
      // 模拟上传
      const newVideo = {
        id: videos.value.length + 1,
        title: uploadForm.title,
        author: '当前用户',
        views: 0,
        likes: 0,
        duration: '00:00',
        uploadTime: new Date().toISOString().split('T')[0],
        category: uploadForm.category,
        thumbnail: 'https://via.placeholder.com/300x200/cccccc/ffffff?text=新视频',
        description: uploadForm.description,
        isLiked: false
      }
      
      videos.value.unshift(newVideo)
      closeUploadModal()
      alert('视频上传成功！')
    }
    
    const formatViews = (views) => {
      if (views >= 10000) {
        return (views / 10000).toFixed(1) + '万'
      }
      return views.toString()
    }
    
    const formatTime = (time) => {
      const date = new Date(time)
      const now = new Date()
      const diff = now - date
      const days = Math.floor(diff / (1000 * 60 * 60 * 24))
      
      if (days === 0) return '今天'
      if (days === 1) return '昨天'
      if (days < 7) return `${days}天前`
      if (days < 30) return `${Math.floor(days / 7)}周前`
      if (days < 365) return `${Math.floor(days / 30)}个月前`
      return `${Math.floor(days / 365)}年前`
    }
    
    return {
      searchKeyword,
      sortBy,
      showVideoModal,
      showUploadModal,
      currentVideo,
      categories,
      selectedCategory,
      videos,
      recommendedVideos,
      filteredVideos,
      uploadForm,
      searchVideos,
      selectCategory,
      sortVideos,
      playVideo,
      closeVideoModal,
      toggleLike,
      closeUploadModal,
      handleFileUpload,
      uploadVideo,
      formatViews,
      formatTime
    }
  }
}
</script>

<style scoped>
.bilibili-container {
  min-height: 100vh;
  background-color: #f4f5f7;
}

/* 顶部导航栏 */
.bilibili-header {
  background: linear-gradient(135deg, #00a1d6 0%, #0080ff 100%);
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

.search-bar {
  display: flex;
  align-items: center;
  background: white;
  border-radius: 20px;
  overflow: hidden;
}

.search-input {
  border: none;
  padding: 0.5rem 1rem;
  width: 300px;
  outline: none;
}

.search-btn {
  background: #00a1d6;
  border: none;
  padding: 0.5rem 1rem;
  color: white;
  cursor: pointer;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.upload-btn {
  background: #ff6b6b;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  cursor: pointer;
  font-weight: 500;
}

.user-avatar {
  font-size: 1.5rem;
  cursor: pointer;
}

/* 分类标签 */
.category-tabs {
  background: white;
  padding: 1rem 2rem;
  display: flex;
  gap: 1rem;
  overflow-x: auto;
  border-bottom: 1px solid #eee;
}

.category-tab {
  background: none;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  cursor: pointer;
  white-space: nowrap;
  transition: all 0.3s ease;
}

.category-tab:hover {
  background: #f0f0f0;
}

.category-tab.active {
  background: #00a1d6;
  color: white;
}

/* 主要内容区域 */
.main-content {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 2rem;
  padding: 2rem;
  max-width: 1400px;
  margin: 0 auto;
}

/* 视频列表 */
.video-list {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.sort-select {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 6px;
}

.videos-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;
}

.video-card {
  cursor: pointer;
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.3s ease;
  background: white;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.video-card:hover {
  transform: translateY(-5px);
}

.video-thumbnail {
  position: relative;
  height: 160px;
  overflow: hidden;
}

.thumbnail-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.video-duration {
  position: absolute;
  bottom: 8px;
  right: 8px;
  background: rgba(0,0,0,0.8);
  color: white;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 0.8rem;
}

.play-overlay {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: rgba(0,0,0,0.7);
  color: white;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.video-card:hover .play-overlay {
  opacity: 1;
}

.video-info {
  padding: 1rem;
}

.video-title {
  font-size: 1rem;
  margin-bottom: 0.5rem;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.video-meta {
  display: flex;
  gap: 1rem;
  font-size: 0.8rem;
  color: #666;
}

/* 侧边栏 */
.sidebar {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  height: fit-content;
}

.recommended-section h3 {
  margin-bottom: 1rem;
  color: #333;
}

.recommended-videos {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.recommended-video {
  display: flex;
  gap: 0.5rem;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 6px;
  transition: background-color 0.3s ease;
}

.recommended-video:hover {
  background: #f5f5f5;
}

.rec-thumbnail {
  width: 80px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}

.rec-info h4 {
  font-size: 0.9rem;
  margin-bottom: 0.25rem;
  line-height: 1.3;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.rec-info p {
  font-size: 0.8rem;
  color: #666;
  margin-bottom: 0.25rem;
}

.rec-info span {
  font-size: 0.7rem;
  color: #999;
}

/* 模态框样式 */
.video-modal,
.upload-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.video-modal-content,
.upload-modal-content {
  background: white;
  border-radius: 12px;
  max-width: 800px;
  width: 90%;
  max-height: 90%;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid #eee;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #666;
}

.video-player {
  padding: 1.5rem;
}

.player-placeholder {
  background: #f5f5f5;
  height: 400px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  color: #666;
}

.play-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.video-details {
  padding: 1.5rem;
}

.video-stats {
  display: flex;
  gap: 1rem;
  align-items: center;
  margin-bottom: 1rem;
  color: #666;
}

.like-btn,
.share-btn {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  transition: background-color 0.3s ease;
}

.like-btn:hover,
.share-btn:hover {
  background: #f0f0f0;
}

.video-description h4 {
  margin-bottom: 0.5rem;
  color: #333;
}

.video-description p {
  color: #666;
  line-height: 1.6;
}

/* 上传表单样式 */
.upload-form {
  padding: 1.5rem;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #333;
  font-weight: 500;
}

.form-input,
.form-textarea,
.form-select {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 1rem;
}

.form-textarea {
  height: 100px;
  resize: vertical;
}

.file-upload {
  position: relative;
}

.file-input {
  position: absolute;
  opacity: 0;
  width: 100%;
  height: 100%;
  cursor: pointer;
}

.upload-placeholder {
  border: 2px dashed #ddd;
  border-radius: 6px;
  padding: 2rem;
  text-align: center;
  color: #666;
  transition: border-color 0.3s ease;
}

.upload-placeholder:hover {
  border-color: #00a1d6;
}

.form-actions {
  display: flex;
  gap: 1rem;
  margin-top: 1.5rem;
}

.upload-submit-btn {
  background: #00a1d6;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
}

.cancel-btn {
  background: #f5f5f5;
  color: #666;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  cursor: pointer;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-content {
    grid-template-columns: 1fr;
    padding: 1rem;
  }
  
  .bilibili-header {
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
  
  .videos-grid {
    grid-template-columns: 1fr;
  }
  
  .video-modal-content,
  .upload-modal-content {
    width: 95%;
    margin: 1rem;
  }
}
</style> 