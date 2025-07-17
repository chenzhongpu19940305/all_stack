// 模拟特性配置更新API响应
// 实际使用时，这个文件应该由后端服务器提供

// 模拟更新响应
const mockUpdateResponse = {
  success: true,
  message: '更新成功',
  data: {
    id: '001',
    name: '用户登录功能',
    desc: '支持用户名密码登录和第三方登录',
    status: 'enabled',
    updateTime: new Date().toISOString()
  }
};

// 如果需要在浏览器中测试，可以取消注释下面的代码
// window.mockFeatureUpdateResponse = mockUpdateResponse; 