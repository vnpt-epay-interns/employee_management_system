package com.example.Employee_Management_System.controller;

import com.example.Employee_Management_System.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/auth")
public class AuthController {
    private AuthService authService;
}
