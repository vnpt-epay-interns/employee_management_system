package com.example.Employee_Management_System.controller;

import  com.example.Employee_Management_System.domain.User;
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
@CrossOrigin(origins = "*", maxAge=3600)
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/get-task/{id}")
    public ResponseEntity<Response> getTaskById(@PathVariable long id) {
        User employee = getCurrentEmployee();
        return employeeService.getTaskById(id, employee);
    }

    @GetMapping("/get-all-tasks")
    public ResponseEntity<Response> viewTasks() {
        User employee = getCurrentEmployee();
        return employeeService.getTasks(employee);
    }


    @PutMapping("/update-task/{taskId}")
    public ResponseEntity<Response> updateTask(@PathVariable("taskId") Long taskId, @RequestBody UpdateTaskEmployeeRequest request) {
        User employee = getCurrentEmployee();
        return employeeService.updateTask(employee, taskId, request);
    }

    @PostMapping("write-report/task/{taskId}")
    public ResponseEntity<Response> writeReportForTask(@PathVariable("taskId") Long taskId, @RequestBody WriteReportRequest request) {
        User employee = getCurrentEmployee();
        return employeeService.writeReportForTask(employee, taskId, request);
    }

    @PostMapping("/write-report")
    public ResponseEntity<Response> writeReport(@RequestBody WriteReportRequest request) {
        User employee = getCurrentEmployee();
        return employeeService.writeReport(employee, request);
    }

    @GetMapping("/get-all-reports")
    public ResponseEntity<Response> viewReports() {
        User employee = getCurrentEmployee();
        return employeeService.getReports(employee);
    }

    @PostMapping("/working-schedules")
    public ResponseEntity<Response> scheduleWorkingDay(@RequestBody ScheduleWorkingDayRequest request) {
        User employee = getCurrentEmployee();
        return employeeService.scheduleWorkingDay(employee, request);
    }

    @GetMapping("/working-schedules/{year}/{monthNumber}")
    public ResponseEntity<Response> getWorkingDays(@PathVariable int year, @PathVariable int monthNumber) {
        User employee = getCurrentEmployee();
        return employeeService.getWorkingSchedule(employee, year, monthNumber);
    }

    public User getCurrentEmployee() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
