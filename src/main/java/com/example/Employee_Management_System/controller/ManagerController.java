package com.example.Employee_Management_System.controller;

import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.service.ManagerService;
import com.example.Employee_Management_System.service.UserService;
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
    private final UserService userService;

    @PostMapping("/tasks/create")
    public ResponseEntity<Response> createTask(@RequestBody CreateTaskRequest request) {
        User manager = getCurrentManager();
        return managerService.createTask(manager, request);
    }

    @DeleteMapping("/tasks/delete/{taskId}")
    public ResponseEntity<Response> deleteTask(@PathVariable long taskId) {
        User manager = getCurrentManager();
        return managerService.deleteTask(manager, taskId);
    }

    @PutMapping("/tasks/update/{taskId}")
    public ResponseEntity<Response> updateTask(@PathVariable long taskId, @RequestBody UpdateTaskRequest updateTaskRequest) {
        User manager = getCurrentManager();
        return managerService.updateTask(manager, taskId, updateTaskRequest);
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

    @GetMapping("/all-employees")
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
