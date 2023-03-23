package com.example.Employee_Management_System.controller;

import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.CheckEmailExistRequest;
import com.example.Employee_Management_System.dto.request.GoogleLoginRequest;
import com.example.Employee_Management_System.dto.request.LoginRequest;
import com.example.Employee_Management_System.dto.request.RegisterRequest;
import com.example.Employee_Management_System.dto.request.UpdateProfileRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.service.AuthService;
import com.example.Employee_Management_System.service.UserService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
        return ResponseEntity.ok(
                Response.builder()
                        .data(authService.verify(code))
                        .status(200)
                        .message("Verify successfully!")
                        .build());
    }


    @PostMapping( "/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        return authService.formLogin(loginRequest);
    }

    @GetMapping("/get-manager-info/{referencedCode}")
    public ResponseEntity<Response> getManagerInfo(@PathVariable String referencedCode) {
        return authService.getManagerInfo(referencedCode);
    }

    @PostMapping("/google-login")
    public ResponseEntity<Response> googleLogin(@RequestBody GoogleLoginRequest loginRequest) {
        return authService.googleLogin(loginRequest);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/register-account/manager")
    public ResponseEntity<Response> registerManager() {
        return authService.selectRoleManager(getCurrentUser());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/register-account/employee/{referenceCode}")
    public ResponseEntity<Response> registerEmployee(@PathVariable String referenceCode) {
        User user = getCurrentUser();
        return ResponseEntity.ok(
                Response.builder()
                        .data(authService.selectRoleEmployee(user, referenceCode))
                        .status(200)
                        .message("Register employee successfully!")
                        .build());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user-info")
    public ResponseEntity<Response> userInfo() {
        return ResponseEntity.ok(
                Response.builder()
                        .data(userService.getUserInfo(getCurrentUser()))
                        .status(200)
                        .message("Get user information successfully!")
                        .build());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-avatar")
    public ResponseEntity<Response> changeAvatar(@RequestParam("file") MultipartFile file) {
        User user = getCurrentUser();
        return ResponseEntity.ok(
                Response.builder()
                        .data(userService.changeAvatar(user, file))
                        .status(200)
                        .message("Upload avatar successfully")
                        .build());
    }

    private User getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return user;
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/update-user-info")
    public ResponseEntity<Response> updateUserInfo(@RequestBody UpdateProfileRequest updateProfileRequest) {
        User user = getCurrentUser();
        return ResponseEntity.ok(
                Response.builder()
                        .data(userService.updateUserInfo(user, updateProfileRequest))
                        .status(200)
                        .message("Update user information successfully")
                        .build()
        );
    }
}
