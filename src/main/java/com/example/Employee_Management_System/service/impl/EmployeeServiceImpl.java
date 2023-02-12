package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.ScheduleWorkingDayRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.mapper.EmployeeMapper;
import com.example.Employee_Management_System.service.EmployeeService;
import com.example.Employee_Management_System.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private ReportService reportService;

    @Override
    public ResponseEntity<Response> getTaskById(long id, User employee) {
        return null;
    }

    @Override
    public ResponseEntity<Response> getTasks(User employee) {
        return null;
    }

    @Override
    public ResponseEntity<Response> updateTask(User employee, UpdateTaskRequest updateTaskRequest) {
        return null;
    }

    @Override
    public ResponseEntity<Response> writeReport(User employee, WriteReportRequest request) {
        Report report = Report.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(new Date(System.currentTimeMillis()))
                .employeeId(employee.getId())
                .taskId(request.getTaskId())
                .isRead(false)
                .build();

        reportService.save(report);

        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Report has been saved successfully")
                        .data(null)
                        .build()
        );
    }

    @Override
    public ResponseEntity<Response> scheduleWorkingDay(User employee, ScheduleWorkingDayRequest request) {
        return null;
    }

    @Override
    public void save(Employee employee) {
        employeeMapper.save(employee);
    }
}

