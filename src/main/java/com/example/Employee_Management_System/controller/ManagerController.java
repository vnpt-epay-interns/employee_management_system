package com.example.Employee_Management_System.controller;

import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.CreateProjectRequest;
import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.service.ManagerService;
import com.example.Employee_Management_System.service.ProjectService;
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

    private ProjectService projectService;

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

    @GetMapping("/working-schedules/{year}/{monthNumber}")
    public ResponseEntity<Response> viewWorkingSchedule(@PathVariable int year, @PathVariable int monthNumber) {
        User manager = getCurrentManager();
        return managerService.getEmployeeWorkingSchedules(manager, year,  monthNumber);
    }

    @GetMapping("/get-referenced-code")
    public ResponseEntity<Response> getManagerInfo() {
        User manager = getCurrentManager();
        return managerService.getReferenceCode(manager);
    }

    private User getCurrentManager() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    @GetMapping("/get-all-employees")
    public ResponseEntity<Response> getEmployeeBelongToManager() {
        User manager = getCurrentManager();
        return managerService.getEmployeeBelongToManager(manager);
    }

    @PostMapping("/create-project")
    public ResponseEntity<Response> createProject(@RequestBody CreateProjectRequest request) {
        Long managerId = getCurrentManager().getId();
        return managerService.createProject(request, managerId);
    }

    @GetMapping("/get-project/{id}")
    public ResponseEntity<Response> getProjectById(@PathVariable Long id) {
        return managerService.getProjectById(id);
    }

    @GetMapping("/get-all-projects")
    public ResponseEntity<Response> getAllProjects() {
        return managerService.getAllProjects();
    }


}
