package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.UpdateProfileRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.response.UserInformation;
import com.example.Employee_Management_System.repository.UserRepository;
import com.example.Employee_Management_System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public ResponseEntity<Response> updateUserInfo(User user, UpdateProfileRequest updateProfileRequest) {
        user.setFirstName(updateProfileRequest.getFirstName());
        user.setLastName(updateProfileRequest.getLastName());
        userRepository.updateName(user);
        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Update user information successfully")
                        .build()
        );
    }

    @Override
    public ResponseEntity<Response> unlockUser(Long id) {
        User user = userRepository.findUserById(id);
        if (!user.isLocked) {
            throw new IllegalStateException("Manager already approved");
        }
        userRepository.unlockUser(id);
        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Unlock user successfully")
                        .build()
        );
    }

    @Override
    public ResponseEntity<Response> getAllManagerUnverified() {
        List<User> users = userRepository.findAllManagerUnverified();
        List<UserInformation> usersInfo = users.stream().map(UserInformation::new).toList();
        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Get all manager unverified successfully")
                        .data(usersInfo)
                        .build()
        );
    }

    @Override
    public ResponseEntity<Response> changeAvatar(User currentUser, MultipartFile file) {
        System.out.println("change avatar");
        System.out.println(file.getOriginalFilename());
        return null;
    }
}
