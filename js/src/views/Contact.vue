<template>
  <div class="contact">
    <h1 class="title">联系我们</h1>
    <div class="contact-content">
      <div class="contact-form">
        <h2>发送消息</h2>
        <form @submit.prevent="submitForm">
          <div class="form-group">
            <label for="name">姓名：</label>
            <input 
              type="text" 
              id="name" 
              v-model="form.name" 
              required
              placeholder="请输入您的姓名"
            >
          </div>
          
          <div class="form-group">
            <label for="email">邮箱：</label>
            <input 
              type="email" 
              id="email" 
              v-model="form.email" 
              required
              placeholder="请输入您的邮箱"
            >
          </div>
          
          <div class="form-group">
            <label for="subject">主题：</label>
            <input 
              type="text" 
              id="subject" 
              v-model="form.subject" 
              required
              placeholder="请输入消息主题"
            >
          </div>
          
          <div class="form-group">
            <label for="message">消息：</label>
            <textarea 
              id="message" 
              v-model="form.message" 
              required
              placeholder="请输入您的消息"
              rows="5"
            ></textarea>
          </div>
          
          <button type="submit" class="submit-btn">发送消息</button>
        </form>
      </div>
      
      <div class="contact-info">
        <h2>联系信息</h2>
        <div class="info-item">
          <h3>邮箱</h3>
          <p>contact@vueapp.com</p>
        </div>
        <div class="info-item">
          <h3>电话</h3>
          <p>+86 123 4567 8900</p>
        </div>
        <div class="info-item">
          <h3>地址</h3>
          <p>北京市朝阳区某某街道123号</p>
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
  data() {
    return {
      form: {
        name: '',
        email: '',
        subject: '',
        message: ''
      },
      showMessage: false,
      messageText: '',
      messageType: 'success'
    }
  },
  methods: {
    submitForm() {
      // 模拟表单提交
      console.log('表单数据：', this.form)
      
      // 显示成功消息
      this.messageText = '消息发送成功！我们会尽快回复您。'
      this.messageType = 'success'
      this.showMessage = true
      
      // 重置表单
      this.form = {
        name: '',
        email: '',
        subject: '',
        message: ''
      }
      
      // 3秒后隐藏消息
      setTimeout(() => {
        this.showMessage = false
      }, 3000)
    }
  }
}
</script>

<style scoped>
.contact {
  max-width: 1000px;
  margin: 0 auto;
}

.title {
  text-align: center;
  font-size: 2.5rem;
  color: #2c3e50;
  margin-bottom: 2rem;
}

.contact-content {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 3rem;
}

.contact-form, .contact-info {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.contact-form h2, .contact-info h2 {
  color: #42b883;
  margin-bottom: 1.5rem;
  font-size: 1.5rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #2c3e50;
  font-weight: bold;
}

.form-group input, .form-group textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.3s ease;
}

.form-group input:focus, .form-group textarea:focus {
  outline: none;
  border-color: #42b883;
}

.submit-btn {
  background: #42b883;
  color: white;
  border: none;
  padding: 0.75rem 2rem;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.submit-btn:hover {
  background: #3aa876;
}

.info-item {
  margin-bottom: 2rem;
}

.info-item h3 {
  color: #42b883;
  margin-bottom: 0.5rem;
  font-size: 1.1rem;
}

.info-item p {
  color: #2c3e50;
  line-height: 1.6;
}

.message {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 1rem 2rem;
  border-radius: 4px;
  color: white;
  font-weight: bold;
  z-index: 1000;
  animation: slideIn 0.3s ease;
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
  .contact-content {
    grid-template-columns: 1fr;
  }
}
</style> 