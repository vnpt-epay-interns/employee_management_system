package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.repository.UserRepository;
import com.example.Employee_Management_System.service.UserService;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserByEmail(String email) {
        //TODO : custom exception: NOT FOUND EXCPETION
        return userRepository.findByUsername(email).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
