package com.example.Employee_Management_System.controller;

import  com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.request.ScheduleWorkingDayRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskEmployeeRequest;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
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

    @GetMapping("/get-all-subtasks/{taskId}")
    public ResponseEntity<Response> viewSubTasks(@PathVariable long taskId) {
        User employee = getCurrentEmployee();
        return employeeService.getSubTasks(employee, taskId);
    }

    @PostMapping("/create-task")
    public ResponseEntity<Response> createTask(@RequestBody CreateTaskRequest request) {
        User employee = getCurrentEmployee();
        return employeeService.createTask(employee, request);
    }

    @GetMapping("/get-all-project-names")
    public ResponseEntity<Response> getAllProjects() {
        User employee = getCurrentEmployee();
        return employeeService.getAllProjectsByManagerId(employee);
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

    @GetMapping("/reports")
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

    @GetMapping("/get-referenced-code")
    public ResponseEntity<Response> getReferenceCode() {
        User employee = getCurrentEmployee();
        return employeeService.getReferenceCode(employee);
    }
}
