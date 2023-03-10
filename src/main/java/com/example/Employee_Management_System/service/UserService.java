package com.example.Employee_Management_System.service;


import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.UpdateProfileRequest;
import com.example.Employee_Management_System.dto.response.Response;
import org.springframework.http.ResponseEntity;

public interface UserService {

    User getUserByEmail(String email);
    ResponseEntity<Response> getUserInfo(User currentUser);

    ResponseEntity<Response> updateUserInfo(User user, UpdateProfileRequest updateProfileRequest);

    ResponseEntity<Response> unlockUser(Long id);

    ResponseEntity<Response> getAllManagerUnverified();

    ResponseEntity<Response> uploadAvatar(User user, String avatar);
}
