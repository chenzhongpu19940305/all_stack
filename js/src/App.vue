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
        <router-link to="/kibana-query" class="nav-link" active-class="active">
          <span class="nav-icon">🔍</span>
          ES查询
        </router-link>
        <router-link to="/es-field-search" class="nav-link" active-class="active">
          <span class="nav-icon">🔎</span>
          ES字段搜索
        </router-link>
        <router-link to="/query-list" class="nav-link" active-class="active">
          <span class="nav-icon">📋</span>
          查询列表
        </router-link>
        <router-link to="/gallery" class="nav-link" active-class="active">
          <span class="nav-icon">🤖</span>
          AI问答记录
        </router-link>
        <router-link to="/video-gallery" class="nav-link" active-class="active">
          <span class="nav-icon">🎬</span>
          哔哩哔哩
        </router-link>
        <router-link to="/mind-map" class="nav-link" active-class="active">
          <span class="nav-icon">🧠</span>
          思维导图
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

<script setup>
import { ref } from 'vue'

// 全局搜索状态
const globalSearch = ref('')

// 处理全局搜索
const handleGlobalSearch = (searchValue) => {
  console.log('全局搜索:', searchValue)
  // 这里可以添加全局搜索逻辑
  // 比如跳转到搜索结果页面或显示搜索结果
  if (searchValue.trim()) {
    alert(`搜索: ${searchValue}`)
  }
}
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

/* 搜索组件样式 */
.nav-search {
  flex: 1;
  max-width: 400px;
  margin: 0 2rem;
}

/* 自定义搜索组件在导航栏中的样式 */
.nav-search .search-input-wrapper {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
}

.nav-search .search-input-wrapper:hover {
  border-color: rgba(255, 255, 255, 0.3);
}

.nav-search .search-input-wrapper:focus-within {
  border-color: #42b883;
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.2);
}

.nav-search .search-icon {
  color: rgba(255, 255, 255, 0.7);
}

.nav-search .search-input {
  color: white;
}

.nav-search .search-input::placeholder {
  color: rgba(255, 255, 255, 0.6);
}

.nav-search .clear-button {
  color: rgba(255, 255, 255, 0.7);
}

.nav-search .clear-button:hover {
  background: rgba(255, 255, 255, 0.1);
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
  
  .nav-search {
    order: 2;
    max-width: 100%;
    margin: 0;
  }
  
  .nav-links {
    order: 3;
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .nav-link {
    font-size: 0.8rem;
    padding: 0.4rem 0.8rem;
  }
}
</style>