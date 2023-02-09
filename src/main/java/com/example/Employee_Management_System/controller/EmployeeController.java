package com.example.Employee_Management_System.controller;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/get-task/{id}")
    public ResponseEntity<Response> getTaskById(@PathVariable("id") long id) {
        Employee employee = getCurrentEmployee();
        return employeeService.getTaskById(id, employee);
    }

    @GetMapping("/get-tasks")
    public ResponseEntity<Response> viewTask() {
        Employee employee = getCurrentEmployee();
        return employeeService.getTasks(employee);
    }


    @PutMapping("/update-task")
    public ResponseEntity<Response> updateTask(@RequestBody UpdateTaskRequest request) {
        Employee employee = getCurrentEmployee();
        return employeeService.updateTask(employee, request);
    }

    @PostMapping("/write-report")
    public ResponseEntity<Response> writeReport(@RequestBody WriteReportRequest request) {
        Employee employee = getCurrentEmployee();
        return employeeService.writeReport(employee, request);
    }

}
