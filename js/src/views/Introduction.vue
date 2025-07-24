<template>
  <div class="introduction-container">
    <div class="page-header">
      <h1>介绍模块</h1>
      <p>欢迎来到介绍模块，这里包含四个不同的页面内容</p>
    </div>

    <div class="content-wrapper">
      <!-- 页面切换控制区域 -->
      <div class="control-panel">
        <div class="page-tabs">
          <button 
            v-for="page in currentPages" 
            :key="page.id"
            :class="['tab-button', { active: activePage === page.id }]"
            @click="setActivePage(page.id)"
          >
            {{ page.name }}
          </button>
        </div>
        
        <div class="switch-container">
          <label class="switch-label">显示模式:</label>
          <div class="switch-wrapper">
            <span :class="{ active: !showMode }">AA + BB</span>
            <label class="switch">
              <input type="checkbox" v-model="showMode" @change="handleSwitchChange">
              <span class="slider"></span>
            </label>
            <span :class="{ active: showMode }">CC + DD</span>
          </div>
        </div>
      </div>

      <!-- 页面内容区域 -->
      <div class="pages-content">
        <transition name="fade" mode="out-in">
          <div v-if="!showMode" key="group1" class="page-group">
            <div class="page-section">
              <div class="page-card" :class="{ active: activePage === 'AA' }">
                <h2>页面 AA</h2>
                <div class="page-content">
                  <p>这是页面AA的内容区域</p>
                  <div class="feature-list">
                    <div class="feature-item">
                      <span class="feature-icon">🚀</span>
                      <span>功能特性A1</span>
                    </div>
                    <div class="feature-item">
                      <span class="feature-icon">⚡</span>
                      <span>功能特性A2</span>
                    </div>
                    <div class="feature-item">
                      <span class="feature-icon">🎯</span>
                      <span>功能特性A3</span>
                    </div>
                  </div>
                  <button class="action-button">了解更多 AA</button>
                </div>
              </div>
            </div>
            
            <div class="page-section">
              <div class="page-card" :class="{ active: activePage === 'BB' }">
                <h2>页面 BB</h2>
                <div class="page-content">
                  <p>这是页面BB的内容区域</p>
                  <div class="feature-list">
                    <div class="feature-item">
                      <span class="feature-icon">🔧</span>
                      <span>功能特性B1</span>
                    </div>
                    <div class="feature-item">
                      <span class="feature-icon">📊</span>
                      <span>功能特性B2</span>
                    </div>
                    <div class="feature-item">
                      <span class="feature-icon">🛡️</span>
                      <span>功能特性B3</span>
                    </div>
                  </div>
                  <button class="action-button">了解更多 BB</button>
                </div>
              </div>
            </div>
          </div>
          
          <div v-else key="group2" class="page-group">
            <div class="page-section">
              <div class="page-card" :class="{ active: activePage === 'CC' }">
                <h2>页面 CC</h2>
                <div class="page-content">
                  <p>这是页面CC的内容区域</p>
                  <div class="feature-list">
                    <div class="feature-item">
                      <span class="feature-icon">🌟</span>
                      <span>功能特性C1</span>
                    </div>
                    <div class="feature-item">
                      <span class="feature-icon">💡</span>
                      <span>功能特性C2</span>
                    </div>
                    <div class="feature-item">
                      <span class="feature-icon">🎨</span>
                      <span>功能特性C3</span>
                    </div>
                  </div>
                  <button class="action-button">了解更多 CC</button>
                </div>
              </div>
            </div>
            
            <div class="page-section">
              <div class="page-card" :class="{ active: activePage === 'DD' }">
                <h2>页面 DD</h2>
                <div class="page-content">
                  <p>这是页面DD的内容区域</p>
                  <div class="feature-list">
                    <div class="feature-item">
                      <span class="feature-icon">🔮</span>
                      <span>功能特性D1</span>
                    </div>
                    <div class="feature-item">
                      <span class="feature-icon">🎪</span>
                      <span>功能特性D2</span>
                    </div>
                    <div class="feature-item">
                      <span class="feature-icon">🎭</span>
                      <span>功能特性D3</span>
                    </div>
                  </div>
                  <button class="action-button">了解更多 DD</button>
                </div>
              </div>
            </div>
          </div>
        </transition>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

// 响应式数据
const showMode = ref(false) // false: 显示AA+BB, true: 显示CC+DD
const activePage = ref('AA')

// 页面配置
const pages = [
  { id: 'AA', name: 'AA' },
  { id: 'BB', name: 'BB' },
  { id: 'CC', name: 'CC' },
  { id: 'DD', name: 'DD' }
]

// 当前可见的页面
const visiblePages = computed(() => {
  return showMode.value ? ['CC', 'DD'] : ['AA', 'BB']
})

// 当前显示的页面标签
const currentPages = computed(() => {
  return showMode.value 
    ? [{ id: 'CC', name: 'CC' }, { id: 'DD', name: 'DD' }]
    : [{ id: 'AA', name: 'AA' }, { id: 'BB', name: 'BB' }]
})

// 方法
const setActivePage = (pageId) => {
  activePage.value = pageId
}

const handleSwitchChange = () => {
  // 切换模式时，自动选择第一个可见页面
  if (showMode.value) {
    activePage.value = 'CC'
  } else {
    activePage.value = 'AA'
  }
}
</script>

<style scoped>
.introduction-container {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
  min-height: 100vh;
}

.page-header {
  text-align: center;
  margin-bottom: 3rem;
}

.page-header h1 {
  font-size: 2.5rem;
  color: #2c3e50;
  margin-bottom: 1rem;
  font-weight: 700;
}

.page-header p {
  font-size: 1.1rem;
  color: #7f8c8d;
  max-width: 600px;
  margin: 0 auto;
}

.content-wrapper {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.control-panel {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 1.5rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 1rem;
}

.page-tabs {
  display: flex;
  gap: 0.5rem;
}

.tab-button {
  padding: 0.5rem 1rem;
  border: none;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.tab-button:hover {
  background: rgba(255, 255, 255, 0.3);
}

.tab-button.active {
  background: white;
  color: #667eea;
  font-weight: 600;
}

.switch-container {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.switch-label {
  color: white;
  font-weight: 500;
}

.switch-wrapper {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.switch-wrapper span {
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.9rem;
  transition: all 0.3s ease;
}

.switch-wrapper span.active {
  color: white;
  font-weight: 600;
}

.switch {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 24px;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.3);
  transition: 0.3s;
  border-radius: 24px;
}

.slider:before {
  position: absolute;
  content: "";
  height: 18px;
  width: 18px;
  left: 3px;
  bottom: 3px;
  background-color: white;
  transition: 0.3s;
  border-radius: 50%;
}

input:checked + .slider {
  background-color: #42b883;
}

input:checked + .slider:before {
  transform: translateX(26px);
}

.pages-content {
  padding: 2rem;
}

.page-group {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
}

.page-section {
  display: flex;
  flex-direction: column;
}

.page-card {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 1.5rem;
  transition: all 0.3s ease;
  border: 2px solid transparent;
  height: 100%;
}

.page-card.active {
  border-color: #42b883;
  background: #f0fff4;
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(66, 184, 131, 0.15);
}

.page-card h2 {
  color: #2c3e50;
  margin-bottom: 1rem;
  font-size: 1.5rem;
}

.page-content p {
  color: #7f8c8d;
  margin-bottom: 1.5rem;
  line-height: 1.6;
}

.feature-list {
  margin-bottom: 1.5rem;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
  color: #34495e;
}

.feature-icon {
  font-size: 1.2rem;
}

.action-button {
  background: linear-gradient(135deg, #42b883 0%, #369870 100%);
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
  width: 100%;
}

.action-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 15px rgba(66, 184, 131, 0.3);
}

/* 过渡动画 */
.fade-enter-active, .fade-leave-active {
  transition: all 0.3s ease;
}

.fade-enter-from {
  opacity: 0;
  transform: translateX(20px);
}

.fade-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .introduction-container {
    padding: 1rem;
  }
  
  .control-panel {
    flex-direction: column;
    text-align: center;
  }
  
  .page-group {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .page-tabs {
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .switch-container {
    flex-direction: column;
    gap: 0.5rem;
  }
}
</style>