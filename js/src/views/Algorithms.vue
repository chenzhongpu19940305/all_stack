<template>
  <div class="algorithms-container">
    <!-- 左侧算法列表 -->
    <div class="sidebar">
      <div class="sidebar-header">
        <h2>算法列表</h2>
        <button @click="addNewAlgorithm" class="add-btn">
          <span>+</span> 新建算法
        </button>
      </div>
      
      <div class="algorithm-list">
        <div
          v-for="algorithm in algorithms"
          :key="algorithm.id"
          @click="selectAlgorithm(algorithm)"
          :class="['algorithm-item', { active: selectedAlgorithm?.id === algorithm.id }]"
        >
          <div class="algorithm-name">{{ algorithm.name }}</div>
          <div class="algorithm-category">{{ algorithm.category }}</div>
        </div>
      </div>
    </div>

    <!-- 右侧算法详情 -->
    <div class="main-content">
      <div v-if="selectedAlgorithm" class="algorithm-detail">
        <div class="detail-header">
          <input
            v-model="selectedAlgorithm.name"
            class="algorithm-title-input"
            placeholder="算法名称"
            @blur="saveAlgorithm"
          />
          <select v-model="selectedAlgorithm.category" @change="saveAlgorithm" class="category-select">
            <option value="排序算法">排序算法</option>
            <option value="搜索算法">搜索算法</option>
            <option value="图论算法">图论算法</option>
            <option value="动态规划">动态规划</option>
            <option value="贪心算法">贪心算法</option>
            <option value="分治算法">分治算法</option>
            <option value="其他">其他</option>
          </select>
          <button @click="openFullscreen" class="fullscreen-btn">
            📺 全屏展示
          </button>
        </div>

        <div class="detail-content">
          <!-- 文字描述区域 -->
          <div class="text-section">
            <h3>算法描述</h3>
            <textarea
              v-model="selectedAlgorithm.description"
              placeholder="在这里描述算法的原理、步骤、时间复杂度等..."
              class="description-textarea"
              @blur="saveAlgorithm"
            ></textarea>
          </div>

          <!-- 代码实现区域 -->
          <div class="code-section">
            <h3>代码实现</h3>
            <textarea
              v-model="selectedAlgorithm.code"
              placeholder="在这里粘贴算法的代码实现..."
              class="code-textarea"
              @blur="saveAlgorithm"
            ></textarea>
          </div>

          <!-- 截图区域 -->
          <div class="image-section">
            <h3>算法截图</h3>
            <div class="image-upload-area">
              <input
                type="file"
                ref="imageInput"
                @change="handleImageUpload"
                accept="image/*"
                style="display: none"
              />
              <div v-if="selectedAlgorithm.images && selectedAlgorithm.images.length > 0" class="image-gallery">
                <!-- 图片展示区域 -->
                <div class="image-display">
                  <img :src="selectedAlgorithm.images[currentImageIndex].src" alt="算法截图" />
                  
                  <!-- 图片导航按钮 -->
                  <div class="image-nav">
                    <button 
                      @click="prevImage" 
                      :disabled="currentImageIndex === 0"
                      class="image-nav-btn prev-btn"
                    >
                      ‹
                    </button>
                    <button 
                      @click="nextImage" 
                      :disabled="currentImageIndex === selectedAlgorithm.images.length - 1"
                      class="image-nav-btn next-btn"
                    >
                      ›
                    </button>
                  </div>
                  
                  <!-- 图片计数器 -->
                  <div class="image-counter">
                    {{ currentImageIndex + 1 }} / {{ selectedAlgorithm.images.length }}
                  </div>
                </div>
                
                <!-- 图片说明编辑 -->
                <div class="image-description">
                  <textarea
                    v-model="selectedAlgorithm.images[currentImageIndex].desc"
                    placeholder="为这张图片添加说明..."
                    class="desc-textarea"
                    @blur="saveAlgorithm"
                  ></textarea>
                </div>
                
                <!-- 删除当前图片按钮 -->
                <button @click="removeImage(currentImageIndex)" class="remove-image-btn">
                  删除当前图片
                </button>
              </div>
              
              <!-- 拖拽上传区域 -->
              <div 
                class="drag-upload-area"
                :class="{ 'dragover': isDragging }"
                @drop="handleDrop"
                @dragover.prevent="isDragging = true"
                @dragenter.prevent="isDragging = true"
                @dragleave.prevent="isDragging = false"
                @click="$refs.imageInput.click()"
              >
                <div class="drag-content">
                  <div class="drag-icon">📁</div>
                  <p>点击或拖拽图片到此处上传</p>
                  <p class="drag-hint">支持 JPG、PNG、GIF 等格式</p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="detail-actions">
          <button @click="saveAlgorithm" class="save-btn">保存算法</button>
          <button @click="deleteAlgorithm" class="delete-btn">删除算法</button>
        </div>
      </div>

      <div v-else class="no-selection">
        <div class="no-selection-content">
          <h3>选择算法</h3>
          <p>从左侧列表中选择一个算法进行编辑，或创建新的算法</p>
        </div>
      </div>
    </div>
  </div>
  
  <!-- 全屏展示模态框 -->
  <div v-if="showFullscreen" class="fullscreen-modal" @click="closeFullscreen">
    <div class="fullscreen-content" @click.stop>
      <!-- 全屏导航 -->
      <div class="fullscreen-nav">
        <div class="fullscreen-counter">
          {{ currentFullscreenPage + 1 }} / {{ totalFullscreenPages }}
        </div>
      </div>
      
      <!-- 关闭按钮 -->
      <button @click="closeFullscreen" class="close-fullscreen-btn">✕</button>
      
      <!-- 左右导航按钮 -->
      <button 
        @click="prevFullscreenPage" 
        :disabled="currentFullscreenPage === 0" 
        class="fullscreen-nav-left"
      >
        ‹
      </button>
      <button 
        @click="nextFullscreenPage" 
        :disabled="currentFullscreenPage === totalFullscreenPages - 1" 
        class="fullscreen-nav-right"
      >
        ›
      </button>
      
      <!-- 全屏内容 -->
      <div class="fullscreen-body">
        <!-- 代码页 -->
        <div v-if="currentFullscreenPage === 0" class="fullscreen-code">
          <h2>{{ selectedAlgorithm.name }} - 代码实现</h2>
          <textarea
            v-model="selectedAlgorithm.code"
            class="fullscreen-code-textarea"
            placeholder="在这里编辑代码..."
            @blur="saveAlgorithm"
          ></textarea>
        </div>
        
        <!-- 图片页 -->
        <div v-else-if="selectedAlgorithm.images && selectedAlgorithm.images[currentFullscreenPage - 1]" class="fullscreen-image">
          <div class="fullscreen-image-wrapper">
            <!-- 图片区域 -->
            <div class="fullscreen-image-area">
              <img :src="selectedAlgorithm.images[currentFullscreenPage - 1].src" alt="算法截图" />
              
              <!-- 图片操作按钮 -->
              <div class="fullscreen-image-actions">
                <button @click="replaceImage" class="image-action-btn">
                  🔄 替换图片
                </button>
              </div>
            </div>
            
            <!-- 说明区域 -->
            <div class="fullscreen-desc-area">
              <textarea
                v-model="selectedAlgorithm.images[currentFullscreenPage - 1].desc"
                class="fullscreen-desc-textarea"
                placeholder="编辑图片说明..."
                @blur="saveAlgorithm"
              ></textarea>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Algorithms',
  data() {
    return {
      algorithms: [],
      selectedAlgorithm: null,
      nextId: 1,
      currentImageIndex: 0,
      isDragging: false, // 新增拖拽状态
      showFullscreen: false, // 新增全屏展示模态框状态
      currentFullscreenPage: 0, // 当前全屏展示的页码
      totalFullscreenPages: 0 // 总页数
    }
  },
  mounted() {
    this.loadAlgorithms()
    // 添加键盘事件监听
    document.addEventListener('keydown', this.handleKeydown)
  },
  
  beforeUnmount() {
    // 移除键盘事件监听
    document.removeEventListener('keydown', this.handleKeydown)
  },
  methods: {
    // 加载算法数据
    async loadAlgorithms() {
      try {
        const response = await fetch('http://localhost:8080/api/algorithms')
        if (response.ok) {
          const algorithms = await response.json()
          // 转换后端数据格式为前端格式
          this.algorithms = algorithms.map(alg => ({
            id: alg.id,
            name: alg.name,
            category: alg.category,
            description: alg.description,
            code: alg.code,
            images: alg.images ? alg.images.map(img => ({
              id: img.id,
              src: img.src,
              desc: img.desc
            })) : []
          }))
          if (this.algorithms.length > 0) {
            this.nextId = Math.max(...this.algorithms.map(a => a.id)) + 1
          }
        } else {
          console.error('加载算法数据失败:', response.status, response.statusText)
          this.algorithms = []
        }
      } catch (error) {
        console.error('加载算法数据失败:', error)
        this.algorithms = []
      }
    },

    // 选择算法
    selectAlgorithm(algorithm) {
      this.selectedAlgorithm = { ...algorithm }
      this.currentImageIndex = 0 // Reset image index when selecting a new algorithm
    },

    // 添加新算法
    addNewAlgorithm() {
      const newAlgorithm = {
        id: this.nextId++,
        name: '新算法',
        category: '其他',
        description: '',
        code: '',
        images: []
      }
      this.algorithms.push(newAlgorithm)
      this.selectedAlgorithm = { ...newAlgorithm }
      this.saveAlgorithm()
    },

    // 保存算法
    async saveAlgorithm() {
      if (!this.selectedAlgorithm) return

      // 保存到后端
      try {
        const algorithmData = {
          id: this.selectedAlgorithm.id,
          name: this.selectedAlgorithm.name,
          category: this.selectedAlgorithm.category,
          description: this.selectedAlgorithm.description,
          code: this.selectedAlgorithm.code,
          images: this.selectedAlgorithm.images ? this.selectedAlgorithm.images.map(img => ({
            id: img.id,
            src: img.src,
            desc: img.desc,
            sortOrder: img.sortOrder || 0
          })) : []
        }

        const url = this.selectedAlgorithm.id ? 
          `http://localhost:8080/api/algorithms/${this.selectedAlgorithm.id}` : 
          'http://localhost:8080/api/algorithms'
        
        const method = this.selectedAlgorithm.id ? 'PUT' : 'POST'
        
        const response = await fetch(url, {
          method: method,
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(algorithmData)
        })

        if (response.ok) {
          const savedAlgorithm = await response.json()
          // 更新本地数据
          const index = this.algorithms.findIndex(a => a.id === this.selectedAlgorithm.id)
          if (index !== -1) {
            this.algorithms[index] = {
              id: savedAlgorithm.id,
              name: savedAlgorithm.name,
              category: savedAlgorithm.category,
              description: savedAlgorithm.description,
              code: savedAlgorithm.code,
              images: savedAlgorithm.images ? savedAlgorithm.images.map(img => ({
                id: img.id,
                src: img.src,
                desc: img.desc
              })) : []
            }
          } else {
            // 如果是新算法，添加到列表
            this.algorithms.push({
              id: savedAlgorithm.id,
              name: savedAlgorithm.name,
              category: savedAlgorithm.category,
              description: savedAlgorithm.description,
              code: savedAlgorithm.code,
              images: savedAlgorithm.images ? savedAlgorithm.images.map(img => ({
                id: img.id,
                src: img.src,
                desc: img.desc
              })) : []
            })
          }
          this.showSaveMessage(true)
        } else {
          console.error('保存失败:', response.status, response.statusText)
          this.showSaveMessage(false)
        }
      } catch (error) {
        console.error('保存算法数据失败:', error)
        this.showSaveMessage(false)
      }
    },

    // 显示保存消息
    showSaveMessage(success = true) {
      // 创建一个临时的保存提示
      const saveMessage = document.createElement('div')
      saveMessage.textContent = success ? '保存成功！' : '保存失败，请重试！'
      saveMessage.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: ${success ? '#42b883' : '#e74c3c'};
        color: white;
        padding: 10px 20px;
        border-radius: 6px;
        z-index: 1000;
        animation: slideIn 0.3s ease;
      `
      document.body.appendChild(saveMessage)
      
      setTimeout(() => {
        saveMessage.style.animation = 'slideOut 0.3s ease'
        setTimeout(() => {
          document.body.removeChild(saveMessage)
        }, 300)
      }, 2000)
    },

    // 删除算法
    async deleteAlgorithm() {
      if (!this.selectedAlgorithm) return

      if (confirm('确定要删除这个算法吗？')) {
        try {
          const response = await fetch(`http://localhost:8080/api/algorithms/${this.selectedAlgorithm.id}`, {
            method: 'DELETE'
          })

          if (response.ok) {
            this.algorithms = this.algorithms.filter(a => a.id !== this.selectedAlgorithm.id)
            this.selectedAlgorithm = null
            this.showSaveMessage(true)
          } else {
            console.error('删除失败:', response.status, response.statusText)
            this.showSaveMessage(false)
          }
        } catch (error) {
          console.error('删除算法失败:', error)
          this.showSaveMessage(false)
        }
      }
    },

    // 处理图片上传
    handleImageUpload(event) {
      const file = event.target.files[0]
      if (file && file.type.startsWith('image/')) {
        const reader = new FileReader()
        reader.onload = (e) => {
          if (!this.selectedAlgorithm.images) this.selectedAlgorithm.images = []
          this.selectedAlgorithm.images.push({ src: e.target.result, desc: '' })
          this.currentImageIndex = this.selectedAlgorithm.images.length - 1
          this.saveAlgorithm()
        }
        reader.readAsDataURL(file)
      }
      // 清空input，允许重复选择同一文件
      event.target.value = ''
    },

    // 处理拖拽上传
    handleDrop(event) {
      event.preventDefault()
      this.isDragging = false
      const file = event.dataTransfer.files[0]
      if (file && file.type.startsWith('image/')) {
        const reader = new FileReader()
        reader.onload = (e) => {
          if (!this.selectedAlgorithm.images) this.selectedAlgorithm.images = []
          this.selectedAlgorithm.images.push({ src: e.target.result, desc: '' })
          this.currentImageIndex = this.selectedAlgorithm.images.length - 1
          this.saveAlgorithm()
        }
        reader.readAsDataURL(file)
      }
    },

    // 删除图片
    removeImage(idx) {
      this.selectedAlgorithm.images.splice(idx, 1)
      if (this.currentImageIndex >= this.selectedAlgorithm.images.length) {
        this.currentImageIndex = this.selectedAlgorithm.images.length - 1
      }
      this.saveAlgorithm()
    },

    // 上一张图片
    prevImage() {
      if (this.currentImageIndex > 0) {
        this.currentImageIndex--
      }
    },

    // 下一张图片
    nextImage() {
      if (this.currentImageIndex < this.selectedAlgorithm.images.length - 1) {
        this.currentImageIndex++
      }
    },

    // 全屏展示
    openFullscreen() {
      this.showFullscreen = true;
      this.currentFullscreenPage = 0; // 默认显示代码页
      // 计算总页数：代码页(1) + 图片页数
      const imageCount = this.selectedAlgorithm.images ? this.selectedAlgorithm.images.length : 0;
      this.totalFullscreenPages = 1 + imageCount;
    },

    // 关闭全屏
    closeFullscreen() {
      this.showFullscreen = false;
      this.currentFullscreenPage = 0; // 重置页码
    },

    // 上一页全屏
    prevFullscreenPage() {
      console.log('上一页按钮被点击，当前页:', this.currentFullscreenPage)
      if (this.currentFullscreenPage > 0) {
        this.currentFullscreenPage--;
        console.log('切换到页面:', this.currentFullscreenPage)
      }
    },

    // 下一页全屏
    nextFullscreenPage() {
      console.log('下一页按钮被点击，当前页:', this.currentFullscreenPage)
      if (this.currentFullscreenPage < this.totalFullscreenPages - 1) {
        this.currentFullscreenPage++;
        console.log('切换到页面:', this.currentFullscreenPage)
      }
    },

    // 替换图片
    replaceImage() {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = 'image/*'
      input.onchange = (event) => {
        const file = event.target.files[0]
        if (file && file.type.startsWith('image/')) {
          const reader = new FileReader()
          reader.onload = (e) => {
            if (this.selectedAlgorithm.images) {
              this.selectedAlgorithm.images[this.currentFullscreenPage - 1].src = e.target.result
              this.saveAlgorithm()
            }
          }
          reader.readAsDataURL(file)
        }
        input.remove() // 移除input元素
      }
      input.click()
    },

    // 处理键盘事件
    handleKeydown(event) {
      if (this.showFullscreen) {
        if (event.key === 'Escape') {
          this.closeFullscreen()
        } else if (event.key === 'ArrowLeft') {
          this.prevFullscreenPage()
        } else if (event.key === 'ArrowRight') {
          this.nextFullscreenPage()
        }
      }
    }
  }
}
</script>

<style scoped>
.algorithms-container {
  display: flex;
  height: calc(100vh - 120px);
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  overflow: hidden;
}

.sidebar {
  width: 300px;
  background: #f8f9fa;
  border-right: 1px solid #e9ecef;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid #e9ecef;
}

.sidebar-header h2 {
  margin: 0 0 15px 0;
  color: #2c3e50;
  font-size: 1.5rem;
}

.add-btn {
  width: 100%;
  padding: 10px;
  background: #42b883;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: background-color 0.3s;
}

.add-btn:hover {
  background: #3aa876;
}

.algorithm-list {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
}

.algorithm-item {
  padding: 15px;
  margin-bottom: 8px;
  background: white;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #e9ecef;
}

.algorithm-item:hover {
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transform: translateY(-1px);
}

.algorithm-item.active {
  background: #42b883;
  color: white;
  border-color: #42b883;
}

.algorithm-name {
  font-weight: 600;
  margin-bottom: 5px;
}

.algorithm-category {
  font-size: 12px;
  opacity: 0.8;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.algorithm-detail {
  flex: 1;
  padding: 30px;
  overflow-y: auto;
}

.detail-header {
  margin-bottom: 30px;
  display: flex;
  gap: 15px;
  align-items: center;
}

.algorithm-title-input {
  flex: 1;
  font-size: 24px;
  font-weight: bold;
  border: none;
  border-bottom: 2px solid #e9ecef;
  padding: 10px 0;
  outline: none;
  transition: border-color 0.3s;
}

.algorithm-title-input:focus {
  border-bottom-color: #42b883;
}

.category-select {
  padding: 8px 12px;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  background: white;
  font-size: 14px;
  outline: none;
}

.detail-content {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.text-section, .code-section, .image-section {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
}

.text-section h3, .code-section h3, .image-section h3 {
  margin: 0 0 15px 0;
  color: #2c3e50;
  font-size: 18px;
}

.description-textarea, .code-textarea {
  width: 100%;
  min-height: 120px;
  padding: 15px;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.5;
  resize: vertical;
  outline: none;
  transition: border-color 0.3s;
}

.description-textarea:focus, .code-textarea:focus {
  border-color: #42b883;
}

.code-textarea {
  background: #2d3748;
  color: #e2e8f0;
  font-family: 'Courier New', monospace;
}

.image-upload-area {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.drag-upload-area {
  width: 100%;
  max-width: 600px;
  height: 200px;
  border: 2px dashed #e9ecef;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: #6c757d;
  cursor: pointer;
  transition: all 0.3s ease;
  background-color: #f8f9fa;
  margin-top: 15px;
}

.drag-upload-area:hover {
  border-color: #42b883;
  background-color: #e9ecef;
}

.drag-upload-area.dragover {
  border-color: #42b883;
  background-color: #e9ecef;
  transform: scale(1.02);
  box-shadow: 0 4px 12px rgba(66, 184, 131, 0.3);
}

.drag-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.drag-icon {
  font-size: 48px;
  margin-bottom: 10px;
}

.drag-hint {
  font-size: 12px;
  opacity: 0.7;
}

.upload-btn {
  padding: 15px 30px;
  background: #42b883;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.upload-btn:hover {
  background: #3aa876;
}

.image-gallery {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  width: 100%;
}

.image-display {
  position: relative;
  max-width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  overflow: hidden;
}

.image-display img {
  max-width: 100%;
  max-height: 400px; /* Increased max height */
  object-fit: contain; /* Ensure image fits within display area */
}

.image-nav {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  gap: 10px;
  z-index: 10;
}

.image-nav-btn {
  background: rgba(0,0,0,0.5);
  color: white;
  border: none;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 24px;
  transition: background-color 0.3s;
}

.image-nav-btn:hover {
  background: rgba(0,0,0,0.7);
}

.image-nav-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.image-counter {
  position: absolute;
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0,0,0,0.5);
  color: white;
  padding: 5px 10px;
  border-radius: 6px;
  font-size: 14px;
  z-index: 10;
}

.image-description {
  width: 100%;
  max-width: 600px;
  margin-top: 15px;
}

.desc-textarea {
  width: 100%;
  min-height: 80px; /* Adjusted min height */
  padding: 10px;
  border: none;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 13px;
  line-height: 1.4;
  resize: vertical;
  outline: none;
  background: #f8f9fa;
}

.remove-image-btn {
  padding: 10px 20px;
  background: rgba(220, 53, 69, 0.9);
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.remove-image-btn:hover {
  background: rgba(220, 53, 69, 1);
}

.detail-actions {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #e9ecef;
}

.delete-btn {
  padding: 10px 20px;
  background: #dc3545;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.save-btn {
  padding: 10px 20px;
  background: #42b883;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  margin-right: 10px;
  transition: background-color 0.3s;
}

.save-btn:hover {
  background: #3aa876;
}

.delete-btn:hover {
  background: #c82333;
}

.fullscreen-btn {
  padding: 10px 20px;
  background: #6c757d;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.fullscreen-btn:hover {
  background: #5a6268;
}

.no-selection {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.no-selection-content {
  text-align: center;
  color: #6c757d;
}

.no-selection-content h3 {
  margin-bottom: 10px;
  font-size: 24px;
}

.no-selection-content p {
  font-size: 16px;
  line-height: 1.5;
}

/* 动画效果 */
@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

@keyframes slideOut {
  from {
    transform: translateX(0);
    opacity: 1;
  }
  to {
    transform: translateX(100%);
    opacity: 0;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .algorithms-container {
    flex-direction: column;
    height: auto;
  }
  
  .sidebar {
    width: 100%;
    height: 300px;
  }
  
  .algorithm-detail {
    padding: 20px;
  }
  
  .detail-header {
    flex-direction: column;
    align-items: stretch;
  }
}

/* 全屏模态框样式 */
.fullscreen-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: #1a1a2e;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.fullscreen-content {
  background: #1a1a2e;
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.fullscreen-nav {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  background: rgba(0, 0, 0, 0.9);
  color: white;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  z-index: 10;
  backdrop-filter: blur(10px);
}



.fullscreen-counter {
  font-size: 18px;
  font-weight: bold;
  color: #fff;
}

.close-fullscreen-btn {
  position: absolute;
  top: 20px;
  right: 20px;
  background: rgba(220, 53, 69, 0.9);
  color: white;
  border: none;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  cursor: pointer;
  z-index: 20;
  transition: background-color 0.3s;
}

.close-fullscreen-btn:hover {
  background: rgba(220, 53, 69, 1);
}

.fullscreen-body {
  flex: 1;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  padding: 80px 40px 40px 40px;
  overflow: hidden;
}

.fullscreen-code, .fullscreen-image {
  width: 100%;
  max-width: 90vw;
  height: calc(100vh - 120px);
  margin: 0;
  padding: 20px;
  background: #1a1a2e;
  border-radius: 0;
  box-shadow: none;
  color: white;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}

.fullscreen-code h2 {
  margin-top: 0;
  margin-bottom: 30px;
  color: #fff;
  font-size: 28px;
  text-align: center;
}

.fullscreen-code pre {
  background: #2d3748;
  color: #e2e8f0;
  padding: 30px;
  border-radius: 8px;
  overflow-x: auto;
  font-size: 16px;
  line-height: 1.8;
  max-height: 70vh;
}

.fullscreen-code code {
  font-family: 'Courier New', monospace;
  font-size: 16px;
  line-height: 1.8;
}

.fullscreen-code-textarea {
  width: 100%;
  min-height: 60vh;
  padding: 30px;
  border: 1px solid #444;
  border-radius: 8px;
  font-family: 'Courier New', monospace;
  font-size: 16px;
  line-height: 1.8;
  resize: none;
  outline: none;
  transition: border-color 0.3s;
  background: #2d3748;
  color: #e2e8f0;
  overflow-y: auto;
}

.fullscreen-code-textarea:focus {
  border-color: #42b883;
  box-shadow: 0 0 0 2px rgba(66, 184, 131, 0.2);
}

.fullscreen-image {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #1a1a2e;
  overflow: hidden;
}

.fullscreen-image-wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.fullscreen-image-area {
  flex: 1;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #1a1a2e;
  overflow: hidden;
  min-height: 50vh;
}

.fullscreen-image-area img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.fullscreen-desc-area {
  flex: 1;
  padding: 20px;
  background: #1a1a2e;
  display: flex;
  flex-direction: column;
  min-height: 50vh;
}

.fullscreen-image-actions {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 10;
}

.image-action-btn {
  padding: 8px 15px;
  background: rgba(108, 117, 125, 0.8);
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.image-action-btn:hover {
  background: rgba(108, 117, 125, 1);
}

.fullscreen-desc-textarea {
  width: 100%;
  height: 100%;
  min-height: 200px;
  padding: 20px;
  border: 1px solid #444;
  border-radius: 8px;
  font-family: 'Arial', sans-serif;
  font-size: 16px;
  line-height: 1.6;
  resize: none;
  outline: none;
  background: rgba(0, 0, 0, 0.3);
  color: #fff;
  transition: border-color 0.3s;
  backdrop-filter: blur(5px);
  flex: 1;
}

.fullscreen-desc-textarea:focus {
  border-color: #42b883;
  box-shadow: 0 0 0 2px rgba(66, 184, 131, 0.2);
}

/* 左右导航按钮样式 */
.fullscreen-nav-left,
.fullscreen-nav-right {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(66, 184, 131, 0.8);
  color: white;
  border: none;
  border-radius: 50%;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 32px;
  font-weight: bold;
  transition: all 0.3s;
  z-index: 20;
  user-select: none;
}

.fullscreen-nav-left {
  left: 30px;
}

.fullscreen-nav-right {
  right: 30px;
}

.fullscreen-nav-left:hover,
.fullscreen-nav-right:hover {
  background: rgba(66, 184, 131, 1);
  transform: translateY(-50%) scale(1.1);
  box-shadow: 0 4px 12px rgba(66, 184, 131, 0.4);
}

.fullscreen-nav-left:active,
.fullscreen-nav-right:active {
  transform: translateY(-50%) scale(0.95);
}

.fullscreen-nav-left:disabled,
.fullscreen-nav-right:disabled {
  background: rgba(108, 117, 125, 0.5);
  cursor: not-allowed;
  transform: translateY(-50%);
  box-shadow: none;
}

.fullscreen-nav-left:disabled:hover,
.fullscreen-nav-right:disabled:hover {
  background: rgba(108, 117, 125, 0.5);
  transform: translateY(-50%);
  box-shadow: none;
}

/* 保存消息动画 */
@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

@keyframes slideOut {
  from {
    transform: translateX(0);
    opacity: 1;
  }
  to {
    transform: translateX(100%);
    opacity: 0;
  }
}
</style> 