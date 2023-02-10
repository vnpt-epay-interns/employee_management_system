package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.LoginRequest;
import com.example.Employee_Management_System.dto.request.RegisterRequest;
import com.example.Employee_Management_System.dto.response.JwtToken;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.repository.UserRepository;
import com.example.Employee_Management_System.service.AuthService;
import com.example.Employee_Management_System.service.EmployeeService;
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
    private final EmployeeService employeeService;

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

    @Override
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
                        .build()
        );

    }
}
