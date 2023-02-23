package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.LoginRequest;
import com.example.Employee_Management_System.dto.request.RegisterRequest;
import com.example.Employee_Management_System.dto.response.JwtToken;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.repository.UserRepository;
import com.example.Employee_Management_System.service.*;
import com.example.Employee_Management_System.utils.HtmlMailVerifiedCreator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmployeeService employeeService;
    private final ManagerService managerService;

    public ResponseEntity<Response> register(RegisterRequest registerRequest) throws MessagingException, UnsupportedEncodingException {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            // TODO: throw custom exception: RegisterException
            throw new RuntimeException("User already exists");
        }

        User user = User
                .builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .isLocked(true)
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        JwtToken token = JwtToken
                .builder()
                .token(jwtToken)
                .build();
        String code = generateCode(user);
        sendVerificationEmail(user, String.format("localhost:8080/verify/%s", code));
        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(200)
                        .message("Register successfully!")
                        .data(token)
                        .build()
        );
    }

    public ResponseEntity<Response> login(LoginRequest loginRequest) {
        Authentication authencation = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        if (authencation.isAuthenticated()) {

        }
        User user = userRepository
                .findByUsername(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String jwtToken = jwtService.generateToken(user);
        JwtToken token = JwtToken
                .builder()
                .token(jwtToken)
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
        //TODO: throw custom exception: RegisterException
        if (user.getRole() != null){
            throw new RuntimeException("Account already has a role");
        }
        Manager manager = Manager
                .builder().id(user.getId())
                .referencedCode(generateReferenceCode().toString())
                .build();
        user.setLocked(false);
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
            // TODO: throw custom Exception RegisterException
            throw new RuntimeException("Account already has a role");
        }

        if (referenceCode == null) {
            // TODO: throw custom Exception RegisterException
            throw new RuntimeException("Reference code is required");
        }

        Manager manager = userRepository.findManagerByReferenceCode(referenceCode);
        if (manager == null) {
            throw new RuntimeException("Reference code is invalid");
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

    private void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "Your email address";
        String senderName = "Your company name";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<a href='localhost:8080/verify' target=\"_blank\">VERIFY</a>"
                + "Thank you,<br>"
                + "Your company name.";
//
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//
//
//
//        helper.setFrom(fromAddress, senderName);
//        helper.setTo(toAddress);
//        helper.setSubject(subject);
//
//        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
//
//        content = content.replace("[[URL]]", verifyURL);
//
//        helper.setText(content, true);
//
//        mailSender.send(message);



        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
//            content = content.replace("[[name]]", user.getUsername());
            String link = String.format("localhost:8080/verify/%s", generateCode(user));
            content = HtmlMailVerifiedCreator.generateHTML(user.getFirstName(), link);
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
        if (user == null || user.isEnabled()) {
            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .status(400)
                            .message("Wrong verification code")
                            .build()
            );
        } else {
            user.setVerificationCode(null);
            user.setLocked(true);
            userRepository.save(user);
            return ResponseEntity.ok().body(
                    Response.builder()
                            .status(200)
                            .message("Right verification code")
                            .build()
            );
        }
    }

    private String generateCode(User user) {
        String randomCode = UUID.randomUUID().toString();
        user.setVerificationCode(randomCode);
        user.setLocked(false);
        return randomCode;
    }

}
