package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.LoginRequest;
import com.example.Employee_Management_System.dto.request.RegisterRequest;
import com.example.Employee_Management_System.dto.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


public interface AuthService {
    ResponseEntity<Response> register(RegisterRequest registerRequest);

    ResponseEntity<Response> login(LoginRequest loginRequest);


    ResponseEntity<Response> registerManager(User user);

    ResponseEntity<Response> registerEmployee(User user, String referenceCode);
}
