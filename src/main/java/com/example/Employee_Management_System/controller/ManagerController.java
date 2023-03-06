package com.example.Employee_Management_System.controller;

import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.service.ManagerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/manager")
@CrossOrigin(origins = "*", maxAge=3600)
@AllArgsConstructor
public class ManagerController {
    private final ManagerService managerService;

    @PostMapping("/create-task")
    public ResponseEntity<Response> createTask(@RequestBody CreateTaskRequest request) {
        return managerService.createTask(request);
    }

    @DeleteMapping("/delete-task/{taskId}")
    public ResponseEntity<Response> deleteTask(@PathVariable long taskId) {
        return managerService.deleteTask(taskId);
    }

    @PutMapping("/update-task/{taskId}")
    public ResponseEntity<Response> updateTask(@PathVariable long taskId, @RequestBody UpdateTaskRequest updateTaskRequest) {
        return managerService.updateTask(taskId, updateTaskRequest);
    }

    @GetMapping("/get-all-tasks")
    public ResponseEntity<Response> getAllTasks() {
        User manager = getCurrentManager();
        return managerService.getAllTasks(manager);
    }

    @GetMapping("/reports")
    public ResponseEntity<Response> getReports() {
        User manager = getCurrentManager();
        return managerService.getAllReports(manager);
    }

    @GetMapping("/reports/{reportId}")
    public ResponseEntity<Response> getReportByReportId(@PathVariable long reportId) {
        User manager = getCurrentManager();
        return managerService.getReportById(manager, reportId);
    }

    @GetMapping("/reports/task/{taskId}")
    public ResponseEntity<Response> viewReportsByTaskId(@PathVariable long taskId) {
        User manager = getCurrentManager();
        return managerService.getReportsByTaskId(manager, taskId);
    }

    @GetMapping("/reports/employee/{employeeId}")
    public ResponseEntity<Response> getReportsByEmployeeId(@PathVariable long employeeId) {
        User manager = getCurrentManager();
        return managerService.getReportEmployeeId(manager, employeeId);
    }

    @PostMapping("/working-schedule/{monthNumber}")
    public ResponseEntity<Response> viewWorkingSchedule(@PathVariable long monthNumber) {
        User manager = getCurrentManager();
        return managerService.getWorkingSchedules(manager, monthNumber);
    }

    @GetMapping("/get-all-employees")
    public ResponseEntity<Response> getAllEmployees() {
        User manager = getCurrentManager();
        return managerService.getAllEmployees(manager);
    }

    @GetMapping("/get-referenced-code")
    public ResponseEntity<Response> getManagerInfo() {
        User manager = getCurrentManager();
        return managerService.getReferenceCode(manager);
    }

    private User getCurrentManager() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
