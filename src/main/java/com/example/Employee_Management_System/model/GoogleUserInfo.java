package com.example.Employee_Management_System.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoogleUserInfo {
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;
}