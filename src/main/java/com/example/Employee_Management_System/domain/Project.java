package com.example.Employee_Management_System.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
public class Project {
    private Long id;
    private String name;
    private Long managerId;
}
