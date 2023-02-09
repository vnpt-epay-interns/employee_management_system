package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.mapper.UserMapper;
import com.example.Employee_Management_System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
}
