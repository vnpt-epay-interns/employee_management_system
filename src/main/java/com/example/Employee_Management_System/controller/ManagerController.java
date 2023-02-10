package com.example.Employee_Management_System.controller;

import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    @PostMapping("/tasks/create")
    public ResponseEntity<Response> createTask(@RequestBody CreateTaskRequest request) {
        return managerService.createTask(request);
    }

    @DeleteMapping("/tasks/delete/{taskId}")
    public ResponseEntity<Response> deleteTask(@PathVariable long taskId) {
        return managerService.deleteTask(taskId);
    }

    @PutMapping("/tasks/update/{taskId}")
    public ResponseEntity<Response> updateTask(@PathVariable long taskId, @RequestBody UpdateTaskRequest updateTaskRequest) {
        return managerService.updateTask(taskId, updateTaskRequest);
    }

    @GetMapping("/reports")
    public ResponseEntity<Response> getReports() {
        return managerService.getAllReports();
    }

    @GetMapping("/reports/{reportId}")
    public ResponseEntity<Response> getReportByReportId(@PathVariable long reportId) {
        return managerService.getReportById(reportId);
    }

    @GetMapping("/reports/task/{taskId}")
    public ResponseEntity<Response> viewReportsByTaskId(@PathVariable long taskId) {
        return managerService.getReportsByTaskId(taskId);
    }

    @GetMapping("/reports/employee/{employeeId}")
    public ResponseEntity<Response> getReportsByEmployeeId(@PathVariable long employeeId) {
        return managerService.getReportEmployeeId(employeeId);
    }

    @PostMapping("/working-schedule/{monthNumber}")
    public ResponseEntity<Response> viewWorkingSchedule(@PathVariable long monthNumber) {
        return managerService.getWorkingSchedule(monthNumber);
    }




}
