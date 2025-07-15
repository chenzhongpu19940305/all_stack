<template>
  <div class="about">
    <h1 class="title">关于我们</h1>
    <div class="content">
      <div class="info-card">
        <h2>项目介绍</h2>
        <p>这是一个使用Vue 3和JavaScript构建的简单单页应用。项目展示了Vue的基本功能，包括：</p>
        <ul>
          <li>组件化开发</li>
          <li>路由管理</li>
          <li>响应式数据</li>
          <li>事件处理</li>
        </ul>
      </div>
      
      <div class="info-card">
        <h2>技术栈</h2>
        <div class="tech-list">
          <div class="tech-item" v-for="tech in techStack" :key="tech.name">
            <span class="tech-name">{{ tech.name }}</span>
            <span class="tech-version">{{ tech.version }}</span>
          </div>
        </div>
      </div>
      
      <div class="info-card">
        <h2>开发团队</h2>
        <p>这个项目由Vue开发团队创建，展示了Vue框架的强大功能和易用性。</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'About',
  data() {
    return {
      techStack: [
        { name: 'Vue', version: '3.4.38' },
        { name: 'Vue Router', version: '4.4.3' },
        { name: 'Vite', version: '5.0.0' },
        { name: 'JavaScript', version: 'ES2020' }
      ]
    }
  }
}
</script>

<style scoped>
.about {
  max-width: 800px;
  margin: 0 auto;
}

.title {
  text-align: center;
  font-size: 2.5rem;
  color: #2c3e50;
  margin-bottom: 2rem;
}

.content {
  display: grid;
  gap: 2rem;
}

.info-card {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.info-card h2 {
  color: #42b883;
  margin-bottom: 1rem;
  font-size: 1.5rem;
}

.info-card p {
  line-height: 1.6;
  color: #2c3e50;
  margin-bottom: 1rem;
}

.info-card ul {
  list-style-type: disc;
  margin-left: 2rem;
  color: #2c3e50;
}

.info-card li {
  margin-bottom: 0.5rem;
}

.tech-list {
  display: grid;
  gap: 1rem;
}

.tech-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem;
  background: #f8f9fa;
  border-radius: 4px;
}

.tech-name {
  font-weight: bold;
  color: #2c3e50;
}

.tech-version {
  color: #7f8c8d;
  font-size: 0.9rem;
}
</style> 