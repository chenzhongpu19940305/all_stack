<template>
  <div class="route-test">
    <div class="header">
      <h1>🚀 路由测试页面</h1>
      <p>测试所有可用的路由和导航功能</p>
    </div>

    <div class="route-grid">
      <div v-for="route in routes" :key="route.path" class="route-card">
        <div class="route-icon">{{ route.icon }}</div>
        <div class="route-info">
          <h3>{{ route.title }}</h3>
          <p class="route-description">{{ route.description }}</p>
          <p class="route-path">路径: {{ route.path }}</p>
          <p class="route-name">名称: {{ route.name }}</p>
        </div>
        <div class="route-actions">
          <router-link :to="route.path" class="test-btn primary">访问页面</router-link>
          <button @click="testRoute(route)" class="test-btn secondary">测试路由</button>
        </div>
      </div>
    </div>

    <div class="test-results" v-if="testResults.length > 0">
      <h3>测试结果</h3>
      <div v-for="result in testResults" :key="result.id" class="test-result">
        <span class="result-icon">{{ result.success ? '✅' : '❌' }}</span>
        <span class="result-text">{{ result.message }}</span>
        <span class="result-time">{{ result.time }}</span>
      </div>
    </div>

    <div class="navigation-test">
      <h3>导航测试</h3>
      <div class="nav-buttons">
        <button 
          v-for="route in routes" 
          :key="route.path"
          @click="navigateTo(route.path)"
          class="nav-btn"
          :class="{ active: currentRoute === route.path }"
        >
          {{ route.icon }} {{ route.title }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getNavigationMenu } from '../router/routes'

export default {
  name: 'RouteTest',
  setup() {
    const router = useRouter()
    const route = useRoute()
    const testResults = ref([])
    const currentRoute = computed(() => route.path)

    const routes = getNavigationMenu()

    const testRoute = (routeInfo) => {
      const startTime = Date.now()
      
      try {
        // 模拟路由测试
        router.push(routeInfo.path).then(() => {
          const endTime = Date.now()
          const duration = endTime - startTime
          
          testResults.value.push({
            id: Date.now(),
            success: true,
            message: `路由 ${routeInfo.title} 测试成功`,
            time: `${duration}ms`
          })
        }).catch(error => {
          testResults.value.push({
            id: Date.now(),
            success: false,
            message: `路由 ${routeInfo.title} 测试失败: ${error.message}`,
            time: 'N/A'
          })
        })
      } catch (error) {
        testResults.value.push({
          id: Date.now(),
          success: false,
          message: `路由 ${routeInfo.title} 测试异常: ${error.message}`,
          time: 'N/A'
        })
      }
    }

    const navigateTo = (path) => {
      router.push(path)
    }

    onMounted(() => {
      console.log('路由测试页面已加载')
      console.log('当前路由:', currentRoute.value)
      console.log('可用路由:', routes)
    })

    return {
      routes,
      testResults,
      currentRoute,
      testRoute,
      navigateTo
    }
  }
}
</script>

<style scoped>
.route-test {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.header {
  text-align: center;
  margin-bottom: 3rem;
  padding: 2rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 12px;
}

.header h1 {
  margin: 0 0 1rem 0;
  font-size: 2.5rem;
}

.header p {
  margin: 0;
  font-size: 1.1rem;
  opacity: 0.9;
}

.route-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 1.5rem;
  margin-bottom: 3rem;
}

.route-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  display: flex;
  flex-direction: column;
}

.route-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 30px rgba(0,0,0,0.15);
}

.route-icon {
  font-size: 3rem;
  text-align: center;
  margin-bottom: 1rem;
}

.route-info h3 {
  margin: 0 0 0.5rem 0;
  color: #2c3e50;
  font-size: 1.3rem;
}

.route-description {
  color: #666;
  margin: 0 0 1rem 0;
  line-height: 1.4;
}

.route-path, .route-name {
  font-family: 'Courier New', monospace;
  font-size: 0.8rem;
  color: #888;
  margin: 0.2rem 0;
  background: #f8f9fa;
  padding: 0.2rem 0.5rem;
  border-radius: 4px;
}

.route-actions {
  margin-top: auto;
  display: flex;
  gap: 0.5rem;
  padding-top: 1rem;
}

.test-btn {
  flex: 1;
  padding: 0.6rem 1rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  text-decoration: none;
  text-align: center;
  transition: all 0.3s ease;
}

.test-btn.primary {
  background: #4CAF50;
  color: white;
}

.test-btn.primary:hover {
  background: #45a049;
}

.test-btn.secondary {
  background: #2196F3;
  color: white;
}

.test-btn.secondary:hover {
  background: #1976D2;
}

.test-results {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
}

.test-results h3 {
  margin: 0 0 1rem 0;
  color: #2c3e50;
}

.test-result {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 0;
  border-bottom: 1px solid #f0f0f0;
}

.test-result:last-child {
  border-bottom: none;
}

.result-icon {
  font-size: 1.2rem;
}

.result-text {
  flex: 1;
  color: #333;
}

.result-time {
  color: #666;
  font-size: 0.9rem;
  font-family: 'Courier New', monospace;
}

.navigation-test {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
}

.navigation-test h3 {
  margin: 0 0 1rem 0;
  color: #2c3e50;
}

.nav-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.nav-btn {
  padding: 0.5rem 1rem;
  border: 2px solid #e0e0e0;
  background: white;
  color: #333;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
}

.nav-btn:hover {
  border-color: #2196F3;
  color: #2196F3;
}

.nav-btn.active {
  background: #2196F3;
  border-color: #2196F3;
  color: white;
}

@media (max-width: 768px) {
  .route-test {
    padding: 1rem;
  }
  
  .route-grid {
    grid-template-columns: 1fr;
  }
  
  .nav-buttons {
    flex-direction: column;
  }
  
  .nav-btn {
    text-align: center;
  }
}
</style> 