package com.example.Employee_Management_System.repository;

import com.example.Employee_Management_System.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
    // ToDo: save, update, find, delete, paging
    User save(User user);
    User modify(User user);
    Optional<User> findByUsername(String username);
    void deleteByUsername(String username);
    boolean existsByUsername(String email);
}
