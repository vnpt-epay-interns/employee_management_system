package com.example.Employee_Management_System.controller;

import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.service.EmployeeService;
import com.example.Employee_Management_System.service.impl.EmployeeServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cache-demo")
@CrossOrigin(origins = "*", maxAge=3600)
@AllArgsConstructor
public class CacheDemoController {

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;


    @GetMapping("/get-task/{id}")
    public Task getTaskById(@PathVariable long id) {
        User employee = getCurrentEmployee();
        return employeeServiceImpl.getTaskByIdTest(id, employee);
    }



    public User getCurrentEmployee() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
