package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }
    
    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }
    
    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
    
    @Override
    public User save(User user) {
        userMapper.insert(user);
        return user;
    }
    
    @Override
    public User update(User user) {
        userMapper.update(user);
        return user;
    }
    
    @Override
    public void deleteById(Long id) {
        userMapper.deleteById(id);
    }
} 