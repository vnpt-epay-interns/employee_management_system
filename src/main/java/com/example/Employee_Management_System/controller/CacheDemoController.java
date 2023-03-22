package com.example.Employee_Management_System.controller;

import com.example.Employee_Management_System.domain.Project;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.model.ReportDetailedInfo;
import com.example.Employee_Management_System.service.impl.EmployeeServiceImpl;
import com.example.Employee_Management_System.service.impl.ManagerServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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


    public User getCurrentEmployee() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping("/referenced-code")
    public String getReferencedCode() {
        User manager = getCurrentEmployee();
        return employeeServiceImpl.getReferenceCodeCache(manager);
    }

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
