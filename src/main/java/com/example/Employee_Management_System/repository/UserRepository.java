package com.example.Employee_Management_System.repository;

import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ManagerRepository managerService;

    public void save(User user) {
        userMapper.save(user);
    }

    public boolean existsByEmail(String email) {
        return userMapper.findByEmail(email).isPresent();
    }

    public Optional<User> findByUsername(String username) {
        return userMapper.findByEmail(username);
    }

    public Manager findManagerByReferenceCode(String referenceCode) {
        return managerService.findManagerByReferenceCode(referenceCode);
    }

    public void update(User user) {
        userMapper.update(user);
    }

    public User findByVerificationCode(String verificationCode) {
        return userMapper.findByVerificationCode(verificationCode);
    }
}
