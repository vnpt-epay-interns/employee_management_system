package com.example.Employee_Management_System.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Project {
    private Long id;
    private String name;
    private Long managerId;
}
