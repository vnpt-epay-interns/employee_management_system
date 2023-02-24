package com.example.Employee_Management_System.service;


import com.example.Employee_Management_System.domain.User;

public interface UserService {

    User getUserByEmail(String email);
}
