package com.example.Employee_Management_System.controller;

import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
public class AdminController {

    private UserService userService;

    @PutMapping("/verify/{id}")
    public ResponseEntity<Response> unlockUser(@PathVariable Long id) {
        return userService.unlockUser(id);
    }

    @GetMapping("/get-all-manager-unverified")
    public ResponseEntity<Response> getAllManagerUnverified() {
        return userService.getAllManagerUnverified();
    }

}
