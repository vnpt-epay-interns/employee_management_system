package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


public interface ManagerService {

    ResponseEntity<Response> createTask(CreateTaskRequest request);
    ResponseEntity<Response> getAllReports();
    ResponseEntity<Response> getReportById(long employeeId);

    ResponseEntity<Response> getReportEmployeeId(long employeeId);
    ResponseEntity<Response> getWorkingSchedule(long monthNumber);

    void save(Manager manager);
}
