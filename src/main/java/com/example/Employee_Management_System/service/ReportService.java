package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.model.ReportDetailedInfo;

import java.util.List;

public interface ReportService {
    void save(Report report);
    List<ReportDetailedInfo> getAllReports(User manager);

    ReportDetailedInfo getReportById(long reportId);

    List<ReportDetailedInfo> getAllUnreadReportsByEmployeeId(User manager, long employeeId);

    List<ReportDetailedInfo> getAllReportsByTaskId(User employee, long taskId);

    User getManagerOfEmployeeReport(long reportId);

    List<ReportDetailedInfo> getReportsByEmployeeId(Long id);


}
