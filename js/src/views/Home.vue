<template>
  <div class="home">
    <h1 class="title">欢迎来到Vue应用</h1>
    <p class="subtitle">这是一个使用Vue 3和JavaScript构建的简单应用</p>
    
    <div class="features">
      <div class="feature-card" v-for="feature in features" :key="feature.id">
        <h3>{{ feature.title }}</h3>
        <p>{{ feature.description }}</p>
      </div>
    </div>
    
    <div class="counter-section">
      <h2>计数器示例</h2>
      <div class="counter">
        <button @click="decrement" class="btn">-</button>
        <span class="count">{{ count }}</span>
        <button @click="increment" class="btn">+</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      count: 0,
      features: [
        {
          id: 1,
          title: 'Vue 3',
          description: '使用最新的Vue 3框架，享受更好的性能和开发体验'
        },
        {
          id: 2,
          title: 'JavaScript',
          description: '纯JavaScript开发，无需TypeScript，简单易学'
        },
        {
          id: 3,
          title: 'Vue Router',
          description: '单页应用路由管理，流畅的页面切换体验'
        }
      ]
    }
  },
  methods: {
    increment() {
      this.count++
    },
    decrement() {
      this.count--
    }
  }
}
</script>

<style scoped>
.home {
  text-align: center;
}

.title {
  font-size: 2.5rem;
  color: #2c3e50;
  margin-bottom: 1rem;
}

.subtitle {
  font-size: 1.2rem;
  color: #7f8c8d;
  margin-bottom: 3rem;
}

.features {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

.feature-card {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-card h3 {
  color: #42b883;
  margin-bottom: 1rem;
}

.counter-section {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  max-width: 400px;
  margin: 0 auto;
}

.counter-section h2 {
  margin-bottom: 1rem;
  color: #2c3e50;
}

.counter {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
}

.btn {
  background: #42b883;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1.2rem;
  transition: background-color 0.3s ease;
}

.btn:hover {
  background: #3aa876;
}

.count {
  font-size: 2rem;
  font-weight: bold;
  color: #2c3e50;
  min-width: 3rem;
}
</style> 