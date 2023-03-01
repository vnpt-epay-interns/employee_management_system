package com.example.Employee_Management_System.controller;

import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.CheckEmailExistRequest;
import com.example.Employee_Management_System.dto.request.LoginRequest;
import com.example.Employee_Management_System.dto.request.RegisterRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.service.AuthService;
import com.example.Employee_Management_System.service.UserService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge=3600)
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register-account")
    public ResponseEntity<Response> register(@RequestBody RegisterRequest registerRequest) throws UnsupportedEncodingException, MessagingException {
        return authService.register(registerRequest);
    }

    @PostMapping("/exists-email")
    public ResponseEntity<Response> existsEmail(@RequestBody CheckEmailExistRequest request) {
        return authService.existsEmail(request);
    }

    @GetMapping("/verify/{code}")
    public ResponseEntity<Response> verify(@PathVariable String code) {
        return authService.verify(code);
    }


    @PostMapping( "/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @GetMapping("/get-manager-info/{referencedCode}")
    public ResponseEntity<Response> getManagerInfo(@PathVariable String referencedCode) {
        return authService.getManagerInfo(referencedCode);
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/register-account/manager")
    public ResponseEntity<Response> registerManager() {
        return authService.registerManager(getCurrentUser());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/register-account/employee/{referenceCode}")
    public ResponseEntity<Response> registerEmployee(@PathVariable String referenceCode) {
        User user = getCurrentUser();
        return authService.registerEmployee(user, referenceCode);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user-info")
    public ResponseEntity<Response> userInfo() {
        return userService.getUserInfo(getCurrentUser());
    }

    private User getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return user;
    }
}
