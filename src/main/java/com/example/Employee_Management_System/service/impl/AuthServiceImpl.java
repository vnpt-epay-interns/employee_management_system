package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.CheckEmailExistRequest;
import com.example.Employee_Management_System.dto.request.GoogleLoginRequest;
import com.example.Employee_Management_System.dto.request.LoginRequest;
import com.example.Employee_Management_System.dto.request.RegisterRequest;
import com.example.Employee_Management_System.dto.response.LoginResponse;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.response.UserInformation;
import com.example.Employee_Management_System.enums.RegistrationMethod;
import com.example.Employee_Management_System.exception.LoginFailedException;
import com.example.Employee_Management_System.exception.NotFoundException;
import com.example.Employee_Management_System.exception.RegisterException;
import com.example.Employee_Management_System.model.EmployeeInformation;
import com.example.Employee_Management_System.model.GoogleUserInfo;
import com.example.Employee_Management_System.model.ManagerInformation;
import com.example.Employee_Management_System.repository.UserRepository;
import com.example.Employee_Management_System.service.*;
import com.example.Employee_Management_System.utils.AvatarLinkCreator;
import com.example.Employee_Management_System.utils.GoogleAPIHelper;
import com.example.Employee_Management_System.utils.HtmlMailVerifiedCreator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
    private final RedisTemplate<String, Object> redisTemplate;

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
                .registrationMethod(RegistrationMethod.FORM.toString())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .isLocked(true)
                .build();


        String code = generateVerifyEmailCode();
        user.setVerificationCode(code);

        userRepository.save(user);

        // Send verification email asynchronously using CompletableFuture
        CompletableFuture.runAsync(() -> sendVerificationEmail(user, code));

        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(200)
                        .message("Register successfully!")
                        .build()
        );
    }

    public ResponseEntity<Response> formLogin(LoginRequest loginRequest) {
        if (!userRepository.existsByEmail(loginRequest.getEmail())) {
            throw new LoginFailedException("Email is not registered!");
        }

        // when user try to login by form for the email that was registered by other method
        User user = getUserByEmail(loginRequest.getEmail());
        if (!user.getRegistrationMethod().equals(RegistrationMethod.FORM.toString())) {
            throw new LoginFailedException("Email was registered by other method");
        }

        // wrong password
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new LoginFailedException("Wrong password!");
        }



        LoginResponse response = generateAccessTokenAndCreateLoginResponse(user);

        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(200)
                        .message("Login successfully!")
                        .data(response)
                        .build()
        );
    }

    private LoginResponse generateAccessTokenAndCreateLoginResponse(User user) {
        String jwtToken = jwtService.generateToken(user);
        LoginResponse token = LoginResponse
                .builder()
                .token(jwtToken)
                .build();
        return token;
    }

    private User getUserByEmail(String email) {
        User user = userRepository
                .findByUsername(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return user;
    }

    @Override
    public ResponseEntity<Response> selectRoleManager(User user) {
        if (user.getRole() != null){
            throw new RegisterException("Account already has a role");
        }
        Manager manager = Manager
                .builder().id(user.getId())
                .referencedCode(generateReferenceCode().toString())
                .build();

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

    @Override
    public EmployeeInformation selectRoleEmployee(User user, String referenceCode) {
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

        EmployeeInformation employeeInfo = new EmployeeInformation(user.id, user.firstName, user.lastName, user.email, user.avatar);

        List<EmployeeInformation> employees = (List<EmployeeInformation>) redisTemplate.opsForValue().get("employees::" + manager.getId());
        if (employees != null) {
            employees.add(employeeInfo);
            redisTemplate.opsForValue().set("employees::" + manager.getId(), employees);
        }

        return employeeInfo;
    }

    private UUID generateReferenceCode() {
        return UUID.randomUUID();
    }

    private void sendVerificationEmail(User user, String code) {
        String toAddress = user.getEmail();
        String subject = "Please verify your registration";
        String content;

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            content = HtmlMailVerifiedCreator.generateHTML(user.getFirstName(), code);
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
                            .message("Invalid verification code")
                            .data(verificationCode)
                            .build()
            );
        } else {

            user.setVerificationCode(null);
            userRepository.updateVerificationCode(user);
            userRepository.update(user);
            return ResponseEntity.ok().body(
                    Response.builder()
                            .status(200)
                            .message("Verify successfully")
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
                            .status(400)
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

    @Override
    public ResponseEntity<Response> getManagerInfo(String referencedCode) {
        ManagerInformation managerInformation =
        managerService.getManagerInfo(referencedCode)
                        .orElseThrow(() -> new NotFoundException("Invalid manager code"));

        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Get manager info successfully")
                        .data(managerInformation)
                        .build()
        );
    }

    @Override
    public ResponseEntity<Response> googleLogin(GoogleLoginRequest loginRequest) {

        GoogleUserInfo googleUserInfo = GoogleAPIHelper.getUserInfo(loginRequest.getAuthorizationCode());

        String email = googleUserInfo.getEmail();
        User user = null;

        if (userRepository.existsByEmail(email)) {
            // if the email exists, it means the user has already registered
            // so we just need to login
            user = getUserByEmail(email);

            // if the user has registered by form, but login with google, we need to throw an exception
            if (!user.getRegistrationMethod().equals(RegistrationMethod.GOOGLE.toString())) {
                throw new LoginFailedException("Email was registered by other method");
            }


        } else {
            // if the email does not exist, it means the user has not registered yet
            // so we need to register the user first
            user = User.builder()
                    .email(email)
                    .firstName(googleUserInfo.getFirstName())
                    .lastName(googleUserInfo.getLastName())
                    .avatar(googleUserInfo.getAvatar())
                    .verificationCode(null) // verified code is null, meaning this user has already verified, no need to verify email again
                    .isLocked(true)
                    .registrationMethod(RegistrationMethod.GOOGLE.toString())
                    .build();

            userRepository.save(user);
        }

        // generate access token
        LoginResponse response = generateAccessTokenAndCreateLoginResponse(user);

        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Login successfully")
                        .data(response)
                        .build()
        );

    }

    private String generateVerifyEmailCode() {
        return UUID.randomUUID().toString();
    }
}
