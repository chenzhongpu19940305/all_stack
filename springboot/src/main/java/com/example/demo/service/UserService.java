package com.example.demo.service;

import com.example.demo.entity.User;
import java.util.List;

public interface UserService {
    
    List<User> findAll();
    
    User findById(Long id);
    
    User findByUsername(String username);
    
    User save(User user);
    
    User update(User user);
    
    void deleteById(Long id);
} 