package com.example.Employee_Management_System.controller;

import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.ScheduleWorkingDayRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskEmployeeRequest;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.dto.response.Response;
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
    public ResponseEntity<Response> getTaskById(@PathVariable long id) {
        User employee = getCurrentEmployee();
        return employeeService.getTaskById(id, employee);
    }

    @GetMapping("/get-tasks")
    public ResponseEntity<Response> viewTask() {
        User employee = getCurrentEmployee();
        return employeeService.getTasks(employee);
    }


    @PutMapping("/update-task/{taskId}")
    public ResponseEntity<Response> updateTask(@PathVariable("taskId") Long taskId, @RequestBody UpdateTaskEmployeeRequest request) {
        User employee = getCurrentEmployee();
        return employeeService.updateTask(employee, taskId, request);
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

    public User getCurrentEmployee() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
