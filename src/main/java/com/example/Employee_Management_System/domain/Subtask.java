package com.example.Employee_Management_System.domain;

import java.io.Serializable;

public class Subtask implements Serializable {
    private Long id;
    private String title;
    private String description;
    private boolean isCompleted;
    private Long employeeId;
    private Long parentId;
}
