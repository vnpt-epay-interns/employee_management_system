package com.example.Employee_Management_System.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    private int status;
    private String message;
    private Object data;
}
