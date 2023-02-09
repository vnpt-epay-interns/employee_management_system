package com.example.Employee_Management_System.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BeanConfig {
    @Bean
    public PasswordEncoder passwordManager() {
        return new BCryptPasswordEncoder();
    }
}
