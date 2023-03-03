package com.example.Employee_Management_System.service;


import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.response.Response;
import org.springframework.http.ResponseEntity;

public interface UserService {

    User getUserByEmail(String email);
    ResponseEntity<Response> getUserInfo(User currentUser);
}
