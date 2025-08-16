<template>
  <div class="mind-home">
    <div class="home-header">
      <h2>思维导图 - 专业版</h2>
      <div class="actions">
        <button class="tool-btn primary" @click="createNew">新建导图</button>
        <button class="tool-btn" @click="reload">刷新</button>
      </div>
    </div>
    <div class="grid">
      <div v-for="r in rootsList" :key="r.id" class="card">
        <!-- 删除按钮 -->
        <button 
          @click.stop="deleteMindMap(r)" 
          class="delete-btn" 
          :title="`删除思维导图: ${r.text || '中心主题'}`"
        >
          🗑️
        </button>
        
        <div class="card-content" @click="openMap(r.mapId)">
          <div class="card-title">{{ r.text || '中心主题' }}</div>
          <div class="root-preview">
            <div class="root-node">{{ r.text || '中心主题' }}</div>
          </div>
          <div class="card-meta">
            <span>MapID: {{ r.mapId }}</span>
            <span>NodeID: {{ r.id }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const API_BASE = (import.meta && import.meta.env && import.meta.env.VITE_TOOL_API) || 'http://localhost:8080/czp/tool'
const router = useRouter()

const rootsList = ref([])

async function reload() {
  const res = await fetch(`${API_BASE}/api/mindmap/list`)
  const data = await res.json()
  rootsList.value = data.roots || []
}

function openMap(mapId) {
  router.push({ path: '/edraw-mind/editor', query: { mapId: String(mapId) } })
}

function createNew() {
  router.push({ path: '/edraw-mind/editor' })
}

function formatTime(t) {
  return t?.replace('T', ' ').substring(0, 19) || ''
}

async function deleteMindMap(mindMap) {
  if (confirm(`确定要删除思维导图 "${mindMap.text || '中心主题'}" 吗？\n\n此操作将永久删除该思维导图及其所有节点数据，无法恢复！`)) {
    try {
      console.log('开始删除思维导图:', mindMap);
      
      // 调用后端删除API
      const res = await fetch(`${API_BASE}/api/mindmap/delete/${mindMap.mapId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json'
        }
      });
      
      if (res.ok) {
        const result = await res.json();
        if (result.success) {
          alert('思维导图删除成功！');
          // 重新加载列表
          await reload();
        } else {
          alert(`删除失败: ${result.error || '未知错误'}`);
        }
      } else {
        const error = await res.json();
        alert(`删除失败: ${error.message || `HTTP ${res.status}`}`);
      }
    } catch (error) {
      console.error('删除思维导图时发生错误:', error);
      alert(`删除失败: ${error.message || '网络错误，请检查连接'}`);
    }
  }
}

onMounted(async () => {
  await reload()
})
</script>

<style scoped>
.mind-home { padding: 20px; }
.home-header { display:flex; justify-content: space-between; align-items:center; margin-bottom: 16px; }
.actions { display:flex; gap:8px; }
.grid { display:grid; grid-template-columns: repeat(auto-fill, minmax(240px, 1fr)); gap: 12px; }
.card { 
  background: #fff; 
  border: 1px solid #eee; 
  border-radius: 8px; 
  padding: 16px; 
  cursor: pointer; 
  transition: all 0.3s ease; 
  position: relative; 
  overflow: hidden;
}

.card:hover { 
  box-shadow: 0 6px 20px rgba(0,0,0,0.15); 
  transform: translateY(-3px); 
  border-color: #ddd;
}

.card-content {
  cursor: pointer;
}

.card-content:hover .card-title {
  color: #2196F3;
}

.card-title { font-weight:600; margin-bottom:8px; }
.root-preview { height:100px; display:flex; align-items:center; justify-content:center; background:#fafafa; border:1px dashed #ddd; border-radius:6px; margin-bottom:8px; }
.root-node { padding:6px 10px; background:#4CAF50; color:#fff; border-radius:6px; font-weight:600; }
.card-meta { display:flex; justify-content:space-between; color:#666; font-size:12px; }
.tool-btn { display:inline-flex; align-items:center; gap:6px; padding:6px 10px; border:1px solid #ddd; background:#fff; border-radius:6px; cursor:pointer; }
.tool-btn.primary { background:#2196F3; color:#fff; border-color:#1976D2; }
.delete-btn { 
  position: absolute; 
  top: 8px; 
  right: 8px; 
  background: #f44336; 
  border: none; 
  border-radius: 50%; 
  width: 28px; 
  height: 28px; 
  cursor: pointer; 
  font-size: 14px; 
  color: white; 
  display: flex; 
  align-items: center; 
  justify-content: center; 
  transition: all 0.3s ease; 
  box-shadow: 0 2px 6px rgba(244, 67, 54, 0.3);
  z-index: 10;
}

.delete-btn:hover { 
  background: #d32f2f; 
  transform: scale(1.1); 
  box-shadow: 0 4px 12px rgba(244, 67, 54, 0.4);
}

.delete-btn:active { 
  transform: scale(0.95); 
}
</style> 