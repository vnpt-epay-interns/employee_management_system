package com.example.Employee_Management_System.repository;

import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.mapper.ReportMapper;
import com.example.Employee_Management_System.model.ReportBasicInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReportRepository {

    @Autowired
    private ReportMapper reportMapper;

    public List<ReportBasicInfo> getAllUnreadReports(User manager) {
        return reportMapper.getAllUnreadReports(manager);
    }

    public Optional<Report> findById(long reportId) {
        return reportMapper.findById(reportId);
    }

    public List<ReportBasicInfo> findUnreadReportsByEmployeeId(long employeeId) {
        return reportMapper.findUnreadReportsByEmployeeId(employeeId);
    }

    public List<ReportBasicInfo> findUnreadReportsByTaskId(long taskId) {
        return reportMapper.findAllReportsByTaskId(taskId);
    }

    public void save(Report report) {
        reportMapper.save(report);
    }

    public User getManagerOfEmployeeReport(long reportId) {
        return reportMapper.getManagerOfEmployeeReport(reportId);
    }
}
