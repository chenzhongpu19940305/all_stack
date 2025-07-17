<template>
  <div class="gallery-container">
    <!-- 顶部导航栏 -->
    <div class="gallery-header">
      <div class="header-left">
        <div class="logo">
          <span class="logo-icon">🖼️</span>
          <span class="logo-text">图片画廊</span>
        </div>
        <div class="search-bar">
          <input
              v-model="searchKeyword"
              @keyup.enter="searchImages"
              placeholder="搜索图片..."
              class="search-input"
          >
          <button @click="searchImages" class="search-btn">🔍</button>
        </div>
      </div>
      <div class="header-right">
        <button @click="showUploadModal = true" class="upload-btn">上传图片</button>
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
      <!-- 左侧图片列表 -->
      <div class="image-list">
        <div class="list-header">
          <h2>{{ selectedCategory.name }}</h2>
          <div class="sort-options">
            <select v-model="sortBy" @change="sortImages" class="sort-select">
              <option value="latest">最新</option>
              <option value="popular">最热</option>
              <option value="views">浏览量</option>
            </select>
          </div>
        </div>

        <div class="images-grid">
          <div v-if="loading" class="loading-container">
            <div class="loading-spinner"></div>
            <p>加载中...</p>
          </div>
          <div
              v-else-if="filteredImages.length === 0"
              class="no-images"
          >
            <p>暂无图片</p>
          </div>
          <div
              v-else
              v-for="image in filteredImages"
              :key="image.id"
              @click="viewImage(image)"
              class="image-card"
          >
            <div class="image-container">
              <img :src="image.url" :alt="image.title" class="image-preview">
              <div class="image-overlay">
                <div class="view-icon">👁️</div>
              </div>
            </div>
            <div class="image-info">
              <h3 class="image-title">{{ image.title }}</h3>
              <div class="image-meta">
                <span class="author">{{ image.author }}</span>
                <span class="views">{{ formatViews(image.views) }}浏览</span>
                <span class="time">{{ formatTime(image.uploadTime) }}</span>
              </div>
              <div class="image-dimensions">
                <span>{{ image.width }} × {{ image.height }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧推荐区域 -->
      <div class="sidebar">
        <div class="recommended-section">
          <h3>推荐图片</h3>
          <div class="recommended-images">
            <div
                v-for="image in recommendedImages"
                :key="image.id"
                @click="viewImage(image)"
                class="recommended-image"
            >
              <img :src="image.url" :alt="image.title" class="rec-preview">
              <div class="rec-info">
                <h4>{{ image.title }}</h4>
                <p>{{ image.author }}</p>
                <span>{{ formatViews(image.views) }}浏览</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 图片查看模态框 -->
    <div v-if="showImageModal" class="image-modal" @click="closeImageModal">
      <div class="image-modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ currentImage.title }}</h3>
          <button @click="closeImageModal" class="close-btn">✕</button>
        </div>
        <div class="image-viewer">
          <img :src="currentImage.url" :alt="currentImage.title" class="full-image">
        </div>
        <div class="image-details">
          <div class="image-stats">
            <span>{{ formatViews(currentImage.views) }}浏览</span>
            <span>{{ formatTime(currentImage.uploadTime) }}</span>
            <button class="like-btn" @click="toggleLike">
              {{ currentImage.isLiked ? '❤️' : '🤍' }} {{ currentImage.likes }}
            </button>
            <button class="share-btn">📤 分享</button>
            <button class="download-btn">⬇️ 下载</button>
          </div>
          <div class="image-description">
            <h4>图片描述</h4>
            <p>{{ currentImage.description }}</p>
          </div>
          <div class="image-info-details">
            <p><strong>尺寸：</strong>{{ currentImage.width }} × {{ currentImage.height }}</p>
            <p><strong>文件大小：</strong>{{ formatFileSize(currentImage.fileSize) }}</p>
            <p><strong>格式：</strong>{{ currentImage.format }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 上传图片模态框 -->
    <div v-if="showUploadModal" class="upload-modal" @click="closeUploadModal">
      <div class="upload-modal-content" @click.stop>
        <div class="modal-header">
          <h3>上传图片</h3>
          <button @click="closeUploadModal" class="close-btn">✕</button>
        </div>
        <div class="upload-form">
          <div class="form-group">
            <label>图片标题：</label>
            <input v-model="uploadForm.title" placeholder="请输入图片标题" class="form-input">
          </div>
          <div class="form-group">
            <label>图片描述：</label>
            <textarea v-model="uploadForm.description" placeholder="请输入图片描述" class="form-textarea"></textarea>
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
            <label>上传图片：</label>
            <div class="file-upload">
              <input type="file" @change="handleFileUpload" accept="image/*" class="file-input">
              <div class="upload-placeholder">
                <span>📁 选择图片文件</span>
              </div>
            </div>
          </div>
          <div class="form-actions">
            <button @click="uploadImage" class="upload-submit-btn">上传图片</button>
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
  name: 'Gallery',
  setup() {
    // 响应式数据
    const searchKeyword = ref('')
    const sortBy = ref('latest')
    const showImageModal = ref(false)
    const showUploadModal = ref(false)
    const currentImage = ref({})
    const loading = ref(false)

    // 分类数据
    const categories = ref([
      { id: 'all', name: '全部' },
      { id: 'nature', name: '自然风景' },
      { id: 'city', name: '城市建筑' },
      { id: 'portrait', name: '人像摄影' },
      { id: 'abstract', name: '抽象艺术' },
      { id: 'food', name: '美食摄影' },
      { id: 'travel', name: '旅行摄影' }
    ])

    const selectedCategory = ref(categories.value[0])

    // 图片数据
    const images = ref([])

    // 推荐图片
    const recommendedImages = computed(() => {
      return images.value.slice(0, 5)
    })

    // 过滤后的图片
    const filteredImages = computed(() => {
      let filtered = images.value

      // 按分类过滤
      if (selectedCategory.value.id !== 'all') {
        filtered = filtered.filter(image => image.category === selectedCategory.value.id)
      }

      // 按搜索关键词过滤
      if (searchKeyword.value) {
        filtered = filtered.filter(image =>
            image.title.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
            image.author.toLowerCase().includes(searchKeyword.value.toLowerCase())
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

    // 加载模拟图片数据
    const loadMockImages = () => {
      images.value = [
        {
          id: 1,
          title: '山间晨雾',
          author: '自然摄影师',
          views: 125000,
          likes: 3200,
          uploadTime: '2024-01-15',
          category: 'nature',
          url: 'https://via.placeholder.com/800x1200/4CAF50/ffffff?text=山间晨雾',
          description: '清晨的山间雾气缭绕，宛如仙境般的自然美景。',
          width: 800,
          height: 1200,
          fileSize: 2048576,
          format: 'JPEG',
          isLiked: false
        },
        {
          id: 2,
          title: '现代都市夜景',
          author: '城市摄影师',
          views: 89000,
          likes: 2100,
          uploadTime: '2024-01-14',
          category: 'city',
          url: 'https://via.placeholder.com/1200x800/2196F3/ffffff?text=都市夜景',
          description: '繁华都市的夜晚，霓虹灯闪烁，展现现代城市的魅力。',
          width: 1200,
          height: 800,
          fileSize: 1536000,
          format: 'JPEG',
          isLiked: true
        },
        {
          id: 3,
          title: '人物肖像',
          author: '人像摄影师',
          views: 156000,
          likes: 4500,
          uploadTime: '2024-01-13',
          category: 'portrait',
          url: 'https://via.placeholder.com/1000x1500/FF9800/ffffff?text=人物肖像',
          description: '专业人像摄影，捕捉人物最真实的情感瞬间。',
          width: 1000,
          height: 1500,
          fileSize: 2560000,
          format: 'JPEG',
          isLiked: false
        },
        {
          id: 4,
          title: '抽象几何',
          author: '艺术摄影师',
          views: 234000,
          likes: 6700,
          uploadTime: '2024-01-12',
          category: 'abstract',
          url: 'https://via.placeholder.com/900x600/9C27B0/ffffff?text=抽象几何',
          description: '抽象的几何图形组合，展现现代艺术的独特魅力。',
          width: 900,
          height: 600,
          fileSize: 1024000,
          format: 'PNG',
          isLiked: false
        },
        {
          id: 5,
          title: '精致美食',
          author: '美食摄影师',
          views: 345000,
          likes: 8900,
          uploadTime: '2024-01-11',
          category: 'food',
          url: 'https://via.placeholder.com/1200x900/FF5722/ffffff?text=精致美食',
          description: '精心制作的美食摄影，展现食物的色香味俱全。',
          width: 1200,
          height: 900,
          fileSize: 3072000,
          format: 'JPEG',
          isLiked: true
        },
        {
          id: 6,
          title: '旅行风景',
          author: '旅行摄影师',
          views: 178000,
          likes: 5200,
          uploadTime: '2024-01-10',
          category: 'travel',
          url: 'https://via.placeholder.com/1500x1000/607D8B/ffffff?text=旅行风景',
          description: '世界各地的美丽风景，记录旅行中的精彩瞬间。',
          width: 1500,
          height: 1000,
          fileSize: 4096000,
          format: 'JPEG',
          isLiked: false
        }
      ]
    }

    // 搜索图片
    const searchImages = () => {
      if (!searchKeyword.value.trim()) {
        loadMockImages()
        return
      }

      // 本地搜索
      const filtered = images.value.filter(image =>
          image.title.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
          image.author.toLowerCase().includes(searchKeyword.value.toLowerCase())
      )
      images.value = filtered
    }

    // 根据分类获取图片
    const selectCategory = (category) => {
      selectedCategory.value = category
      loadMockImages()
    }

    const sortImages = () => {
      // 排序逻辑已在computed中实现
    }

    const viewImage = (image) => {
      currentImage.value = image
      showImageModal.value = true

      // 增加浏览量（模拟）
      image.views++
    }

    const closeImageModal = () => {
      showImageModal.value = false
      currentImage.value = {}
    }

    const toggleLike = () => {
      const wasLiked = currentImage.value.isLiked
      currentImage.value.isLiked = !currentImage.value.isLiked

      if (currentImage.value.isLiked) {
        currentImage.value.likes++
      } else {
        currentImage.value.likes--
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

    const uploadImage = () => {
      if (!uploadForm.title || !uploadForm.category || !uploadForm.file) {
        alert('请填写完整信息并选择图片文件')
        return
      }

      // 模拟上传
      const newImage = {
        id: images.value.length + 1,
        title: uploadForm.title,
        author: '当前用户',
        views: 0,
        likes: 0,
        uploadTime: new Date().toISOString().split('T')[0],
        category: uploadForm.category,
        url: 'https://via.placeholder.com/800x600/cccccc/ffffff?text=新图片',
        description: uploadForm.description,
        width: 800,
        height: 600,
        fileSize: 1024000,
        format: 'JPEG',
        isLiked: false
      }

      images.value.unshift(newImage)
      closeUploadModal()
      alert('图片上传成功！(模拟模式)')
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

    const formatFileSize = (bytes) => {
      if (bytes >= 1048576) {
        return (bytes / 1048576).toFixed(1) + ' MB'
      }
      if (bytes >= 1024) {
        return (bytes / 1024).toFixed(1) + ' KB'
      }
      return bytes + ' B'
    }

    // 页面初始化
    onMounted(() => {
      loadMockImages()
    })

    return {
      searchKeyword,
      sortBy,
      showImageModal,
      showUploadModal,
      currentImage,
      loading,
      categories,
      selectedCategory,
      images,
      recommendedImages,
      filteredImages,
      uploadForm,
      searchImages,
      selectCategory,
      sortImages,
      viewImage,
      closeImageModal,
      toggleLike,
      closeUploadModal,
      handleFileUpload,
      uploadImage,
      formatViews,
      formatTime,
      formatFileSize
    }
  }
}
</script>

<style scoped>
.gallery-container {
  min-height: 100vh;
  background-color: #f4f5f7;
}

/* 顶部导航栏 */
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
  background: #4CAF50;
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
  background: #FF9800;
  color: white;
  border: none;
  padding: 0.5rem 1rem;