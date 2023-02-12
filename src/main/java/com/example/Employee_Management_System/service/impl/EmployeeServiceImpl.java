package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.domain.WorkingSchedule;
import com.example.Employee_Management_System.dto.request.ScheduleWorkingDayRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse;
import com.example.Employee_Management_System.mapper.EmployeeMapper;
import com.example.Employee_Management_System.repository.EmployeeRepository;
import com.example.Employee_Management_System.service.EmployeeService;
import com.example.Employee_Management_System.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.sql.Date;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ReportService reportService;

    @Override
    public ResponseEntity<Response> getTaskById(long id, User employee) {
        return null;
    }

    @Override
    public ResponseEntity<Response> getAllTasks() {
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
        WorkingSchedule workingSchedule = WorkingSchedule.builder()
                .date(request.getDate())
                .atMorning(request.isAtMorning())
                .atAfternoon(request.isAtAfternoon())
                .employeeId(employee.getId()).build();
        employeeRepository.scheduleWorkingDays(workingSchedule);
        return ResponseEntity.ok(Response.builder().message("!!!").status(200).data(null).build());
    }

    @Override
    public ResponseEntity<Response> getWorkingDays(User employee) {
        List<WorkingScheduleResponse> workingScheduleResponses = employeeRepository.getSchedule(employee);
        return ResponseEntity.ok(Response.builder().data(workingScheduleResponses).build());
    }

    @Override
    public void save(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public ResponseEntity<Response> getTasks(User employee) {
        return null;
    }
}

