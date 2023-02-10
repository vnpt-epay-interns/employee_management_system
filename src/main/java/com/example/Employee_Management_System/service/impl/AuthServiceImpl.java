package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.LoginRequest;
import com.example.Employee_Management_System.dto.request.RegisterRequest;
import com.example.Employee_Management_System.dto.response.JwtToken;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.repository.UserRepository;
import com.example.Employee_Management_System.service.AuthService;
import com.example.Employee_Management_System.service.JwtService;
import com.example.Employee_Management_System.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<Response> register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getEmail())) {
            // TODO: throw custom exception: RegisterException
            throw new RuntimeException("User already exists");
        }

        User user = User
                .builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .isLocked(false)
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        JwtToken token = JwtToken
                .builder()
                .token(jwtToken)
                .build();

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
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
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
}
