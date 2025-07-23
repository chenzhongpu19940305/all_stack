<template>
  <div class="query-list-container">
    <div class="header">
      <h1>查询记录列表</h1>
      <div class="search-bar">
        <input 
          v-model="searchKeyword" 
          type="text" 
          placeholder="搜索记录..." 
          class="search-input"
        />
        <button @click="searchRecords" class="search-btn">搜索</button>
      </div>
    </div>

    <div class="table-container">
      <table class="query-table">
        <thead>
          <tr>
            <th>序号</th>
            <th>应用ID</th>
            <th>请求路径</th>
            <th>查询日期</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(record, index) in filteredRecords" :key="record.id" class="table-row">
            <td>{{ index + 1 }}</td>
            <td>{{ record.appId }}</td>
            <td>{{ record.reqPath }}</td>
            <td>{{ formatDate(record.date) }}</td>
            <td>
              <span :class="['status-badge', record.status]">{{ record.status }}</span>
            </td>
            <td>
              <button @click="openDrawer(record)" class="detail-btn">查看详情</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 抽屉柜 -->
    <div 
      v-if="showDrawer" 
      class="drawer-overlay" 
      @click="closeDrawer"
    >
      <div class="drawer-content" @click.stop>
        <div class="drawer-header">
          <h2>查询详情</h2>
          <button @click="closeDrawer" class="close-btn">×</button>
        </div>
        <div class="drawer-body">
          <QueryDetail 
            :app-id="selectedRecord.appId"
            :req-path="selectedRecord.reqPath"
            :date="selectedRecord.date"
            :status="selectedRecord.status"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import QueryDetail from '../components/QueryDetail.vue'

// 响应式数据
const searchKeyword = ref('')
const showDrawer = ref(false)
const selectedRecord = ref({})

// 模拟数据
const records = ref([
  {
    id: 1,
    appId: 'app_001',
    reqPath: '/api/users/search',
    date: '2024-01-15',
    status: '成功'
  },
  {
    id: 2,
    appId: 'app_002',
    reqPath: '/api/orders/list',
    date: '2024-01-15',
    status: '成功'
  },
  {
    id: 3,
    appId: 'app_003',
    reqPath: '/api/products/filter',
    date: '2024-01-15',
    status: '失败'
  },
  {
    id: 4,
    appId: 'app_001',
    reqPath: '/api/logs/query',
    date: '2024-01-15',
    status: '成功'
  },
  {
    id: 5,
    appId: 'app_004',
    reqPath: '/api/analytics/data',
    date: '2024-01-15',
    status: '成功'
  }
])

// 计算属性
const filteredRecords = computed(() => {
  if (!searchKeyword.value) {
    return records.value
  }
  
  const keyword = searchKeyword.value.toLowerCase()
  return records.value.filter(record => 
    record.appId.toLowerCase().includes(keyword) ||
    record.reqPath.toLowerCase().includes(keyword) ||
    record.date.toLowerCase().includes(keyword)
  )
})

// 方法
const formatDate = (dateString) => {
  const date = new Date(dateString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

const searchRecords = () => {
  // 这里可以添加实际的搜索逻辑
  console.log('搜索关键词:', searchKeyword.value)
}

const openDrawer = (record) => {
  selectedRecord.value = record
  showDrawer.value = true
}

const closeDrawer = () => {
  showDrawer.value = false
  selectedRecord.value = {}
}
</script>

<style scoped>
.query-list-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 2px solid #e5e7eb;
}

.header h1 {
  margin: 0;
  color: #1f2937;
  font-size: 28px;
  font-weight: 600;
}

.search-bar {
  display: flex;
  gap: 10px;
}

.search-input {
  padding: 10px 15px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 14px;
  width: 250px;
  outline: none;
  transition: border-color 0.2s;
}

.search-input:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.search-btn {
  padding: 10px 20px;
  background: #3b82f6;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: background-color 0.2s;
}

.search-btn:hover {
  background: #2563eb;
}

.table-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.query-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.query-table th {
  background: #f9fafb;
  padding: 15px 12px;
  text-align: left;
  font-weight: 600;
  color: #374151;
  border-bottom: 1px solid #e5e7eb;
}

.query-table td {
  padding: 15px 12px;
  border-bottom: 1px solid #f3f4f6;
  color: #374151;
}

.table-row:hover {
  background: #f9fafb;
}

.status-badge {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.成功 {
  background: #dcfce7;
  color: #166534;
}

.status-badge.失败 {
  background: #fee2e2;
  color: #dc2626;
}

.detail-btn {
  padding: 6px 12px;
  background: #3b82f6;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: background-color 0.2s;
}

.detail-btn:hover {
  background: #2563eb;
}

/* 抽屉柜样式 */
.drawer-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: flex-end;
  z-index: 1000;
  animation: fadeIn 0.3s ease-out;
}

.drawer-content {
  background: white;
  height: 100vh;
  width: 90%;
  max-width: 600px;
  box-shadow: -4px 0 25px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  animation: slideIn 0.3s ease-out;
}

.drawer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #e5e7eb;
  background: #f9fafb;
  flex-shrink: 0;
}

.drawer-header h2 {
  margin: 0;
  color: #1f2937;
  font-size: 20px;
  font-weight: 600;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  color: #6b7280;
  cursor: pointer;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.close-btn:hover {
  background: #e5e7eb;
  color: #374151;
}

.drawer-body {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header {
    flex-direction: column;
    gap: 15px;
    align-items: stretch;
  }
  
  .search-bar {
    flex-direction: column;
  }
  
  .search-input {
    width: 100%;
  }
  
  .query-table {
    font-size: 12px;
  }
  
  .query-table th,
  .query-table td {
    padding: 10px 8px;
  }
  
  .drawer-content {
    width: 100%;
    max-width: none;
  }
}
</style>