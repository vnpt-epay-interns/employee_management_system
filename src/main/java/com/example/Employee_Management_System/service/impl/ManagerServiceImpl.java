package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.mapper.ManagerMapper;
import com.example.Employee_Management_System.model.ReportBasicInfo;
import com.example.Employee_Management_System.repository.ManagerRepository;
import com.example.Employee_Management_System.service.ManagerService;
import com.example.Employee_Management_System.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ReportService reportService;

    @Override
    public ResponseEntity<Response> createTask(CreateTaskRequest request) {
        return ResponseEntity.ok(Response.builder().status(200).message("Create task successfully!").build());
    }

    @Override
    public ResponseEntity<Response> getAllReports() {
        List<ReportBasicInfo> unreadReports = reportService.getAllUnreadReports();

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get all reports successfully!")
                .data(unreadReports)
                .build()
        );
    }

    @Override
    public ResponseEntity<Response> getReportById(long reportId) {
        Report report = reportService.getReportById(reportId);

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get report successfully!")
                .data(report)
                .build()
        );
    }

    @Override
    public ResponseEntity<Response> getReportEmployeeId(long employeeId) {
        List<ReportBasicInfo> unreadReportsByEmployeeId = reportService.getAllUnreadReportsByEmployeeId(employeeId);

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get all reports successfully!")
                .data(unreadReportsByEmployeeId)
                .build()
        );
    }

    @Override
    public ResponseEntity<Response> getWorkingSchedule(long monthNumber) {
        return null;
    }

    @Override
    public void save(Manager manager) {
        managerRepository.save(manager);
    }

    @Override
    public ResponseEntity<Response> getReportsByTaskId(long taskId) {
        List<ReportBasicInfo> unreadReportsByTaskId = reportService.getAllUnreadReportsByTaskId(taskId);

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get all reports successfully!")
                .data(unreadReportsByTaskId)
                .build()
        );
    }
}
