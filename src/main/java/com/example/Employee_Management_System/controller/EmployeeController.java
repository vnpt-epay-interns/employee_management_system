package com.example.Employee_Management_System.controller;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.ScheduleWorkingDayRequest;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/get-task/{id}")
    public ResponseEntity<Response> getTaskById(@PathVariable("id") long id) {
        User employee = getCurrentEmployee();
        return employeeService.getTaskById(id, employee);
    }

    @GetMapping("/get-tasks")
    public ResponseEntity<Response> viewTask() {
        User employee = getCurrentEmployee();
        return employeeService.getTasks(employee);
    }


    @PutMapping("/update-task")
    public ResponseEntity<Response> updateTask(@RequestBody UpdateTaskRequest request) {
        User employee = getCurrentEmployee();
        return employeeService.updateTask(employee, request);
    }

    @PostMapping("/write-report")
    public ResponseEntity<Response> writeReport(@RequestBody WriteReportRequest request) {
        User employee = getCurrentEmployee();
        return employeeService.writeReport(employee, request);
    }

    @PostMapping("/schedule-working-day")
    public ResponseEntity<Response> scheduleWorkingDay(@RequestBody ScheduleWorkingDayRequest request) {
        User employee = getCurrentEmployee();
        return employeeService.scheduleWorkingDay(employee, request);
    }

    @GetMapping("/get-working-days")
    public ResponseEntity<Response> getWorkingDays() {
        User employee = getCurrentEmployee();
        return employeeService.getWorkingDays(employee);
    }

    public User getCurrentEmployee() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }


}
