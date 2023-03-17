package com.example.Employee_Management_System.controller;

import com.example.Employee_Management_System.domain.Project;
import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.UpdateTaskEmployeeRequest;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.response.TaskDTO;
import com.example.Employee_Management_System.dto.response.UserInformation;
import com.example.Employee_Management_System.model.ReportDetailedInfo;
import com.example.Employee_Management_System.service.impl.EmployeeServiceImpl;
import com.example.Employee_Management_System.service.impl.ManagerServiceImpl;
import com.example.Employee_Management_System.service.impl.UserServiceImpl;
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

    @Autowired
    private ManagerServiceImpl managerServiceImpl;

//    @Autowired
//    private UserServiceImpl userServiceImpl;


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


    @GetMapping("/referenced-code")
    public String getReferencedCode() {
        User manager = getCurrentEmployee();
        return employeeServiceImpl.getReferenceCodeCache(manager);
    }

//    public UserInformation getUserInformation() {
//        User currentUser = getCurrentEmployee();
//        return userServiceImpl.getUserInfoCache(currentUser);
//    }

    @GetMapping("/get-all-projects")
    public List<Project> getAllProject() {
        User manager = getCurrentEmployee();
        return managerServiceImpl.getAllProjectCache(manager);
    }

    @GetMapping("/get-project/{id}")
    public Project getProjectById(Long id) {
        User manager = getCurrentEmployee();
        return managerServiceImpl.getProjectByIdCache(manager.getId());
    }

    public void writeReport(User employee, WriteReportRequest request) {
        employeeServiceImpl.writeReportCache(employee, request);
    }

    @GetMapping("/get-all-reports")
    public List<ReportDetailedInfo> getAllReport() {
        User manager = getCurrentEmployee();
        return managerServiceImpl.getAllReportCache(manager);
    }

    @GetMapping("/get-all-reports/{employeeId}")
    public List<ReportDetailedInfo> getReportsByEmployeeId(@PathVariable Long employeeId) {
        User manager = getCurrentEmployee();
        return managerServiceImpl.getReportsByEmployeeId(manager);
    }


    @GetMapping("/get-all-reports-e")
    public List<ReportDetailedInfo> getReportsEmployee() {
        User employee = getCurrentEmployee();
        return employeeServiceImpl.getReportsCache(employee);
    }

    @GetMapping("/get-all-reports/{taskId}")
    public List<ReportDetailedInfo> getReportsByTaskId(@PathVariable Long employeeId) {
        User employee = getCurrentEmployee();
        return employeeServiceImpl.getReportsByTaskIdCache(employee);
    }



}
