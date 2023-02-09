package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.dto.request.ScheduleWorkingDayRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.mapper.EmployeeMapper;
import com.example.Employee_Management_System.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public ResponseEntity<Response> getTaskById(long id, Employee employee) {
        return null;
    }

    @Override
    public ResponseEntity<Response> getTasks(Employee employee) {
        return null;
    }

    @Override
    public ResponseEntity<Response> updateTask(Employee employee, UpdateTaskRequest updateTaskRequest) {
        return null;
    }

    @Override
    public ResponseEntity<Response> writeReport(Employee employee, WriteReportRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Response> scheduleWorkingDay(Employee employee, ScheduleWorkingDayRequest request) {
        return null;
    }
}
