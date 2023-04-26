package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.model.ReportDetailedInfo;
import com.example.Employee_Management_System.repository.ReportRepository;
import com.example.Employee_Management_System.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    @Override
    public void save(Report report) {
        reportRepository.save(report);
    }

    @Override
    public List<ReportDetailedInfo> getAllReports(User manager) {

        List<ReportDetailedInfo> reportsFromDB = reportRepository.getAllReports(manager);
        return reportsFromDB;

    }

    @Override
    public ReportDetailedInfo getReportById(long reportId) {
        //TODO: throw exception if not found
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }

    @Override
    public List<ReportDetailedInfo> getAllUnreadReportsByEmployeeId(User manager, long employeeId) {
        return reportRepository.findReportsByEmployeeId(employeeId);
    }

    @Override
    public List<ReportDetailedInfo> getAllReportsByTaskId(User user, long taskId) {


        List<ReportDetailedInfo> reportsFromDB = reportRepository.getReportsByTaskId(taskId);
        return reportsFromDB;

    }

    @Override
    public User getManagerOfEmployeeReport(long reportId) {
        return reportRepository.getManagerOfEmployeeReport(reportId);
    }

    @Override
    public List<ReportDetailedInfo> getReportsByEmployeeId(Long id) {


        List<ReportDetailedInfo> reportsByEmployeeIdFromDB = reportRepository.getReportsByEmployeeId(id);
        return reportsByEmployeeIdFromDB;
    }

}
