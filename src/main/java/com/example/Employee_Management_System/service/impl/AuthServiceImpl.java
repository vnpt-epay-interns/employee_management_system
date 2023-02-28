package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.CheckEmailExistRequest;
import com.example.Employee_Management_System.dto.request.LoginRequest;
import com.example.Employee_Management_System.dto.request.RegisterRequest;
import com.example.Employee_Management_System.dto.response.LoginResponse;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.exception.NotFoundException;
import com.example.Employee_Management_System.exception.RegisterException;
import com.example.Employee_Management_System.repository.UserRepository;
import com.example.Employee_Management_System.service.*;
import com.example.Employee_Management_System.utils.AvatarLinkCreator;
import com.example.Employee_Management_System.utils.HtmlMailVerifiedCreator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmployeeService employeeService;
    private final ManagerService managerService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<Response> register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RegisterException("User already exists");
        }
        User user = User
                .builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .avatar(AvatarLinkCreator.createAvatarLink(registerRequest.getFirstName(), registerRequest.getLastName()))
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .isLocked(true)
                .build();


        String code = generateVerifyEmailCode();
        user.setVerificationCode(code);
        userRepository.save(user);
        sendVerificationEmail(user, String.format("http://127.0.0.1:8080/api/auth/verify/%s", code));

        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(200)
                        .message("Register successfully!")
                        .build()
        );
    }

    public ResponseEntity<Response> login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        User user = userRepository
                .findByUsername(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
        String jwtToken = jwtService.generateToken(user);
        LoginResponse token = LoginResponse
                .builder()
                .token(jwtToken)
                .role(user.getRole())
                .build();
        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(200)
                        .message("Login successfully!")
                        .data(token)
                        .build()
        );
    }

    @Override
    public ResponseEntity<Response> registerManager(User user) {
        if (user.getRole() != null){
            throw new RegisterException("Account already has a role");
        }
        Manager manager = Manager
                .builder().id(user.getId())
                .referencedCode(generateReferenceCode().toString())
                .build();

        user.setRole("MANAGER");
        user.setLocked(false);
        user.setRole("MANAGER");
        userRepository.update(user);

        managerService.save(manager);

        return ResponseEntity.ok(
                Response
                .builder()
                .status(200)
                .message("Register manager successfully!")
                .data(null)
                .build()

        );
    }

    public ResponseEntity<Response> registerEmployee(User user, String referenceCode) {
        if (user.getRole() != null) {
            throw new RegisterException("Account already has a role");
        }

        if (referenceCode == null) {
            throw new RegisterException("Reference code is required");
        }

        Manager manager = userRepository.findManagerByReferenceCode(referenceCode);
        if (manager == null) {
            throw new RegisterException("Reference code is invalid");
        }

        user.setRole("EMPLOYEE");
        user.setLocked(false);
        // save user to user table
        userRepository.update(user);

        Employee employee = Employee
                .builder()
                .id(user.getId())
                .managerId(manager.getId())
                .build();

        // save employee to employee table
        employeeService.save(employee);
        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(200)
                        .message("Register employee successfully!")
                        .data(null)
                        .build()
        );
    }

    private UUID generateReferenceCode() {
        return UUID.randomUUID();
    }

    private void sendVerificationEmail(User user, String siteUrl) {
        String toAddress = user.getEmail();
        String subject = "Please verify your registration";
        String content;

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            String sb = siteUrl;
            content = HtmlMailVerifiedCreator.generateHTML(user.getFirstName(), sb);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<Response> verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
        if (user == null) {
            return ResponseEntity.ok().body(
                    Response.builder()
                            .status(400)
                            .message("Wrong verification code")
                            .data(verificationCode)
                            .build()
            );
        } else {
            user.setVerificationCode(null);
            user.setLocked(false);
            userRepository.save(user);
            return ResponseEntity.ok().body(
                    Response.builder()
                            .status(200)
                            .message("Right verification code")
                            .build()
            );
        }
    }

    @Override
    public ResponseEntity<Response> existsEmail(CheckEmailExistRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.ok(
                    Response
                            .builder()
                            .status(200)
                            .message("Email already exists")
                            .build()
            );
        } else {
            return ResponseEntity.ok(
                    Response
                            .builder()
                            .status(200)
                            .message("Email not exists")
                            .build()
            );
        }
    }

    private String generateVerifyEmailCode() {
        return UUID.randomUUID().toString();
    }
}
