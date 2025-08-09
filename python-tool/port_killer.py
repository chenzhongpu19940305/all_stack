import subprocess
import sys
import re
import tkinter as tk
from tkinter import messagebox, ttk
import threading

class PortKiller:
    def __init__(self):
        self.root = tk.Tk()
        self.root.title("端口关闭工具")
        self.root.geometry("500x400")
        self.root.resizable(False, False)
        
        # 设置窗口居中
        self.center_window()
        
        self.setup_ui()
        
    def center_window(self):
        """窗口居中显示"""
        self.root.update_idletasks()
        width = self.root.winfo_width()
        height = self.root.winfo_height()
        x = (self.root.winfo_screenwidth() // 2) - (width // 2)
        y = (self.root.winfo_screenheight() // 2) - (height // 2)
        self.root.geometry(f"{width}x{height}+{x}+{y}")
        
    def setup_ui(self):
        """设置用户界面"""
        # 标题
        title_label = tk.Label(self.root, text="端口关闭工具", font=("微软雅黑", 16, "bold"))
        title_label.pack(pady=10)
        
        # 端口输入框架
        port_frame = tk.Frame(self.root)
        port_frame.pack(pady=10)
        
        tk.Label(port_frame, text="端口号:", font=("微软雅黑", 12)).pack(side=tk.LEFT, padx=5)
        self.port_entry = tk.Entry(port_frame, font=("微软雅黑", 12), width=10)
        self.port_entry.pack(side=tk.LEFT, padx=5)
        
        # 按钮框架
        button_frame = tk.Frame(self.root)
        button_frame.pack(pady=10)
        
        self.check_btn = tk.Button(button_frame, text="查看端口", font=("微软雅黑", 10), 
                                  command=self.check_port, bg="#4CAF50", fg="white", width=10)
        self.check_btn.pack(side=tk.LEFT, padx=5)
        
        self.kill_btn = tk.Button(button_frame, text="关闭端口", font=("微软雅黑", 10), 
                                 command=self.kill_port, bg="#f44336", fg="white", width=10)
        self.kill_btn.pack(side=tk.LEFT, padx=5)
        
        self.refresh_btn = tk.Button(button_frame, text="刷新列表", font=("微软雅黑", 10), 
                                    command=self.refresh_all_ports, bg="#2196F3", fg="white", width=10)
        self.refresh_btn.pack(side=tk.LEFT, padx=5)
        
        # 结果显示区域
        result_frame = tk.Frame(self.root)
        result_frame.pack(pady=10, padx=10, fill=tk.BOTH, expand=True)
        
        tk.Label(result_frame, text="端口占用情况:", font=("微软雅黑", 12, "bold")).pack(anchor=tk.W)
        
        # 创建Treeview显示端口信息
        columns = ("端口", "协议", "状态", "进程ID", "进程名")
        self.tree = ttk.Treeview(result_frame, columns=columns, show="headings", height=12)
        
        # 设置列标题和宽度
        for col in columns:
            self.tree.heading(col, text=col)
            if col == "端口":
                self.tree.column(col, width=80)
            elif col == "协议":
                self.tree.column(col, width=60)
            elif col == "状态":
                self.tree.column(col, width=100)
            elif col == "进程ID":
                self.tree.column(col, width=80)
            else:
                self.tree.column(col, width=150)
        
        # 添加滚动条
        scrollbar = ttk.Scrollbar(result_frame, orient=tk.VERTICAL, command=self.tree.yview)
        self.tree.configure(yscrollcommand=scrollbar.set)
        
        self.tree.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        scrollbar.pack(side=tk.RIGHT, fill=tk.Y)
        
        # 状态栏
        self.status_label = tk.Label(self.root, text="就绪", relief=tk.SUNKEN, anchor=tk.W)
        self.status_label.pack(side=tk.BOTTOM, fill=tk.X)
        
        # 绑定回车键
        self.port_entry.bind("<Return>", lambda e: self.check_port())
        
        # 初始化时显示所有端口
        self.refresh_all_ports()
        
    def update_status(self, message):
        """更新状态栏"""
        self.status_label.config(text=message)
        self.root.update()
        
    def get_port_info(self, port=None):
        """获取端口信息"""
        try:
            if port:
                # 查询特定端口
                cmd = f'netstat -ano | findstr ":{port}"'
            else:
                # 查询所有监听端口
                cmd = 'netstat -ano | findstr "LISTENING"'
            
            result = subprocess.run(cmd, shell=True, capture_output=True, text=True, encoding='gbk')
            return result.stdout.strip().split('\n') if result.stdout.strip() else []
        except Exception as e:
            messagebox.showerror("错误", f"获取端口信息失败: {str(e)}")
            return []
    
    def get_process_name(self, pid):
        """根据进程ID获取进程名"""
        try:
            cmd = f'tasklist /FI "PID eq {pid}" /FO CSV /NH'
            result = subprocess.run(cmd, shell=True, capture_output=True, text=True, encoding='gbk')
            if result.stdout.strip():
                # 解析CSV格式输出
                line = result.stdout.strip().split('\n')[0]
                process_name = line.split(',')[0].strip('"')
                return process_name
        except:
            pass
        return "未知"
    
    def parse_netstat_line(self, line):
        """解析netstat输出行"""
        if not line.strip():
            return None
            
        parts = line.split()
        if len(parts) < 5:
            return None
            
        protocol = parts[0]
        local_address = parts[1]
        state = parts[3] if len(parts) > 3 else ""
        pid = parts[-1]
        
        # 提取端口号
        if ':' in local_address:
            port = local_address.split(':')[-1]
        else:
            return None
            
        # 获取进程名
        process_name = self.get_process_name(pid)
        
        return {
            'port': port,
            'protocol': protocol,
            'state': state,
            'pid': pid,
            'process_name': process_name
        }
    
    def check_port(self):
        """查看指定端口"""
        port = self.port_entry.get().strip()
        if not port:
            messagebox.showwarning("警告", "请输入端口号")
            return
            
        if not port.isdigit():
            messagebox.showerror("错误", "端口号必须是数字")
            return
            
        self.update_status(f"正在查询端口 {port}...")
        
        def check_thread():
            lines = self.get_port_info(port)
            
            # 清空现有数据
            for item in self.tree.get_children():
                self.tree.delete(item)
            
            if not lines or (len(lines) == 1 and not lines[0].strip()):
                self.root.after(0, lambda: self.update_status(f"端口 {port} 未被占用"))
                self.root.after(0, lambda: messagebox.showinfo("信息", f"端口 {port} 未被占用"))
                return
            
            found = False
            for line in lines:
                info = self.parse_netstat_line(line)
                if info and info['port'] == port:
                    found = True
                    self.root.after(0, lambda i=info: self.tree.insert("", tk.END, values=(
                        i['port'], i['protocol'], i['state'], i['pid'], i['process_name']
                    )))
            
            if found:
                self.root.after(0, lambda: self.update_status(f"端口 {port} 正在被使用"))
            else:
                self.root.after(0, lambda: self.update_status(f"端口 {port} 未被占用"))
                self.root.after(0, lambda: messagebox.showinfo("信息", f"端口 {port} 未被占用"))
        
        threading.Thread(target=check_thread, daemon=True).start()
    
    def kill_port(self):
        """关闭指定端口的进程"""
        port = self.port_entry.get().strip()
        if not port:
            messagebox.showwarning("警告", "请输入端口号")
            return
            
        if not port.isdigit():
            messagebox.showerror("错误", "端口号必须是数字")
            return
        
        # 确认操作
        if not messagebox.askyesno("确认", f"确定要关闭端口 {port} 的所有进程吗？"):
            return
            
        self.update_status(f"正在关闭端口 {port}...")
        
        def kill_thread():
            lines = self.get_port_info(port)
            
            if not lines or (len(lines) == 1 and not lines[0].strip()):
                self.root.after(0, lambda: self.update_status(f"端口 {port} 未被占用"))
                self.root.after(0, lambda: messagebox.showinfo("信息", f"端口 {port} 未被占用，无需关闭"))
                return
            
            killed_pids = set()
            success_count = 0
            
            for line in lines:
                info = self.parse_netstat_line(line)
                if info and info['port'] == port and info['pid'] not in killed_pids:
                    try:
                        pid = info['pid']
                        process_name = info['process_name']
                        
                        # 尝试关闭进程
                        cmd = f'taskkill /F /PID {pid}'
                        result = subprocess.run(cmd, shell=True, capture_output=True, text=True)
                        
                        if result.returncode == 0:
                            killed_pids.add(pid)
                            success_count += 1
                            self.root.after(0, lambda p=process_name, i=pid: 
                                          self.update_status(f"已关闭进程: {p} (PID: {i})"))
                        else:
                            self.root.after(0, lambda p=process_name, i=pid: 
                                          messagebox.showwarning("警告", f"无法关闭进程: {p} (PID: {i})\n可能需要管理员权限"))
                    except Exception as e:
                        self.root.after(0, lambda err=str(e): 
                                      messagebox.showerror("错误", f"关闭进程时出错: {err}"))
            
            if success_count > 0:
                self.root.after(0, lambda: self.update_status(f"成功关闭 {success_count} 个进程"))
                self.root.after(0, lambda: messagebox.showinfo("成功", f"成功关闭端口 {port} 的 {success_count} 个进程"))
                # 刷新显示
                self.root.after(1000, self.check_port)
            else:
                self.root.after(0, lambda: self.update_status("没有进程被关闭"))
        
        threading.Thread(target=kill_thread, daemon=True).start()
    
    def refresh_all_ports(self):
        """刷新显示所有监听端口"""
        self.update_status("正在刷新端口列表...")
        
        def refresh_thread():
            lines = self.get_port_info()
            
            # 清空现有数据
            for item in self.tree.get_children():
                self.tree.delete(item)
            
            port_info = {}
            for line in lines:
                info = self.parse_netstat_line(line)
                if info:
                    port_key = f"{info['port']}_{info['protocol']}"
                    if port_key not in port_info:
                        port_info[port_key] = info
            
            # 按端口号排序
            sorted_ports = sorted(port_info.values(), key=lambda x: int(x['port']) if x['port'].isdigit() else 0)
            
            for info in sorted_ports:
                self.root.after(0, lambda i=info: self.tree.insert("", tk.END, values=(
                    i['port'], i['protocol'], i['state'], i['pid'], i['process_name']
                )))
            
            self.root.after(0, lambda: self.update_status(f"已显示 {len(sorted_ports)} 个监听端口"))
        
        threading.Thread(target=refresh_thread, daemon=True).start()
    
    def run(self):
        """运行应用程序"""
        self.root.mainloop()

def main():
    """主函数"""
    try:
        app = PortKiller()
        app.run()
    except Exception as e:
        messagebox.showerror("错误", f"程序启动失败: {str(e)}")
        sys.exit(1)

if __name__ == "__main__":
    main()