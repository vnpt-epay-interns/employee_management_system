package com.example.Employee_Management_System.controller;

import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.LoginRequest;
import com.example.Employee_Management_System.dto.request.RegisterRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
   
    private final AuthService authService;

    @PostMapping(value = "/register-account", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
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

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
