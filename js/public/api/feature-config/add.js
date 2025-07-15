// 模拟新增特性API响应
// 实际使用时，这个文件应该由后端服务器提供

// 模拟新增响应
const mockAddResponse = {
  success: true,
  message: '新增成功',
  data: {
    id: '009',
    name: '新特性名称',
    desc: '新特性描述',
    version: 'common',
    deploymentUnit: 'data',
    status: 'enabled',
    updateTime: new Date().toISOString()
  }
};

// 如果需要在浏览器中测试，可以取消注释下面的代码
// window.mockFeatureAddResponse = mockAddResponse; 