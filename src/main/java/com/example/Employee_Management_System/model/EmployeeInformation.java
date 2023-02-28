package com.example.Employee_Management_System.model;

import lombok.Data;

@Data
public class EmployeeInformation {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;
}
