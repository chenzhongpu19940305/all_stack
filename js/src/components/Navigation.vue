<template>
  <nav class="navigation">
    <div class="nav-container">
      <div class="nav-brand">
        <router-link to="/" class="brand-link">
          <span class="brand-icon">🏠</span>
          <span class="brand-text">企业知识管理系统</span>
        </router-link>
      </div>
      
      <div class="nav-menu">
        <router-link to="/" class="nav-link" active-class="active">
          <span class="nav-icon">🏠</span>
          <span class="nav-text">首页</span>
        </router-link>
        
        <router-link to="/gallery" class="nav-link" active-class="active">
          <span class="nav-icon">🤖</span>
          <span class="nav-text">AI问答记录</span>
        </router-link>
        

        
        <router-link to="/video-gallery" class="nav-link" active-class="active">
          <span class="nav-icon">🎥</span>
          <span class="nav-text">视频管理</span>
        </router-link>
        

        

        

        
        <router-link to="/mind-map" class="nav-link" active-class="active">
          <span class="nav-icon">🧠</span>
          <span class="nav-text">思维导图</span>
        </router-link>
        

      </div>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'Navigation'
}
</script>

<style scoped>
.navigation {
  background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.nav-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 2rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  min-height: 60px;
}

.nav-brand {
  display: flex;
  align-items: center;
}

.brand-link {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: white;
  text-decoration: none;
  font-weight: bold;
  font-size: 1.2rem;
  transition: opacity 0.3s ease;
}

.brand-link:hover {
  opacity: 0.8;
}

.brand-icon {
  font-size: 1.5rem;
}

.nav-menu {
  display: flex;
  gap: 0.5rem;
  align-items: center;
  flex-wrap: wrap;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 0.3rem;
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  padding: 0.5rem 0.8rem;
  border-radius: 6px;
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.nav-link:hover {
  background: rgba(255, 255, 255, 0.1);
  color: white;
}

.nav-link.active {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  font-weight: 600;
}

.nav-icon {
  font-size: 1rem;
}

.nav-text {
  display: none;
}

/* 响应式设计 */
@media (min-width: 768px) {
  .nav-text {
    display: inline;
  }
  
  .nav-menu {
    gap: 0.3rem;
  }
  
  .nav-link {
    padding: 0.6rem 1rem;
  }
}

@media (max-width: 767px) {
  .nav-container {
    padding: 0 1rem;
    flex-direction: column;
    gap: 0.5rem;
    padding-top: 0.5rem;
    padding-bottom: 0.5rem;
  }
  
  .nav-menu {
    width: 100%;
    justify-content: center;
    gap: 0.2rem;
  }
  
  .nav-link {
    padding: 0.4rem 0.6rem;
    font-size: 0.8rem;
  }
  
  .brand-text {
    font-size: 1rem;
  }
}

@media (max-width: 480px) {
  .nav-menu {
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .nav-link {
    padding: 0.3rem 0.5rem;
    font-size: 0.7rem;
  }
}
</style>