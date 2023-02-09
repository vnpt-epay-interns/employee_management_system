package com.example.Employee_Management_System.dto.response;

import lombok.Data;

@Data
public class Response {

    private int status;
    private String message;
    private Object data;

}
