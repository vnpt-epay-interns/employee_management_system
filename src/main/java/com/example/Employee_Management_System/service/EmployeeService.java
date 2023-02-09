package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.dto.request.ScheduleWorkingDayRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.dto.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


public interface EmployeeService {

    ResponseEntity<Response> getTaskById(long id, Employee employee);
    ResponseEntity<Response> getTasks(Employee employee);
    ResponseEntity<Response> updateTask(Employee employee, UpdateTaskRequest updateTaskRequest);
    ResponseEntity<Response> writeReport(Employee employee, WriteReportRequest request);
    ResponseEntity<Response> scheduleWorkingDay(Employee employee, ScheduleWorkingDayRequest request);


}
