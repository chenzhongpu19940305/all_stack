#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
JDK版本切换工具
支持在JDK 1.8和JDK 17之间切换
"""

import os
import sys
import winreg
import tkinter as tk
from tkinter import messagebox, ttk
import subprocess

class JDKSwitcher:
    def __init__(self):
        self.root = tk.Tk()
        self.root.title("JDK版本切换工具")
        self.root.geometry("400x300")
        self.root.resizable(False, False)
        
        # JDK路径配置（需要根据实际安装路径修改）
        self.jdk_paths = {
            "JDK 1.8": "C:\Program Files\Java\jdk1.8.0_162",
            "JDK 17": "C:\\Program Files\\Java\\jdk-17"
        }
        
        self.setup_ui()
        self.check_current_jdk()￥
    
    def setup_ui(self):
        """设置用户界面"""
        # 标题
        title_label = tk.Label(self.root, text="JDK版本切换工具", font=("Arial", 16, "bold"))
        title_label.pack(pady=20)
        
        # 当前JDK版本显示
        self.current_label = tk.Label(self.root, text="当前JDK版本: 检测中...", font=("Arial", 12))
        self.current_label.pack(pady=10)
        
        # JDK版本选择
        select_frame = tk.Frame(self.root)
        select_frame.pack(pady=20)
        
        tk.Label(select_frame, text="选择JDK版本:", font=("Arial", 12)).pack()
        
        self.jdk_var = tk.StringVar()
        self.jdk_combo = ttk.Combobox(select_frame, textvariable=self.jdk_var, 
                                     values=list(self.jdk_paths.keys()), 
                                     state="readonly", width=20)
        self.jdk_combo.pack(pady=10)
        
        # 按钮框架
        button_frame = tk.Frame(self.root)
        button_frame.pack(pady=20)
        
        # 切换按钮
        switch_btn = tk.Button(button_frame, text="切换JDK版本", 
                              command=self.switch_jdk, 
                              bg="#4CAF50", fg="white", 
                              font=("Arial", 12), 
                              padx=20, pady=5)
        switch_btn.pack(side=tk.LEFT, padx=10)
        
        # 刷新按钮
        refresh_btn = tk.Button(button_frame, text="刷新状态", 
                               command=self.check_current_jdk, 
                               bg="#2196F3", fg="white", 
                               font=("Arial", 12), 
                               padx=20, pady=5)
        refresh_btn.pack(side=tk.LEFT, padx=10)
        
        # 配置按钮
        config_btn = tk.Button(button_frame, text="配置路径", 
                              command=self.configure_paths, 
                              bg="#FF9800", fg="white", 
                              font=("Arial", 12), 
                              padx=20, pady=5)
        config_btn.pack(side=tk.LEFT, padx=10)
        
        # 状态栏
        self.status_label = tk.Label(self.root, text="就绪", 
                                    relief=tk.SUNKEN, anchor=tk.W)
        self.status_label.pack(side=tk.BOTTOM, fill=tk.X)
    
    def check_current_jdk(self):
        """检查当前JDK版本"""
        try:
            result = subprocess.run(['java', '-version'], 
                                  capture_output=True, text=True, stderr=subprocess.STDOUT)
            if result.returncode == 0:
                version_info = result.stdout
                if '1.8' in version_info:
                    current_version = "JDK 1.8"
                elif '17' in version_info:
                    current_version = "JDK 17"
                else:
                    current_version = "未知版本"
                
                self.current_label.config(text=f"当前JDK版本: {current_version}")
                self.status_label.config(text="JDK版本检测完成")
            else:
                self.current_label.config(text="当前JDK版本: 未检测到Java")
                self.status_label.config(text="未检测到Java环境")
        except Exception as e:
            self.current_label.config(text="当前JDK版本: 检测失败")
            self.status_label.config(text=f"检测失败: {str(e)}")
    
    def switch_jdk(self):
        """切换JDK版本"""
        selected_jdk = self.jdk_var.get()
        if not selected_jdk:
            messagebox.showwarning("警告", "请选择要切换的JDK版本")
            return
        
        jdk_path = self.jdk_paths[selected_jdk]
        
        # 检查JDK路径是否存在
        if not os.path.exists(jdk_path):
            messagebox.showerror("错误", f"JDK路径不存在: {jdk_path}\n请先配置正确的JDK路径")
            return
        
        try:
            self.status_label.config(text="正在切换JDK版本...")
            self.root.update()
            
            # 设置JAVA_HOME环境变量
            self.set_environment_variable('JAVA_HOME', jdk_path)
            
            # 更新PATH环境变量
            self.update_path_variable(jdk_path)
            
            messagebox.showinfo("成功", f"JDK版本已切换到 {selected_jdk}\n\n" +
                               "注意: 需要重启命令行或应用程序才能生效")
            
            self.status_label.config(text="JDK版本切换完成")
            
            # 刷新当前版本显示
            self.check_current_jdk()
            
        except Exception as e:
            messagebox.showerror("错误", f"切换JDK版本失败:\n{str(e)}")
            self.status_label.config(text="切换失败")
    
    def set_environment_variable(self, name, value):
        """设置系统环境变量"""
        try:
            # 打开系统环境变量注册表项
            key = winreg.OpenKey(winreg.HKEY_LOCAL_MACHINE,
                               r'SYSTEM\CurrentControlSet\Control\Session Manager\Environment',
                               0, winreg.KEY_ALL_ACCESS)
            
            # 设置环境变量
            winreg.SetValueEx(key, name, 0, winreg.REG_EXPAND_SZ, value)
            winreg.CloseKey(key)
            
            # 广播环境变量更改消息
            import ctypes
            from ctypes import wintypes
            
            HWND_BROADCAST = 0xFFFF
            WM_SETTINGCHANGE = 0x001A
            SMTO_ABORTIFHUNG = 0x0002
            
            ctypes.windll.user32.SendMessageTimeoutW(
                HWND_BROADCAST, WM_SETTINGCHANGE, 0, "Environment",
                SMTO_ABORTIFHUNG, 5000, ctypes.byref(wintypes.DWORD())
            )
            
        except PermissionError:
            raise Exception("权限不足，请以管理员身份运行此程序")
        except Exception as e:
            raise Exception(f"设置环境变量失败: {str(e)}")
    
    def update_path_variable(self, jdk_path):
        """更新PATH环境变量中的JDK路径"""
        try:
            # 获取当前PATH
            key = winreg.OpenKey(winreg.HKEY_LOCAL_MACHINE,
                               r'SYSTEM\CurrentControlSet\Control\Session Manager\Environment',
                               0, winreg.KEY_ALL_ACCESS)
            
            current_path, _ = winreg.QueryValueEx(key, 'PATH')
            
            # 移除旧的JDK路径
            path_entries = current_path.split(';')
            new_path_entries = []
            
            for entry in path_entries:
                # 跳过包含Java的路径（简单过滤）
                if 'java' not in entry.lower() or 'jdk' not in entry.lower():
                    new_path_entries.append(entry)
            
            # 添加新的JDK路径到开头
            new_jdk_bin = os.path.join(jdk_path, 'bin')
            new_path_entries.insert(0, new_jdk_bin)
            
            # 重新组合PATH
            new_path = ';'.join(new_path_entries)
            
            # 设置新的PATH
            winreg.SetValueEx(key, 'PATH', 0, winreg.REG_EXPAND_SZ, new_path)
            winreg.CloseKey(key)
            
        except Exception as e:
            raise Exception(f"更新PATH变量失败: {str(e)}")
    
    def configure_paths(self):
        """配置JDK路径"""
        config_window = tk.Toplevel(self.root)
        config_window.title("配置JDK路径")
        config_window.geometry("500x200")
        config_window.resizable(False, False)
        
        # JDK 1.8路径
        tk.Label(config_window, text="JDK 1.8 路径:").pack(pady=5)
        jdk8_entry = tk.Entry(config_window, width=60)
        jdk8_entry.pack(pady=5)
        jdk8_entry.insert(0, self.jdk_paths["JDK 1.8"])
        
        # JDK 17路径
        tk.Label(config_window, text="JDK 17 路径:").pack(pady=5)
        jdk17_entry = tk.Entry(config_window, width=60)
        jdk17_entry.pack(pady=5)
        jdk17_entry.insert(0, self.jdk_paths["JDK 17"])
        
        def save_config():
            self.jdk_paths["JDK 1.8"] = jdk8_entry.get()
            self.jdk_paths["JDK 17"] = jdk17_entry.get()
            messagebox.showinfo("成功", "JDK路径配置已保存")
            config_window.destroy()
        
        tk.Button(config_window, text="保存配置", command=save_config).pack(pady=20)
    
    def run(self):
        """运行应用程序"""
        self.root.mainloop()

def main():
    """主函数"""
    try:
        app = JDKSwitcher()
        app.run()
    except Exception as e:
        messagebox.showerror("错误", f"程序启动失败:\n{str(e)}")

if __name__ == "__main__":
    main()