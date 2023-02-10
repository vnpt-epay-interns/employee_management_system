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

    // ToDo: save, update, find, delete, paging
    public void save(User user) {
        userMapper.save(user);
    }

    public boolean existsByUsername(String email) {
    }

    public Optional<User> findByUsername(String username) {
        return null;
    }

    public Manager findManagerByReferenceCode(String referenceCode) {
    }

    public void update(User user) {
    }
//    User modify(User user);
//    Optional<User> findByUsername(String username);
//    void deleteByUsername(String username);
//    boolean existsByUsername(String email);
}
