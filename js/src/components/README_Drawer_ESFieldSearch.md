# 在抽屉界面中使用ESFieldSearchInput组件

本文档介绍如何在主界面和抽屉界面之间传递数据，并在抽屉界面中使用ESFieldSearchInput组件。

## 数据流程

主界面 → 抽屉界面 → ESFieldSearchInput组件

## 实现方式

### 1. 主界面定义数据

在主界面中，我们需要定义要传递给抽屉界面的数据，包括可用字段列表和字段值。

```javascript
// 在主界面组件中
const availableFields = ref(['title', 'content', 'author', 'created_at'])
const fieldValues = ref({
  title: ['首页', '关于我们', '产品介绍'],
  content: ['Vue3', 'React18', 'TypeScript'],
  author: ['张三', '李四', '王五'],
  created_at: ['今天', '昨天', '本周']
})
```

### 2. 打开抽屉并传递数据

当用户点击打开抽屉按钮时，我们将数据传递给抽屉组件：

```html
<!-- 主界面模板 -->
<button @click="openDrawer" class="open-drawer-btn">打开抽屉</button>

<!-- 抽屉组件 -->
<drawer-component 
  v-if="showDrawer" 
  :available-fields="availableFields"
  :field-values="fieldValues"
  @close="closeDrawer"
  @apply="handleApply"
/>
```

```javascript
// 主界面方法
const showDrawer = ref(false)

const openDrawer = () => {
  showDrawer.value = true
}

const closeDrawer = () => {
  showDrawer.value = false
}

const handleApply = (conditions) => {
  console.log('应用搜索条件:', conditions)
  // 处理从抽屉返回的数据
  closeDrawer()
}
```

### 3. 抽屉组件接收数据并传递给ESFieldSearchInput

抽屉组件接收从主界面传递的数据，并将其传递给ESFieldSearchInput组件：

```html
<!-- 抽屉组件模板 -->
<template>
  <div class="drawer-overlay" @click="$emit('close')">
    <div class="drawer-content" @click.stop>
      <div class="drawer-header">
        <h3>ES字段搜索</h3>
        <button @click="$emit('close')" class="close-btn">&times;</button>
      </div>
      
      <div class="drawer-body">
        <!-- 使用ESFieldSearchInput组件 -->
        <ESFieldSearchInput 
          v-model="selectedConditions"
          placeholder="搜索ES字段..."
          :available-fields="availableFields"
          :field-values="fieldValues"
          @search="handleSearch"
        />
        
        <!-- 显示结果 -->
        <div v-if="selectedConditions.length > 0" class="result-section">
          <h4>已选择的条件:</h4>
          <pre>{{ JSON.stringify(selectedConditions, null, 2) }}</pre>
        </div>
      </div>
      
      <div class="drawer-footer">
        <button @click="$emit('close')" class="cancel-btn">取消</button>
        <button @click="applySearch" class="apply-btn">应用</button>
      </div>
    </div>
  </div>
</template>
```

```javascript
// 抽屉组件脚本
import { ref } from 'vue'
import ESFieldSearchInput from './ESFieldSearchInput.vue'

const props = defineProps({
  availableFields: {
    type: Array,
    required: true
  },
  fieldValues: {
    type: Object,
    required: true
  },
  // 可以添加初始条件，用于预设搜索条件
  initialConditions: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['close', 'apply'])

// 使用传入的初始条件初始化selectedConditions
const selectedConditions = ref([...props.initialConditions])

const handleSearch = (conditions) => {
  console.log('搜索条件:', conditions)
}

const applySearch = () => {
  emit('apply', selectedConditions.value)
}
```

#### 预设搜索条件

如果你需要在打开抽屉时预设一些搜索条件，可以在打开抽屉时传递初始条件：

```html
<!-- 主界面模板 -->
<button @click="openDrawerWithConditions" class="open-drawer-btn">打开带预设条件的抽屉</button>
```

```javascript
// 主界面方法
const presetConditions = [
  { field: 'author', value: '张三' },
  { field: 'created_at', value: '今天' }
]

const openDrawerWithConditions = () => {
  showDrawer.value = true
  // 传递预设条件
  initialConditions.value = presetConditions
}
```

## 完整示例

项目中已提供了一个完整的示例组件 `DrawerExample.vue`，展示了如何在主界面和抽屉界面之间传递数据，并在抽屉界面中使用ESFieldSearchInput组件。

该示例包括以下功能：

1. 在主界面配置可用字段和字段值
   - 添加和删除字段
   - 为每个字段添加和删除值
2. 打开抽屉并将数据传递给抽屉界面
3. 在抽屉界面使用ESFieldSearchInput组件
4. 处理搜索结果和应用搜索条件

你可以直接查看 `DrawerExample.vue` 文件，了解如何实现这些功能。

## 使用 setQueryData 方法设置查询数据

如果你需要在抽屉界面中动态设置 ESFieldSearchInput 组件的查询数据，可以使用组件提供的 `setQueryData` 方法。这对于从主界面传递特定的字段和值给组件非常有用。

### 1. 在抽屉组件中获取 ESFieldSearchInput 组件的引用

```html
<!-- 抽屉组件模板 -->
<ESFieldSearchInput 
  ref="esFieldSearchInput"
  v-model="selectedConditions"
  placeholder="搜索ES字段..."
  :available-fields="availableFields"
  :field-values="fieldValues"
  @search="handleSearch"
/>
```

### 2. 使用 setQueryData 方法设置查询数据

```javascript
// 抽屉组件脚本
import { ref, onMounted } from 'vue'

// 获取组件引用
const esFieldSearchInput = ref(null)

// 从主界面接收的查询数据
const props = defineProps({
  // ...
  queryData: {
    type: Object,
    default: () => ({})
  }
})

// 在组件挂载后设置查询数据
onMounted(() => {
  if (props.queryData && Object.keys(props.queryData).length > 0) {
    esFieldSearchInput.value.setQueryData(props.queryData)
  }
})
```

### 3. 在主界面中传递查询数据

```html
<!-- 主界面模板 -->
<drawer-component 
  v-if="showDrawer" 
  :available-fields="availableFields"
  :field-values="fieldValues"
  :query-data="queryData"
  @close="closeDrawer"
  @apply="handleApply"
/>
```

```javascript
// 主界面脚本
const queryData = ref({
  field: 'author',
  value: '张三'
})

const openDrawerWithQueryData = () => {
  showDrawer.value = true
  // 设置要传递的查询数据
  queryData.value = {
    field: 'author',
    value: '张三'
  }
}
```

## 注意事项

1. 确保在主界面和抽屉界面之间正确传递数据
2. 使用v-model绑定ESFieldSearchInput组件的选中条件
3. 使用props将字段列表和字段值传递给ESFieldSearchInput组件
4. 处理从ESFieldSearchInput组件返回的搜索事件
5. 实现应用按钮，将搜索条件从抽屉界面返回给主界面
6. 如果需要预设查询条件，可以使用initialConditions或setQueryData方法

## 样式建议

1. 使用固定定位和z-index确保抽屉显示在页面顶层
2. 添加遮罩层，点击时关闭抽屉
3. 使用动画效果使抽屉平滑打开和关闭
4. 为抽屉添加标题、内容区域和底部按钮区域
5. 确保在移动设备上抽屉能够自适应显示