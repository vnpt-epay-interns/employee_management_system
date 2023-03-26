package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.CheckEmailExistRequest;
import com.example.Employee_Management_System.dto.request.GoogleLoginRequest;
import com.example.Employee_Management_System.dto.request.LoginRequest;
import com.example.Employee_Management_System.dto.request.RegisterRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.response.UserInformation;
import com.example.Employee_Management_System.model.EmployeeInformation;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;


public interface AuthService {
    ResponseEntity<Response> register(RegisterRequest registerRequest) throws MessagingException, UnsupportedEncodingException;

    ResponseEntity<Response> formLogin(LoginRequest loginRequest);

    ResponseEntity<Response> selectRoleManager(User user);

    UserInformation selectRoleEmployee(User user, String referenceCode);

    UserInformation verify(String code);

    ResponseEntity<Response> existsEmail(CheckEmailExistRequest request);

    ResponseEntity<Response> getManagerInfo(String referencedCode);

    ResponseEntity<Response> googleLogin(GoogleLoginRequest loginRequest);
}
