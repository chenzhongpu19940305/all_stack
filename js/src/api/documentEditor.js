// 文档编辑器API服务
const API_BASE_URL = 'http://localhost:8080/czp/tool/api/document-editor'

/**
 * 获取文档列表
 * @param {Object} params - 查询参数
 * @param {number} params.page - 页码，默认1
 * @param {number} params.size - 每页大小，默认20
 * @param {string} params.q - 搜索关键词
 * @returns {Promise<Object>} 响应数据
 */
export const getDocuments = async (params = {}) => {
  const { page = 1, size = 20, q } = params
  const queryParams = new URLSearchParams({
    page: page.toString(),
    size: size.toString()
  })
  
  if (q && q.trim()) {
    queryParams.append('q', q.trim())
  }
  
  try {
    const response = await fetch(`${API_BASE_URL}/documents?${queryParams}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }
    
    return await response.json()
  } catch (error) {
    console.error('获取文档列表失败:', error)
    throw error
  }
}

/**
 * 根据ID获取文档详情
 * @param {string} id - 文档ID
 * @returns {Promise<Object>} 响应数据
 */
export const getDocument = async (id) => {
  try {
    const response = await fetch(`${API_BASE_URL}/documents/${id}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }
    
    return await response.json()
  } catch (error) {
    console.error('获取文档详情失败:', error)
    throw error
  }
}

/**
 * 创建新文档
 * @param {Object} document - 文档数据
 * @param {string} document.title - 文档标题
 * @param {string} document.content - 文档内容
 * @returns {Promise<Object>} 响应数据
 */
export const createDocument = async (document) => {
  try {
    const response = await fetch(`${API_BASE_URL}/documents`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(document)
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }
    
    return await response.json()
  } catch (error) {
    console.error('创建文档失败:', error)
    throw error
  }
}

/**
 * 更新文档
 * @param {string} id - 文档ID
 * @param {Object} document - 文档数据
 * @param {string} document.title - 文档标题
 * @param {string} document.content - 文档内容
 * @returns {Promise<Object>} 响应数据
 */
export const updateDocument = async (id, document) => {
  try {
    const response = await fetch(`${API_BASE_URL}/documents/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(document)
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }
    
    return await response.json()
  } catch (error) {
    console.error('更新文档失败:', error)
    throw error
  }
}

/**
 * 删除文档
 * @param {string} id - 文档ID
 * @returns {Promise<Object>} 响应数据
 */
export const deleteDocument = async (id) => {
  try {
    const response = await fetch(`${API_BASE_URL}/documents/${id}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json'
      }
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }
    
    return await response.json()
  } catch (error) {
    console.error('删除文档失败:', error)
    throw error
  }
}