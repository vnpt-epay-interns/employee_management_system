package com.example.Employee_Management_System.controller;

import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private UserService userService;

    private ResponseEntity<Response> unlockUser(User user) {
        return userService.unlockUser(user);
    }
}
