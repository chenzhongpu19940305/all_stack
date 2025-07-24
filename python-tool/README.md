# JDK版本切换工具

这是一个Windows下的JDK版本切换工具，支持在JDK 1.8和JDK 17之间快速切换。

## 功能特性

- 🔄 一键切换JDK版本（1.8 ↔ 17）
- 🖥️ 图形化界面，操作简单
- ⚙️ 自动设置JAVA_HOME和PATH环境变量
- 📊 实时显示当前JDK版本
- 🛠️ 支持自定义JDK安装路径

## 使用方法

### 方法一：直接运行Python脚本

1. 确保已安装Python 3.6+
2. 运行脚本：
   ```bash
   python jdk_switcher.py
   ```

### 方法二：构建exe文件

1. 双击运行 `build_exe.bat`
2. 等待构建完成
3. 在 `dist` 目录下找到 `JDK切换工具.exe`
4. 双击运行exe文件

## 使用步骤

1. **配置JDK路径**（首次使用）：
   - 点击"配置路径"按钮
   - 输入JDK 1.8和JDK 17的安装路径
   - 点击"保存配置"

2. **切换JDK版本**：
   - 从下拉菜单选择目标JDK版本
   - 点击"切换JDK版本"按钮
   - 等待切换完成

3. **验证切换结果**：
   - 点击"刷新状态"查看当前版本
   - 或在命令行运行 `java -version`

## 注意事项

⚠️ **重要提醒**：

1. **管理员权限**：程序需要管理员权限来修改系统环境变量
2. **重启生效**：切换后需要重启命令行或应用程序才能生效
3. **路径配置**：首次使用前请确保配置了正确的JDK安装路径
4. **JDK安装**：确保系统已安装对应版本的JDK

## 默认JDK路径

- JDK 1.8: `C:\Program Files\Java\jdk1.8.0_XXX`
- JDK 17: `C:\Program Files\Java\jdk-17`

请根据实际安装路径进行调整。

## 常见问题

### Q: 提示"权限不足"怎么办？
A: 右键以管理员身份运行程序。

### Q: 切换后java -version还是旧版本？
A: 重新打开命令行窗口，或重启相关应用程序。

### Q: 提示"JDK路径不存在"？
A: 点击"配置路径"按钮，设置正确的JDK安装路径。

### Q: 如何找到JDK安装路径？
A: 通常在以下位置：
- `C:\Program Files\Java\`
- `C:\Program Files (x86)\Java\`
- 或查看现有的JAVA_HOME环境变量

## 技术说明

- 使用Python + tkinter开发
- 通过Windows注册表修改环境变量
- 支持PyInstaller打包成独立exe文件
- 兼容Windows 7/8/10/11

## 文件说明

- `jdk_switcher.py` - 主程序文件
- `requirements.txt` - Python依赖列表
- `build_exe.bat` - 构建脚本
- `README.md` - 使用说明