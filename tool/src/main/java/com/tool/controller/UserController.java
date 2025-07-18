package com.tool.controller;

import com.tool.entity.User;
import com.tool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 * 
 * @author tool-service
 * @version 1.0.0
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 创建用户
     * 
     * @param user 用户信息
     * @return 创建结果
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody User user) {
        Map<String, Object> result = new HashMap<String, Object>();
        
        try {
            // 检查用户名是否已存在
            if (userService.existsByUsername(user.getUsername())) {
                result.put("success", false);
                result.put("message", "用户名已存在");
                return ResponseEntity.badRequest().body(result);
            }
            
            // 检查邮箱是否已存在
            if (userService.existsByEmail(user.getEmail())) {
                result.put("success", false);
                result.put("message", "邮箱已存在");
                return ResponseEntity.badRequest().body(result);
            }
            
            User createdUser = userService.createUser(user);
            result.put("success", true);
            result.put("message", "用户创建成功");
            result.put("data", createdUser);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建用户失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 根据ID获取用户
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<String, Object>();
        
        User user = userService.findById(id);
        if (user != null) {
            result.put("success", true);
            result.put("data", user);
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "用户不存在");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取所有用户
     * 
     * @return 用户列表
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        Map<String, Object> result = new HashMap<String, Object>();
        
        try {
            List<User> users = userService.findAllUsers();
            result.put("success", true);
            result.put("data", users);
            result.put("total", users.size());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取用户列表失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 更新用户信息
     * 
     * @param id 用户ID
     * @param user 用户信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody User user) {
        Map<String, Object> result = new HashMap<String, Object>();
        
        try {
            user.setId(id);
            User updatedUser = userService.updateUser(user);
            if (updatedUser != null) {
                result.put("success", true);
                result.put("message", "用户更新成功");
                result.put("data", updatedUser);
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "用户不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新用户失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 删除用户
     * 
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<String, Object>();
        
        try {
            userService.deleteUser(id);
            result.put("success", true);
            result.put("message", "用户删除成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除用户失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 搜索用户
     * 
     * @param keyword 关键词
     * @return 搜索结果
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchUsers(@RequestParam String keyword) {
        Map<String, Object> result = new HashMap<String, Object>();
        
        try {
            List<User> users = userService.searchUsers(keyword);
            result.put("success", true);
            result.put("data", users);
            result.put("total", users.size());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "搜索用户失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
} 