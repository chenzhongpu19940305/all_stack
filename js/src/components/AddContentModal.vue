<template>
  <div class="modal-overlay" @click="closeModal">
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <h2>添加新内容</h2>
        <span class="close" @click="closeModal">&times;</span>
      </div>
      
      <div class="modal-body">
        <form @submit.prevent="submitForm">
          <div class="form-group">
            <label for="title">标题 *</label>
            <input 
              type="text" 
              id="title"
              v-model="formData.title"
              required
              placeholder="请输入内容标题"
            >
          </div>
          
          <!-- 录屏功能区域 -->
          <div class="form-group">
            <label>录屏功能</label>
            <div class="screen-recording">
              <!-- 录屏开始按钮 -->
              <div v-if="!isRecording && !recordingPreview" class="recording-start">
                <button 
                  type="button" 
                  @click="startRecording" 
                  :disabled="!canRecord"
                  class="btn btn-success"
                >
                  🎬 开始录屏
                </button>
                <div v-if="!canRecord" class="recording-notice">
                  <small>录屏功能需要现代浏览器支持</small>
                </div>
              </div>
              
              <!-- 录屏进行中 -->
              <div v-if="isRecording" class="recording-active">
                <div class="recording-status">
                  <span class="recording-indicator">🔴 录制中...</span>
                  <div class="recording-time">{{ formatTime(recordingTime) }}</div>
                </div>
                <div class="recording-controls">
                  <button 
                    type="button" 
                    @click="stopRecording" 
                    class="btn btn-danger"
                  >
                    ⏹️ 停止录屏
                  </button>
                </div>
              </div>
              
              <!-- 录屏预览 -->
              <div v-if="recordingPreview && !isRecording" class="recording-preview">
                <h4>录屏预览</h4>
                <video 
                  :src="recordingPreview" 
                  controls 
                  class="preview-video"
                ></video>
                <div class="preview-actions">
                  <button 
                    type="button" 
                    @click="useRecording" 
                    class="btn btn-primary"
                  >
                    ✅ 使用此录屏
                  </button>
                  <button 
                    type="button" 
                    @click="discardRecording" 
                    class="btn btn-secondary"
                  >
                    ❌ 重新录制
                  </button>
                </div>
              </div>
            </div>
          </div>
          
          <div class="form-group">
            <label for="video">上传视频 *</label>
            <input 
              type="file" 
              id="video"
              ref="videoInput"
              accept="video/*"
              @change="handleVideoUpload"
              :required="!formData.videoPath"
            >
            <div v-if="formData.videoName" class="file-preview">
              <span>已选择: {{ formData.videoName }}</span>
            </div>
          </div>
          
          <div class="form-actions">
            <button type="submit" class="btn btn-primary" :disabled="submitting">
              {{ submitting ? '保存中...' : '保存' }}
            </button>
            <button type="button" @click="closeModal" class="btn btn-secondary">
              取消
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AddContentModal',
  data() {
    return {
      formData: {
        title: '',
        videoPath: '',
        videoFile: null,
        videoName: '',
        videoPreview: ''
      },
      submitting: false,
      apiBaseUrl: 'http://localhost:8080/czp/tool/api',
      // 录屏相关
      isRecording: false,
      canRecord: false,
      mediaRecorder: null,
      recordingChunks: [],
      recordingTime: 0,
      recordingTimer: null,
      recordingPreview: null,
      recordedBlob: null
    }
  },
  mounted() {
    this.checkRecordingSupport()
  },
  beforeDestroy() {
    this.stopRecording()
  },
  methods: {
    // 检查录屏支持
    async checkRecordingSupport() {
      try {
        // 只检查API是否可用，不实际获取媒体流
        if (navigator.mediaDevices && navigator.mediaDevices.getDisplayMedia) {
          this.canRecord = true
        } else {
          this.canRecord = false
        }
      } catch (error) {
        console.log('录屏功能不可用:', error)
        this.canRecord = false
      }
    },
    
    // 开始录屏
    async startRecording() {
      try {
        const stream = await navigator.mediaDevices.getDisplayMedia({
          video: { mediaSource: 'screen' }
        })
        
        this.mediaRecorder = new MediaRecorder(stream, {
          mimeType: 'video/webm;codecs=vp9'
        })
        
        this.recordingChunks = []
        this.recordingTime = 0
        this.recordingPreview = null
        this.recordedBlob = null
        
        this.mediaRecorder.ondataavailable = (event) => {
          if (event.data.size > 0) {
            this.recordingChunks.push(event.data)
          }
        }
        
        this.mediaRecorder.onstop = () => {
          this.recordedBlob = new Blob(this.recordingChunks, {
            type: 'video/webm'
          })
          this.recordingPreview = URL.createObjectURL(this.recordedBlob)
          this.isRecording = false
          this.clearRecordingTimer()
          
          // 自动上传录屏视频
          this.uploadRecording()
        }
        
        this.mediaRecorder.start()
        this.isRecording = true
        this.startRecordingTimer()
        
        console.log('录屏开始')
        
      } catch (error) {
        console.error('录屏失败:', error)
        alert('录屏失败: ' + error.message)
      }
    },
    
    // 停止录屏
    stopRecording() {
      if (this.mediaRecorder && this.isRecording) {
        this.mediaRecorder.stop()
        this.mediaRecorder.stream.getTracks().forEach(track => track.stop())
        this.clearRecordingTimer()
      }
    },
    
    // 开始录制计时器
    startRecordingTimer() {
      this.recordingTimer = setInterval(() => {
        this.recordingTime++
      }, 1000)
    },
    
    // 清除录制计时器
    clearRecordingTimer() {
      if (this.recordingTimer) {
        clearInterval(this.recordingTimer)
        this.recordingTimer = null
      }
    },
    
    // 格式化时间
    formatTime(seconds) {
      const mins = Math.floor(seconds / 60)
      const secs = seconds % 60
      return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
    },
    
    // 使用录屏
    useRecording() {
      if (this.recordedBlob) {
        this.formData.videoFile = this.recordedBlob
        this.formData.videoName = `录屏_${new Date().toLocaleString()}.webm`
        this.formData.videoPath = this.recordingPreview
        console.log('使用录屏视频')
      }
    },
    
    // 丢弃录屏
    discardRecording() {
      this.recordingPreview = null
      this.recordedBlob = null
      this.recordingTime = 0
      this.clearRecordingTimer()
      
      // 释放录屏预览URL
      if (this.recordingPreview) {
        URL.revokeObjectURL(this.recordingPreview)
      }
    },
    
    // 上传录屏视频
    async uploadRecording() {
      if (!this.recordedBlob) return
      
      try {
        const formData = new FormData()
        formData.append('file', this.recordedBlob, 'screen-recording.webm')
        
        const response = await fetch(`${this.apiBaseUrl}/upload/video`, {
          method: 'POST',
          body: formData
        })
        
        if (response.ok) {
          const result = await response.json()
          if (result.success) {
            this.formData.videoPath = result.fileUrl
            this.formData.videoName = '录屏视频.webm'
            console.log('录屏上传成功:', result.fileUrl)
          } else {
            console.error('录屏上传失败:', result.message)
            alert('录屏上传失败: ' + result.message)
          }
        } else {
          console.error('录屏上传失败:', response.status)
          alert('录屏上传失败')
        }
        
      } catch (error) {
        console.error('录屏上传失败:', error)
        alert('录屏上传失败')
      }
    },
    
    // 关闭模态框
    closeModal() {
      this.stopRecording()
      this.$emit('close')
    },
    
    // 处理视频上传
    async handleVideoUpload(event) {
      const file = event.target.files[0]
      if (file) {
        this.formData.videoFile = file
        this.formData.videoName = file.name
        
        try {
          // 上传视频文件
          const formData = new FormData()
          formData.append('file', file)
          
          const response = await fetch(`${this.apiBaseUrl}/upload/video`, {
            method: 'POST',
            body: formData
          })
          
          if (response.ok) {
            const result = await response.json()
            if (result.success) {
              this.formData.videoPath = result.fileUrl
              console.log('视频上传成功:', result.fileUrl)
            } else {
              console.error('视频上传失败:', result.message)
              alert('视频上传失败: ' + result.message)
            }
          } else {
            console.error('视频上传失败:', response.status)
            alert('视频上传失败')
          }
          
        } catch (error) {
          console.error('视频上传失败:', error)
          alert('视频上传失败')
        }
      }
    },
    
    // 提交表单
    async submitForm() {
      if (!this.formData.title.trim()) {
        alert('请填写标题')
        return
      }
      
      if (!this.formData.videoPath) {
        alert('请上传视频文件或录制视频')
        return
      }
      
      this.submitting = true
      
      try {
        // 构建提交数据
        const submitData = {
          title: this.formData.title,
          keywords: '',
          description: '',
          content: '',
          videoPath: this.formData.videoPath,
          imagePath: ''
        }
        
        // 调用后端API创建内容
        const response = await fetch(`${this.apiBaseUrl}/content`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(submitData)
        })
        
        if (response.ok) {
          const result = await response.json()
          console.log('内容创建成功:', result)
          
          // 通知父组件保存成功
          this.$emit('save', result)
          
          // 重置表单
          this.resetForm()
          
        } else {
          console.error('创建失败:', response.status)
          alert('创建失败，请重试')
        }
        
      } catch (error) {
        console.error('创建失败:', error)
        alert('创建失败，请重试')
      } finally {
        this.submitting = false
      }
    },
    
    // 重置表单
    resetForm() {
      this.formData = {
        title: '',
        videoPath: '',
        videoFile: null,
        videoName: '',
        videoPreview: ''
      }
      
      // 重置录屏相关
      this.isRecording = false
      this.recordingChunks = []
      this.recordingTime = 0
      this.recordingPreview = null
      this.recordedBlob = null
      this.clearRecordingTimer()
      
      // 重置文件输入
      if (this.$refs.videoInput) {
        this.$refs.videoInput.value = ''
      }
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
  max-width: 600px;
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

/* 表单样式 */
.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: #333;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
  font-family: inherit;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #2196F3;
}

.form-group input[type="file"] {
  padding: 8px;
  border: 2px dashed #ddd;
  background: #fafafa;
  cursor: pointer;
}

.form-group input[type="file"]:hover {
  border-color: #2196F3;
  background: #f0f8ff;
}

/* 录屏功能样式 */
.screen-recording {
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  padding: 15px;
  background: #f9f9f9;
}

.recording-start {
  text-align: center;
}

.recording-notice {
  margin-top: 10px;
  color: #666;
  font-size: 12px;
}

.recording-active {
  text-align: center;
}

.recording-status {
  margin-bottom: 15px;
}

.recording-indicator {
  display: block;
  font-weight: bold;
  color: #e74c3c;
  margin-bottom: 10px;
  font-size: 16px;
}

.recording-time {
  font-size: 24px;
  font-weight: bold;
  color: #e74c3c;
  margin: 10px 0;
}

.recording-controls {
  display: flex;
  justify-content: center;
  gap: 10px;
}

.recording-preview {
  border-top: 1px solid #ddd;
  padding-top: 15px;
}

.recording-preview h4 {
  margin-bottom: 15px;
  color: #333;
  text-align: center;
}

.preview-video {
  width: 100%;
  max-height: 200px;
  border-radius: 5px;
  margin-bottom: 15px;
}

.preview-actions {
  display: flex;
  justify-content: center;
  gap: 10px;
}

/* 文件预览 */
.file-preview {
  margin-top: 10px;
  padding: 10px;
  background: #f5f5f5;
  border-radius: 5px;
  font-size: 12px;
  color: #666;
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

.btn-primary:hover:not(:disabled) {
  background: #1976D2;
}

.btn-primary:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.btn-secondary {
  background: #757575;
  color: white;
}

.btn-secondary:hover {
  background: #616161;
}

.btn-success {
  background: #4CAF50;
  color: white;
}

.btn-success:hover:not(:disabled) {
  background: #45a049;
}

.btn-success:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.btn-danger {
  background: #f44336;
  color: white;
}

.btn-danger:hover:not(:disabled) {
  background: #d32f2f;
}

.btn-danger:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.btn-sm {
  padding: 5px 10px;
  font-size: 12px;
}

.form-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 30px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .modal-content {
    width: 95%;
    margin: 10% auto;
  }
  
  .form-actions {
    flex-direction: column;
  }
  
  .recording-controls {
    flex-direction: column;
  }
  
  .preview-actions {
    flex-direction: column;
  }
}
</style>