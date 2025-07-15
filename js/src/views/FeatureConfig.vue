<template>
  <div class="feature-config">
    <h1 class="title">特性-配置维护</h1>
    
    <!-- 查询区域 -->
    <div class="search-section">
      <!-- 版本选择 -->
      <div class="filter-row">
        <label class="filter-label">版本：</label>
        <div class="radio-group">
          <label 
            v-for="version in versionOptions" 
            :key="version.value" 
            class="radio-item"
          >
            <input 
              type="radio" 
              :value="version.value"
              v-model="selectedVersion"
              name="version"
            >
            <span>{{ version.label }}</span>
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
            >
            <span>{{ unit.label }}</span>
          </label>
        </div>
      </div>
      
      <!-- 特性展示区域 -->
      <div class="feature-display">
        <div class="display-header">
          <span class="display-title">当前版本和部署单元对应的特性：</span>
          <span class="display-count">共 {{ filteredFeatures.length }} 个特性</span>
          <button @click="openAddFeatureDrawer" class="add-feature-icon">
            <span class="add-icon">+</span>
          </button>
        </div>
        <div class="feature-buttons">
          <div 
            v-for="feature in filteredFeatures" 
            :key="feature.id"
            class="feature-item"
          >
            <button 
              @click="selectFeature(feature)"
              :class="['feature-btn', { 'selected': selectedFeatureIds.includes(feature.id) }]"
            >
              <div class="feature-btn-name">{{ feature.name }}</div>
              <div class="feature-btn-wiki" v-if="feature.wikiUrl">
                <a :href="feature.wikiUrl" target="_blank" class="wiki-link">
                  📖 Wiki
                </a>
              </div>
            </button>
            <button 
              @click="editFeature(feature)"
              class="edit-feature-btn"
              title="修改特性"
            >
              ✏️
            </button>
          </div>
          <div v-if="filteredFeatures.length === 0" class="no-features">
            暂无特性数据
          </div>
        </div>
      </div>
      
      <!-- 查询输入框 -->
      <div class="search-container">
        <div class="search-input-group">
          <input 
            type="text" 
            v-model="searchKeyword" 
            placeholder="请输入查询关键词（可选）"
            class="search-input"
            @keyup.enter="searchFeatures"
          >
          <div v-if="getSelectedFeatureCode()" class="selected-feature-hint">
            已选中特性: {{ getSelectedFeatureName() }}
          </div>
        </div>
        <button @click="searchFeatures" class="search-btn" :disabled="loading">
          {{ loading ? '查询中...' : '查询' }}
        </button>
      </div>
    </div>

    <!-- 配置列表展示区域 -->
    <div class="list-section">
      <div class="list-header">
        <h2>配置列表</h2>
        <div class="header-actions">
          <span class="total-count">共 {{ configList.length }} 条记录</span>
          <button @click="openAddConfigModal" class="add-config-btn">
            <span class="add-icon">+</span>
            新增配置
          </button>
        </div>
      </div>
      
      <div class="config-table-container">
        <table class="config-table">
          <thead>
            <tr>
              <th>特性</th>
              <th>配置Key</th>
              <th>配置默认值</th>
              <th>配置描述</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr 
              v-for="config in configList" 
              :key="config.id" 
              class="config-row"
            >
              <td class="feature-cell">
                <div class="feature-info">
                  <div class="feature-name">{{ config.featureName }}</div>
                  <div class="feature-code">{{ config.featureCode }}</div>
                </div>
              </td>
              <td class="key-cell">{{ config.key }}</td>
              <td class="value-cell">{{ config.value || '-' }}</td>
              <td class="desc-cell">{{ config.description || '暂无描述' }}</td>
              <td class="action-cell">
                <button @click="editConfig(config)" class="edit-btn">修改</button>
              </td>
            </tr>
          </tbody>
        </table>
        
        <div v-if="configList.length === 0 && !loading" class="no-data">
          暂无数据
        </div>
      </div>
    </div>

    <!-- 修改配置弹窗 -->
    <div v-if="showEditModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>修改配置</h3>
          <button @click="closeModal" class="close-btn">&times;</button>
        </div>
        
        <div class="modal-body">
          <div class="form-group">
            <label>配置Key：</label>
            <input 
              type="text" 
              v-model="editingConfig.key" 
              disabled
              class="form-input"
            >
          </div>
          
          <div class="form-group">
            <label>配置默认值：</label>
            <input 
              type="text" 
              v-model="editingConfig.value" 
              placeholder="请输入配置默认值"
              class="form-input"
            >
          </div>
          
          <div class="form-group">
            <label>版本：</label>
            <select v-model="editingConfig.version" class="form-select">
              <option value="">请选择版本</option>
              <option 
                v-for="version in versionOptions" 
                :key="version.value" 
                :value="version.value"
              >
                {{ version.label }}
              </option>
            </select>
          </div>
          
          <div class="form-group">
            <label>部署单元：</label>
            <select v-model="editingConfig.deploymentUnit" class="form-select">
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
          
          <div class="form-group">
            <label>所属特性：</label>
            <select v-model="editingConfig.featureCode" class="form-select" @change="onEditFeatureChange">
              <option value="">请选择所属特性</option>
              <option 
                v-for="feature in filteredFeatures" 
                :key="feature.code" 
                :value="feature.code"
              >
                {{ feature.name }} ({{ feature.code }})
              </option>
            </select>
          </div>
          
          <div class="form-group">
            <label>配置描述：</label>
            <textarea 
              v-model="editingConfig.description" 
              placeholder="请输入配置描述"
              rows="4"
              class="form-textarea"
            ></textarea>
          </div>
          
          <div class="form-group">
            <label>配置状态：</label>
            <select v-model="editingConfig.status" class="form-select">
              <option value="enabled">启用</option>
              <option value="disabled">禁用</option>
            </select>
          </div>
        </div>
        
        <div class="modal-footer">
          <button @click="closeModal" class="cancel-btn">取消</button>
          <button @click="saveConfig" class="save-btn" :disabled="saving">
            {{ saving ? '保存中...' : '保存' }}
          </button>
        </div>
      </div>
    </div>

    <!-- 新增配置弹窗 -->
    <div v-if="showAddModal" class="modal-overlay" @click="closeAddModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>新增配置</h3>
          <button @click="closeAddModal" class="close-btn">&times;</button>
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
            <label>配置默认值：</label>
            <input 
              type="text" 
              v-model="newConfig.value" 
              placeholder="请输入配置默认值"
              class="form-input"
            >
          </div>
          
          <div class="form-group">
            <label>版本：</label>
            <select v-model="newConfig.version" class="form-select">
              <option value="">请选择版本</option>
              <option 
                v-for="version in versionOptions" 
                :key="version.value" 
                :value="version.value"
              >
                {{ version.label }}
              </option>
            </select>
          </div>
          
          <div class="form-group">
            <label>部署单元：</label>
            <select v-model="newConfig.deploymentUnit" class="form-select">
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
          
          <div class="form-group">
            <label>所属特性：</label>
            <select v-model="newConfig.featureCode" class="form-select" @change="onFeatureChange">
              <option value="">请选择所属特性</option>
              <option 
                v-for="feature in filteredFeatures" 
                :key="feature.code" 
                :value="feature.code"
              >
                {{ feature.name }} ({{ feature.code }})
              </option>
            </select>
          </div>
          
          <div class="form-group">
            <label>配置描述：</label>
            <textarea 
              v-model="newConfig.description" 
              placeholder="请输入配置描述"
              rows="4"
              class="form-textarea"
            ></textarea>
          </div>
          
          <div class="form-group">
            <label>版本：</label>
            <select v-model="newConfig.version" class="form-select">
              <option value="">请选择版本</option>
              <option 
                v-for="version in versionOptions" 
                :key="version.value" 
                :value="version.value"
              >
                {{ version.label }}
              </option>
            </select>
          </div>
          
          <div class="form-group">
            <label>部署单元：</label>
            <select v-model="newConfig.deploymentUnit" class="form-select">
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
          
          <div class="form-group">
            <label>状态：</label>
            <select v-model="newConfig.status" class="form-select">
              <option value="enabled">启用</option>
              <option value="disabled">禁用</option>
            </select>
          </div>
        </div>
        
        <div class="modal-footer">
          <button @click="closeAddModal" class="cancel-btn">取消</button>
          <button @click="addConfig" class="save-btn" :disabled="adding">
            {{ adding ? '保存中...' : '保存' }}
          </button>
        </div>
      </div>
    </div>
    
    <!-- 新增特性抽屉 -->
    <div v-if="showAddFeatureDrawer" class="drawer-overlay" @click="closeAddFeatureDrawer">
      <div class="drawer-content" @click.stop>
        <div class="drawer-header">
          <h3>新增特性</h3>
          <button @click="closeAddFeatureDrawer" class="close-btn">&times;</button>
        </div>
        
        <div class="drawer-body">
          <div class="form-group">
            <label>版本：</label>
            <select v-model="newFeature.version" class="form-select">
              <option value="">请选择版本</option>
              <option 
                v-for="version in versionOptions" 
                :key="version.value" 
                :value="version.value"
              >
                {{ version.label }}
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
            <label>特性编码：</label>
            <input 
              type="text" 
              v-model="newFeature.code" 
              placeholder="请输入特性编码"
              class="form-input"
            >
          </div>
          
          <div class="form-group">
            <label>特性描述：</label>
            <textarea 
              v-model="newFeature.description" 
              placeholder="请输入特性描述"
              rows="4"
              class="form-textarea"
            ></textarea>
          </div>
          
          <div class="form-group">
            <label>Wiki说明地址：</label>
            <input 
              type="url" 
              v-model="newFeature.wikiUrl" 
              placeholder="请输入Wiki说明地址（可选）"
              class="form-input"
            >
          </div>
        </div>
        
        <div class="drawer-footer">
          <button @click="closeAddFeatureDrawer" class="cancel-btn">取消</button>
          <button @click="addFeature" class="save-btn" :disabled="addingFeature">
            {{ addingFeature ? '保存中...' : '保存' }}
          </button>
        </div>
      </div>
    </div>

    <!-- 修改特性弹窗 -->
    <div v-if="showEditFeatureModal" class="modal-overlay" @click="closeEditFeatureModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>修改特性</h3>
          <button @click="closeEditFeatureModal" class="close-btn">&times;</button>
        </div>
        
        <div class="modal-body">
          <div class="form-group">
            <label>特性名称：</label>
            <input 
              type="text" 
              v-model="editingFeature.name" 
              placeholder="请输入特性名称"
              class="form-input"
            >
          </div>
          
          <div class="form-group">
            <label>特性编码：</label>
            <input 
              type="text" 
              v-model="editingFeature.code" 
              placeholder="请输入特性编码"
              class="form-input"
            >
          </div>
          
          <div class="form-group">
            <label>版本：</label>
            <select v-model="editingFeature.version" class="form-select">
              <option value="">请选择版本</option>
              <option 
                v-for="version in versionOptions" 
                :key="version.value" 
                :value="version.value"
              >
                {{ version.label }}
              </option>
            </select>
          </div>
          
          <div class="form-group">
            <label>部署单元：</label>
            <select v-model="editingFeature.deploymentUnit" class="form-select">
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
          
          <div class="form-group">
            <label>特性描述：</label>
            <textarea 
              v-model="editingFeature.description" 
              placeholder="请输入特性描述"
              rows="4"
              class="form-textarea"
            ></textarea>
          </div>
          
          <div class="form-group">
            <label>Wiki说明地址：</label>
            <input 
              type="url" 
              v-model="editingFeature.wikiUrl" 
              placeholder="请输入Wiki说明地址（可选）"
              class="form-input"
            >
          </div>
          
          <div class="form-group">
            <label>特性状态：</label>
            <select v-model="editingFeature.status" class="form-select">
              <option value="enabled">启用</option>
              <option value="disabled">禁用</option>
            </select>
          </div>
        </div>
        
        <div class="modal-footer">
          <button @click="closeEditFeatureModal" class="cancel-btn">取消</button>
          <button @click="saveFeature" class="save-btn" :disabled="savingFeature">
            {{ savingFeature ? '保存中...' : '保存' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FeatureConfig',
  data() {
    return {
      searchKeyword: '',
      selectedVersion: 'common',
      selectedDeploymentUnit: 'data',
      selectedFeatureIds: [],
      loading: false,
      saving: false,
      adding: false,
      addingFeature: false,
      savingFeature: false,
      configList: [],
      filteredFeatures: [],
      showEditModal: false,
      showAddModal: false,
      showAddFeatureDrawer: false,
      showEditFeatureModal: false,
      editingConfig: {
        id: '',
        key: '',
        value: '',
        featureName: '',
        featureCode: '',
        description: '',
        version: '',
        deploymentUnit: '',
        status: 'enabled'
      },
      newConfig: {
        key: '',
        value: '',
        featureCode: '',
        featureName: '',
        description: '',
        version: '',
        deploymentUnit: '',
        status: 'enabled'
      },
      newFeature: {
        name: '',
        code: '',
        description: '',
        version: '',
        deploymentUnit: '',
        wikiUrl: ''
      },
      editingFeature: {
        id: '',
        name: '',
        code: '',
        description: '',
        version: '',
        deploymentUnit: '',
        wikiUrl: '',
        status: 'enabled'
      },
      versionOptions: [
        { value: '', label: '全部' },
        { value: 'common', label: 'common' },
        { value: 'imei', label: 'imei' },
        { value: 'service', label: 'service' },
        { value: 'ideal', label: 'ideal' }
      ],
      deploymentUnits: [
        { value: '', label: '全部' },
        { value: 'data', label: 'data' },
        { value: 'entry', label: 'entry' },
        { value: 'router', label: 'router' },
        { value: 'audit', label: 'audit' }
      ]
    }
  },
  methods: {
    // 获取过滤后的特性列表
    async getFilteredFeatures() {
      try {
        const response = await fetch(window.location.protocol+'/cpsc/privacy/testmock/api/feature-config/getFeatures', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            version: this.selectedVersion,
            deploymentUnit: this.selectedDeploymentUnit
          })
        })
        
        if (response.ok) {
          const result = await response.json()
          console.log('API响应数据:', result) // 调试信息
          if (result.success) {
            console.log('特性数据:', result.data) // 调试信息
            return result.data || []
          } else {
            throw new Error(result.message || '获取特性失败')
          }
        } else {
          throw new Error(`获取特性失败: ${response.status}`)
        }
      } catch (error) {
        console.error('获取特性失败:', error)
        // 返回空数组，不显示模拟数据
        return []
      }
    },
    
    // 选择特性
    selectFeature(feature) {
      const index = this.selectedFeatureIds.indexOf(feature.id)
      if (index > -1) {
        // 如果已选中，则取消选中
        this.selectedFeatureIds.splice(index, 1)
      } else {
        // 如果未选中，则添加到选中列表
        this.selectedFeatureIds.push(feature.id)
      }
    },
    
    // 更新过滤后的特性
    async updateFilteredFeatures() {
      this.filteredFeatures = await this.getFilteredFeatures()
    },
    
    // 查询特性配置
    async searchFeatures() {
      this.loading = true
      
      try {
        const response = await fetch(window.location.protocol+'/cpsc/privacy/testmock/api/feature-config/search', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            keyword: this.searchKeyword.trim(),
            version: this.selectedVersion,
            deploymentUnit: this.selectedDeploymentUnit,
            selectedFeatureCodes: this.getSelectedFeatureCode() ? this.getSelectedFeatureCode().split(',') : []
          })
        })
        
        if (response.ok) {
          const result = await response.json()
          console.log('查询配置API响应:', result) // 调试信息
          if (result.success) {
            console.log('配置数据:', result.data) // 调试信息
            this.configList = result.data || []
          } else {
            throw new Error(result.message || '查询失败')
          }
        } else {
          throw new Error(`查询失败: ${response.status}`)
        }
      } catch (error) {
        console.error('查询失败:', error)
        // 返回空数组，不显示模拟数据
        this.configList = []
      } finally {
        this.loading = false
      }
    },
    
    // 获取选中特性的编码
    getSelectedFeatureCode() {
      if (this.selectedFeatureIds.length === 0) {
        return null
      }
      
      const selectedFeatures = this.filteredFeatures.filter(feature => 
        this.selectedFeatureIds.includes(feature.id)
      )
      return selectedFeatures.map(feature => feature.code).join(',')
    },
    
    // 获取选中特性的名称
    getSelectedFeatureName() {
      if (this.selectedFeatureIds.length === 0) {
        return null
      }
      
      const selectedFeatures = this.filteredFeatures.filter(feature => 
        this.selectedFeatureIds.includes(feature.id)
      )
      return selectedFeatures.map(feature => feature.name).join(', ')
    },
    
    
    
    // 编辑配置
    editConfig(config) {
      this.editingConfig = { ...config }
      this.showEditModal = true
    },
    
    // 保存配置
    async saveConfig() {
      if (!this.editingConfig.description.trim()) {
        alert('请输入配置描述')
        return
      }

      this.saving = true
      
      try {
        const response = await fetch(window.location.protocol+'/cpsc/privacy/testmock/api/feature-config/update', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            id: this.editingConfig.id,
            value: this.editingConfig.value,
            description: this.editingConfig.description,
            status: this.editingConfig.status
          })
        })
        
        if (response.ok) {
          const result = await response.json()
          if (result.success) {
            alert('保存成功')
            this.closeModal()
            // 重新查询以刷新列表
            this.searchFeatures()
          } else {
            throw new Error(result.message || '保存失败')
          }
        } else {
          throw new Error(`保存失败: ${response.status}`)
        }
      } catch (error) {
        console.error('保存失败:', error)
        alert('保存失败: ' + error.message)
      } finally {
        this.saving = false
      }
    },
    

    
    // 关闭弹窗
    closeModal() {
      this.showEditModal = false
      this.editingConfig = {
        id: '',
        key: '',
        value: '',
        featureName: '',
        featureCode: '',
        description: '',
        version: '',
        deploymentUnit: '',
        status: 'enabled'
      }
    },
    
    // 关闭新增弹窗
    closeAddModal() {
      this.showAddModal = false
    },
    
    // 新增配置特性选择变化
    onFeatureChange() {
      const selectedFeature = this.filteredFeatures.find(feature => feature.code === this.newConfig.featureCode)
      if (selectedFeature) {
        this.newConfig.featureName = selectedFeature.name
      } else {
        this.newConfig.featureName = ''
      }
    },
    
    // 编辑配置特性选择变化
    onEditFeatureChange() {
      const selectedFeature = this.filteredFeatures.find(feature => feature.code === this.editingConfig.featureCode)
      if (selectedFeature) {
        this.editingConfig.featureName = selectedFeature.name
      } else {
        this.editingConfig.featureName = ''
      }
    },
    
    // 新增配置
    async addConfig() {
      // 验证必填字段
      if (!this.newConfig.key.trim()) {
        alert('请输入配置Key')
        return
      }
      
      if (!this.newConfig.value.trim()) {
        alert('请输入配置默认值')
        return
      }
      
      if (!this.newConfig.featureCode) {
        alert('请选择所属特性')
        return
      }
      
      if (!this.newConfig.description.trim()) {
        alert('请输入配置描述')
        return
      }
      // 版本和部署单元允许为空（全部）
      // if (!this.newConfig.version) {
      //   alert('请选择版本')
      //   return
      // }
      // if (!this.newConfig.deploymentUnit) {
      //   alert('请选择部署单元')
      //   return
      // }

      this.adding = true
      
      try {
        const response = await fetch(window.location.protocol+'/cpsc/privacy/testmock/api/feature-config/add', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            key: this.newConfig.key.trim(),
            value: this.newConfig.value.trim(),
            featureCode: this.newConfig.featureCode,
            featureName: this.newConfig.featureName,
            description: this.newConfig.description.trim(),
            version: this.newConfig.version,
            deploymentUnit: this.newConfig.deploymentUnit,
            status: this.newConfig.status
          })
        })
        
        if (response.ok) {
          const result = await response.json()
          if (result.success) {
            alert('新增成功')
            this.closeAddModal()
            // 重新查询以刷新列表
            this.searchFeatures()
          } else {
            throw new Error(result.message || '新增失败')
          }
        } else {
          throw new Error(`新增失败: ${response.status}`)
        }
      } catch (error) {
        console.error('新增失败:', error)
        alert('新增失败: ' + error.message)
      } finally {
        this.adding = false
      }
    },
    

    
    // 关闭新增特性抽屉
    closeAddFeatureDrawer() {
      this.showAddFeatureDrawer = false
    },
    
    // 新增特性
    async addFeature() {
      // 验证必填字段
      if (!this.newFeature.name.trim()) {
        alert('请输入特性名称')
        return
      }
      
      if (!this.newFeature.code.trim()) {
        alert('请输入特性编码')
        return
      }
      
      if (!this.newFeature.description.trim()) {
        alert('请输入特性描述')
        return
      }
      // 版本和部署单元允许为空（全部）
      // if (!this.newFeature.version) {
      //   alert('请选择版本')
      //   return
      // }
      // if (!this.newFeature.deploymentUnit) {
      //   alert('请选择部署单元')
      //   return
      // }

      this.addingFeature = true
      
      try {
        const response = await fetch(window.location.protocol+'/cpsc/privacy/testmock/api/feature-config/addFeature', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            name: this.newFeature.name.trim(),
            code: this.newFeature.code.trim(),
            description: this.newFeature.description.trim(),
            version: this.newFeature.version,
            deploymentUnit: this.newFeature.deploymentUnit,
            wikiUrl: this.newFeature.wikiUrl.trim()
          })
        })
        
        if (response.ok) {
          const result = await response.json()
          if (result.success) {
            alert('新增特性成功')
            this.closeAddFeatureDrawer()
            // 重新获取特性列表
            this.updateFilteredFeatures()
          } else {
            throw new Error(result.message || '新增特性失败')
          }
        } else {
          throw new Error(`新增特性失败: ${response.status}`)
        }
      } catch (error) {
        console.error('新增特性失败:', error)
        alert('新增特性失败: ' + error.message)
      } finally {
        this.addingFeature = false
      }
    },
    

    
    // 编辑特性
    editFeature(feature) {
      this.editingFeature = { ...feature }
      this.showEditFeatureModal = true
    },
    
    // 保存特性
    async saveFeature() {
      // 验证必填字段
      if (!this.editingFeature.name.trim()) {
        alert('请输入特性名称')
        return
      }
      
      if (!this.editingFeature.code.trim()) {
        alert('请输入特性编码')
        return
      }
      
      if (!this.editingFeature.description.trim()) {
        alert('请输入特性描述')
        return
      }
      // 版本和部署单元允许为空（全部）
      // if (!this.editingFeature.version) {
      //   alert('请选择版本')
      //   return
      // }
      // if (!this.editingFeature.deploymentUnit) {
      //   alert('请选择部署单元')
      //   return
      // }

      this.savingFeature = true
      
      try {
        const response = await fetch(window.location.protocol+'/cpsc/privacy/testmock/api/feature-config/updateFeature', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            id: this.editingFeature.id,
            name: this.editingFeature.name.trim(),
            code: this.editingFeature.code.trim(),
            description: this.editingFeature.description.trim(),
            version: this.editingFeature.version,
            deploymentUnit: this.editingFeature.deploymentUnit,
            wikiUrl: this.editingFeature.wikiUrl.trim(),
            status: this.editingFeature.status
          })
        })
        
        if (response.ok) {
          const result = await response.json()
          if (result.success) {
            alert('修改特性成功')
            this.closeEditFeatureModal()
            // 重新获取特性列表
            this.updateFilteredFeatures()
          } else {
            throw new Error(result.message || '修改特性失败')
          }
        } else {
          throw new Error(`修改特性失败: ${response.status}`)
        }
      } catch (error) {
        console.error('修改特性失败:', error)
        alert('修改特性失败: ' + error.message)
      } finally {
        this.savingFeature = false
      }
    },
    

    
    // 关闭修改特性弹窗
    closeEditFeatureModal() {
      this.showEditFeatureModal = false
      this.editingFeature = {
        id: '',
        name: '',
        code: '',
        description: '',
        version: '',
        deploymentUnit: '',
        wikiUrl: '',
        status: 'enabled'
      }
    },
    
    // 格式化日期
    formatDate(dateString) {
      if (!dateString) return '-'
      const date = new Date(dateString)
      return date.toLocaleString('zh-CN')
    },
    openAddConfigModal() {
      this.newConfig = {
        key: '',
        value: '',
        featureCode: '',
        featureName: '',
        description: '',
        version: this.selectedVersion,
        deploymentUnit: this.selectedDeploymentUnit,
        selectedFeatures: [],
        status: 'enabled'
      }
      this.showAddModal = true
    },
    openAddFeatureDrawer() {
      this.newFeature = {
        name: '',
        code: '',
        description: '',
        version: this.selectedVersion,
        deploymentUnit: this.selectedDeploymentUnit,
        wikiUrl: ''
      }
      this.showAddFeatureDrawer = true
    }
  },
  mounted() {
    // 页面加载时自动查询所有数据
    this.searchFeatures()
    this.updateFilteredFeatures()
  },
  watch: {
    selectedVersion() {
      // 当版本改变时，重置选中的特性并更新特性列表
      this.selectedFeatureIds = []
      this.updateFilteredFeatures()
    },
    selectedDeploymentUnit() {
      // 当部署单元改变时，重置选中的特性并更新特性列表
      this.selectedFeatureIds = []
      this.updateFilteredFeatures()
    }
  }
}
</script>

<style scoped>
.feature-config {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.title {
  text-align: center;
  font-size: 2rem;
  color: #2c3e50;
  margin-bottom: 2rem;
}

.search-section {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  margin-bottom: 2rem;
}

.filter-row {
  margin-bottom: 1rem;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.filter-label {
  min-width: 80px;
  font-weight: 500;
  color: #2c3e50;
}

.radio-group {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.radio-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  font-size: 0.9rem;
  color: #2c3e50;
}

.radio-item input[type="radio"] {
  margin: 0;
  cursor: pointer;
}

.radio-item:hover {
  color: #42b883;
}

.feature-display {
  background: #f8f9fa;
  padding: 1rem;
  border-radius: 4px;
  margin: 1rem 0;
}

.display-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.display-title {
  font-weight: 500;
  color: #2c3e50;
  font-size: 0.9rem;
}

.display-count {
  color: #7f8c8d;
  font-size: 0.9rem;
}

.add-feature-icon {
  background: #42b883;
  color: white;
  border: none;
  border-radius: 50%;
  width: 32px;
  height: 32px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  font-weight: bold;
  transition: all 0.3s ease;
}

.add-feature-icon:hover {
  background: #3aa876;
  transform: scale(1.1);
}

.add-icon {
  line-height: 1;
}

.feature-buttons {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.feature-item {
  position: relative;
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.feature-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0.75rem 1rem;
  border: 2px solid #e9ecef;
  border-radius: 6px;
  background: white;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 120px;
  text-align: center;
}

.feature-btn:hover {
  border-color: #42b883;
  box-shadow: 0 2px 8px rgba(66, 184, 131, 0.2);
}

.feature-btn.selected {
  border-color: #42b883;
  background: #42b883;
  color: white;
}

.feature-btn-name {
  font-weight: 500;
  font-size: 0.9rem;
  margin-bottom: 0.25rem;
}

.feature-btn-status {
  font-size: 0.75rem;
  padding: 0.25rem 0.5rem;
  border-radius: 3px;
  font-weight: 500;
}

.feature-btn-wiki {
  margin-top: 0.25rem;
}

.wiki-link {
  font-size: 0.75rem;
  color: #42b883;
  text-decoration: none;
  padding: 0.2rem 0.4rem;
  border-radius: 3px;
  background: rgba(66, 184, 131, 0.1);
  transition: all 0.3s ease;
}

.wiki-link:hover {
  background: rgba(66, 184, 131, 0.2);
  color: #3aa876;
}

.feature-btn.selected .wiki-link {
  color: white;
  background: rgba(255, 255, 255, 0.2);
}

.feature-btn.selected .wiki-link:hover {
  background: rgba(255, 255, 255, 0.3);
}

.edit-feature-btn {
  background: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  width: 32px;
  height: 32px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.9rem;
  transition: all 0.3s ease;
  flex-shrink: 0;
}

.edit-feature-btn:hover {
  background: #2980b9;
  transform: scale(1.05);
}

.no-features {
  text-align: center;
  padding: 2rem;
  color: #7f8c8d;
  font-size: 1rem;
  background: #f8f9fa;
  border-radius: 6px;
  border: 2px dashed #dee2e6;
  margin: 1rem 0;
}

.search-container {
  display: flex;
  gap: 1rem;
  align-items: center;
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #eee;
}

.search-input-group {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.selected-feature-hint {
  font-size: 0.85rem;
  color: #42b883;
  font-weight: 500;
  background: #f0f9f4;
  padding: 0.25rem 0.5rem;
  border-radius: 3px;
  border-left: 3px solid #42b883;
}

.search-input {
  flex: 1;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.search-input:focus {
  outline: none;
  border-color: #42b883;
}

.search-btn {
  padding: 0.75rem 2rem;
  background: #42b883;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 500;
}

.search-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}



.list-section {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.list-header {
  padding: 1.5rem;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.add-config-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: #42b883;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.3s ease;
}

.add-config-btn:hover {
  background: #3aa876;
  transform: translateY(-1px);
}

.add-config-btn .add-icon {
  font-size: 1rem;
  font-weight: bold;
}

.list-header h2 {
  color: #2c3e50;
  margin: 0;
}

.total-count {
  color: #7f8c8d;
  font-size: 0.9rem;
}

.config-table-container {
  padding: 1rem;
  overflow-x: auto;
}

.config-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.config-table th {
  background: #f8f9fa;
  padding: 1rem;
  text-align: left;
  font-weight: 600;
  color: #2c3e50;
  border-bottom: 2px solid #e9ecef;
}

.config-table td {
  padding: 1rem;
  border-bottom: 1px solid #e9ecef;
  vertical-align: top;
}

.config-row:hover {
  background: #f8f9fa;
}

.feature-cell {
  min-width: 200px;
}

.feature-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.feature-name {
  font-weight: 600;
  color: #2c3e50;
  font-size: 0.95rem;
}

.feature-code {
  font-size: 0.8rem;
  color: #7f8c8d;
  background: #f1f3f4;
  padding: 0.2rem 0.4rem;
  border-radius: 3px;
  display: inline-block;
  width: fit-content;
}

.key-cell {
  font-family: 'Courier New', monospace;
  font-weight: 500;
  color: #2c3e50;
  min-width: 150px;
}

.value-cell {
  font-family: 'Courier New', monospace;
  color: #42b883;
  font-weight: 500;
  min-width: 120px;
}

.desc-cell {
  color: #7f8c8d;
  line-height: 1.4;
  min-width: 200px;
}

.action-cell {
  text-align: center;
  min-width: 80px;
}

.edit-btn {
  padding: 0.5rem 1rem;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
}

.edit-btn:hover {
  background: #2980b9;
}

.no-data {
  text-align: center;
  padding: 3rem;
  color: #7f8c8d;
  font-size: 1.1rem;
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
  padding: 1.5rem;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  margin: 0;
  color: #2c3e50;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #7f8c8d;
}

.close-btn:hover {
  color: #2c3e50;
}

.modal-body {
  padding: 1.5rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #2c3e50;
  font-weight: 500;
}

.form-input, .form-select, .form-textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.form-input:disabled {
  background: #f8f9fa;
  color: #7f8c8d;
}

.form-textarea {
  resize: vertical;
  min-height: 100px;
}

.form-input:focus, .form-select:focus, .form-textarea:focus {
  outline: none;
  border-color: #42b883;
}



.modal-footer {
  padding: 1.5rem;
  border-top: 1px solid #eee;
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
}

.cancel-btn, .save-btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 500;
}

.cancel-btn {
  background: #6c757d;
  color: white;
}

.save-btn {
  background: #42b883;
  color: white;
}

.save-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.cancel-btn:hover {
  background: #5a6268;
}

.save-btn:hover:not(:disabled) {
  background: #3aa876;
}

/* 抽屉样式 */
.drawer-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: flex-end;
  z-index: 1000;
}

.drawer-content {
  background: white;
  width: 400px;
  height: 100vh;
  overflow-y: auto;
  box-shadow: -2px 0 10px rgba(0,0,0,0.1);
  animation: slideIn 0.3s ease;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0);
  }
}

.drawer-header {
  padding: 1.5rem;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #f8f9fa;
}

.drawer-header h3 {
  margin: 0;
  color: #2c3e50;
}

.drawer-body {
  padding: 1.5rem;
}

.drawer-footer {
  padding: 1.5rem;
  border-top: 1px solid #eee;
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  background: #f8f9fa;
}
</style> 