<template>
  <div class="about">
    <div class="hero-section">
      <h1 class="main-title">项目介绍</h1>
      <p class="subtitle">基于Vue 3和JavaScript构建的现代化Web应用</p>
    </div>
    
    <div class="content-grid">
      <!-- 项目概述 -->
      <div class="card">
        <div class="card-icon">📋</div>
        <h3>项目概述</h3>
        <p>这是一个功能丰富的单页应用，展示了现代Web开发的最佳实践。项目包含算法学习、特性配置、ES查询等多个功能模块。</p>
      </div>
      
      <!-- 技术特性 -->
      <div class="card">
        <div class="card-icon">⚡</div>
        <h3>技术特性</h3>
        <ul class="feature-list">
          <li>组件化开发</li>
          <li>响应式数据绑定</li>
          <li>路由管理</li>
          <li>状态管理</li>
        </ul>
      </div>
      
      <!-- 技术栈 -->
      <div class="card">
        <div class="card-icon">🛠️</div>
        <h3>技术栈</h3>
        <div class="tech-grid">
          <div class="tech-item" v-for="tech in techStack">
            <span class="tech-name">{{ tech.name }}</span>
            <span class="tech-version">{{ tech.version }}</span>
          </div>
        </div>
      </div>
      
      <!-- 功能模块 -->
      <div class="card">
        <div class="card-icon">🎯</div>
        <h3>功能模块</h3>
        <div class="module-list">
          <div class="module-item" v-for="module in modules">
            <span class="module-name">{{ module.name }}</span>
            <span class="module-desc">{{ module.description }}</span>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 统计信息 -->
    <div class="stats-section">
      <div class="stat-item">
        <div class="stat-number">{{ stats.components }}</div>
        <div class="stat-label">组件数量</div>
      </div>
      <div class="stat-item">
        <div class="stat-number">{{ stats.features }}</div>
        <div class="stat-label">功能特性</div>
      </div>
      <div class="stat-item">
        <div class="stat-number">{{ stats.lines }}</div>
        <div class="stat-label">代码行数</div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'

export default {
  name: 'About',
  setup: function() {
    var techStack = reactive([
      { name: 'Vue', version: '3.4.0' },
      { name: 'Vue Router', version: '4.2.5' },
      { name: 'Vite', version: '5.0.0' },
      { name: 'JavaScript', version: 'ES5' }
    ]);

    var modules = reactive([
      { name: '算法学习', description: '算法可视化与学习平台' },
      { name: '特性配置', description: '系统特性管理工具' },
      { name: 'ES查询', description: 'Elasticsearch查询工具' },
      { name: '数据管理', description: '用户数据CRUD操作' }
    ]);

    var stats = reactive({
      components: 8,
      features: 12,
      lines: 5000
    });

    function updateStats() {
      // 模拟数据更新
      setTimeout(function() {
        stats.components = 10;
      }, 2000);
    }

    onMounted(function() {
      updateStats();
    });

    return {
      techStack: techStack,
      modules: modules,
      stats: stats
    }
  }
}
</script>

<style scoped>
.about {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.hero-section {
  text-align: center;
  margin-bottom: 3rem;
  padding: 3rem 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 12px;
}

.main-title {
  font-size: 3rem;
  margin-bottom: 1rem;
  font-weight: 700;
}

.subtitle {
  font-size: 1.2rem;
  opacity: 0.9;
}

.content-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

.card {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  border: 1px solid #f0f0f0;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 30px rgba(0,0,0,0.15);
}

.card-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
  text-align: center;
}

.card h3 {
  color: #2c3e50;
  margin-bottom: 1rem;
  font-size: 1.5rem;
  font-weight: 600;
}

.card p {
  color: #666;
  line-height: 1.6;
  margin-bottom: 1rem;
}

.feature-list {
  list-style: none;
  padding: 0;
}

.feature-list li {
  padding: 0.5rem 0;
  color: #666;
  position: relative;
  padding-left: 1.5rem;
}

.feature-list li:before {
  content: "✓";
  position: absolute;
  left: 0;
  color: #42b883;
  font-weight: bold;
}

.tech-grid {
  display: grid;
  gap: 0.5rem;
}

.tech-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem;
  background: #f8f9fa;
  border-radius: 6px;
  border: 1px solid #e9ecef;
}

.tech-name {
  font-weight: 600;
  color: #2c3e50;
}

.tech-version {
  color: #6c757d;
  font-size: 0.9rem;
  background: #e9ecef;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
}

.module-list {
  display: grid;
  gap: 1rem;
}

.module-item {
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 8px;
  border-left: 4px solid #42b883;
}

.module-name {
  display: block;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 0.25rem;
}

.module-desc {
  display: block;
  color: #666;
  font-size: 0.9rem;
}

.stats-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 2rem;
  margin-top: 3rem;
}

.stat-item {
  text-align: center;
  padding: 2rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
  border: 1px solid #f0f0f0;
}

.stat-number {
  display: block;
  font-size: 3rem;
  font-weight: 700;
  color: #42b883;
  margin-bottom: 0.5rem;
}

.stat-label {
  color: #666;
  font-size: 1rem;
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .about {
    padding: 1rem;
  }
  
  .main-title {
    font-size: 2rem;
  }
  
  .content-grid {
    grid-template-columns: 1fr;
  }
  
  .stats-section {
    grid-template-columns: 1fr;
  }
  
  .card {
    padding: 1.5rem;
  }
}

/* 动画效果 */
.card {
  animation: fadeInUp 0.6s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.stat-item {
  animation: fadeInUp 0.6s ease-out;
  animation-delay: 0.2s;
  animation-fill-mode: both;
}

.stat-item:nth-child(2) {
  animation-delay: 0.4s;
}

.stat-item:nth-child(3) {
  animation-delay: 0.6s;
}
</style> 