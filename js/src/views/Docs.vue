<template>
  <div class="docs-container">
    <div class="header">
      <h2>文档库（Word/Excel/PDF/PPT）</h2>
      <div class="actions">
        <input v-model="uploadTitle" placeholder="输入文档组标题" class="title-input" />
        <input type="file" multiple @change="onPickFiles" :accept="acceptTypes" />
        <button class="tool-btn primary" @click="uploadDocs" :disabled="!canUpload">上传</button>
        <input v-model.trim="searchKw" placeholder="搜索标题..." class="title-input" @input="debouncedSearch" />
        <button class="tool-btn" @click="doSearch">搜索</button>
      </div>
    </div>

    <div class="list">
      <div v-for="r in records" :key="r.id" class="doc-card">
        <div class="doc-title">{{ r.title }}</div>
        <div class="doc-meta">创建: {{ formatTime(r.createdAt) }}｜更新: {{ formatTime(r.updatedAt) }}</div>
        <div class="files" v-if="filesByRecord[r.id]">
          <div class="file-item" v-for="f in filesByRecord[r.id]" :key="f.id">
            <span class="file-icon">{{ iconFor(f.mimeType) }}</span>
            <span class="file-name" :title="f.name">{{ f.name }}</span>
            <span class="file-size">{{ prettySize(f.size) }}</span>
            <button class="tool-btn" @click="previewFile(f)">预览</button>
            <button class="tool-btn" @click="downloadFile(f)">下载</button>
          </div>
        </div>
        <div class="files" v-else>
          <button class="tool-btn" @click="loadFiles(r.id)">加载附件</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'

const API_BASE = (import.meta && import.meta.env && import.meta.env.VITE_TOOL_API) || 'http://localhost:8080/czp/tool'
const acceptTypes = '.pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,application/pdf,application/msword,application/vnd.ms-excel,application/vnd.ms-powerpoint,application/vnd.openxmlformats-officedocument.presentationml.presentation,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'

const uploadTitle = ref('')
const pickedFiles = ref([])
const canUpload = computed(() => uploadTitle.value.trim() && pickedFiles.value.length > 0)

const records = ref([])
const filesByRecord = ref({})
const searchKw = ref('')
let searchTimer = null

function onPickFiles(e) {
  pickedFiles.value = Array.from(e.target.files || [])
}

async function uploadDocs() {
  if (!canUpload.value) return
  const form = new FormData()
  form.append('title', uploadTitle.value.trim())
  pickedFiles.value.forEach(f => form.append('files', f))
  const res = await fetch(`${API_BASE}/api/docs/records`, { method: 'POST', body: form })
  const data = await res.json()
  if (data.success) {
    uploadTitle.value = ''
    pickedFiles.value = []
    await loadRecords()
    alert('上传成功')
  } else {
    alert('上传失败')
  }
}

async function loadRecords() {
  const q = searchKw.value.trim()
  const url = q ? `${API_BASE}/api/docs/records?q=${encodeURIComponent(q)}` : `${API_BASE}/api/docs/records`
  const res = await fetch(url)
  const data = await res.json()
  records.value = data.records || []
}

async function loadFiles(recordId) {
  const res = await fetch(`${API_BASE}/api/docs/records/${recordId}/files`)
  const data = await res.json()
  filesByRecord.value = { ...filesByRecord.value, [recordId]: data.files || [] }
}

function downloadUrl(f) {
  return `${API_BASE}/api/docs/files/${f.id}/download`
}

function previewUrl(f) {
  return `${API_BASE}/api/docs/files/${f.id}`
}

function previewFile(f) {
  const url = previewUrl(f)
  if (f.mimeType && f.mimeType.includes('pdf')) {
    // 简单内嵌预览PDF：新窗口打开
    window.open(url, '_blank')
  } else if (f.mimeType && (f.mimeType.includes('word') || f.mimeType.includes('excel') || f.mimeType.includes('sheet') || f.mimeType.includes('powerpoint') || f.mimeType.includes('presentation'))) {
    // 使用 Office Online 预览（需要公网可访问）
    const abs = url
    const viewer = `https://view.officeapps.live.com/op/view.aspx?src=${encodeURIComponent(abs)}`
    window.open(viewer, '_blank')
  } else {
    // 其他先提供下载/本机打开
    window.open(downloadUrl(f), '_blank')
  }
}

async function downloadFile(f) {
  try {
    const res = await fetch(downloadUrl(f))
    if (!res.ok) throw new Error('network')
    const blob = await res.blob()
    // 从响应头获取文件名
    let filename = f.name || 'file'
    const disp = res.headers.get('content-disposition') || ''
    const matchStar = /filename\*=(?:UTF-8'')?([^;]+)/i.exec(disp)
    const match = /filename="?([^";]+)"?/i.exec(disp)
    if (matchStar) {
      filename = decodeURIComponent(matchStar[1])
    } else if (match) {
      filename = match[1]
    }
    const a = document.createElement('a')
    a.href = URL.createObjectURL(blob)
    a.download = filename
    document.body.appendChild(a)
    a.click()
    a.remove()
    URL.revokeObjectURL(a.href)
  } catch (e) {
    window.open(downloadUrl(f), '_blank')
  }
}

function debouncedSearch() {
  if (searchTimer) clearTimeout(searchTimer)
  searchTimer = setTimeout(doSearch, 300)
}

async function doSearch() {
  await loadRecords()
}

function formatTime(t) {
  return t?.replace('T', ' ').substring(0, 19) || ''
}

function iconFor(mime) {
  if (!mime) return '📄'
  if (mime.includes('pdf')) return '📕'
  if (mime.includes('word')) return '📘'
  if (mime.includes('excel') || mime.includes('sheet')) return '📗'
  if (mime.includes('powerpoint') || mime.includes('presentation')) return '📙'
  return '📄'
}

function prettySize(s) {
  if (s < 1024) return `${s} B`
  if (s < 1024*1024) return `${(s/1024).toFixed(1)} KB`
  if (s < 1024*1024*1024) return `${(s/1024/1024).toFixed(1)} MB`
  return `${(s/1024/1024/1024).toFixed(1)} GB`
}

onMounted(loadRecords)
</script>

<style scoped>
.docs-container { padding: 20px; }
.header { display:flex; justify-content: space-between; align-items:center; margin-bottom: 16px; }
.actions { display:flex; gap:8px; align-items:center; }
.title-input { padding:6px 10px; border:1px solid #ddd; border-radius:6px; }
.tool-btn { display:inline-flex; align-items:center; gap:6px; padding:6px 10px; border:1px solid #ddd; background:#fff; border-radius:6px; cursor:pointer; }
.tool-btn.primary { background:#2196F3; color:#fff; border-color:#1976D2; }
.list { display:grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap:12px; }
.doc-card { background:#fff; border:1px solid #eee; border-radius:8px; padding:12px; }
.doc-title { font-weight:600; margin-bottom:6px; }
.doc-meta { color:#666; font-size:12px; margin-bottom:8px; }
.file-item { display:flex; gap:8px; align-items:center; padding:6px 0; border-top:1px dashed #eee; }
.file-icon { width:22px; text-align:center; }
.file-name { flex:1; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }
.file-size { color:#999; font-size:12px; }
.link-btn { color:#1976D2; text-decoration:none; }
.link-btn:hover { text-decoration:underline; }
</style> 