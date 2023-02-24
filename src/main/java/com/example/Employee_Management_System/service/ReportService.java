package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.model.ReportBasicInfo;

import java.util.List;

public interface ReportService {
    void save(Report report);
    List<ReportBasicInfo> getAllUnreadReports(User manager);

    Report getReportById(long reportId);

    List<ReportBasicInfo> getAllUnreadReportsByEmployeeId(User manager, long employeeId);

    List<ReportBasicInfo> getAllUnreadReportsByTaskId(long taskId);

    User getManagerOfEmployeeReport(long reportId);
}
