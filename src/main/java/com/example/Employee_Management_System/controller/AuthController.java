package com.example.Employee_Management_System.controller;

import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.LoginRequest;
import com.example.Employee_Management_System.dto.request.RegisterRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.service.AuthService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    @Autowired
    private final AuthService authService;

    @PostMapping("/register-account")
    public ResponseEntity<Response> register(@RequestBody RegisterRequest registerRequest) throws UnsupportedEncodingException, MessagingException {
        return authService.register(registerRequest);
    }

    @PostMapping( "/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }



    @PostMapping("/register-account/manager")
    public ResponseEntity<Response> registerManager() {
        return authService.registerManager(getCurrentUser());
    }

    @PostMapping("/register-account/employee/{referenceCode}")
    public ResponseEntity<Response> registerEmployee(@PathVariable String referenceCode) {
        User user = getCurrentUser();
        return authService.registerEmployee(user, referenceCode);
    }

    @GetMapping("/verify/{code}")
    public ResponseEntity<Response> verify(@PathVariable String code) {
        return authService.verify(code);
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
