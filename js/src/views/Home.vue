<template>
  <div class="home">
    <div class="hero">
      <h1 class="title">欢迎使用</h1>
      <p class="subtitle">现代化Web应用开发平台</p>
    </div>
    
    <div class="quick-actions">
              <div class="action-card" v-for="action in actions">
          <div class="action-icon">{{ action.icon }}</div>
          <h3>{{ action.title }}</h3>
          <p>{{ action.description }}</p>
          <router-link :to="action.path" class="action-btn">开始使用</router-link>
        </div>
    </div>
    

  </div>
</template>

<script>
import { reactive } from 'vue'
import { getNavigationMenu } from '../router/routes'

export default {
  name: 'Home',
  setup() {

    
    // 使用reactive()管理复杂响应式对象
    var state = reactive({
      actions: getNavigationMenu().filter(route => route.path !== '/')
    });



    // 返回响应式数据和方法
    return {
      actions: state.actions
    }
  }
}
</script>

<style scoped>
.home {
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

.quick-actions {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

.action-card {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
  text-align: center;
  transition: transform 0.3s ease;
  border: 1px solid #f0f0f0;
}

.action-card:hover {
  transform: translateY(-5px);
}

.action-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.action-card h3 {
  color: #2c3e50;
  margin-bottom: 1rem;
  font-size: 1.5rem;
}

.action-card p {
  color: #666;
  margin-bottom: 1.5rem;
  line-height: 1.6;
}

.action-btn {
  display: inline-block;
  background: #42b883;
  color: white;
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  text-decoration: none;
  font-weight: 500;
  transition: background-color 0.3s ease;
}

.action-btn:hover {
  background: #3aa876;
}



@media (max-width: 768px) {
  .home {
    padding: 1rem;
  }
  
  .title {
    font-size: 2rem;
  }
  
  .quick-actions {
    grid-template-columns: 1fr;
  }
  
  .action-card {
    padding: 1.5rem;
  }
}
</style>