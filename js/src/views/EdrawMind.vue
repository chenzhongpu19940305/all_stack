<template>
  <div class="edraw-mind-container">
    <!-- 顶部工具栏 -->
    <div class="toolbar">
      <div class="toolbar-section">
        <button @click="createNewMap" class="tool-btn primary">
          <span class="icon">📄</span>
          新建
        </button>
        <button @click="saveMap" class="tool-btn">
          <span class="icon">💾</span>
          保存
        </button>
        <button @click="loadMap" class="tool-btn">
          <span class="icon">📁</span>
          打开
        </button>
        <button @click="exportMap" class="tool-btn">
          <span class="icon">📤</span>
          导出
        </button>
      </div>
      
      <div class="toolbar-section">
        <button @click="collapseSelected" class="tool-btn" :disabled="!selectedNodeId || selectedNode?.children?.length === 0">
          <span class="icon">➖</span>
          折叠所选
        </button>
        <button @click="expandSelected" class="tool-btn" :disabled="!selectedNodeId || selectedNode?.children?.length === 0">
          <span class="icon">➕</span>
          展开所选
        </button>
      </div>

      <div class="toolbar-section">
        <button @click="undo" :disabled="!canUndo" class="tool-btn">
          <span class="icon">↶</span>
          撤销
        </button>
        <button @click="redo" :disabled="!canRedo" class="tool-btn">
          <span class="icon">↷</span>
          重做
        </button>
      </div>
      
      <div class="toolbar-section">
        <button @click="addChildNode" :disabled="!selectedNodeId" class="tool-btn">
          <span class="icon">➕</span>
          添加子节点
        </button>
        <button @click="addSiblingNode" :disabled="!selectedNodeId" class="tool-btn">
          <span class="icon">⚡</span>
          添加同级节点
        </button>
        <button @click="deleteNode" :disabled="!selectedNodeId || isRootNode" class="tool-btn danger">
          <span class="icon">🗑️</span>
          删除节点
        </button>
      </div>
      
      <div class="toolbar-section">
        <select v-model="currentLayout" @change="applyLayout" class="layout-selector">
          <option value="tree">树形布局</option>
          <option value="radial">放射布局</option>
          <option value="mindmap">思维导图</option>
        </select>
      </div>
      
      <div class="toolbar-section">
        <button @click="zoomIn" class="tool-btn">
          <span class="icon">🔍</span>
          放大
        </button>
        <button @click="zoomOut" class="tool-btn">
          <span class="icon">🔎</span>
          缩小
        </button>
        <button @click="resetZoom" class="tool-btn">
          <span class="icon">🎯</span>
          重置
        </button>
        <span class="zoom-level">{{ Math.round(zoomLevel * 100) }}%</span>
      </div>
    </div>
    
    <!-- 主编辑区域 -->
    <div class="main-content">
      <!-- 左侧属性面板 -->
      <div class="property-panel" v-show="showPropertyPanel">
        <div class="panel-header">
          <h3>属性设置</h3>
          <button @click="showPropertyPanel = false" class="close-btn">×</button>
        </div>
        
        <div class="panel-content" v-if="selectedNodeId">
          <div class="property-group">
            <label>节点文本</label>
            <textarea 
              v-model="selectedNode.text" 
              @input="updateNodeText"
              class="text-input"
              rows="3"
            ></textarea>
          </div>
          
          <div class="property-group">
            <label>节点形状</label>
            <select v-model="selectedNode.shape" @change="updateNodeStyle" class="shape-selector">
              <option value="rectangle">矩形</option>
              <option value="rounded">圆角矩形</option>
              <option value="circle">圆形</option>
              <option value="diamond">菱形</option>
              <option value="cloud">云朵</option>
            </select>
          </div>
          
          <div class="property-group">
            <label>背景颜色</label>
            <input 
              type="color" 
              v-model="selectedNode.backgroundColor" 
              @change="updateNodeStyle"
              class="color-input"
            >
          </div>
          
          <div class="property-group">
            <label>边框颜色</label>
            <input 
              type="color" 
              v-model="selectedNode.borderColor" 
              @change="updateNodeStyle"
              class="color-input"
            >
          </div>
          
          <div class="property-group">
            <label>字体大小</label>
            <input 
              type="range" 
              min="12" 
              max="24" 
              v-model="selectedNode.fontSize" 
              @input="updateNodeStyle"
              class="range-input"
            >
            <span>{{ selectedNode.fontSize }}px</span>
          </div>

          <div class="property-group">
            <label>详细说明（AI问答记录）</label>
            <div class="qa-search">
              <input 
                type="text"
                v-model.trim="qaSearchKeyword"
                @input="onQaSearchInput"
                @focus="onQaSearchFocus"
                @keydown.enter.prevent.stop="triggerQaSearch"
                class="text-input"
                placeholder="输入关键字搜索AI问答记录"
              >
              <div v-if="qaDropdownVisible && qaSearchResults.length" class="qa-dropdown">
                <div 
                  v-for="item in qaSearchResults" 
                  :key="item.id" 
                  class="qa-item"
                  @click="selectQaRecord(item)"
                  :title="item.title"
                >
                  <span class="qa-title">{{ item.title }}</span>
                  <span class="qa-id">#{{ item.id }}</span>
                </div>
              </div>
            </div>
            <div v-if="selectedNode.detailRecordId" class="qa-selected">
              已选择：<span class="qa-selected-title">{{ selectedNode.detailRecordTitle || ('记录 ' + selectedNode.detailRecordId) }}</span>
              <button class="link-btn" @click="clearQaSelection">清除</button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 画布区域 -->
      <div class="canvas-container" ref="canvasContainer">
        <div 
          class="canvas" 
          ref="canvas"
          :style="canvasStyle"
          @mousedown="handleCanvasMouseDown"
          @mousemove="handleCanvasMouseMove"
          @mouseup="handleCanvasMouseUp"
          @wheel="handleWheel"
          @contextmenu.prevent="handleContextMenu"
        >
          <!-- 网格背景 -->
          <svg class="grid-background" v-show="showGrid">
            <defs>
              <pattern id="grid" width="20" height="20" patternUnits="userSpaceOnUse">
                <path d="M 20 0 L 0 0 0 20" fill="none" stroke="#e0e0e0" stroke-width="1"/>
              </pattern>
            </defs>
            <rect :width="canvasDimensions.width" :height="canvasDimensions.height" fill="url(#grid)" />
          </svg>
          
          <!-- 连接线 -->
          <svg class="connections-layer" :width="canvasDimensions.width" :height="canvasDimensions.height" style="overflow: visible;">
            <g v-for="connection in connections" :key="connection.id">
              <path 
                :d="connection.path" 
                :stroke="connection.color"
                :stroke-width="connection.width"
                fill="none"
                class="connection-line"
              />
              <polygon 
                :points="connection.arrowPoints" 
                :fill="connection.color"
                class="arrow-head"
              />
            </g>
          </svg>
          
          <!-- 节点层 -->
          <div 
            v-for="node in nodes" 
            :key="node.id"
            :class="[
              'mind-node',
              node.shape,
              { 
                'selected': selectedNodeId === node.id,
                'editing': editingNodeId === node.id,
                'root': node.isRoot,
                'has-detail': !!node.detailRecordId
              }
            ]"
            :style="getNodeStyle(node)"
            :data-node-id="node.id"
            v-show="isNodeVisible(node.id)"
            @mousedown="handleNodeMouseDown(node, $event)"
            @dblclick="startEditing(node.id)"
            @click="selectNode(node.id)"
          >
            <div 
              class="node-content"
              :contenteditable="editingNodeId === node.id"
              @blur="finishEditing"
              @keydown.enter.prevent="finishEditing"
              @keydown.esc.prevent="cancelEditing"
            >
              {{ node.text }}
            </div>
            
            <!-- 节点操作按钮 -->
            <div class="node-actions" v-show="selectedNodeId === node.id && !editingNodeId">
              <button @click.stop="addChildNode(node.id)" class="action-btn" title="添加子节点">
                +
              </button>
              <button @click.stop="showPropertyPanel = true" class="action-btn" title="属性设置">
                ⚙️
              </button>
              <button 
                v-if="node.detailRecordId"
                @click.stop="openDetailRecord(node)" 
                class="action-btn" 
                title="查看详细说明（AI问答记录）">
                🔗
              </button>
            </div>

            <!-- 折叠/展开同一小圆圈（折叠显示数量，展开显示箭头） -->
            <button 
              v-if="node.children && node.children.length > 0"
              class="collapse-toggle"
              @click.stop="toggleCollapse(node.id)"
              :title="node.collapsed ? ('展开子节点（' + countDescendants(node.id) + '）') : '折叠子节点'"
            >
              <span v-if="node.collapsed">{{ countDescendants(node.id) }}</span>
              <span v-else>▾</span>
            </button>
          </div>
        </div>
      </div>
      
      <!-- 小地图 -->
      <div class="minimap" v-show="showMinimap">
        <div class="minimap-header">
          <span>导航</span>
          <button @click="showMinimap = false" class="close-btn">×</button>
        </div>
        <div class="minimap-content" ref="minimapContent">
          <!-- 小地图内容 -->
        </div>
      </div>
    </div>
    
    <!-- 右键菜单 -->
    <div 
      v-show="contextMenu.show" 
      class="context-menu"
      :style="{ left: contextMenu.x + 'px', top: contextMenu.y + 'px' }"
    >
      <div class="menu-item" @click="addChildNode">添加子节点</div>
      <div class="menu-item" @click="addSiblingNode">添加同级节点</div>
      <div class="menu-separator"></div>
      <div class="menu-item" @click="copyNode">复制</div>
      <div class="menu-item" @click="pasteNode">粘贴</div>
      <div class="menu-separator"></div>
      <div class="menu-item" @click="toggleCollapse(selectedNodeId)">切换折叠/展开（所选）</div>
      <div class="menu-item" @click="collapseSelected">折叠所选</div>
      <div class="menu-item" @click="expandSelected">展开所选</div>
      <div class="menu-separator"></div>
      <div class="menu-item danger" @click="deleteNode">删除</div>
    </div>
    
    <!-- 状态栏 -->
    <div class="status-bar">
      <span>节点数: {{ nodes.length }}</span>
      <span>选中: {{ selectedNodeId ? selectedNode?.text : '无' }}</span>
      <span>缩放: {{ Math.round(zoomLevel * 100) }}%</span>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onUnmounted, nextTick, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useRoute } from 'vue-router'

// 响应式数据
const nodes = ref([])
const connections = ref([])
const selectedNodeId = ref(null)
const editingNodeId = ref(null)
const nextNodeId = ref(1)

// 后端API基础地址（可用环境变量覆盖）
const API_BASE = (import.meta && import.meta.env && import.meta.env.VITE_TOOL_API) || 'http://localhost:8080/czp/tool'
const currentMapId = ref(null)
const mapTitle = ref('未命名导图')

// 画布相关
const canvas = ref(null)
const canvasContainer = ref(null)
const zoomLevel = ref(1)
const panOffset = reactive({ x: 0, y: 0 })
const showGrid = ref(true)

// 布局间距常量
const NODE_HORIZONTAL_GAP = 200
const NODE_VERTICAL_GAP = 140
// 画布扩展留白，避免箭头被100%区域裁切
const CANVAS_PADDING = 400

// 容器尺寸（用于计算画布实际宽高）
const containerSize = reactive({ width: 0, height: 0 })

// 计算当前所有节点的包围盒
function getNodesBounds() {
  if (!nodes.value || nodes.value.length === 0) {
    return { minX: 0, minY: 0, maxX: 800, maxY: 600 }
  }
  let minX = Infinity, minY = Infinity, maxX = -Infinity, maxY = -Infinity
  nodes.value.forEach(n => {
    if (!isNodeVisible(n.id)) return
    const w = n.width || 100
    const h = n.height || 50
    minX = Math.min(minX, n.x)
    minY = Math.min(minY, n.y)
    maxX = Math.max(maxX, n.x + w)
    maxY = Math.max(maxY, n.y + h)
  })
  // 忽略负值的左上扩展，仅向右下扩展，避免无限大
  return { minX: Math.max(minX, 0), minY: Math.max(minY, 0), maxX, maxY }
}

// 画布实际宽高（保证至少覆盖节点范围 + padding）
const canvasDimensions = computed(() => {
  const bounds = getNodesBounds()
  const width = Math.max(containerSize.width, bounds.maxX + CANVAS_PADDING, 1200)
  const height = Math.max(containerSize.height, bounds.maxY + CANVAS_PADDING, 900)
  return { width, height }
})

// 计算某父节点的垂直间距（根据子节点高度自适应，至少为常量阈值）
function getVerticalGapForParent(parentNode) {
  const base = NODE_VERTICAL_GAP
  if (!parentNode || !parentNode.children || parentNode.children.length === 0) return base
  const maxChildHeight = parentNode.children
    .map(id => nodes.value.find(n => n.id === id))
    .filter(Boolean)
    .reduce((m, n) => Math.max(m, n.height || 50), 50)
  // 让相邻子节点之间至少留出 30px 的可视间隔（高度偏移按中心点计算）
  const adaptive = maxChildHeight + 30
  return Math.max(base, adaptive)
}

// 根据父节点与子节点数量计算每个子节点的Y坐标（单子节点时避免与父节点同Y）
function computeChildrenYPositions(parent) {
  const count = parent.children.length
  const vGap = getVerticalGapForParent(parent)
  if (count <= 0) return []
  if (count === 1) {
    return [parent.y + vGap]
  }
  const startY = parent.y - ((count - 1) * vGap) / 2
  return Array.from({ length: count }, (_, i) => startY + i * vGap)
}

// 可见性：若任一祖先节点折叠，则该节点不可见
function isNodeVisible(nodeId) {
  const node = nodes.value.find(n => n.id === nodeId)
  if (!node) return false
  // 仅检查祖先，不检查自身；自身折叠表示“折叠其子孙”，不隐藏自己
  let current = nodes.value.find(n => n.id === node.parentId)
  while (current) {
    if (current.collapsed) return false
    if (!current.parentId) break
    current = nodes.value.find(n => n.id === current.parentId)
  }
  return true
}

// 判断 descendant
function isDescendant(ancestorId, nodeId) {
  let current = nodes.value.find(n => n.id === nodeId)
  while (current && current.parentId) {
    if (current.parentId === ancestorId) return true
    current = nodes.value.find(n => n.id === current.parentId)
  }
  return false
}

// 折叠/展开
function toggleCollapse(nodeId = selectedNodeId.value) {
  if (!nodeId) return
  const node = nodes.value.find(n => n.id === nodeId)
  if (!node) return
  if (!node.children || node.children.length === 0) return
  node.collapsed = !node.collapsed
  // 若折叠导致当前选中不可见，则把选中移动到该节点
  if (node.collapsed && selectedNodeId.value && isDescendant(node.id, selectedNodeId.value)) {
    selectedNodeId.value = node.id
  }
  applyLayout()
  updateConnections()
  saveToHistory()
}

function collapseAll() {
  nodes.value.forEach(n => { if (!n.isRoot) n.collapsed = true })
  // 保持选中可见
  if (selectedNodeId.value && !isNodeVisible(selectedNodeId.value)) {
    selectedNodeId.value = nodes.value.find(n => n.isRoot)?.id || null
  }
  applyLayout()
  updateConnections()
  saveToHistory()
}

function expandAll() {
  nodes.value.forEach(n => { n.collapsed = false })
  applyLayout()
  updateConnections()
  saveToHistory()
}

// 所选折叠/展开
function collapseSelected() {
  if (!selectedNodeId.value) return
  const node = nodes.value.find(n => n.id === selectedNodeId.value)
  if (!node || !node.children || node.children.length === 0) return
  node.collapsed = true
  applyLayout()
  updateConnections()
  saveToHistory()
}

function expandSelected() {
  if (!selectedNodeId.value) return
  const node = nodes.value.find(n => n.id === selectedNodeId.value)
  if (!node || !node.children || node.children.length === 0) return
  node.collapsed = false
  applyLayout()
  updateConnections()
  saveToHistory()
}

// 展开单个节点（用于点击折叠徽标）
function expandNode(nodeId) {
  const node = nodes.value.find(n => n.id === nodeId)
  if (!node) return
  node.collapsed = false
  applyLayout()
  updateConnections()
  saveToHistory()
}

// 统计一个节点的后代数量（用于折叠徽标数字）
function countDescendants(nodeId) {
  const node = nodes.value.find(n => n.id === nodeId)
  if (!node || !node.children) return 0
  let count = 0
  function dfs(nid) {
    const n = nodes.value.find(x => x.id === nid)
    if (!n || !n.children) return
    n.children.forEach(cid => {
      count += 1
      dfs(cid)
    })
  }
  node.children.forEach(cid => {
    count += 1
    dfs(cid)
  })
  return count
}

// 计算可见叶子数量，用于避免兄弟子树重叠
function computeVisibleLeafCount(node) {
  if (!node || node.collapsed || !node.children || node.children.length === 0) return 1
  let sum = 0
  node.children.forEach(id => {
    const child = nodes.value.find(n => n.id === id)
    if (child) sum += computeVisibleLeafCount(child)
  })
  return Math.max(1, sum)
}

// 基于可见叶子数的子节点Y坐标分配，避免子树重叠
function computeChildrenYPositionsSubtreeAware(parent) {
  const children = parent.children
    .map(id => nodes.value.find(n => n.id === id))
    .filter(Boolean)
  if (children.length === 0) return []
  const vGap = getVerticalGapForParent(parent)
  const sizes = children.map(ch => computeVisibleLeafCount(ch))
  const totalLeaves = sizes.reduce((a, b) => a + b, 0)
  if (children.length === 1) return [parent.y + vGap]
  const startY = parent.y - ((totalLeaves - 1) * vGap) / 2
  let acc = 0
  const positions = []
  for (let i = 0; i < children.length; i++) {
    const size = sizes[i]
    const centerOffset = (acc + (size - 1) / 2) * vGap
    positions.push(startY + centerOffset)
    acc += size
  }
  return positions
}

// 仅重新布局某一父节点的直系子节点（水平到右侧、垂直等距对称）
function layoutImmediateChildren(parentId) {
  const parent = nodes.value.find(n => n.id === parentId)
  if (!parent || !parent.children || parent.children.length === 0) return
  const childrenNodes = parent.children
    .map(id => nodes.value.find(n => n.id === id))
    .filter(Boolean)
  // 基于父节点的实际宽度计算子节点位置，确保子节点在父节点右侧
  const baseX = parent.x + parent.width + 50 // 父节点宽度 + 50px间距
  const yPositions = computeChildrenYPositionsSubtreeAware(parent)
  childrenNodes.forEach((child, index) => {
    child.x = baseX
    child.y = yPositions[index]
  })
}

// UI状态
const showPropertyPanel = ref(false)
const showMinimap = ref(true)
const currentLayout = ref('mindmap')

// 交互状态
const isDragging = ref(false)
const dragStartPos = reactive({ x: 0, y: 0 })
const draggedNode = ref(null)
// 新增：分别管理画布平移与节点拖拽
const isPanning = ref(false)
const isDraggingNode = ref(false)

// 历史记录
const history = ref([])
const historyIndex = ref(-1)

// 右键菜单
const contextMenu = reactive({
  show: false,
  x: 0,
  y: 0
})

// 计算属性
const selectedNode = computed(() => {
  return nodes.value.find(node => node.id === selectedNodeId.value)
})

const isRootNode = computed(() => {
  return selectedNode.value?.isRoot || false
})

const canUndo = computed(() => historyIndex.value > 0)
const canRedo = computed(() => historyIndex.value < history.value.length - 1)

const canvasStyle = computed(() => ({
  width: canvasDimensions.value.width + 'px',
  height: canvasDimensions.value.height + 'px',
  transform: `translate(${panOffset.x}px, ${panOffset.y}px) scale(${zoomLevel.value})`,
  transformOrigin: '0 0'
}))

// 初始化
const route = useRoute()
onMounted(async () => {
  setupEventListeners()
  const qid = route.query.mapId
  if (qid) {
    // 从首页进入，加载指定导图
    const id = Number(qid)
    if (id) {
      const res = await fetch(`${API_BASE}/api/mindmap/${id}`)
      const data = await res.json()
      if (data.success) {
        currentMapId.value = data.map.id
        mapTitle.value = data.map.title || '未命名导图'
        currentLayout.value = data.map.layout || 'mindmap'
        const loaded = (data.nodes || []).map(n => ({
          id: n.id, text: n.text, x: n.x, y: n.y, isRoot: !!n.isRoot,
          parentId: n.parentId ?? null, children: [], collapsed: !!n.collapsed,
          shape: n.shape, backgroundColor: n.backgroundColor, borderColor: n.borderColor,
          fontSize: n.fontSize, width: n.width, height: n.height,
          detailRecordId: n.detailRecordId || null, detailRecordTitle: n.detailRecordTitle || null
        }))
        nodes.value = loaded
        const idToNode = new Map(nodes.value.map(n => [n.id, n]))
        nodes.value.forEach(n => { if (n.parentId) idToNode.get(n.parentId)?.children.push(n.id) })
        selectedNodeId.value = nodes.value.find(n => n.isRoot)?.id || null
        nextNodeId.value = (nodes.value.reduce((m, n) => Math.max(m, Number(n.id) || 0), 0) || 0) + 1
        applyLayout()
        updateConnections()
        saveToHistory()
        return
      }
    }
  }
  // 默认新建
  createInitialMap()
})

// 创建初始思维导图
function createInitialMap() {
  const centerX = 400
  const centerY = 300
  
  // 计算根节点的自适应尺寸
  const textSize = calculateTextSize('中心主题', 16)
  const minWidth = 80
  const minHeight = 40
  const maxWidth = 300
  const adaptiveWidth = Math.max(minWidth, Math.min(maxWidth, textSize.width + 20))
  const adaptiveHeight = Math.max(minHeight, textSize.height + 10)
  
  const rootNode = {
    id: nextNodeId.value++,
    text: '中心主题',
    x: centerX,
    y: centerY,
    isRoot: true,
    parentId: null,
    children: [],
    collapsed: false,
    shape: 'rounded',
    backgroundColor: '#4CAF50',
    borderColor: '#45a049',
    fontSize: 16,
    width: adaptiveWidth,
    height: adaptiveHeight
  }
  
  nodes.value = [rootNode]
  selectedNodeId.value = rootNode.id
  currentMapId.value = null
  mapTitle.value = '未命名导图'
  saveToHistory()
}

// 节点操作
function addChildNode(parentId = selectedNodeId.value) {
  if (!parentId) return
  
  const parent = nodes.value.find(node => node.id === parentId)
  if (!parent) return
  
  // 优化：所有子节点都在父节点右侧，基于父节点实际宽度计算位置
  let newX, newY
  const nodeSpacing = 100  // 增加节点间距从80到100
  
  // 基于父节点的实际宽度计算子节点的X坐标
  newX = parent.x + parent.width + 50  // 父节点宽度 + 50px间距
  
  if (parent.isRoot) {
    // 根节点的所有子节点都放在右侧
    const existingChildren = parent.children.map(childId => 
      nodes.value.find(n => n.id === childId)
    ).filter(Boolean)
    
    // 所有子节点都在右侧，垂直排列，增加间距
    newY = parent.y - (existingChildren.length * 50) + existingChildren.length * nodeSpacing
  } else {
    // 非根节点，继续保持在右侧
    newY = parent.y + parent.children.length * nodeSpacing
  }
  
  // 计算新节点的自适应尺寸
  const textSize = calculateTextSize('新节点', 14)
  const minWidth = 80
  const minHeight = 40
  const maxWidth = 300
  const adaptiveWidth = Math.max(minWidth, Math.min(maxWidth, textSize.width + 20))
  const adaptiveHeight = Math.max(minHeight, textSize.height + 10)
  
  const newNode = {
    id: nextNodeId.value++,
    text: '新节点',
    x: newX,
    y: newY,
    parentId: parent.id,
    children: [],
    shape: 'rectangle',
    backgroundColor: '#2196F3',
    borderColor: '#1976D2',
    fontSize: 14,
    width: adaptiveWidth,
    height: adaptiveHeight
  }
  
  parent.children.push(newNode.id)
  nodes.value.push(newNode)
  selectedNodeId.value = newNode.id
  
  // 先重排当前父节点直系子节点，立刻拉开间距
  layoutImmediateChildren(parent.id)
  // 再根据当前布局全局重排，避免需要折叠/展开才生效
  applyLayout()
  updateConnections()
  saveToHistory()
}

function addSiblingNode() {
  if (!selectedNodeId.value) return
  
  const selectedNode = nodes.value.find(node => node.id === selectedNodeId.value)
  if (!selectedNode || selectedNode.isRoot) return
  
  addChildNode(selectedNode.parentId)
}

function deleteNode() {
  if (!selectedNodeId.value || isRootNode.value) return
  
  const nodeToDelete = nodes.value.find(node => node.id === selectedNodeId.value)
  if (!nodeToDelete) return
  const parentIdToRelayout = nodeToDelete.parentId
  
  // 递归删除子节点
  function deleteNodeAndChildren(nodeId) {
    const node = nodes.value.find(n => n.id === nodeId)
    if (!node) return
    
    // 删除所有子节点
    node.children.forEach(childId => deleteNodeAndChildren(childId))
    
    // 从父节点的children数组中移除
    if (node.parentId) {
      const parent = nodes.value.find(n => n.id === node.parentId)
      if (parent) {
        parent.children = parent.children.filter(id => id !== nodeId)
      }
    }
    
    // 从nodes数组中移除
    const index = nodes.value.findIndex(n => n.id === nodeId)
    if (index > -1) {
      nodes.value.splice(index, 1)
    }
  }
  
  deleteNodeAndChildren(selectedNodeId.value)
  selectedNodeId.value = null
  
  // 仅重排删除后的父节点直系子节点，保证间距
  if (parentIdToRelayout) {
    layoutImmediateChildren(parentIdToRelayout)
  }
  
  updateConnections()
  saveToHistory()
}

// 编辑功能
function startEditing(nodeId) {
  editingNodeId.value = nodeId
  nextTick(() => {
    const nodeElement = document.querySelector(`[data-node-id="${nodeId}"] .node-content`)
    if (nodeElement) {
      nodeElement.focus()
      // 选中所有文本
      const range = document.createRange()
      range.selectNodeContents(nodeElement)
      const selection = window.getSelection()
      selection.removeAllRanges()
      selection.addRange(range)
    }
  })
}

function finishEditing() {
  if (!editingNodeId.value) return
  
  const nodeElement = document.querySelector(`[data-node-id="${editingNodeId.value}"] .node-content`)
  if (nodeElement) {
    const node = nodes.value.find(n => n.id === editingNodeId.value)
    if (node) {
      node.text = nodeElement.textContent.trim() || '新节点'
      
      // 重新计算节点尺寸
      const textSize = calculateTextSize(node.text, node.fontSize)
      const minWidth = 80
      const minHeight = 40
      const maxWidth = 300
      
      node.width = Math.max(minWidth, Math.min(maxWidth, textSize.width + 20))
      node.height = Math.max(minHeight, textSize.height + 10)
      
      // 如果节点有父节点，重新布局其子节点
      if (node.parentId) {
        const parent = nodes.value.find(n => n.id === node.parentId)
        if (parent) {
          layoutImmediateChildren(parent.id)
        }
      } else if (node.children && node.children.length > 0) {
        // 如果是根节点或有子节点，重新布局子节点
        layoutImmediateChildren(node.id)
      }
      
      updateConnections()
      saveToHistory()
    }
  }
  
  editingNodeId.value = null
}

function cancelEditing() {
  editingNodeId.value = null
}

// 选择节点
function selectNode(nodeId) {
  selectedNodeId.value = nodeId
  contextMenu.show = false
}

// 更新连接线
function updateConnections() {
  connections.value = []
  const visible = new Set(nodes.value.filter(n => isNodeVisible(n.id)).map(n => n.id))
  nodes.value.forEach(node => {
    if (!visible.has(node.id)) return
    if (node.parentId && visible.has(node.parentId)) {
      const parent = nodes.value.find(n => n.id === node.parentId)
      if (parent) {
        const connection = createConnection(parent, node)
        connections.value.push(connection)
      }
    }
  })
}

function createConnection(fromNode, toNode) {
  const fromWidth = fromNode.width || 100
  const fromHeight = fromNode.height || 50
  const toWidth = toNode.width || 100
  const toHeight = toNode.height || 50
  
  // 计算节点中心点
  const fromCenterX = fromNode.x + fromWidth / 2
  const fromCenterY = fromNode.y + fromHeight / 2
  const toCenterX = toNode.x + toWidth / 2
  const toCenterY = toNode.y + toHeight / 2
  
  // 优化：对于右侧布局，固定连接点位置
  let fromX, fromY, toX, toY
  
  // 父节点右边缘连接到子节点左边缘
  fromX = fromNode.x + fromWidth
  fromY = fromCenterY
  toX = toNode.x
  toY = toCenterY
  
  // 创建路径
  const path = `M ${fromX} ${fromY} L ${toX} ${toY}`
  
  // 优化箭头计算 - 确保箭头正确指向子节点
  const angle = Math.atan2(toY - fromY, toX - fromX)
  const arrowLength = 8  // 减小箭头长度
  const arrowAngle = Math.PI / 4  // 调整箭头角度
  
  // 箭头位置稍微向内偏移，避免重叠
  const arrowBaseX = toX - 3
  const arrowBaseY = toY
  
  const arrowX1 = arrowBaseX - arrowLength * Math.cos(angle - arrowAngle)
  const arrowY1 = arrowBaseY - arrowLength * Math.sin(angle - arrowAngle)
  const arrowX2 = arrowBaseX - arrowLength * Math.cos(angle + arrowAngle)
  const arrowY2 = arrowBaseY - arrowLength * Math.sin(angle + arrowAngle)
  
  return {
    id: `${fromNode.id}-${toNode.id}`,
    path,
    color: '#666',
    width: 2,
    arrowPoints: `${arrowBaseX},${arrowBaseY} ${arrowX1},${arrowY1} ${arrowX2},${arrowY2}`
  }
}

// 布局算法
function applyLayout() {
  if (nodes.value.length === 0) return
  
  switch (currentLayout.value) {
    case 'tree':
      applyTreeLayout()
      break
    case 'radial':
      applyRadialLayout()
      break
    case 'mindmap':
    default:
      applyMindmapLayout()
      break
  }
  
  updateConnections()
}

function applyTreeLayout() {
  const rootNode = nodes.value.find(node => node.isRoot)
  if (!rootNode) return
  
  const levelWidth = NODE_HORIZONTAL_GAP
  const nodeHeight = NODE_VERTICAL_GAP
  
  function layoutNode(node, level, index, siblings) {
    node.x = level * levelWidth
    // 垂直中心：以 (siblings - 1) / 2 为中心偏移
    node.y = (index - (siblings - 1) / 2) * nodeHeight + 300
    
    let childIndex = 0
    node.children.forEach(childId => {
      const child = nodes.value.find(n => n.id === childId)
      if (child) {
        layoutNode(child, level + 1, childIndex, node.children.length)
        childIndex++
      }
    })
  }
  
  layoutNode(rootNode, 0, 0, 1)
}

function applyRadialLayout() {
  const rootNode = nodes.value.find(node => node.isRoot)
  if (!rootNode) return
  
  rootNode.x = 400
  rootNode.y = 300
  
  const radius = 150
  const angleStep = (2 * Math.PI) / rootNode.children.length
  
  rootNode.children.forEach((childId, index) => {
    const child = nodes.value.find(n => n.id === childId)
    if (child) {
      const angle = index * angleStep
      child.x = rootNode.x + radius * Math.cos(angle)
      child.y = rootNode.y + radius * Math.sin(angle)
    }
  })
}

function applyMindmapLayout() {
  const rootNode = nodes.value.find(node => node.isRoot)
  if (!rootNode) return
  
  rootNode.x = 200  // 将根节点移到左侧，为右侧子节点留出空间
  rootNode.y = 300
  
  if (rootNode.children && rootNode.children.length > 0) {
    // 所有子节点都放在右侧
    const yPositions = computeChildrenYPositionsSubtreeAware(rootNode)
    rootNode.children.forEach((childId, index) => {
      const child = nodes.value.find(n => n.id === childId)
      if (child) {
        child.x = rootNode.x + rootNode.width + 50  // 基于根节点实际宽度 + 50px间距
        child.y = yPositions[index]
        // 对于未折叠的子节点，继续布局其子树
        if (!child.collapsed) {
          layoutSubtree(child, 1)
        }
      }
    })
  }
}

// 递归布局子树
function layoutSubtree(node, direction = 1) {  // 默认方向为右侧
  if (!node.children || node.children.length === 0) return
  
  const yPositions = computeChildrenYPositionsSubtreeAware(node)
  node.children.forEach((childId, index) => {
    const child = nodes.value.find(n => n.id === childId)
    if (child) {
      child.x = node.x + node.width + 50  // 基于父节点实际宽度 + 50px间距
      child.y = yPositions[index]
      if (!child.collapsed) {
        layoutSubtree(child, 1)  // 递归时也强制使用右侧方向
      }
    }
  })
}

// 右侧布局算法 - 所有子节点都在右侧
function applyRightSideLayout() {
  const rootNode = nodes.value.find(node => node.isRoot)
  if (!rootNode) return
  
  rootNode.x = 200
  rootNode.y = 300
  
  function layoutNodeToRight(node, level = 0) {
    if (!node.children || node.children.length === 0) return
    
    const baseX = node.x + 220  // 增加水平间距
    const nodeSpacing = 100  // 增加垂直间距
    const startY = node.y - (node.children.length - 1) * 50
    
    node.children.forEach((childId, index) => {
      const child = nodes.value.find(n => n.id === childId)
      if (child) {
        child.x = baseX
        child.y = startY + index * nodeSpacing
        
        layoutNodeToRight(child, level + 1)
      }
    })
  }
  
  layoutNodeToRight(rootNode)
  updateConnections()
}

// 计算文本尺寸的辅助函数
function calculateTextSize(text, fontSize) {
  // 创建临时元素来测量文本尺寸
  const tempElement = document.createElement('div')
  tempElement.style.position = 'absolute'
  tempElement.style.visibility = 'hidden'
  tempElement.style.fontSize = fontSize + 'px'
  tempElement.style.fontFamily = "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif"
  tempElement.style.padding = '10px'
  tempElement.style.lineHeight = '1.4'
  tempElement.style.wordBreak = 'break-word'
  tempElement.style.wordWrap = 'break-word'
  tempElement.style.whiteSpace = 'pre-wrap'
  tempElement.style.maxWidth = '280px' // 设置最大宽度以支持换行
  tempElement.textContent = text || '新节点'
  
  document.body.appendChild(tempElement)
  const width = tempElement.offsetWidth
  const height = tempElement.offsetHeight
  document.body.removeChild(tempElement)
  
  return { width, height }
}

// 获取节点样式
function getNodeStyle(node) {
  // 计算自适应尺寸
  const textSize = calculateTextSize(node.text, node.fontSize)
  const minWidth = 80
  const minHeight = 40
  const maxWidth = 300
  
  // 自适应宽度和高度，但设置最小值和最大值
  const adaptiveWidth = Math.max(minWidth, Math.min(maxWidth, textSize.width + 20))
  const adaptiveHeight = Math.max(minHeight, textSize.height + 10)
  
  // 更新节点的实际尺寸（用于布局计算）
  node.width = adaptiveWidth
  node.height = adaptiveHeight
  
  return {
    left: node.x + 'px',
    top: node.y + 'px',
    width: adaptiveWidth + 'px',
    height: adaptiveHeight + 'px',
    backgroundColor: node.detailRecordId ? '#b71c1c' : node.backgroundColor,
    color: node.detailRecordId ? '#ffffff' : undefined,
    borderColor: node.borderColor,
    fontSize: node.fontSize + 'px'
  }
}

// 更新节点样式
function updateNodeStyle() {
  if (selectedNode.value) {
    // 重新计算节点尺寸（特别是字体大小改变时）
    const textSize = calculateTextSize(selectedNode.value.text, selectedNode.value.fontSize)
    const minWidth = 80
    const minHeight = 40
    const maxWidth = 300
    
    selectedNode.value.width = Math.max(minWidth, Math.min(maxWidth, textSize.width + 20))
    selectedNode.value.height = Math.max(minHeight, textSize.height + 10)
    
    // 如果节点有父节点，重新布局其子节点
    if (selectedNode.value.parentId) {
      const parent = nodes.value.find(n => n.id === selectedNode.value.parentId)
      if (parent) {
        layoutImmediateChildren(parent.id)
      }
    } else if (selectedNode.value.children && selectedNode.value.children.length > 0) {
      // 如果是根节点或有子节点，重新布局子节点
      layoutImmediateChildren(selectedNode.value.id)
    }
    
    updateConnections()
  }
  // 样式更新会通过响应式数据自动触发
  saveToHistory()
}

function updateNodeText() {
  if (selectedNode.value) {
    // 重新计算节点尺寸
    const textSize = calculateTextSize(selectedNode.value.text, selectedNode.value.fontSize)
    const minWidth = 80
    const minHeight = 40
    const maxWidth = 300
    
    selectedNode.value.width = Math.max(minWidth, Math.min(maxWidth, textSize.width + 20))
    selectedNode.value.height = Math.max(minHeight, textSize.height + 10)
    
    // 如果节点有父节点，重新布局其子节点
    if (selectedNode.value.parentId) {
      const parent = nodes.value.find(n => n.id === selectedNode.value.parentId)
      if (parent) {
        layoutImmediateChildren(parent.id)
      }
    } else if (selectedNode.value.children && selectedNode.value.children.length > 0) {
      // 如果是根节点或有子节点，重新布局子节点
      layoutImmediateChildren(selectedNode.value.id)
    }
    
    updateConnections()
  }
  saveToHistory()
}

// AI问答记录选择
const qaSearchKeyword = ref('')
const qaSearchResults = ref([])
const qaDropdownVisible = ref(false)
let qaSearchTimer = null

function onQaSearchFocus() {
  if (qaSearchResults.value.length) qaDropdownVisible.value = true
}

function onQaSearchInput() {
  if (qaSearchTimer) clearTimeout(qaSearchTimer)
  qaSearchTimer = setTimeout(async () => {
    const kw = qaSearchKeyword.value.trim()
    if (!kw) {
      qaSearchResults.value = []
      qaDropdownVisible.value = false
      return
    }
    try {
      // 复用 tool 模块 GalleryController 的搜索接口
      // POST /api/gallery/records/search  body: { query, page, size }
      const res = await fetch(`${API_BASE}/api/gallery/records/search`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ query: kw, page: 1, size: 10 })
      })
      const data = await res.json()
      const list = data?.data?.records || []
      qaSearchResults.value = list.map((it) => ({ id: it.id, title: it.title || '' })).filter(it => it.id && it.title)
      qaDropdownVisible.value = qaSearchResults.value.length > 0
    } catch (e) {
      qaSearchResults.value = []
      qaDropdownVisible.value = false
    }
  }, 300)
}

function triggerQaSearch() {
  // 立即触发一次查询
  if (qaSearchTimer) clearTimeout(qaSearchTimer)
  onQaSearchInput()
}

function selectQaRecord(item) {
  if (!selectedNode.value) return
  selectedNode.value.detailRecordId = item.id
  selectedNode.value.detailRecordTitle = item.title
  qaSearchKeyword.value = item.title
  qaDropdownVisible.value = false
  saveToHistory()
}

function clearQaSelection() {
  if (!selectedNode.value) return
  selectedNode.value.detailRecordId = null
  selectedNode.value.detailRecordTitle = null
  qaSearchKeyword.value = ''
  saveToHistory()
}

// 缩放功能
function zoomIn() {
  zoomLevel.value = Math.min(zoomLevel.value * 1.2, 3)
}

function zoomOut() {
  zoomLevel.value = Math.max(zoomLevel.value / 1.2, 0.1)
}

function resetZoom() {
  zoomLevel.value = 1
  panOffset.x = 0
  panOffset.y = 0
}

// 历史记录
function saveToHistory() {
  const state = {
    nodes: JSON.parse(JSON.stringify(nodes.value)),
    connections: JSON.parse(JSON.stringify(connections.value))
  }
  
  // 移除当前位置之后的历史记录
  history.value = history.value.slice(0, historyIndex.value + 1)
  history.value.push(state)
  historyIndex.value = history.value.length - 1
  
  // 限制历史记录数量
  if (history.value.length > 50) {
    history.value.shift()
    historyIndex.value--
  }
}

function undo() {
  if (!canUndo.value) return
  
  historyIndex.value--
  const state = history.value[historyIndex.value]
  nodes.value = JSON.parse(JSON.stringify(state.nodes))
  connections.value = JSON.parse(JSON.stringify(state.connections))
}

function redo() {
  if (!canRedo.value) return
  
  historyIndex.value++
  const state = history.value[historyIndex.value]
  nodes.value = JSON.parse(JSON.stringify(state.nodes))
  connections.value = JSON.parse(JSON.stringify(state.connections))
}

// 文件操作
async function createNewMap() {
  if (confirm('确定要创建新的思维导图吗？当前内容将丢失。')) {
    nodes.value = []
    connections.value = []
    selectedNodeId.value = null
    nextNodeId.value = 1
    history.value = []
    historyIndex.value = -1
    createInitialMap()
  }
}

async function saveMap() {
  const payload = {
    map: { id: currentMapId.value, title: mapTitle.value, layout: currentLayout.value },
    nodes: nodes.value.map(n => {
      const base = {
        id: n.id,
        parentId: n.parentId,
        text: n.text,
        x: Math.round(n.x),
        y: Math.round(n.y),
        width: n.width,
        height: n.height,
        shape: n.shape,
        backgroundColor: n.backgroundColor,
        borderColor: n.borderColor,
        fontSize: n.fontSize,
        isRoot: !!n.isRoot,
        collapsed: !!n.collapsed
      }
      if (n.detailRecordId) {
        base.detailRecordId = n.detailRecordId
        if (n.detailRecordTitle) base.detailRecordTitle = n.detailRecordTitle
      }
      return base
    })
  }
  const res = await fetch(`${API_BASE}/api/mindmap/save`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  })
  const data = await res.json()
  if (data.success) {
    currentMapId.value = data.mapId
    alert('已保存，ID: ' + data.mapId)
  } else {
    alert('保存失败: ' + (data.error || '未知错误'))
  }
}

async function loadMap() {
  const idStr = prompt('请输入要加载的导图ID', currentMapId.value ? String(currentMapId.value) : '')
  const id = idStr ? Number(idStr) : NaN
  if (!id || isNaN(id)) return
  const res = await fetch(`${API_BASE}/api/mindmap/${id}`)
  const data = await res.json()
  if (!data.success) {
    alert('加载失败: ' + (data.error || '未知错误'))
    return
  }
  currentMapId.value = data.map.id
  mapTitle.value = data.map.title || '未命名导图'
  currentLayout.value = data.map.layout || 'mindmap'
  // 重建节点
  const loaded = (data.nodes || []).map(n => ({
    id: n.id,
    text: n.text,
    x: n.x,
    y: n.y,
    isRoot: !!n.isRoot,
    parentId: n.parentId ?? null,
    children: [],
    collapsed: !!n.collapsed,
    shape: n.shape,
    backgroundColor: n.backgroundColor,
    borderColor: n.borderColor,
    fontSize: n.fontSize,
    width: n.width,
    height: n.height,
    detailRecordId: n.detailRecordId || null,
    detailRecordTitle: n.detailRecordTitle || null
  }))
  nodes.value = loaded
  // 重建 children 关系
  const idToNode = new Map(nodes.value.map(n => [n.id, n]))
  nodes.value.forEach(n => { if (n.parentId) idToNode.get(n.parentId)?.children.push(n.id) })
  // 选中根节点
  selectedNodeId.value = nodes.value.find(n => n.isRoot)?.id || null
  // 更新nextNodeId避免冲突
  nextNodeId.value = (nodes.value.reduce((m, n) => Math.max(m, Number(n.id) || 0), 0) || 0) + 1
  updateConnections()
  saveToHistory()
}

function exportMap() {
  // 这里可以实现导出为PNG、SVG等格式
  alert('导出功能开发中...')
}

// 事件处理
function setupEventListeners() {
  // 键盘快捷键
  document.addEventListener('keydown', (e) => {
    if (e.ctrlKey || e.metaKey) {
      switch (e.key) {
        case 'z':
          e.preventDefault()
          if (e.shiftKey) {
            redo()
          } else {
            undo()
          }
          break
        case 's':
          e.preventDefault()
          saveMap()
          break
        case 'n':
          e.preventDefault()
          createNewMap()
          break
      }
    } else {
      switch (e.key) {
        case 'Delete':
          if (selectedNodeId.value && !editingNodeId.value) {
            deleteNode()
          }
          break
        case 'Tab':
          e.preventDefault()
          if (selectedNodeId.value) {
            addChildNode()
          }
          break
        case 'Enter':
          if (selectedNodeId.value && !editingNodeId.value) {
            addSiblingNode()
          }
          break
      }
    }
  })
  
  // 点击空白处取消选择
  document.addEventListener('click', (e) => {
    if (!e.target.closest('.mind-node') && !e.target.closest('.context-menu')) {
      contextMenu.show = false
    }
    if (!e.target.closest('.qa-search')) {
      qaDropdownVisible.value = false
    }
  })
}

// 鼠标事件处理
function handleCanvasMouseDown(e) {
  if (e.button === 0) { // 左键
    // 开始画布平移
    isPanning.value = true
    dragStartPos.x = e.clientX - panOffset.x
    dragStartPos.y = e.clientY - panOffset.y
  }
}

function handleCanvasMouseMove(e) {
  if (isDraggingNode.value && draggedNode.value) {
    // 拖拽节点：更新节点坐标（考虑平移与缩放）
    const rect = canvas.value?.getBoundingClientRect()
    if (rect) {
      const sceneX = (e.clientX - rect.left - panOffset.x) / zoomLevel.value
      const sceneY = (e.clientY - rect.top - panOffset.y) / zoomLevel.value
      draggedNode.value.x = sceneX - dragStartPos.x
      draggedNode.value.y = sceneY - dragStartPos.y
      updateConnections()
    }
    return
  }
  if (isPanning.value) {
    // 平移画布
    panOffset.x = e.clientX - dragStartPos.x
    panOffset.y = e.clientY - dragStartPos.y
  }
}

function handleCanvasMouseUp() {
  // 结束任一拖拽/平移
  if (isDraggingNode.value) {
    updateConnections()
    saveToHistory()
  }
  isPanning.value = false
  isDraggingNode.value = false
  isDragging.value = false
  draggedNode.value = null
}

function handleNodeMouseDown(node, e) {
  e.stopPropagation()
  draggedNode.value = node
  // 开始节点拖拽
  isDraggingNode.value = true
  const rect = canvas.value?.getBoundingClientRect()
  if (rect) {
    const sceneX = (e.clientX - rect.left - panOffset.x) / zoomLevel.value
    const sceneY = (e.clientY - rect.top - panOffset.y) / zoomLevel.value
    dragStartPos.x = sceneX - node.x
    dragStartPos.y = sceneY - node.y
  } else {
    dragStartPos.x = e.clientX - node.x
    dragStartPos.y = e.clientY - node.y
  }
}

function handleWheel(e) {
  e.preventDefault()
  const delta = e.deltaY > 0 ? 0.9 : 1.1
  zoomLevel.value = Math.max(0.1, Math.min(3, zoomLevel.value * delta))
}

function handleContextMenu(e) {
  e.preventDefault()
  contextMenu.x = e.clientX
  contextMenu.y = e.clientY
  contextMenu.show = true
}

// 复制粘贴功能
let copiedNode = null

function copyNode() {
  if (selectedNodeId.value) {
    copiedNode = JSON.parse(JSON.stringify(selectedNode.value))
  }
}

function pasteNode() {
  if (copiedNode && selectedNodeId.value) {
    const newNode = {
      ...copiedNode,
      id: nextNodeId.value++,
      parentId: selectedNodeId.value,
      x: selectedNode.value.x + selectedNode.value.width + 50,  // 基于父节点实际宽度 + 50px间距
      y: selectedNode.value.y,
      children: []
    }
    
    selectedNode.value.children.push(newNode.id)
    nodes.value.push(newNode)
    
    // 粘贴后重排该父节点直系子节点，确保间距
    layoutImmediateChildren(selectedNode.value.id)
    
    selectedNodeId.value = newNode.id
    updateConnections()
    saveToHistory()
  }
}

const router = useRouter()

function openDetailRecord(node) {
  if (!node?.detailRecordId) return
  // 跳到AI问答记录页，并携带查询参数以便页面高亮该条
  router.push({ path: '/gallery', query: { highlightId: String(node.detailRecordId) } })
}
</script>

<style scoped>
/* 将原有样式保持不变（此处只粘贴头部，完整样式已在文件原有位置） */
.edraw-mind-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f5f5f5;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* 工具栏样式 */
.toolbar {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  background: white;
  border-bottom: 1px solid #e0e0e0;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  gap: 20px;
  flex-wrap: wrap;
}

.toolbar-section {
  display: flex;
  align-items: center;
  gap: 8px;
}

.tool-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 8px 12px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.tool-btn:hover {
  background: #f0f0f0;
  border-color: #bbb;
}

.tool-btn.primary {
  background: #2196F3;
  color: white;
  border-color: #1976D2;
}

.tool-btn.primary:hover {
  background: #1976D2;
}

.tool-btn.danger {
  background: #f44336;
  color: white;
  border-color: #d32f2f;
}

.tool-btn.danger:hover {
  background: #d32f2f;
}

.tool-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.layout-selector {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: white;
}

.zoom-level {
  font-size: 12px;
  color: #666;
  min-width: 40px;
  text-align: center;
}

/* 主内容区域 */
.main-content {
  display: flex;
  flex: 1;
  overflow: hidden;
}

/* 属性面板 */
.property-panel {
  width: 280px;
  background: white;
  border-right: 1px solid #e0e0e0;
  display: flex;
  flex-direction: column;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #e0e0e0;
  background: #f8f9fa;
}

.panel-header h3 {
  margin: 0;
  font-size: 16px;
  color: #333;
}

.close-btn {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  color: #666;
  padding: 0;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-btn:hover {
  color: #333;
  background: #e0e0e0;
  border-radius: 50%;
}

.panel-content {
  padding: 15px;
  overflow-y: auto;
}

.property-group {
  margin-bottom: 20px;
}

.property-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
  font-size: 14px;
}

.text-input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  resize: vertical;
  min-height: 60px;
}

.shape-selector {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: white;
}

.color-input {
  width: 100%;
  height: 40px;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}

.range-input {
  width: calc(100% - 50px);
  margin-right: 10px;
}

/* 画布容器 */
.canvas-container {
  flex: 1;
  position: relative;
  overflow: hidden;
  background: #fafafa;
}

.canvas {
  width: 100%;
  height: 100%;
  position: relative;
  cursor: grab;
  transform-origin: 0 0;
  overflow: visible;
}

.canvas:active {
  cursor: grabbing;
}

/* 网格背景 */
.grid-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
}

/* 连接线层 */
.connections-layer {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
  overflow: visible;
}

.connection-line {
  stroke-linecap: round;
  stroke-linejoin: round;
}

.arrow-head {
  stroke-width: 0;
}

/* 节点样式 */
.mind-node {
  position: absolute;
  background: white;
  border: 2px solid #ddd;
  border-radius: 8px;
  cursor: pointer;
  user-select: none;
  transition: all 0.2s ease;
  z-index: 2;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.mind-node:hover {
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  transform: translateY(-1px);
}

.mind-node.selected {
  border-color: #2196F3;
  box-shadow: 0 0 0 3px rgba(33, 150, 243, 0.2);
  z-index: 3;
}

/* 有详细说明的节点背景变红（浅红）由内联样式控制，这里可选保留轻微阴影 */
.mind-node.has-detail { box-shadow: 0 2px 8px rgba(229,57,53,0.12); }

.mind-node.editing {
  border-color: #FF9800;
  box-shadow: 0 0 0 3px rgba(255, 152, 0, 0.2);
  z-index: 4;
}

.mind-node.root {
  border-width: 3px;
  font-weight: bold;
}

/* 节点形状 */
.mind-node.rectangle { border-radius: 4px; }
.mind-node.rounded { border-radius: 12px; }
.mind-node.circle { border-radius: 50%; }
.mind-node.diamond { border-radius: 0; transform: rotate(45deg); }
.mind-node.diamond .node-content { transform: rotate(-45deg); }
.mind-node.cloud { border-radius: 50px 20px 50px 20px; }

.node-content {
  padding: 10px;
  text-align: center;
  word-break: break-word;
  word-wrap: break-word;
  white-space: pre-wrap;
  outline: none;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  line-height: 1.4;
}

.node-content[contenteditable="true"] {
  background: rgba(255, 152, 0, 0.1);
  border-radius: 4px;
}

/* 节点操作按钮 */
.node-actions {
  position: absolute;
  top: -30px;
  right: -10px;
  display: flex;
  gap: 5px;
}

.action-btn {
  width: 24px;
  height: 24px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.action-btn:hover {
  background: #f0f0f0;
  border-color: #bbb;
}

/* 折叠/展开同一小圆圈 */
.collapse-toggle {
  position: absolute;
  bottom: -10px;
  right: -10px;
  min-width: 22px;
  height: 22px;
  padding: 0 6px;
  border: 1px solid #ddd;
  background: #FF9800;
  color: #fff;
  border-radius: 11px;
  font-size: 12px;
  line-height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 6px rgba(0,0,0,0.2);
  cursor: pointer;
  z-index: 5;
}

.collapse-toggle:hover { filter: brightness(0.95); }

/* 小地图 */
.minimap {
  position: absolute;
  bottom: 20px;
  right: 20px;
  width: 200px;
  height: 150px;
  background: white;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  z-index: 10;
}

.minimap-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  border-bottom: 1px solid #e0e0e0;
  background: #f8f9fa;
  border-radius: 8px 8px 0 0;
  font-size: 12px;
  font-weight: 500;
}

.minimap-content { height: calc(100% - 40px); background: #fafafa; border-radius: 0 0 8px 8px; }

/* 右键菜单 */
.context-menu {
  position: fixed;
  background: white;
  border: 1px solid #ddd;
  border-radius: 6px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  z-index: 1000;
  min-width: 150px;
  overflow: hidden;
}

.menu-item { padding: 10px 15px; cursor: pointer; font-size: 14px; transition: background 0.2s; }
.menu-item:hover { background: #f0f0f0; }
.menu-item.danger { color: #f44336; }
.menu-item.danger:hover { background: #ffebee; }
.menu-separator { height: 1px; background: #e0e0e0; margin: 5px 0; }

/* 状态栏 */
.status-bar {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 8px 20px;
  background: #f8f9fa;
  border-top: 1px solid #e0e0e0;
  font-size: 12px;
  color: #666;
}

/* 图标样式 */
.icon { font-size: 16px; }

/* 响应式设计 */
@media (max-width: 768px) {
  .toolbar { padding: 8px 10px; gap: 10px; }
  .tool-btn { padding: 6px 8px; font-size: 12px; }
  .property-panel { width: 240px; }
  .minimap { width: 150px; height: 100px; bottom: 10px; right: 10px; }
}

/* 动画效果 */
@keyframes nodeAppear { from { opacity: 0; transform: scale(0.8); } to { opacity: 1; transform: scale(1); } }
.mind-node { animation: nodeAppear 0.3s ease-out; }

/* 滚动条样式 */
::-webkit-scrollbar { width: 8px; height: 8px; }
::-webkit-scrollbar-track { background: #f1f1f1; border-radius: 4px; }
::-webkit-scrollbar-thumb { background: #c1c1c1; border-radius: 4px; }
::-webkit-scrollbar-thumb:hover { background: #a8a8a8; }

/* QA 搜索下拉 */
.qa-search { position: relative; }
.qa-dropdown {
  position: absolute;
  left: 0; right: 0;
  top: calc(100% + 4px);
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 6px;
  box-shadow: 0 6px 16px rgba(0,0,0,0.12);
  max-height: 220px;
  overflow: auto;
  z-index: 20;
}
.qa-item {
  padding: 8px 10px;
  display: flex;
  justify-content: space-between;
  gap: 10px;
  cursor: pointer;
  font-size: 13px;
}
.qa-item:hover { background: #f6f6f6; }
.qa-title { color: #333; flex: 1; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.qa-id { color: #999; font-variant-numeric: tabular-nums; }
.qa-selected { margin-top: 6px; font-size: 12px; color: #555; display:flex; align-items:center; gap:8px; }
.link-btn { background: none; border: none; color: #1976D2; cursor: pointer; padding: 0; }
.link-btn:hover { text-decoration: underline; }
</style>