package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ManagerService {

    ResponseEntity<Response> createTask(User manager, CreateTaskRequest request);
    ResponseEntity<Response> deleteTask(User manager, long taskId);
    ResponseEntity<Response> updateTask(User manager, long taskId, UpdateTaskRequest updateTaskRequest);
    ResponseEntity<Response> getAllReports(User manager);
    ResponseEntity<Response> getReportById(User manager, long reportId);
    ResponseEntity<Response> getReportEmployeeId(User manager, long employeeId);
    ResponseEntity<Response> getWorkingSchedules(User manager, long monthNumber);

    void save(Manager manager);

    ResponseEntity<Response> getAllEmployees(User manager);
    ResponseEntity<Response> getReportsByTaskId(User manager, long taskId);
}
