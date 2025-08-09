<template>
  <div class="mind-map-container">
    <h1 class="title">思维导图</h1>
    <div class="toolbar">
      <button @click="addNode" class="tool-btn">添加节点</button>
      <button @click="addConditionNode" class="tool-btn condition-btn">添加条件分支</button>
      <button @click="addMultipleNodes" class="tool-btn multiple-btn">批量添加节点</button>
      <button @click="deleteSelectedNode" class="tool-btn">删除节点</button>
      <button @click="clearMap" class="tool-btn">清空</button>
      <div v-if="selectedNode !== null" class="node-type-selector">
        <span>节点类型：</span>
        <select v-model="selectedNodeType" @change="changeNodeType">
          <option value="normal">普通节点</option>
          <option value="condition">条件分支</option>
        </select>
      </div>
      <div v-if="selectedNode !== null" class="batch-input">
        <span>批量添加数量：</span>
        <input v-model.number="batchCount" type="number" min="1" max="10" class="batch-input-field">
      </div>
    </div>
    <div class="mind-map" ref="mindMapContainer">
      <div 
        v-for="node in nodes" 
        :key="node.id" 
        :class="['node', { 'selected': selectedNode === node.id }, { 'editing': editingNode === node.id }, node.type || 'normal']"
        :style="getNodeStyle(node)"
        :data-id="node.id"
        @click="selectNode(node.id, $event)"
        @dblclick="startEditing(node.id, $event)"
        @mousedown="startDrag(node.id, $event)"
      >
        <div class="node-content">
          <div 
            class="node-text" 
            :contenteditable="editingNode === node.id"
            @blur="finishEditing(node.id, $event)"
            @keydown.enter.prevent="$event.target.blur()"
            @keydown.esc.prevent="cancelEditing(node.id, $event)"
            ref="nodeTextElements"
          >{{ node.text }}</div>
          <div v-if="selectedNode === node.id && editingNode !== node.id" class="edit-hint">双击编辑</div>
        </div>
      </div>
      <svg class="connections">
        <g 
          v-for="connection in autoConnections" 
          :key="connection.id"
        >
          <path 
            :d="getConnectionPath(connection)" 
            class="connection-path"
          />
          <polygon 
            :points="getArrowPoints(connection)" 
            class="arrow-head"
          />
        </g>
      </svg>
    </div>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue';

export default {
  name: 'MindMap',
  setup() {
    const mindMapContainer = ref(null);
    const nodes = reactive([]);
    const selectedNode = ref(null);
    const editingNode = ref(null);
    const nextNodeId = ref(1);
    const isDragging = ref(false);
    const draggedNodeId = ref(null);
    const dragStartPos = reactive({ x: 0, y: 0 });
    const nodeTextElements = ref([]);
    const originalText = ref('');
    const selectedNodeType = ref('normal');
    const batchCount = ref(3); // 批量添加节点数量
    
    // 计算自动连接线（父子节点之间）
    const autoConnections = computed(() => {
      const result = [];
      for (const node of nodes) {
        if (node.parentId !== null) {
          result.push({
            id: `auto-${node.parentId}-${node.id}`,
            from: node.parentId,
            to: node.id
          });
        }
      }
      return result;
    });
    
    // 添加根节点
    const addRootNode = () => {
      const centerX = mindMapContainer.value.clientWidth / 2;
      const centerY = mindMapContainer.value.clientHeight / 2;
      
      nodes.push({
        id: nextNodeId.value++,
        text: '中心主题',
        x: centerX,
        y: centerY,
        parentId: null,
        type: 'normal'
      });
      
      selectedNode.value = nodes[0].id;
    };
    
    // 调整父节点位置，使其位于所有子节点的垂直中间
    const adjustParentPosition = (parentId) => {
      const parentNode = nodes.find(node => node.id === parentId);
      if (!parentNode) return;
      
      const children = nodes.filter(node => node.parentId === parentId);
      if (children.length === 0) return;
      
      // 计算所有子节点的Y坐标范围
      const childrenYs = children.map(child => child.y);
      const minY = Math.min(...childrenYs);
      const maxY = Math.max(...childrenYs);
      
      // 将父节点位置调整到子节点的垂直中间
      const newY = (minY + maxY) / 2;
      
      // 确保父节点Y坐标在合理范围内（避免负值或过大值）
      parentNode.y = Math.max(50, Math.min(newY, mindMapContainer.value?.clientHeight - 100 || 1000));
    };
    
    // 添加普通子节点
    const addNode = () => {
      if (nodes.length === 0) {
        addRootNode();
        return;
      }
      
      if (selectedNode.value === null) {
        alert('请先选择一个节点');
        return;
      }
      
      const parentNode = nodes.find(node => node.id === selectedNode.value);
      if (!parentNode) return;
      
      // 查找当前父节点的所有子节点
      const siblings = nodes.filter(node => node.parentId === parentNode.id);
      
      // 计算新节点位置 - 在父节点右侧，垂直方向避免重叠
      const baseX = parentNode.x + 150;
      let newY = parentNode.y;
      
      // 如果已有子节点，计算合适的Y位置
      if (siblings.length > 0) {
        // 找到最下方的子节点位置
        const maxY = Math.max(...siblings.map(node => node.y));
        newY = maxY + 80; // 在最下方子节点下方80px处添加新节点
      }
      
      // 添加新节点
      const newNode = {
        id: nextNodeId.value++,
        text: '新节点',
        x: baseX,
        y: newY,
        parentId: parentNode.id,
        type: 'normal'
      };
      
      nodes.push(newNode);
      
      // 调整父节点位置
      adjustParentPosition(parentNode.id);
      
      selectedNode.value = newNode.id;
      selectedNodeType.value = 'normal';
    };
    
    // 添加条件分支节点
    const addConditionNode = () => {
      if (nodes.length === 0) {
        // 如果没有节点，先添加根节点
        addRootNode();
        return;
      }
      
      if (selectedNode.value === null) {
        alert('请先选择一个节点');
        return;
      }
      
      const parentNode = nodes.find(node => node.id === selectedNode.value);
      if (!parentNode) return;
      
      // 查找当前父节点的所有子节点
      const siblings = nodes.filter(node => node.parentId === parentNode.id);
      
      // 计算新节点位置 - 在父节点右侧，垂直方向避免重叠
      const baseX = parentNode.x + 150;
      let newY = parentNode.y;
      
      // 如果已有子节点，计算合适的Y位置
      if (siblings.length > 0) {
        // 找到最下方的子节点位置
        const maxY = Math.max(...siblings.map(node => node.y));
        newY = maxY + 80; // 在最下方子节点下方80px处添加新节点
      }
      
      // 添加条件分支节点
      const newNode = {
        id: nextNodeId.value++,
        text: '条件判断',
        x: baseX,
        y: newY,
        parentId: parentNode.id,
        type: 'condition'
      };
      
      nodes.push(newNode);
      
      // 调整父节点位置
      adjustParentPosition(parentNode.id);
      
      selectedNode.value = newNode.id;
      selectedNodeType.value = 'condition';
    };
    
    // 批量添加节点
    const addMultipleNodes = () => {
      if (nodes.length === 0) {
        // 如果没有节点，先添加根节点
        addRootNode();
        return;
      }
      
      if (selectedNode.value === null) {
        alert('请先选择一个节点');
        return;
      }
      
      const parentNode = nodes.find(node => node.id === selectedNode.value);
      if (!parentNode) return;
      
      const count = Math.min(Math.max(batchCount.value || 1, 1), 10); // 限制在1-10之间
      
      // 查找当前父节点的所有子节点
      const siblings = nodes.filter(node => node.parentId === parentNode.id);
      
      // 计算起始Y位置
      let startY = parentNode.y;
      if (siblings.length > 0) {
        const maxY = Math.max(...siblings.map(node => node.y));
        startY = maxY + 80;
      }
      
      // 计算节点布局 - 在父节点右侧垂直排列
      for (let i = 0; i < count; i++) {
        const newX = parentNode.x + 150;
        const newY = startY + (i * 80);
        
        // 添加新节点
        const newNode = {
          id: nextNodeId.value++,
          text: `节点${i + 1}`,
          x: newX,
          y: newY,
          parentId: parentNode.id,
          type: 'normal'
        };
        
        nodes.push(newNode);
      }
      
      // 调整父节点位置
      adjustParentPosition(parentNode.id);
      
      // 选中最后一个添加的节点
      selectedNode.value = nodes[nodes.length - 1].id;
      selectedNodeType.value = 'normal';
    };
    
    // 更改节点类型
    const changeNodeType = () => {
      if (selectedNode.value === null) return;
      
      const node = nodes.find(n => n.id === selectedNode.value);
      if (node) {
        node.type = selectedNodeType.value;
      }
    };
    
    // 删除选中节点
    const deleteSelectedNode = () => {
      if (selectedNode.value === null) return;
      
      // 找到所有子节点
      const childrenIds = findAllChildren(selectedNode.value);
      
      // 删除所有子节点和当前节点
      const idsToRemove = [selectedNode.value, ...childrenIds];
      for (let i = nodes.length - 1; i >= 0; i--) {
        if (idsToRemove.includes(nodes[i].id)) {
          nodes.splice(i, 1);
        }
      }
      
      selectedNode.value = null;
    };
    
    // 查找所有子节点
    const findAllChildren = (nodeId) => {
      const childrenIds = [];
      const directChildren = nodes.filter(node => node.parentId === nodeId);
      
      directChildren.forEach(child => {
        childrenIds.push(child.id);
        childrenIds.push(...findAllChildren(child.id));
      });
      
      return childrenIds;
    };
    
    // 清空思维导图
    const clearMap = () => {
      nodes.splice(0, nodes.length);
      selectedNode.value = null;
      nextNodeId.value = 1;
    };
    
    // 选择节点
    const selectNode = (nodeId, event) => {
      if (isDragging.value) return;
      
      // 正常模式下选择节点
      selectedNode.value = nodeId;
      
      // 更新选中节点类型
      const node = nodes.find(n => n.id === nodeId);
      if (node) {
        selectedNodeType.value = node.type || 'normal';
      }
      
      event.stopPropagation();
    };
    
    // 开始编辑节点
    const startEditing = (nodeId, event) => {
      if (isDragging.value) return;
      
      const node = nodes.find(n => n.id === nodeId);
      if (!node) return;
      
      // 保存原始文本，以便取消编辑时恢复
      originalText.value = node.text;
      
      // 设置编辑状态
      editingNode.value = nodeId;
      selectedNode.value = nodeId;
      
      // 阻止事件冒泡，防止触发拖拽
      event.stopPropagation();
      
      // 在下一个事件循环中聚焦文本区域
      setTimeout(() => {
        const elements = nodeTextElements.value;
        if (elements && elements.length) {
          // 查找当前编辑节点的文本元素
          const textElement = elements.find(el => el.parentNode.parentNode.getAttribute('data-id') === String(nodeId));
          if (textElement) {
            textElement.focus();
            // 将光标放在文本末尾
            const range = document.createRange();
            const sel = window.getSelection();
            range.selectNodeContents(textElement);
            range.collapse(false); // false表示折叠到末尾
            sel.removeAllRanges();
            sel.addRange(range);
          }
        }
      }, 0);
    };
    
    // 完成编辑
    const finishEditing = (nodeId, event) => {
      const node = nodes.find(n => n.id === nodeId);
      if (node) {
        // 更新节点文本
        const newText = event.target.innerText.trim();
        node.text = newText || '节点';
      }
      
      // 清除编辑状态
      editingNode.value = null;
    };
    
    // 取消编辑
    const cancelEditing = (nodeId, event) => {
      const node = nodes.find(n => n.id === nodeId);
      if (node) {
        // 恢复原始文本
        event.target.innerText = originalText.value;
      }
      
      // 清除编辑状态
      editingNode.value = null;
      event.target.blur();
    };
    
    // 获取节点样式
    const getNodeStyle = (node) => {
      return {
        left: `${node.x}px`,
        top: `${node.y}px`,
      };
    };
    
    // 获取连接线路径
    const getConnectionPath = (connection) => {
      const fromNode = nodes.find(node => node.id === connection.from);
      const toNode = nodes.find(node => node.id === connection.to);
      
      if (!fromNode || !toNode) return '';
      
      // 父节点右侧中心点作为起点
      const fromX = fromNode.x + 120; // 节点右边缘
      const fromY = fromNode.y + 25; // 节点高度的一半
      
      // 子节点左侧中心点作为终点
      const toX = toNode.x; // 节点左边缘
      const toY = toNode.y + 25; // 节点高度的一半
      
      const arrowHeadLength = 15; // 箭头头部长度
      
      // 连接线终点连接到箭头底部
      const adjustedToX = toX - arrowHeadLength;
      const adjustedToY = toY;
      
      // 生成路径
      return `M ${fromX} ${fromY} L ${adjustedToX} ${adjustedToY}`;
    };
    
    // 获取箭头点
    const getArrowPoints = (connection) => {
      const fromNode = nodes.find(node => node.id === connection.from);
      const toNode = nodes.find(node => node.id === connection.to);
      
      if (!fromNode || !toNode) return '';
      
      // 箭头参数
      const arrowLength = 15;
      const arrowWidth = 8;
      
      // 箭头尖端位置（子节点左侧中心）
      const tipX = toNode.x;
      const tipY = toNode.y + 25;
      
      // 箭头指向右侧（从父节点指向子节点），所以箭头的底部在左侧
      // 计算箭头的三个点
      const point1X = tipX; // 箭头尖端
      const point1Y = tipY;
      
      const point2X = tipX - arrowLength; // 箭头底部上方
      const point2Y = tipY - arrowWidth;
      
      const point3X = tipX - arrowLength; // 箭头底部下方
      const point3Y = tipY + arrowWidth;
      
      return `${point1X},${point1Y} ${point2X},${point2Y} ${point3X},${point3Y}`;
    };
    
    // 拖拽相关函数
    const startDrag = (nodeId, event) => {
      isDragging.value = true;
      draggedNodeId.value = nodeId;
      dragStartPos.x = event.clientX;
      dragStartPos.y = event.clientY;
      
      document.addEventListener('mousemove', onDrag);
      document.addEventListener('mouseup', stopDrag);
      
      event.preventDefault();
    };
    
    const onDrag = (event) => {
      if (!isDragging.value) return;
      
      const node = nodes.find(n => n.id === draggedNodeId.value);
      if (!node) return;
      
      const dx = event.clientX - dragStartPos.x;
      const dy = event.clientY - dragStartPos.y;
      
      node.x += dx;
      node.y += dy;
      
      dragStartPos.x = event.clientX;
      dragStartPos.y = event.clientY;
    };
    
    const stopDrag = () => {
      isDragging.value = false;
      draggedNodeId.value = null;
      
      document.removeEventListener('mousemove', onDrag);
      document.removeEventListener('mouseup', stopDrag);
    };
    
    // 初始化
    onMounted(() => {
      // 点击空白区域取消选择和编辑
      mindMapContainer.value.addEventListener('click', (event) => {
        // 确保点击的是空白区域，而不是节点
        if (event.target === mindMapContainer.value || event.target.classList.contains('mind-map')) {
          if (editingNode.value !== null) {
            // 如果正在编辑，先完成编辑
            const node = nodes.find(n => n.id === editingNode.value);
            if (node) {
              const elements = nodeTextElements.value;
              if (elements && elements.length) {
                const textElement = elements.find(el => el.parentNode.parentNode.getAttribute('data-id') === String(editingNode.value));
                if (textElement) {
                  textElement.blur();
                }
              }
            }
          } else {
            // 取消选择节点
            selectedNode.value = null;
          }
        }
      });
    });
    
    return {
      mindMapContainer,
      nodes,
      selectedNode,
      editingNode,
      nodeTextElements,
      autoConnections,
      selectedNodeType,
      batchCount,
      addNode,
      addConditionNode,
      addMultipleNodes,
      deleteSelectedNode,
      clearMap,
      selectNode,
      startEditing,
      finishEditing,
      cancelEditing,
      changeNodeType,
      getNodeStyle,
      getConnectionPath,
      getArrowPoints,
      startDrag
    };
  }
};
</script>

<style scoped>
.mind-map-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 20px;
}

.title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.toolbar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  flex-wrap: wrap;
  align-items: center;
}

.tool-btn {
  padding: 8px 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.tool-btn:hover {
  background-color: #45a049;
}

.condition-btn {
  background-color: #FF9800;
}

.condition-btn:hover {
  background-color: #F57C00;
}





.node-type-selector {
  margin-left: 20px;
  display: flex;
  align-items: center;
  gap: 5px;
}

.node-type-selector select {
  padding: 5px;
  border-radius: 4px;
  border: 1px solid #ddd;
  outline: none;
}

.mind-map {
  flex: 1;
  position: relative;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: auto;
  background-color: #f9f9f9;
  min-height: 500px;
}

.node {
  position: absolute;
  width: 120px;
  min-height: 50px;
  background-color: #fff;
  border: 2px solid #4CAF50;
  border-radius: 8px;
  padding: 10px;
  cursor: move;
  user-select: none;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
  transition: transform 0.2s, box-shadow 0.2s, width 0.3s;
  z-index: 1;
}

.node.condition {
  border: 2px solid #FF9800;
  background-color: #FFF8E1;
  border-radius: 4px;
  width: 140px;
}

.node.selected {
  border-color: #2196F3;
  box-shadow: 0 4px 8px rgba(33, 150, 243, 0.3);
  z-index: 2;
}

.node.editing {
  width: 180px;
  border-color: #FF9800;
  box-shadow: 0 4px 8px rgba(255, 152, 0, 0.3);
  z-index: 3;
  cursor: text;
}

.node-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
}

.node-text {
  font-size: 14px;
  text-align: center;
  word-break: break-word;
  outline: none;
  min-height: 20px;
  width: 100%;
  padding: 4px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.editing .node-text {
  background-color: rgba(255, 152, 0, 0.1);
  border: 1px dashed #FF9800;
}

.edit-hint {
  font-size: 10px;
  color: #888;
  margin-top: 5px;
  opacity: 0.7;
}

.connections {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 10;
}

.connection-path {
  fill: none;
  stroke: #666;
  stroke-width: 3px;
}

.arrow-head {
  fill: #666;
  stroke: none;
}

/* 条件节点特殊样式 */
.condition .node-text {
  font-weight: bold;
  color: #E65100;
}

.condition.selected {
  border-color: #F57C00;
  box-shadow: 0 4px 8px rgba(255, 152, 0, 0.3);
}

.condition.editing {
  width: 200px;
  border-color: #F57C00;
}
</style>