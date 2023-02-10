package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ManagerService {

    ResponseEntity<Response> createTask(CreateTaskRequest request);
    ResponseEntity<Response> deleteTask(long taskId);
    ResponseEntity<Response> updateTask(long taskId, UpdateTaskRequest updateTaskRequest);
    ResponseEntity<Response> getAllReports();
    ResponseEntity<Response> getReportById(long reportId);
    ResponseEntity<Response> getReportEmployeeId(long employeeId);
    ResponseEntity<Response> getWorkingSchedules(long monthNumber);

    void save(Manager manager);

    ResponseEntity<Response> getAllEmployees();
    ResponseEntity<Response> getReportsByTaskId(long taskId);
}
