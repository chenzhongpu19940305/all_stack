package com.tool.service;

import com.tool.entity.User;

import java.util.List;

/**
 * 用户服务接口
 * 
 * @author tool-service
 * @version 1.0.0
 */
public interface UserService {

    /**
     * 创建用户
     * 
     * @param user 用户信息
     * @return 创建的用户
     */
    User createUser(User user);

    /**
     * 根据ID查找用户
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    User findById(Long id);

    /**
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查找用户
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    User findByEmail(String email);

    /**
     * 更新用户信息
     * 
     * @param user 用户信息
     * @return 更新后的用户
     */
    User updateUser(User user);

    /**
     * 删除用户
     * 
     * @param id 用户ID
     */
    void deleteUser(Long id);

    /**
     * 获取所有用户
     * 
     * @return 用户列表
     */
    List<User> findAllUsers();

    /**
     * 根据状态查找用户
     * 
     * @param status 状态
     * @return 用户列表
     */
    List<User> findByStatus(Integer status);

    /**
     * 根据关键词搜索用户
     * 
     * @param keyword 关键词
     * @return 用户列表
     */
    List<User> searchUsers(String keyword);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);
} 