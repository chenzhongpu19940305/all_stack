<template>
  <div id="app">
    <nav class="navbar">
      <div class="nav-brand">
        <span class="brand-icon">🚀</span>
        <span class="brand-text">Vue应用</span>
      </div>
      
      <div class="nav-links">
        <router-link to="/" class="nav-link" active-class="active">
          <span class="nav-icon">🏠</span>
          首页
        </router-link>
        <router-link to="/dashboard" class="nav-link" active-class="active">
          <span class="nav-icon">📊</span>
          仪表板
        </router-link>
        <router-link to="/about" class="nav-link" active-class="active">
          <span class="nav-icon">ℹ️</span>
          关于
        </router-link>
        <router-link to="/algorithms" class="nav-link" active-class="active">
          <span class="nav-icon">📊</span>
          算法学习
        </router-link>
        <router-link to="/feature-config" class="nav-link" active-class="active">
          <span class="nav-icon">⚙️</span>
          特性配置
        </router-link>
        <router-link to="/feature-module" class="nav-link" active-class="active">
          <span class="nav-icon">📦</span>
          特性模块
        </router-link>
        <router-link to="/kibana-query" class="nav-link" active-class="active">
          <span class="nav-icon">🔍</span>
          ES查询
        </router-link>
        <router-link to="/bilibili" class="nav-link" active-class="active">
          <span class="nav-icon">📺</span>
          B站
        </router-link>
        <router-link to="/contact" class="nav-link" active-class="active">
          <span class="nav-icon">📞</span>
          联系我们
        </router-link>
      </div>
    </nav>
    
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script>
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'App',
  setup() {
    // 使用Vue 3的响应式API
    // 这里可以添加全局状态管理
    return {}
  }
})
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Arial', sans-serif;
  background-color: #f5f5f5;
  line-height: 1.6;
}

#app {
  min-height: 100vh;
}

.navbar {
  background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.nav-brand {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: white;
  font-weight: 700;
  font-size: 1.2rem;
}

.brand-icon {
  font-size: 1.5rem;
}

.brand-text {
  color: white;
}

.nav-links {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.nav-link {
  color: white;
  text-decoration: none;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.9rem;
}

.nav-link:hover {
  background: rgba(255,255,255,0.1);
  color: #42b883;
}

.nav-link.active {
  background: #42b883;
  color: white;
}

.nav-icon {
  font-size: 1rem;
}

.main-content {
  min-height: calc(100vh - 80px);
  background-color: #f5f5f5;
}

@media (max-width: 768px) {
  .navbar {
    flex-direction: column;
    gap: 1rem;
    padding: 1rem;
  }
  
  .nav-links {
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .nav-link {
    font-size: 0.8rem;
    padding: 0.4rem 0.8rem;
  }
}
</style> 