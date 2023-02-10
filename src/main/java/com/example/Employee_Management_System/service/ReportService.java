package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.model.ReportBasicInfo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReportService {
    List<ReportBasicInfo> getAllUnreadReports();

    Report getReportById(long reportId);

    List<ReportBasicInfo> getAllUnreadReportsByEmployeeId(long employeeId);

    List<ReportBasicInfo> getAllUnreadReportsByTaskId(long taskId);
}
