<template>
  <div class="document-editor-container">
    <div class="editor-header">
      <div class="header-left">
        <button @click="goBack" class="back-btn">← 返回</button>
        <div class="document-title-input">
          <input 
            v-model="documentTitle" 
            placeholder="请输入文档标题..."
            class="title-input"
          >
        </div>
      </div>
      <div class="header-right">
        <button @click="saveDocument" class="save-btn">💾 保存</button>
        <button @click="newDocument" class="new-btn">📄 新建</button>
      </div>
    </div>

    <div class="editor-main">
      <div class="editor-toolbar">
        <div class="toolbar-group">
          <button @click="formatText('bold')" :class="{active: isFormatActive('bold')}" class="toolbar-btn">B</button>
          <button @click="formatText('italic')" :class="{active: isFormatActive('italic')}" class="toolbar-btn">I</button>
          <button @click="formatText('underline')" :class="{active: isFormatActive('underline')}" class="toolbar-btn">U</button>
        </div>
        <div class="toolbar-group">
          <select @change="changeFontSize($event)" class="font-size-select">
            <option value="12px">12px</option>
            <option value="14px" selected>14px</option>
            <option value="16px">16px</option>
            <option value="18px">18px</option>
            <option value="20px">20px</option>
            <option value="24px">24px</option>
          </select>
        </div>
        <div class="toolbar-group">
          <button @click="insertImage" class="toolbar-btn">🖼️ 插入图片</button>
          <input ref="imageInput" type="file" accept="image/*" @change="handleImageUpload" style="display: none">
        </div>
      </div>

      <div class="editor-content">
        <div 
          ref="editorArea"
          class="editor-area"
          contenteditable="true"
          @paste="handlePaste"
          @input="handleInput"
          @keydown="handleKeydown"
          placeholder="开始编写您的文档..."
        ></div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getDocument, createDocument, updateDocument } from '../api/documentEditor.js'

export default {
  name: 'DocumentEditorEdit',
  setup() {
    const router = useRouter()
    const route = useRoute()
    const editorArea = ref(null)
    const imageInput = ref(null)
    const documentTitle = ref('')
    const currentDocumentId = ref(null)
    
    // 格式化工具
    const formatText = (command) => {
      document.execCommand(command, false, null)
      editorArea.value.focus()
    }
    
    const isFormatActive = (command) => {
      return document.queryCommandState(command)
    }
    
    const changeFontSize = (event) => {
      document.execCommand('fontSize', false, '7')
      const fontElements = editorArea.value.querySelectorAll('font[size="7"]')
      fontElements.forEach(el => {
        el.removeAttribute('size')
        el.style.fontSize = event.target.value
      })
      editorArea.value.focus()
    }
    
    // 图片处理
    const insertImage = () => {
      imageInput.value.click()
    }
    
    const handleImageUpload = (event) => {
      const file = event.target.files[0]
      if (file) {
        const reader = new FileReader()
        reader.onload = (e) => {
          insertImageToEditor(e.target.result)
        }
        reader.readAsDataURL(file)
      }
    }
    
    const insertImageToEditor = (imageSrc) => {
      const img = document.createElement('img')
      img.src = imageSrc
      img.style.maxWidth = '100%'
      img.style.height = 'auto'
      img.style.margin = '10px 0'
      img.style.borderRadius = '8px'
      img.style.boxShadow = '0 2px 8px rgba(0,0,0,0.1)'
      
      const selection = window.getSelection()
      if (selection.rangeCount > 0) {
        const range = selection.getRangeAt(0)
        range.insertNode(img)
        range.collapse(false)
      } else {
        editorArea.value.appendChild(img)
      }
      
      editorArea.value.focus()
    }
    
    // 粘贴处理
    const handlePaste = (event) => {
      event.preventDefault()
      
      const clipboardData = event.clipboardData || window.clipboardData
      const items = clipboardData.items
      
      for (let i = 0; i < items.length; i++) {
        const item = items[i]
        
        if (item.type.indexOf('image') !== -1) {
          const blob = item.getAsFile()
          const reader = new FileReader()
          reader.onload = (e) => {
            insertImageToEditor(e.target.result)
          }
          reader.readAsDataURL(blob)
          return
        }
        
        if (item.type === 'text/plain') {
          item.getAsString((text) => {
            document.execCommand('insertText', false, text)
          })
        }
      }
    }
    
    // 文档管理
    const saveDocument = async () => {
      const content = editorArea.value.innerHTML
      const title = documentTitle.value || '无标题文档'
      
      try {
        const documentData = { title, content }
        let result
        
        if (currentDocumentId.value) {
          // 更新现有文档
          result = await updateDocument(currentDocumentId.value, documentData)
        } else {
          // 创建新文档
          result = await createDocument(documentData)
          if (result.success && result.document) {
            currentDocumentId.value = result.document.id
            // 更新URL以反映新的文档ID
            router.replace(`/document-editor/edit/${result.document.id}`)
          }
        }
        
        if (result.success) {
          alert('文档已保存！')
        } else {
          alert(result.message || '保存失败')
        }
      } catch (error) {
        console.error('保存文档失败:', error)
        alert('保存失败，请稍后重试')
      }
    }
    
    const newDocument = () => {
      if (confirm('确定要新建文档吗？未保存的内容将丢失。')) {
        documentTitle.value = ''
        editorArea.value.innerHTML = ''
        currentDocumentId.value = null
        router.push('/document-editor/edit')
        nextTick(() => {
          editorArea.value.focus()
        })
      }
    }
    
    const goBack = () => {
      router.push('/document-editor')
    }
    
    const loadDocument = async (id) => {
      try {
        const result = await getDocument(id)
        if (result.success && result.document) {
          const doc = result.document
          documentTitle.value = doc.title
          editorArea.value.innerHTML = doc.content || ''
          currentDocumentId.value = doc.id
        } else {
          alert(result.message || '文档不存在')
          router.push('/document-editor')
        }
      } catch (error) {
        console.error('加载文档失败:', error)
        alert('加载文档失败，请稍后重试')
        router.push('/document-editor')
      }
    }
    
    const handleInput = () => {
      // 自动保存功能可以在这里实现
    }
    
    const handleKeydown = (event) => {
      // Ctrl+S 保存
      if (event.ctrlKey && event.key === 's') {
        event.preventDefault()
        saveDocument()
      }
      
      // Ctrl+N 新建
      if (event.ctrlKey && event.key === 'n') {
        event.preventDefault()
        newDocument()
      }
    }
    
    onMounted(() => {
      // 如果有文档ID参数，加载对应文档
      const docId = route.params.id
      if (docId) {
        loadDocument(docId)
      }
      
      nextTick(() => {
        editorArea.value.focus()
      })
    })
    
    return {
      editorArea,
      imageInput,
      documentTitle,
      formatText,
      isFormatActive,
      changeFontSize,
      insertImage,
      handleImageUpload,
      handlePaste,
      handleInput,
      handleKeydown,
      saveDocument,
      newDocument,
      goBack
    }
  }
}
</script>

<style scoped>
.document-editor-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f5f5;
}

/* 头部样式 */
.editor-header {
  background: white;
  padding: 1rem 2rem;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 2rem;
}

.back-btn {
  padding: 0.5rem 1rem;
  background: #6c757d;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
}

.back-btn:hover {
  background: #5a6268;
}

.document-title-input {
  min-width: 300px;
}

.title-input {
  width: 100%;
  padding: 0.5rem 1rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 1rem;
  outline: none;
  transition: border-color 0.3s ease;
}

.title-input:focus {
  border-color: #4CAF50;
}

.header-right {
  display: flex;
  gap: 1rem;
}

.save-btn, .new-btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
}

.save-btn {
  background: #4CAF50;
  color: white;
}

.save-btn:hover {
  background: #45a049;
}

.new-btn {
  background: #2196F3;
  color: white;
}

.new-btn:hover {
  background: #1976D2;
}

/* 编辑器主体 */
.editor-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
  padding: 2rem;
}

/* 工具栏 */
.editor-toolbar {
  background: white;
  padding: 1rem;
  border-radius: 8px 8px 0 0;
  border: 1px solid #e0e0e0;
  display: flex;
  gap: 1rem;
  align-items: center;
}

.toolbar-group {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.toolbar-btn {
  padding: 0.5rem 0.8rem;
  border: 1px solid #ddd;
  background: white;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: all 0.3s ease;
}

.toolbar-btn:hover {
  background: #f0f0f0;
}

.toolbar-btn.active {
  background: #4CAF50;
  color: white;
  border-color: #4CAF50;
}

.font-size-select {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  outline: none;
}

/* 编辑区域 */
.editor-content {
  flex: 1;
  background: white;
  border: 1px solid #e0e0e0;
  border-top: none;
  border-radius: 0 0 8px 8px;
  overflow: hidden;
}

.editor-area {
  width: 100%;
  height: 100%;
  padding: 2rem;
  outline: none;
  font-family: 'Microsoft YaHei', Arial, sans-serif;
  font-size: 14px;
  line-height: 1.6;
  color: #333;
  overflow-y: auto;
  min-height: 500px;
}

.editor-area:empty:before {
  content: attr(placeholder);
  color: #999;
  pointer-events: none;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .editor-header {
    flex-direction: column;
    gap: 1rem;
    padding: 1rem;
  }
  
  .header-left {
    flex-direction: column;
    gap: 1rem;
    width: 100%;
  }
  
  .document-title-input {
    min-width: auto;
    width: 100%;
  }
  
  .editor-main {
    padding: 1rem;
  }
  
  .editor-toolbar {
    flex-wrap: wrap;
  }
  
  .editor-area {
    padding: 1rem;
  }
}
</style>