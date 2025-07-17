// 模拟特性配置查询API响应
// 实际使用时，这个文件应该由后端服务器提供

// 模拟特性配置数据
const mockFeatures = [
  {
    id: '001',
    name: '用户登录功能',
    desc: '支持用户名密码登录和第三方登录',
    status: 'enabled',
    version: 'common',
    deploymentUnit: 'data',
    updateTime: '2024-01-15T10:30:00Z'
  },
  {
    id: '002', 
    name: '数据导出功能',
    desc: '支持Excel和PDF格式的数据导出',
    status: 'enabled',
    version: 'service',
    deploymentUnit: 'entry',
    updateTime: '2024-01-14T15:20:00Z'
  },
  {
    id: '003',
    name: '消息推送功能',
    desc: '实时消息推送和通知',
    status: 'disabled',
    version: 'imei',
    deploymentUnit: 'router',
    updateTime: '2024-01-13T09:45:00Z'
  },
  {
    id: '004',
    name: '文件上传功能',
    desc: '支持多种格式文件上传',
    status: 'enabled',
    version: 'ideal',
    deploymentUnit: 'audit',
    updateTime: '2024-01-12T14:15:00Z'
  },
  {
    id: '005',
    name: '数据统计功能',
    desc: '提供各种数据统计和分析功能',
    status: 'enabled',
    version: 'common',
    deploymentUnit: 'entry',
    updateTime: '2024-01-11T11:20:00Z'
  },
  {
    id: '006',
    name: '权限管理功能',
    desc: '用户权限和角色管理',
    status: 'enabled',
    version: 'service',
    deploymentUnit: 'data',
    updateTime: '2024-01-10T16:45:00Z'
  },
  {
    id: '007',
    name: '设备识别功能',
    desc: 'IMEI设备识别和管理',
    status: 'enabled',
    version: 'imei',
    deploymentUnit: 'router',
    updateTime: '2024-01-09T14:30:00Z'
  },
  {
    id: '008',
    name: '审计日志功能',
    desc: '系统操作审计和日志记录',
    status: 'enabled',
    version: 'ideal',
    deploymentUnit: 'audit',
    updateTime: '2024-01-08T16:45:00Z'
  }
];

// 模拟查询响应
const mockSearchResponse = {
  success: true,
  features: mockFeatures,
  total: mockFeatures.length,
  message: '查询成功'
};

// 如果需要在浏览器中测试，可以取消注释下面的代码
// window.mockFeatureSearchResponse = mockSearchResponse; 