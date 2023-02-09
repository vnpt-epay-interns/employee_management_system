package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl {

    @Autowired
    private UserService userService;

}
