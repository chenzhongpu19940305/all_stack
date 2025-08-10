<template>
  <div class="docs-container">
    <div class="top-section">
      <div class="search-section">
        <div class="search-controls">
          <input v-model.trim="searchKw" placeholder="搜索标题..." class="search-input" @input="debouncedSearch" />
          <button class="tool-btn" @click="doSearch">搜索</button>
        </div>
        <h2>文档库（Word/Excel/PDF/PPT）</h2>
      </div>
      <div class="upload-section">
        <input v-model="uploadTitle" placeholder="输入文档组标题" class="title-input" />
        <input type="file" multiple @change="onPickFiles" :accept="acceptTypes" />
        <button class="tool-btn primary" @click="uploadDocs" :disabled="!canUpload">上传</button>
      </div>
    </div>

    <div class="list">
      <div v-for="r in records" :key="r.id" class="doc-card">
        <div class="doc-header">
          <div class="doc-title">{{ r.title }}</div>
          <button class="tool-btn danger" @click="deleteRecord(r)">删除记录</button>
        </div>
        <div class="doc-meta">创建: {{ formatTime(r.createdAt) }}｜更新: {{ formatTime(r.updatedAt) }}</div>
        <div class="files" v-if="filesByRecord[r.id]">
          <div class="file-item" v-for="f in filesByRecord[r.id]" :key="f.id">
            <div class="file-info">
              <span class="file-icon">{{ iconFor(f.mimeType) }}</span>
              <span class="file-name" :title="f.name">{{ f.name }}</span>
              <span class="file-size">{{ prettySize(f.size) }}</span>
            </div>
            <div class="file-actions">
              <button class="tool-btn small" @click="previewFile(f)">预览</button>
              <button class="tool-btn small" @click="downloadFile(f)">下载</button>
              <button class="tool-btn small danger" @click="deleteFile(f)">删除</button>
            </div>
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

async function deleteRecord(record) {
  if (!confirm(`确定要删除文档记录"${record.title}"吗？这将删除该记录下的所有文件。`)) {
    return
  }
  
  try {
    const res = await fetch(`${API_BASE}/api/docs/records/${record.id}`, {
      method: 'DELETE'
    })
    const data = await res.json()
    
    if (data.success) {
      alert('删除成功')
      await loadRecords()
      // 清除该记录的文件缓存
      delete filesByRecord.value[record.id]
    } else {
      alert('删除失败: ' + (data.message || '未知错误'))
    }
  } catch (e) {
    alert('删除失败: ' + e.message)
  }
}

async function deleteFile(file) {
  if (!confirm(`确定要删除文件"${file.name}"吗？`)) {
    return
  }
  
  try {
    const res = await fetch(`${API_BASE}/api/docs/files/${file.id}`, {
      method: 'DELETE'
    })
    const data = await res.json()
    
    if (data.success) {
      alert('文件删除成功')
      // 重新加载该记录的文件列表
      await loadFiles(file.recordId || findRecordIdByFile(file))
    } else {
      alert('删除失败: ' + (data.message || '未知错误'))
    }
  } catch (e) {
    alert('删除失败: ' + e.message)
  }
}

function findRecordIdByFile(file) {
  for (const [recordId, files] of Object.entries(filesByRecord.value)) {
    if (files.some(f => f.id === file.id)) {
      return recordId
    }
  }
  return null
}

onMounted(loadRecords)
</script>

<style scoped>
.docs-container { 
  padding: 24px; 
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: 100vh;
}

/* 顶部区域布局 */
.top-section { 
  margin-bottom: 32px; 
  background: rgba(255, 255, 255, 0.9);
  padding: 20px;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
}
.search-section { 
  display: flex; 
  justify-content: space-between; 
  align-items: center; 
  margin-bottom: 16px; 
}
.search-section h2 { 
  margin: 0; 
  order: 2; 
  color: #2c3e50;
  font-size: 24px;
  font-weight: 700;
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
.search-controls { 
  display: flex; 
  gap: 12px; 
  align-items: center; 
  order: 1; 
}
.search-input { 
  padding: 12px 16px; 
  border: 2px solid #e1e8ed; 
  border-radius: 12px; 
  width: 240px;
  font-size: 14px;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.8);
}
.search-input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  background: white;
}
.upload-section { 
  display: flex; 
  gap: 12px; 
  align-items: center; 
  justify-content: flex-end; 
  padding-top: 16px;
  border-top: 1px solid #e1e8ed;
}
.title-input { 
  padding: 12px 16px; 
  border: 2px solid #e1e8ed; 
  border-radius: 12px;
  font-size: 14px;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.8);
}
.title-input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  background: white;
}

/* 按钮样式 */
.tool-btn { 
  display: inline-flex; 
  align-items: center; 
  gap: 8px; 
  padding: 12px 20px; 
  border: none; 
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%); 
  color: white;
  border-radius: 12px; 
  cursor: pointer; 
  font-size: 14px;
  font-weight: 600;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}
.tool-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}
.tool-btn.small { 
  padding: 8px 16px; 
  font-size: 12px;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.2);
}
.tool-btn.small:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}
.tool-btn.primary { 
  background: linear-gradient(45deg, #4facfe 0%, #00f2fe 100%); 
  box-shadow: 0 4px 15px rgba(79, 172, 254, 0.3);
}
.tool-btn.primary:hover {
  box-shadow: 0 6px 20px rgba(79, 172, 254, 0.4);
}
.tool-btn.danger { 
  background: linear-gradient(45deg, #ff6b6b 0%, #ee5a52 100%); 
  box-shadow: 0 4px 15px rgba(255, 107, 107, 0.3);
}
.tool-btn.danger:hover { 
  box-shadow: 0 6px 20px rgba(255, 107, 107, 0.4);
}

/* 文档列表 */
.list { 
  display: grid; 
  grid-template-columns: repeat(auto-fill, minmax(380px, 1fr)); 
  gap: 20px; 
}
.doc-card { 
  background: rgba(255, 255, 255, 0.95); 
  border: none;
  border-radius: 20px; 
  padding: 24px; 
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}
.doc-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
}
.doc-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
}
.doc-header { 
  display: flex; 
  justify-content: space-between; 
  align-items: center; 
  margin-bottom: 12px; 
}
.doc-title { 
  font-weight: 700; 
  flex: 1; 
  color: #2c3e50;
  font-size: 18px;
}
.doc-meta { 
  color: #7f8c8d; 
  font-size: 13px; 
  margin-bottom: 16px;
  font-weight: 500;
}

/* 文件项布局 */
.file-item { 
  display: flex; 
  flex-direction: column; 
  gap: 12px; 
  padding: 16px 0; 
  border-top: 2px dashed #e8f4fd;
  transition: all 0.3s ease;
}
.file-item:hover {
  background: rgba(102, 126, 234, 0.05);
  border-radius: 12px;
  padding: 16px 12px;
  margin: 0 -12px;
}
.file-info { 
  display: flex; 
  gap: 12px; 
  align-items: center; 
}
.file-icon { 
  width: 28px; 
  height: 28px;
  text-align: center; 
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 12px;
}
.file-name { 
  flex: 1; 
  overflow: hidden; 
  text-overflow: ellipsis; 
  white-space: nowrap; 
  font-weight: 600;
  color: #2c3e50;
  font-size: 15px;
}
.file-size { 
  color: #95a5a6; 
  font-size: 12px; 
  min-width: 70px;
  font-weight: 500;
  background: #ecf0f1;
  padding: 4px 8px;
  border-radius: 6px;
}
.file-actions { 
  display: flex; 
  gap: 8px; 
  justify-content: flex-end; 
}

.link-btn { 
  color: #667eea; 
  text-decoration: none;
  font-weight: 600;
  transition: all 0.3s ease;
  padding: 4px 8px;
  border-radius: 6px;
}
.link-btn:hover { 
  background: rgba(102, 126, 234, 0.1);
  color: #764ba2;
  text-decoration: none;
}
</style>