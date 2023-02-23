package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.ScheduleWorkingDayRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.dto.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;


public interface EmployeeService {

    ResponseEntity<Response> getTaskById(long id, User employee);
    ResponseEntity<Response> getAllTasks();
    ResponseEntity<Response> updateTask(User employee, UpdateTaskRequest updateTaskRequest);
    ResponseEntity<Response> writeReport(User employee, WriteReportRequest request);
    ResponseEntity<Response> scheduleWorkingDay(User employee, ScheduleWorkingDayRequest request);
    ResponseEntity<Response> getWorkingDays(User employee);
    void save(Employee employee);

    ResponseEntity<Response> getTasks(User employee);
}
