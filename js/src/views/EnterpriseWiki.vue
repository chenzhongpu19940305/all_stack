<template>
  <div class="enterprise-wiki">
    <!-- 头部导航 -->
    <header class="header">
      <div class="logo">
        <h1>企业知识库</h1>
      </div>
      <div class="search-container">
        <input 
          type="text" 
          v-model="searchKeyword" 
          @keyup.enter="searchContent"
          placeholder="搜索视频内容..." 
          class="search-input"
        >
        <button @click="searchContent" class="search-btn">搜索</button>
      </div>
      <div class="nav-actions">
        <button @click="showAddModal" class="btn btn-primary">添加视频</button>
      </div>
    </header>

    <!-- 主要内容区域 -->
    <main class="main-content">
      <!-- 搜索结果区域 -->
      <div class="search-results">
        <div v-if="!hasSearched" class="welcome-section">
          <h2>欢迎使用企业知识管理系统</h2>
          <p>在这里搜索和管理您的企业视频知识内容</p>
        </div>
        
        <div v-else-if="loading" class="loading">
          正在搜索中...
        </div>
        
        <div v-else-if="searchResults.length === 0" class="no-results">
          <p>未找到相关内容，请尝试其他关键词</p>
        </div>
        
        <div v-else class="results-list">
          <div 
            v-for="item in searchResults" 
            :key="item.id" 
            class="result-item"
            @click="showContentDetail(item)"
          >
            <div class="result-title">{{ item.title }}</div>
            <div class="result-video" v-if="item.videoPath">
              <video 
                :src="item.videoPath" 
                class="video-preview"
                muted
                preload="metadata"
              ></video>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- 内容详情模态框 -->
    <ContentDetailModal 
      v-if="showDetailModal"
      :content="selectedContent"
      @close="closeDetailModal"
    />

    <!-- 添加内容模态框 -->
    <AddContentModal 
      v-if="showAddContentModal"
      @close="closeAddModal"
      @save="addNewContent"
    />
  </div>
</template>

<script>
import ContentDetailModal from '../components/ContentDetailModal.vue'
import AddContentModal from '../components/AddContentModal.vue'

export default {
  name: 'EnterpriseWiki',
  components: {
    ContentDetailModal,
    AddContentModal
  },
  data() {
    return {
      searchKeyword: '',
      searchResults: [],
      loading: false,
      hasSearched: false,
      showDetailModal: false,
      showAddContentModal: false,
      selectedContent: null,
      apiBaseUrl: 'http://localhost:8080/api'
    }
  },
  methods: {
    // 搜索内容
    async searchContent() {
      if (!this.searchKeyword.trim()) {
        return
      }
      
      this.loading = true
      this.hasSearched = true
      
      try {
        const response = await fetch(`${this.apiBaseUrl}/content/search?q=${encodeURIComponent(this.searchKeyword)}`)
        if (response.ok) {
          this.searchResults = await response.json()
        } else {
          console.error('搜索失败:', response.status)
          // 如果API不可用，使用模拟数据
          this.searchResults = this.getMockSearchResults()
        }
      } catch (error) {
        console.error('搜索失败:', error)
        // 使用模拟数据
        this.searchResults = this.getMockSearchResults()
      } finally {
        this.loading = false
      }
    },
    
    // 获取模拟搜索结果
    getMockSearchResults() {
      return [
        {
          id: 1,
          title: '系统登录操作指南',
          description: '详细说明如何登录企业系统，包括用户名密码设置等',
          keywords: '登录,系统,用户名,密码',
          videoPath: '/uploads/videos/login-guide.mp4',
          imagePath: '/uploads/images/login-steps.jpg',
          content: '1. 打开浏览器\n2. 输入系统地址\n3. 输入用户名和密码\n4. 点击登录按钮'
        },
        {
          id: 2,
          title: '文件上传操作步骤',
          description: '介绍如何在系统中上传和管理文件',
          keywords: '文件,上传,管理',
          videoPath: '/uploads/videos/file-upload.mp4',
          imagePath: '/uploads/images/upload-steps.jpg',
          content: '1. 选择要上传的文件\n2. 点击上传按钮\n3. 等待上传完成\n4. 确认文件已上传'
        }
      ]
    },
    
    // 显示内容详情
    showContentDetail(content) {
      this.selectedContent = content
      this.showDetailModal = true
    },
    
    // 关闭详情模态框
    closeDetailModal() {
      this.showDetailModal = false
      this.selectedContent = null
    },
    
    // 显示添加内容模态框
    showAddModal() {
      this.showAddContentModal = true
    },
    
    // 关闭添加内容模态框
    closeAddModal() {
      this.showAddContentModal = false
    },
    
    // 添加新内容
    async addNewContent(contentData) {
      try {
        const response = await fetch(`${this.apiBaseUrl}/content`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(contentData)
        })
        
        if (response.ok) {
          console.log('添加成功')
          this.closeAddModal()
          // 重新搜索以显示新内容
          await this.searchContent()
        } else {
          console.error('添加失败:', response.status)
        }
      } catch (error) {
        console.error('添加失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.enterprise-wiki {
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 头部样式 */
.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px 0;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  position: sticky;
  top: 0;
  z-index: 100;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
}

.logo h1 {
  font-size: 24px;
  font-weight: bold;
  margin: 0;
}

.search-container {
  display: flex;
  align-items: center;
  flex: 1;
  max-width: 500px;
  margin: 0 20px;
}

.search-input {
  flex: 1;
  padding: 12px 16px;
  border: none;
  border-radius: 25px 0 0 25px;
  font-size: 16px;
  outline: none;
}

.search-btn {
  padding: 12px 20px;
  background: #4CAF50;
  color: white;
  border: none;
  border-radius: 0 25px 25px 0;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
}

.search-btn:hover {
  background: #45a049;
}

.nav-actions {
  display: flex;
  gap: 10px;
}

/* 按钮样式 */
.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
  text-decoration: none;
  display: inline-block;
}

.btn-primary {
  background: #2196F3;
  color: white;
}

.btn-primary:hover {
  background: #1976D2;
}

/* 主要内容区域 */
.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 20px;
}

/* 欢迎区域 */
.welcome-section {
  text-align: center;
  padding: 60px 20px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  margin-bottom: 30px;
}

.welcome-section h2 {
  font-size: 32px;
  margin-bottom: 15px;
  color: #333;
}

.welcome-section p {
  font-size: 18px;
  color: #666;
}

/* 搜索结果区域 */
.search-results {
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  overflow: hidden;
}

.result-item {
  padding: 20px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
  transition: background-color 0.3s;
}

.result-item:hover {
  background-color: #f9f9f9;
}

.result-item:last-child {
  border-bottom: none;
}

.result-title {
  font-size: 18px;
  font-weight: bold;
  color: #2196F3;
  margin-bottom: 15px;
}

.result-video {
  margin-top: 10px;
}

.video-preview {
  width: 100%;
  max-width: 300px;
  height: 180px;
  border-radius: 8px;
  object-fit: cover;
  background: #000;
}

/* 加载和空状态 */
.loading, .no-results {
  text-align: center;
  padding: 40px;
  color: #666;
}

.loading::after {
  content: '';
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 2px solid #f3f3f3;
  border-top: 2px solid #2196F3;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-left: 10px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header {
    flex-direction: column;
    gap: 15px;
  }
  
  .search-container {
    margin: 0;
    max-width: 100%;
  }
  
  .video-preview {
    max-width: 100%;
    height: 150px;
  }
}
</style> 