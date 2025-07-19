<template>
  <div class="modal-overlay" @click="closeModal">
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <h2>{{ content.title }}</h2>
        <span class="close" @click="closeModal">&times;</span>
      </div>
      
      <div class="modal-body">
        <!-- 视频播放区域 -->
        <div class="video-section" v-if="content.videoPath">
          <video 
            ref="videoPlayer" 
            controls 
            class="content-video"
            :src="content.videoPath"
          >
            您的浏览器不支持视频播放
          </video>
        </div>
        
        <!-- 内容信息 -->
        <div class="content-info">
          <div class="info-item">
            <h3>标题</h3>
            <p>{{ content.title }}</p>
          </div>
          
          <div class="info-item" v-if="content.description">
            <h3>描述</h3>
            <p>{{ content.description }}</p>
          </div>
          
          <div class="info-item" v-if="content.keywords">
            <h3>关键词</h3>
            <p>{{ content.keywords }}</p>
          </div>
          
          <div class="info-item" v-if="content.content">
            <h3>操作步骤</h3>
            <div class="content-text">{{ content.content }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ContentDetailModal',
  props: {
    content: {
      type: Object,
      required: true
    }
  },
  methods: {
    // 关闭模态框
    closeModal() {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  border-radius: 10px;
  width: 90%;
  max-width: 800px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.modal-header {
  padding: 20px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h2 {
  margin: 0;
  color: #333;
}

.close {
  color: #aaa;
  font-size: 28px;
  font-weight: bold;
  cursor: pointer;
  transition: color 0.3s;
}

.close:hover {
  color: #000;
}

.modal-body {
  padding: 20px;
}

/* 视频区域 */
.video-section {
  margin-bottom: 30px;
  text-align: center;
}

.content-video {
  max-width: 100%;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

/* 内容信息 */
.content-info {
  border-top: 1px solid #eee;
  padding-top: 20px;
}

.info-item {
  margin-bottom: 20px;
}

.info-item h3 {
  margin-bottom: 8px;
  color: #333;
  font-size: 16px;
  font-weight: bold;
}

.info-item p {
  color: #666;
  line-height: 1.6;
  margin: 0;
}

.content-text {
  color: #666;
  line-height: 1.6;
  white-space: pre-wrap;
  background: #f9f9f9;
  padding: 15px;
  border-radius: 5px;
  border-left: 4px solid #2196F3;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .modal-content {
    width: 95%;
    margin: 10% auto;
  }
}
</style> 