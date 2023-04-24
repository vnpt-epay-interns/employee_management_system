package com.example.Employee_Management_System.service;


import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.UpdateProfileRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.response.UserInformation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    User getUserByEmail(String email);

    UserInformation getUserInfo(User currentUser);

    UserInformation updateUserInfo(User user, UpdateProfileRequest updateProfileRequest);

    UserInformation unlockUser(Long id);

    ResponseEntity<Response> getAllManagerUnverified();

    UserInformation changeAvatar(User currentUser, MultipartFile file);
}
