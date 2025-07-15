<template>
  <div id="app">
    <nav class="navbar">
      <router-link to="/" class="nav-link">首页</router-link>
      <router-link to="/about" class="nav-link">关于</router-link>
      <router-link to="/contact" class="nav-link">联系我们</router-link>
      <router-link to="/kibana-query" class="nav-link">ES查询</router-link>
      <router-link to="/feature-config" class="nav-link">特性配置</router-link>
      <router-link to="/algorithms" class="nav-link">算法学习</router-link>
    </nav>
    
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script>
export default {
  name: 'App'
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
}

#app {
  min-height: 100vh;
}

.navbar {
  background-color: #2c3e50;
  padding: 1rem 2rem;
  display: flex;
  gap: 2rem;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.nav-link {
  color: white;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.nav-link:hover {
  color: #42b883;
}

.main-content {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}
</style> 