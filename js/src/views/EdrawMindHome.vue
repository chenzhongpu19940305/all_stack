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
      <div v-for="r in rootsList" :key="r.id" class="card" @click="openMap(r.mapId)">
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

onMounted(async () => {
  await reload()
})
</script>

<style scoped>
.mind-home { padding: 20px; }
.home-header { display:flex; justify-content: space-between; align-items:center; margin-bottom: 16px; }
.actions { display:flex; gap:8px; }
.grid { display:grid; grid-template-columns: repeat(auto-fill, minmax(240px, 1fr)); gap: 12px; }
.card { background:#fff; border:1px solid #eee; border-radius:8px; padding:12px; cursor:pointer; transition:.2s; }
.card:hover { box-shadow:0 4px 12px rgba(0,0,0,0.1); transform: translateY(-2px); }
.card-title { font-weight:600; margin-bottom:8px; }
.root-preview { height:100px; display:flex; align-items:center; justify-content:center; background:#fafafa; border:1px dashed #ddd; border-radius:6px; margin-bottom:8px; }
.root-node { padding:6px 10px; background:#4CAF50; color:#fff; border-radius:6px; font-weight:600; }
.card-meta { display:flex; justify-content:space-between; color:#666; font-size:12px; }
.tool-btn { display:inline-flex; align-items:center; gap:6px; padding:6px 10px; border:1px solid #ddd; background:#fff; border-radius:6px; cursor:pointer; }
.tool-btn.primary { background:#2196F3; color:#fff; border-color:#1976D2; }
</style> 