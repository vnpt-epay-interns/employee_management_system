package com.example.Employee_Management_System.service.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.UpdateProfileRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.response.UserInformation;
import com.example.Employee_Management_System.repository.UserRepository;
import com.example.Employee_Management_System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Value("${amazon.s3.default-bucket}")
    private String bucketName;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AmazonS3 s3client;

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
        try {
            String link = uploadFile(file);
            currentUser.setAvatar(link);
            userRepository.update(currentUser);

            return ResponseEntity.ok(
                    Response.builder()
                            .status(200)
                            .message("Upload avatar successfully")
                            .build()
            );
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }

    public ResponseEntity<Response> uploadAvatar (User user, String avatar){
        user.setAvatar(avatar);
        userRepository.update(user);
        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Error when upload avatar")
                        .build()
        );
    }


    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        s3client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata).withCannedAcl(CannedAccessControlList.PublicRead));
        return s3client.getUrl(bucketName, fileName).toString();
    }
}
