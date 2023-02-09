package com.example.Employee_Management_System.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtToken {
    private String token;
}