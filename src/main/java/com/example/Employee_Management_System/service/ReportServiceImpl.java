package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.model.ReportBasicInfo;
import com.example.Employee_Management_System.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public void save(Report report) {
        reportRepository.save(report);
    }

    @Override
    public List<ReportBasicInfo> getAllUnreadReports() {
        return reportRepository.getAllUnreadReports();
    }

    @Override
    public Report getReportById(long reportId) {
        return reportRepository.findById(reportId);
    }

    @Override
    public List<ReportBasicInfo> getAllUnreadReportsByEmployeeId(long employeeId) {
        return reportRepository.findUnreadReportsByEmployeeId(employeeId);
    }

    @Override
    public List<ReportBasicInfo> getAllUnreadReportsByTaskId(long taskId) {
        return reportRepository.findUnreadReportsByTaskId(taskId);
    }
}
