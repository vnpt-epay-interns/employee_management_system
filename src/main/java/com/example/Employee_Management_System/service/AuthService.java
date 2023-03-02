package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.CheckEmailExistRequest;
import com.example.Employee_Management_System.dto.request.LoginRequest;
import com.example.Employee_Management_System.dto.request.RegisterRequest;
import com.example.Employee_Management_System.dto.response.Response;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;


public interface AuthService {
    ResponseEntity<Response> register(RegisterRequest registerRequest) throws MessagingException, UnsupportedEncodingException;


    ResponseEntity<Response> login(LoginRequest loginRequest);

    ResponseEntity<Response> registerManager(User user);

    ResponseEntity<Response> registerEmployee(User user, String referenceCode);

    ResponseEntity<Response> verify(String code);

    ResponseEntity<Response> existsEmail(CheckEmailExistRequest request);
    ResponseEntity<Response> getManagerInfo(String referencedCode);

}
