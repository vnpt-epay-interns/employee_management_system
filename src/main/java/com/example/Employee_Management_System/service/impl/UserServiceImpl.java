package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.response.UserInformation;
import com.example.Employee_Management_System.repository.UserRepository;
import com.example.Employee_Management_System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserByEmail(String email) {
        //TODO : custom exception: NOT FOUND EXCPETION
        return userRepository.findByUsername(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public ResponseEntity<Response> getUserByEmployeeId(Long employeeId) {
        User user = userRepository.findUserById(employeeId);
        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(200)
                        .message("Successfully")
                        .data(user)
                        .build()
        );
    }

    @Override
    public ResponseEntity<Response> getUserInfo(User currentUser) {
        // the current user contains all the information of the user, no need to query the database
        UserInformation userInformation = new UserInformation(currentUser);
        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Get user information successfully")
                        .data(userInformation)
                        .build()
        );
    }
}
