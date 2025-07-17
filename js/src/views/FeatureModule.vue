<template>
  <div class="feature-module">
    <h1 class="title">特性模块</h1>
    
    <!-- 筛选区域 -->
    <div class="filter-section">
      <!-- 应用选择 -->
      <div class="filter-row">
        <label class="filter-label">应用：</label>
        <div class="radio-group">
          <label 
            v-for="app in applicationOptions" 
            :key="app.value" 
            class="radio-item"
          >
            <input 
              type="radio" 
              :value="app.value"
              v-model="selectedApplication"
              name="application"
              @change="onApplicationChange"
            >
            <span>{{ app.label }}</span>
          </label>
        </div>
      </div>
      
      <!-- 部署单元选择 -->
      <div class="filter-row">
        <label class="filter-label">部署单元：</label>
        <div class="radio-group">
          <label 
            v-for="unit in deploymentUnits" 
            :key="unit.value" 
            class="radio-item"
          >
            <input 
              type="radio" 
              :value="unit.value"
              v-model="selectedDeploymentUnit"
              name="deploymentUnit"
              @change="onDeploymentUnitChange"
            >
            <span>{{ unit.label }}</span>
          </label>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧特性列表 -->
      <div class="feature-list-section">
        <div class="feature-list-header">
          <h3>特性列表</h3>
          <button @click="openAddFeatureModal" class="add-feature-btn">
            <span class="add-icon">+</span>
            新增特性
          </button>
        </div>
        
        <div class="feature-list">
          <div 
            v-for="feature in filteredFeatures" 
            :key="feature.id"
            class="feature-item"
            :class="{ 'selected': selectedFeature?.id === feature.id }"
            @click="selectFeature(feature)"
          >
            <div class="feature-info">
              <div class="feature-name">{{ feature.name }}</div>
              <div class="feature-code">{{ feature.code }}</div>
              <div class="feature-desc">{{ feature.description }}</div>
            </div>
            <div class="feature-actions">
              <button @click.stop="editFeature(feature)" class="edit-btn" title="编辑特性">
                ✏️
              </button>
              <button @click.stop="deleteFeature(feature)" class="delete-btn" title="删除特性">
                🗑️
              </button>
            </div>
          </div>
          
          <div v-if="filteredFeatures.length === 0" class="no-features">
            暂无特性数据
          </div>
        </div>
      </div>

      <!-- 右侧详情区域 -->
      <div class="detail-section">
        <div v-if="selectedFeature" class="detail-content">
          <!-- 特性图片区域 -->
          <div class="feature-image-section">
            <h4>特性图片</h4>
            <div class="image-container">
              <img 
                v-if="selectedFeature.imageUrl" 
                :src="selectedFeature.imageUrl" 
                :alt="selectedFeature.name"
                class="feature-image"
              >
              <div v-else class="no-image">
                <span class="no-image-icon">🖼️</span>
                <p>暂无图片</p>
                <button @click="uploadImage" class="upload-btn">上传图片</button>
              </div>
            </div>
          </div>

          <!-- 配置列表区域 -->
          <div class="config-section">
            <div class="config-header">
              <h4>配置列表</h4>
              <button @click="openAddConfigModal" class="add-config-btn">
                <span class="add-icon">+</span>
                新增配置
              </button>
            </div>
            
            <div class="config-list">
              <div 
                v-for="config in featureConfigs" 
                :key="config.id"
                class="config-item"
              >
                <div class="config-info">
                  <div class="config-key">{{ config.key }}</div>
                  <div class="config-value">{{ config.value || '未设置' }}</div>
                  <div class="config-desc">{{ config.description }}</div>
                </div>
                <div class="config-actions">
                  <button @click="editConfig(config)" class="edit-btn">编辑</button>
                  <button @click="deleteConfig(config)" class="delete-btn">删除</button>
                </div>
              </div>
              
              <div v-if="featureConfigs.length === 0" class="no-configs">
                暂无配置数据
              </div>
            </div>
          </div>
        </div>
        
        <div v-else class="no-selection">
          <div class="no-selection-content">
            <span class="no-selection-icon">📋</span>
            <p>请选择一个特性查看详情</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 新增特性弹窗 -->
    <div v-if="showAddFeatureModal" class="modal-overlay" @click="closeAddFeatureModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>新增特性</h3>
          <button @click="closeAddFeatureModal" class="close-btn">&times;</button>
        </div>
        
        <div class="modal-body">
          <div class="form-group">
            <label>特性名称：</label>
            <input 
              type="text" 
              v-model="newFeature.name" 
              placeholder="请输入特性名称"
              class="form-input"
            >
          </div>
          
          <div class="form-group">
            <label>特性代码：</label>
            <input 
              type="text" 
              v-model="newFeature.code" 
              placeholder="请输入特性代码"
              class="form-input"
            >
          </div>
          
          <div class="form-group">
            <label>特性描述：</label>
            <textarea 
              v-model="newFeature.description" 
              placeholder="请输入特性描述"
              rows="3"
              class="form-textarea"
            ></textarea>
          </div>
          
          <div class="form-group">
            <label>所属应用：</label>
            <select v-model="newFeature.application" class="form-select">
              <option value="">请选择应用</option>
              <option 
                v-for="app in applicationOptions" 
                :key="app.value" 
                :value="app.value"
              >
                {{ app.label }}
              </option>
            </select>
          </div>
          
          <div class="form-group">
            <label>部署单元：</label>
            <select v-model="newFeature.deploymentUnit" class="form-select">
              <option value="">请选择部署单元</option>
              <option 
                v-for="unit in deploymentUnits" 
                :key="unit.value" 
                :value="unit.value"
              >
                {{ unit.label }}
              </option>
            </select>
          </div>
        </div>
        
        <div class="modal-footer">
          <button @click="closeAddFeatureModal" class="cancel-btn">取消</button>
          <button @click="addFeature" class="save-btn" :disabled="adding">
            {{ adding ? '保存中...' : '保存' }}
          </button>
        </div>
      </div>
    </div>

    <!-- 新增配置弹窗 -->
    <div v-if="showAddConfigModal" class="modal-overlay" @click="closeAddConfigModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>新增配置</h3>
          <button @click="closeAddConfigModal" class="close-btn">&times;</button>
        </div>
        
        <div class="modal-body">
          <div class="form-group">
            <label>配置Key：</label>
            <input 
              type="text" 
              v-model="newConfig.key" 
              placeholder="请输入配置Key"
              class="form-input"
            >
          </div>
          
          <div class="form-group">
            <label>配置值：</label>
            <input 
              type="text" 
              v-model="newConfig.value" 
              placeholder="请输入配置值"
              class="form-input"
            >
          </div>
          
          <div class="form-group">
            <label>配置描述：</label>
            <textarea 
              v-model="newConfig.description" 
              placeholder="请输入配置描述"
              rows="3"
              class="form-textarea"
            ></textarea>
          </div>
        </div>
        
        <div class="modal-footer">
          <button @click="closeAddConfigModal" class="cancel-btn">取消</button>
          <button @click="addConfig" class="save-btn" :disabled="addingConfig">
            {{ addingConfig ? '保存中...' : '保存' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent, ref, computed, onMounted } from 'vue'

export default defineComponent({
  name: 'FeatureModule',
  setup() {
    // 响应式数据
    const selectedApplication = ref('')
    const selectedDeploymentUnit = ref('')
    const selectedFeature = ref(null)
    const showAddFeatureModal = ref(false)
    const showAddConfigModal = ref(false)
    const adding = ref(false)
    const addingConfig = ref(false)

    // 应用选项
    const applicationOptions = ref([
      { value: 'app1', label: '应用A' },
      { value: 'app2', label: '应用B' },
      { value: 'app3', label: '应用C' },
      { value: 'app4', label: '应用D' }
    ])

    // 部署单元选项
    const deploymentUnits = ref([
      { value: 'unit1', label: '部署单元1' },
      { value: 'unit2', label: '部署单元2' },
      { value: 'unit3', label: '部署单元3' },
      { value: 'unit4', label: '部署单元4' }
    ])

    // 特性数据
    const features = ref([
      {
        id: 1,
        name: '用户管理',
        code: 'USER_MANAGEMENT',
        description: '用户信息的增删改查功能',
        application: 'app1',
        deploymentUnit: 'unit1',
        imageUrl: null
      },
      {
        id: 2,
        name: '权限控制',
        code: 'PERMISSION_CONTROL',
        description: '系统权限管理功能',
        application: 'app1',
        deploymentUnit: 'unit1',
        imageUrl: null
      },
      {
        id: 3,
        name: '数据统计',
        code: 'DATA_STATISTICS',
        description: '数据统计和分析功能',
        application: 'app2',
        deploymentUnit: 'unit2',
        imageUrl: null
      }
    ])

    // 配置数据
    const configs = ref([
      {
        id: 1,
        featureId: 1,
        key: 'user.max_count',
        value: '1000',
        description: '最大用户数量'
      },
      {
        id: 2,
        featureId: 1,
        key: 'user.default_role',
        value: 'user',
        description: '默认用户角色'
      },
      {
        id: 3,
        featureId: 2,
        key: 'permission.enabled',
        value: 'true',
        description: '是否启用权限控制'
      }
    ])

    // 新增特性表单
    const newFeature = ref({
      name: '',
      code: '',
      description: '',
      application: '',
      deploymentUnit: ''
    })

    // 新增配置表单
    const newConfig = ref({
      key: '',
      value: '',
      description: ''
    })

    // 计算属性
    const filteredFeatures = computed(() => {
      return features.value.filter(feature => {
        const appMatch = !selectedApplication.value || feature.application === selectedApplication.value
        const unitMatch = !selectedDeploymentUnit.value || feature.deploymentUnit === selectedDeploymentUnit.value
        return appMatch && unitMatch
      })
    })

    const featureConfigs = computed(() => {
      if (!selectedFeature.value) return []
      return configs.value.filter(config => config.featureId === selectedFeature.value.id)
    })

    // 方法
    const onApplicationChange = () => {
      selectedFeature.value = null
    }

    const onDeploymentUnitChange = () => {
      selectedFeature.value = null
    }

    const selectFeature = (feature) => {
      selectedFeature.value = feature
    }

    const openAddFeatureModal = () => {
      showAddFeatureModal.value = true
      newFeature.value = {
        name: '',
        code: '',
        description: '',
        application: selectedApplication.value,
        deploymentUnit: selectedDeploymentUnit.value
      }
    }

    const closeAddFeatureModal = () => {
      showAddFeatureModal.value = false
    }

    const addFeature = async () => {
      if (!newFeature.value.name || !newFeature.value.code) {
        alert('请填写完整信息')
        return
      }

      adding.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      const newFeatureData = {
        id: Date.now(),
        ...newFeature.value
      }
      
      features.value.push(newFeatureData)
      closeAddFeatureModal()
      adding.value = false
    }

    const editFeature = (feature) => {
      // 实现编辑特性功能
      console.log('编辑特性:', feature)
    }

    const deleteFeature = (feature) => {
      if (confirm('确定要删除这个特性吗？')) {
        features.value = features.value.filter(f => f.id !== feature.id)
        if (selectedFeature.value?.id === feature.id) {
          selectedFeature.value = null
        }
      }
    }

    const openAddConfigModal = () => {
      if (!selectedFeature.value) {
        alert('请先选择一个特性')
        return
      }
      showAddConfigModal.value = true
      newConfig.value = {
        key: '',
        value: '',
        description: ''
      }
    }

    const closeAddConfigModal = () => {
      showAddConfigModal.value = false
    }

    const addConfig = async () => {
      if (!newConfig.value.key) {
        alert('请填写配置Key')
        return
      }

      addingConfig.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      const newConfigData = {
        id: Date.now(),
        featureId: selectedFeature.value.id,
        ...newConfig.value
      }
      
      configs.value.push(newConfigData)
      closeAddConfigModal()
      addingConfig.value = false
    }

    const editConfig = (config) => {
      // 实现编辑配置功能
      console.log('编辑配置:', config)
    }

    const deleteConfig = (config) => {
      if (confirm('确定要删除这个配置吗？')) {
        configs.value = configs.value.filter(c => c.id !== config.id)
      }
    }

    const uploadImage = () => {
      // 实现图片上传功能
      console.log('上传图片')
    }

    // 初始化
    onMounted(() => {
      // 可以在这里加载初始数据
    })

    return {
      selectedApplication,
      selectedDeploymentUnit,
      selectedFeature,
      showAddFeatureModal,
      showAddConfigModal,
      adding,
      addingConfig,
      applicationOptions,
      deploymentUnits,
      features,
      configs,
      newFeature,
      newConfig,
      filteredFeatures,
      featureConfigs,
      onApplicationChange,
      onDeploymentUnitChange,
      selectFeature,
      openAddFeatureModal,
      closeAddFeatureModal,
      addFeature,
      editFeature,
      deleteFeature,
      openAddConfigModal,
      closeAddConfigModal,
      addConfig,
      editConfig,
      deleteConfig,
      uploadImage
    }
  }
})
</script>

<style scoped>
.feature-module {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.title {
  font-size: 2rem;
  color: #2c3e50;
  margin-bottom: 30px;
  text-align: center;
}

.filter-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  margin-bottom: 20px;
}

.filter-row {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.filter-row:last-child {
  margin-bottom: 0;
}

.filter-label {
  font-weight: 600;
  color: #2c3e50;
  min-width: 80px;
  margin-right: 15px;
}

.radio-group {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.radio-item {
  display: flex;
  align-items: center;
  gap: 5px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 6px;
  transition: background-color 0.3s;
}

.radio-item:hover {
  background-color: #f8f9fa;
}

.radio-item input[type="radio"] {
  margin: 0;
}

.main-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  min-height: 600px;
}

.feature-list-section {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  overflow: hidden;
}

.feature-list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e9ecef;
}

.feature-list-header h3 {
  margin: 0;
  color: #2c3e50;
}

.add-feature-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  background: #42b883;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.add-feature-btn:hover {
  background: #3aa876;
}

.add-icon {
  font-size: 16px;
  font-weight: bold;
}

.feature-list {
  max-height: 500px;
  overflow-y: auto;
}

.feature-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #f1f3f4;
  cursor: pointer;
  transition: background-color 0.3s;
}

.feature-item:hover {
  background-color: #f8f9fa;
}

.feature-item.selected {
  background-color: #e3f2fd;
  border-left: 4px solid #42b883;
}

.feature-info {
  flex: 1;
}

.feature-name {
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 5px;
}

.feature-code {
  font-size: 12px;
  color: #6c757d;
  margin-bottom: 5px;
}

.feature-desc {
  font-size: 13px;
  color: #6c757d;
  line-height: 1.4;
}

.feature-actions {
  display: flex;
  gap: 5px;
}

.edit-btn, .delete-btn {
  background: none;
  border: none;
  cursor: pointer;
  padding: 5px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.edit-btn:hover {
  background-color: #e3f2fd;
}

.delete-btn:hover {
  background-color: #ffebee;
}

.no-features {
  padding: 40px 20px;
  text-align: center;
  color: #6c757d;
}

.detail-section {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  overflow: hidden;
}

.detail-content {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.feature-image-section {
  padding: 20px;
  border-bottom: 1px solid #e9ecef;
}

.feature-image-section h4 {
  margin: 0 0 15px 0;
  color: #2c3e50;
}

.image-container {
  min-height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px dashed #dee2e6;
  border-radius: 8px;
  background-color: #f8f9fa;
}

.feature-image {
  max-width: 100%;
  max-height: 200px;
  object-fit: contain;
}

.no-image {
  text-align: center;
  color: #6c757d;
}

.no-image-icon {
  font-size: 48px;
  display: block;
  margin-bottom: 10px;
}

.upload-btn {
  background: #42b883;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  margin-top: 10px;
}

.config-section {
  flex: 1;
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.config-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.config-header h4 {
  margin: 0;
  color: #2c3e50;
}

.add-config-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  background: #42b883;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
}

.config-list {
  flex: 1;
  overflow-y: auto;
}

.config-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  margin-bottom: 10px;
  background-color: #f8f9fa;
}

.config-info {
  flex: 1;
}

.config-key {
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 5px;
}

.config-value {
  font-size: 13px;
  color: #6c757d;
  margin-bottom: 3px;
}

.config-desc {
  font-size: 12px;
  color: #6c757d;
}

.config-actions {
  display: flex;
  gap: 5px;
}

.config-actions .edit-btn,
.config-actions .delete-btn {
  background: none;
  border: 1px solid #dee2e6;
  padding: 4px 8px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.3s;
}

.config-actions .edit-btn:hover {
  background-color: #e3f2fd;
  border-color: #42b883;
}

.config-actions .delete-btn:hover {
  background-color: #ffebee;
  border-color: #dc3545;
}

.no-configs {
  padding: 40px 20px;
  text-align: center;
  color: #6c757d;
}

.no-selection {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.no-selection-content {
  text-align: center;
  color: #6c757d;
}

.no-selection-icon {
  font-size: 64px;
  display: block;
  margin-bottom: 15px;
}

/* 弹窗样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e9ecef;
}

.modal-header h3 {
  margin: 0;
  color: #2c3e50;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #6c757d;
}

.modal-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 600;
  color: #2c3e50;
}

.form-input,
.form-select,
.form-textarea {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  font-size: 14px;
}

.form-input:focus,
.form-select:focus,
.form-textarea:focus {
  outline: none;
  border-color: #42b883;
}

.form-textarea {
  resize: vertical;
  min-height: 80px;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 20px;
  border-top: 1px solid #e9ecef;
}

.cancel-btn {
  background: #6c757d;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
}

.save-btn {
  background: #42b883;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
}

.save-btn:disabled {
  background: #6c757d;
  cursor: not-allowed;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-content {
    grid-template-columns: 1fr;
  }
  
  .filter-row {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .filter-label {
    margin-bottom: 10px;
  }
  
  .radio-group {
    width: 100%;
  }
}
</style> 