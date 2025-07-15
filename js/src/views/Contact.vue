<template>
  <div class="contact">
    <div class="hero">
      <h1 class="title">联系我们</h1>
      <p class="subtitle">我们随时为您提供帮助</p>
    </div>
    
    <div class="contact-grid">
      <div class="contact-form">
        <h2>发送消息</h2>
        <form @submit.prevent="submitForm">
          <div class="form-group">
            <label for="name">姓名</label>
            <input 
              type="text" 
              id="name" 
              v-model="form.name" 
              required
              placeholder="请输入您的姓名"
            >
          </div>
          
          <div class="form-group">
            <label for="email">邮箱</label>
            <input 
              type="email" 
              id="email" 
              v-model="form.email" 
              required
              placeholder="请输入您的邮箱"
            >
          </div>
          
          <div class="form-group">
            <label for="subject">主题</label>
            <input 
              type="text" 
              id="subject" 
              v-model="form.subject" 
              required
              placeholder="请输入消息主题"
            >
          </div>
          
          <div class="form-group">
            <label for="message">消息</label>
            <textarea 
              id="message" 
              v-model="form.message" 
              required
              placeholder="请输入您的消息"
              rows="5"
            ></textarea>
          </div>
          
          <button type="submit" class="submit-btn" :disabled="submitting">
            {{ submitting ? '发送中...' : '发送消息' }}
          </button>
        </form>
      </div>
      
      <div class="contact-info">
        <h2>联系信息</h2>
        <div class="info-list">
          <div class="info-item">
            <div class="info-icon">📧</div>
            <div class="info-content">
              <h3>邮箱</h3>
              <p>contact@vueapp.com</p>
            </div>
          </div>
          <div class="info-item">
            <div class="info-icon">📞</div>
            <div class="info-content">
              <h3>电话</h3>
              <p>+86 123 4567 8900</p>
            </div>
          </div>
          <div class="info-item">
            <div class="info-icon">📍</div>
            <div class="info-content">
              <h3>地址</h3>
              <p>北京市朝阳区某某街道123号</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <div v-if="showMessage" class="message" :class="messageType">
      {{ messageText }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'Contact',
  data: function() {
    return {
      form: {
        name: '',
        email: '',
        subject: '',
        message: ''
      },
      submitting: false,
      showMessage: false,
      messageText: '',
      messageType: 'success'
    }
  },
  methods: {
    submitForm: function() {
      var self = this;
      self.submitting = true;
      
      // 模拟表单提交
      console.log('表单数据：', self.form);
      
      setTimeout(function() {
        // 显示成功消息
        self.messageText = '消息发送成功！我们会尽快回复您。';
        self.messageType = 'success';
        self.showMessage = true;
        
        // 重置表单
        self.form = {
          name: '',
          email: '',
          subject: '',
          message: ''
        };
        
        self.submitting = false;
        
        // 3秒后隐藏消息
        setTimeout(function() {
          self.showMessage = false;
        }, 3000);
      }, 1000);
    }
  }
}
</script>

<style scoped>
.contact {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.hero {
  text-align: center;
  margin-bottom: 3rem;
  padding: 3rem 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 12px;
}

.title {
  font-size: 3rem;
  margin-bottom: 1rem;
  font-weight: 700;
}

.subtitle {
  font-size: 1.2rem;
  opacity: 0.9;
}

.contact-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 3rem;
}

.contact-form, .contact-info {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
  border: 1px solid #f0f0f0;
}

.contact-form h2, .contact-info h2 {
  color: #2c3e50;
  margin-bottom: 2rem;
  font-size: 1.8rem;
  font-weight: 600;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #2c3e50;
  font-weight: 600;
  font-size: 0.9rem;
}

.form-group input, .form-group textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 1rem;
  transition: border-color 0.3s ease;
  background: #f8f9fa;
}

.form-group input:focus, .form-group textarea:focus {
  outline: none;
  border-color: #42b883;
  background: white;
}

.submit-btn {
  background: #42b883;
  color: white;
  border: none;
  padding: 0.75rem 2rem;
  border-radius: 6px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
  width: 100%;
  font-weight: 600;
}

.submit-btn:hover:not(:disabled) {
  background: #3aa876;
}

.submit-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.info-list {
  display: grid;
  gap: 1.5rem;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 8px;
  border-left: 4px solid #42b883;
}

.info-icon {
  font-size: 1.5rem;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #42b883;
  color: white;
  border-radius: 50%;
}

.info-content h3 {
  color: #2c3e50;
  margin-bottom: 0.25rem;
  font-size: 1rem;
  font-weight: 600;
}

.info-content p {
  color: #666;
  font-size: 0.9rem;
  margin: 0;
}

.message {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 1rem 2rem;
  border-radius: 6px;
  color: white;
  font-weight: 600;
  z-index: 1000;
  animation: slideIn 0.3s ease;
  box-shadow: 0 4px 20px rgba(0,0,0,0.2);
}

.message.success {
  background: #42b883;
}

.message.error {
  background: #e74c3c;
}

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

@media (max-width: 768px) {
  .contact {
    padding: 1rem;
  }
  
  .title {
    font-size: 2rem;
  }
  
  .contact-grid {
    grid-template-columns: 1fr;
  }
  
  .contact-form, .contact-info {
    padding: 1.5rem;
  }
}
</style> 