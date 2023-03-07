package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.model.ReportDetailedInfo;

import java.util.List;

public interface ReportService {
    void save(Report report);
    List<ReportDetailedInfo> getAllUnreadReports(User manager);

    Report getReportById(long reportId);

    List<ReportDetailedInfo> getAllUnreadReportsByEmployeeId(User manager, long employeeId);

    List<ReportDetailedInfo> getAllReportsByTaskId(long taskId);

    User getManagerOfEmployeeReport(long reportId);

    List<ReportDetailedInfo> getReportsByEmployeeId(Long id);

    List<ReportDetailedInfo> getReportsByTaskId(long taskId);
}
