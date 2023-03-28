package com.example.Employee_Management_System.repository;

import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.mapper.ReportMapper;
import com.example.Employee_Management_System.model.ReportDetailedInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReportRepository {

    @Autowired
    private ReportMapper reportMapper;

    public List<ReportDetailedInfo> getAllReports(User manager) {
        return reportMapper.getAllReports(manager.getId());
    }

    public Optional<ReportDetailedInfo> findById(long reportId) {
        return reportMapper.findById(reportId);
    }

    public List<ReportDetailedInfo> findReportsByEmployeeId(long employeeId) {
        return reportMapper.findReportsByEmployeeId(employeeId);
    }

    public List<ReportDetailedInfo> findReportsByTaskId(long taskId) {
        return reportMapper.findAllReportsByTaskId(taskId);
    }

    public void save(Report report) {
        reportMapper.save(report);
    }

    public User getManagerOfEmployeeReport(long reportId) {
        return reportMapper.getManagerOfEmployeeReport(reportId);
    }

    public List<ReportDetailedInfo> getReportsByEmployeeId(Long id) {
        return reportMapper.getReportsByEmployeeId(id);
    }

    public List<ReportDetailedInfo> getReportsByTaskId(long taskId) {
        return reportMapper.findAllReportsByTaskId(taskId);
    }

    public String getNameFromReport(Long id) {
        return reportMapper.getNameFromReportByEmployeeId(id);
    }
}
