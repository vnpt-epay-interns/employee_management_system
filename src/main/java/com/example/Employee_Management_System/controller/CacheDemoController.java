package com.example.Employee_Management_System.controller;

import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.UpdateTaskEmployeeRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.response.TaskDTO;
import com.example.Employee_Management_System.service.impl.EmployeeServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cache-demo")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
public class CacheDemoController {

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;


    @GetMapping("/get-task/{id}")
    public Task getTaskById(@PathVariable long id) {
        User employee = getCurrentEmployee();
        return employeeServiceImpl.getTaskByIdCaching(id, employee);
    }


    @GetMapping("/get-all-tasks")
    public List<TaskDTO> viewTasks() {
        User employee = getCurrentEmployee();
        return employeeServiceImpl.getAllTasksCaching(employee);
    }

    @PutMapping("/update-task/{taskId}")
    public Task updateTask(@PathVariable("taskId") Long taskId, @RequestBody UpdateTaskEmployeeRequest request) {
        User employee = getCurrentEmployee();
        return employeeServiceImpl.updateTaskCaching(employee, taskId, request);
    }

    public User getCurrentEmployee() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
