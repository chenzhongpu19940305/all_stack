<template>
  <div class="home">
    <div class="hero">
      <h1 class="title">欢迎使用</h1>
      <p class="subtitle">现代化Web应用开发平台</p>
    </div>
    
    <div class="quick-actions">
      <div class="action-card" v-for="action in state.actions" :key="action.name">
        <div class="action-icon">{{ action.icon }}</div>
        <h3>{{ action.title }}</h3>
        <p>{{ action.description }}</p>
        <div class="action-buttons">
          <router-link :to="action.path" class="action-btn">开始使用</router-link>
          <button @click="showAPI(action.name)" class="api-btn">展示API</button>
        </div>
      </div>
    </div>

    <!-- API抽屉页面 -->
    <div v-if="showApiDrawer" class="api-drawer-overlay" @click="closeApiDrawer">
      <div class="api-drawer" @click.stop>
        <div class="api-drawer-header">
          <h3>{{ currentApiTitle }} - API文档</h3>
          <button @click="closeApiDrawer" class="close-btn">×</button>
        </div>
        <div class="api-drawer-content">
          <div v-if="currentApiContent" class="api-list">
            <div v-for="(api, index) in currentApiContent" :key="index" class="api-item">
              <div class="api-method" :class="api.method.toLowerCase()">
                {{ api.method }}
              </div>
              <div class="api-info">
                <div class="api-url">{{ api.url }}</div>
                <div class="api-description">{{ api.description }}</div>
                <div v-if="api.parameters && api.parameters.length" class="api-parameters">
                  <h5>参数：</h5>
                  <div class="parameter-list">
                    <div v-for="param in api.parameters" :key="param.name" class="parameter-item">
                      <span class="param-name">{{ param.name }}</span>
                      <span class="param-type">{{ param.type }}</span>
                      <span class="param-required" :class="{ required: param.required }">
                        {{ param.required ? '必填' : '可选' }}
                      </span>
                      <span class="param-desc">{{ param.description }}</span>
                    </div>
                  </div>
                </div>
                <div v-if="api.requestBody" class="api-request-body">
                  <h5>请求体：</h5>
                  <pre class="json-example">{{ api.requestBody }}</pre>
                </div>
                <div v-if="api.response" class="api-response">
                  <h5>响应示例：</h5>
                  <pre class="json-example">{{ api.response }}</pre>
                </div>
              </div>
            </div>
          </div>
          <div v-else class="no-api">暂无API文档</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { getNavigationMenu } from '../router/routes'

// 使用reactive()管理复杂响应式对象
const state = reactive({
  actions: getNavigationMenu()
    .filter(route => route.path !== '/')
    .filter(route => route.name !== 'EdrawMind') // 在首页不显示EdrawMind编辑器
});

// 使用ref()管理简单响应式数据
const showApiDrawer = ref(false)
const currentApiTitle = ref('')
const currentApiContent = ref([])

// API文档内容映射 - 详细的API接口信息
const apiDocs = {
  'Docs': [
    {
      method: 'GET',
      url: '/api/docs/documents',
      description: '获取文档列表',
      parameters: [
        { name: 'page', type: 'number', required: false, description: '页码，默认1' },
        { name: 'size', type: 'number', required: false, description: '每页大小，默认20' },
        { name: 'q', type: 'string', required: false, description: '搜索关键词' }
      ],
      response: `{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "pages": 5,
    "current": 1,
    "records": [...]
  }
}`
    },
    {
      method: 'POST',
      url: '/api/docs/upload',
      description: '上传文档',
      parameters: [
        { name: 'file', type: 'file', required: true, description: '文档文件' },
        { name: 'title', type: 'string', required: true, description: '文档标题' },
        { name: 'category', type: 'string', required: false, description: '文档分类' }
      ],
      requestBody: `{
  "title": "示例文档",
  "category": "技术文档"
}`,
      response: `{
  "code": 200,
  "message": "上传成功",
  "data": {
    "id": "doc_123",
    "title": "示例文档",
    "url": "/uploads/documents/example.pdf"
  }
}`
    }
  ],
  'DocumentEditor': [
    {
      method: 'GET',
      url: '/api/document-editor/documents',
      description: '获取文档列表',
      parameters: [
        { name: 'page', type: 'number', required: false, description: '页码' },
        { name: 'size', type: 'number', required: false, description: '每页大小' },
        { name: 'q', type: 'string', required: false, description: '搜索关键词' }
      ],
      response: `{
  "code": 200,
  "data": {
    "total": 50,
    "records": [...]
  }
}`
    },
    {
      method: 'GET',
      url: '/api/document-editor/documents/{id}',
      description: '获取文档详情',
      parameters: [
        { name: 'id', type: 'string', required: true, description: '文档ID' }
      ],
      response: `{
  "code": 200,
  "data": {
    "id": "doc_123",
    "title": "文档标题",
    "content": "文档内容",
    "createdAt": "2024-01-01T00:00:00Z"
  }
}`
    },
    {
      method: 'POST',
      url: '/api/document-editor/documents',
      description: '创建新文档',
      requestBody: `{
  "title": "新文档",
  "content": "文档内容"
}`,
      response: `{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": "doc_456",
    "title": "新文档"
  }
}`
    },
    {
      method: 'PUT',
      url: '/api/document-editor/documents/{id}',
      description: '更新文档',
      parameters: [
        { name: 'id', type: 'string', required: true, description: '文档ID' }
      ],
      requestBody: `{
  "title": "更新后的标题",
  "content": "更新后的内容"
}`,
      response: `{
  "code": 200,
  "message": "更新成功"
}`
    },
    {
      method: 'DELETE',
      url: '/api/document-editor/documents/{id}',
      description: '删除文档',
      parameters: [
        { name: 'id', type: 'string', required: true, description: '文档ID' }
      ],
      response: `{
  "code": 200,
  "message": "删除成功"
}`
    }
  ],
  'Gallery': [
    {
      method: 'GET',
      url: '/api/gallery/records',
      description: '获取问答记录列表',
      parameters: [
        { name: 'page', type: 'number', required: false, description: '页码' },
        { name: 'size', type: 'number', required: false, description: '每页大小' }
      ],
      response: `{
  "code": 200,
  "data": {
    "total": 30,
    "records": [...]
  }
}`
    },
    {
      method: 'POST',
      url: '/api/gallery/records',
      description: '创建问答记录',
      requestBody: `{
  "question": "问题内容",
  "answer": "答案内容",
  "tags": ["标签1", "标签2"]
}`,
      response: `{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": "qa_123"
  }
}`
    }
  ],
  'VideoGallery': [
    {
      method: 'GET',
      url: '/api/video/records',
      description: '获取视频列表',
      parameters: [
        { name: 'page', type: 'number', required: false, description: '页码' },
        { name: 'category', type: 'string', required: false, description: '视频分类' }
      ],
      response: `{
  "code": 200,
  "data": {
    "total": 25,
    "records": [...]
  }
}`
    },
    {
      method: 'POST',
      url: '/api/video/upload',
      description: '上传视频文件',
      parameters: [
        { name: 'file', type: 'file', required: true, description: '视频文件' },
        { name: 'title', type: 'string', required: true, description: '视频标题' }
      ],
      response: `{
  "code": 200,
  "message": "上传成功",
  "data": {
    "id": "video_123",
    "url": "/uploads/videos/example.mp4"
  }
}`
    }
  ],
  'CodeSnippets': [
    {
      method: 'GET',
      url: '/api/code-snippets/records',
      description: '获取代码片段列表',
      parameters: [
        { name: 'page', type: 'number', required: false, description: '页码' },
        { name: 'language', type: 'string', required: false, description: '编程语言' }
      ],
      response: `{
  "code": 200,
  "data": {
    "total": 40,
    "records": [...]
  }
}`
    },
    {
      method: 'POST',
      url: '/api/code-snippets/records',
      description: '创建代码片段',
      requestBody: `{
  "title": "快速排序算法",
  "code": "function quickSort(arr) {...}",
  "language": "javascript",
  "tags": ["算法", "排序"]
}`,
      response: `{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": "code_123"
  }
}`
    }
  ],
  'EdrawMindHome': [
    {
      method: 'GET',
      url: '/api/mindmap',
      description: '获取思维导图列表',
      response: `{
  "code": 200,
  "data": {
    "total": 15,
    "records": [...]
  }
}`
    },
    {
      method: 'POST',
      url: '/api/mindmap',
      description: '创建思维导图',
      requestBody: `{
  "title": "项目规划",
  "description": "项目整体规划思维导图"
}`,
      response: `{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": "mind_123"
  }
}`
    }
  ],
  'RouteTest': [
    {
      method: 'GET',
      url: '/api/route-test',
      description: '测试路由跳转',
      response: `{
  "code": 200,
  "message": "路由测试成功",
  "data": {
    "currentRoute": "/home",
    "availableRoutes": [...]
  }
}`
    }
  ]
}

// 显示API文档
const showAPI = (actionName) => {
  console.log('showAPI被调用，actionName:', actionName)
  currentApiTitle.value = state.actions.find(a => a.name === actionName)?.title || '未知功能'
  currentApiContent.value = apiDocs[actionName] || []
  showApiDrawer.value = true
  
  console.log('设置后的值:', {
    currentApiTitle: currentApiTitle.value,
    currentApiContent: currentApiContent.value,
    showApiDrawer: showApiDrawer.value
  })
}

// 关闭API抽屉
const closeApiDrawer = () => {
  showApiDrawer.value = false
  currentApiTitle.value = ''
  currentApiContent.value = []
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

.action-buttons {
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
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
  border: none;
  cursor: pointer;
}

.action-btn:hover {
  background: #3aa876;
}

.api-btn {
  display: inline-block;
  background: #667eea;
  color: white;
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  text-decoration: none;
  font-weight: 500;
  transition: background-color 0.3s ease;
  border: none;
  cursor: pointer;
}

.api-btn:hover {
  background: #5a6fd8;
}

/* API抽屉样式 */
.api-drawer-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.api-drawer {
  background: white;
  border-radius: 12px;
  max-width: 800px;
  width: 90%;
  max-height: 90vh;
  overflow: hidden;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
  display: flex;
  flex-direction: column;
}

.api-drawer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem 2rem;
  border-bottom: 1px solid #f0f0f0;
  background: #f8f9fa;
}

.api-drawer-header h3 {
  margin: 0;
  color: #2c3e50;
  font-size: 1.3rem;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #666;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background-color 0.3s ease;
}

.close-btn:hover {
  background: #e9ecef;
}

.api-drawer-content {
  padding: 2rem;
  max-height: 70vh;
  overflow-y: auto;
  flex-grow: 1;
}

.api-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.api-item {
  background: #f9f9f9;
  padding: 1.5rem;
  border-radius: 8px;
  border: 1px solid #eee;
  display: flex;
  align-items: flex-start;
  gap: 1rem;
}

.api-method {
  padding: 0.5rem 1rem;
  border-radius: 5px;
  font-size: 0.9rem;
  font-weight: bold;
  color: white;
  text-transform: uppercase;
  min-width: 60px;
  text-align: center;
}

.api-method.get { background-color: #42b883; }
.api-method.post { background-color: #41b883; }
.api-method.put { background-color: #e0a82e; }
.api-method.delete { background-color: #e74c3c; }

.api-info {
  flex-grow: 1;
}

.api-url {
  font-size: 1rem;
  color: #3498db;
  margin-bottom: 0.5rem;
  font-weight: 500;
  font-family: 'Courier New', monospace;
}

.api-description {
  font-size: 0.9rem;
  color: #555;
  margin-bottom: 1rem;
  line-height: 1.5;
}

.api-parameters, .api-request-body, .api-response {
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px dashed #eee;
}

.api-parameters h5, .api-request-body h5, .api-response h5 {
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
  color: #444;
  font-weight: 600;
}

.parameter-list {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
  margin-top: 0.5rem;
}

.parameter-item {
  background: #e0f7fa;
  padding: 0.4rem 0.8rem;
  border-radius: 4px;
  font-size: 0.8rem;
  color: #007bff;
  display: flex;
  align-items: center;
  gap: 0.3rem;
  flex-wrap: wrap;
}

.param-name {
  font-weight: bold;
  color: #2c3e50;
}

.param-type {
  font-style: italic;
  color: #666;
}

.param-required {
  font-size: 0.75rem;
  color: #e74c3c;
  font-weight: bold;
}

.param-required.required {
  color: #e74c3c;
}

.param-desc {
  font-size: 0.8rem;
  color: #777;
  margin-left: 0.5rem;
}

.json-example {
  background-color: #f5f5f5;
  padding: 1rem;
  border-radius: 5px;
  font-size: 0.9rem;
  overflow-x: auto;
  white-space: pre-wrap;
  word-break: break-all;
  border: 1px solid #ddd;
  font-family: 'Courier New', monospace;
  color: #333;
}

.no-api {
  text-align: center;
  color: #999;
  font-style: italic;
  padding: 2rem;
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

  .action-buttons {
    flex-direction: column;
    align-items: center;
  }

  .api-drawer {
    width: 95%;
    margin: 1rem;
  }

  .api-drawer-header {
    padding: 1rem 1.5rem;
  }

  .api-drawer-content {
    padding: 1.5rem;
  }
}
</style>