package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.model.ReportBasicInfo;
import com.example.Employee_Management_System.repository.ReportRepository;
import com.example.Employee_Management_System.service.ReportService;
import com.example.Employee_Management_System.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    private final TaskService taskService;

    @Override
    public void save(Report report) {
        reportRepository.save(report);
    }

    @Override
    public List<ReportBasicInfo> getAllUnreadReports(User manager) {
        return reportRepository.getAllUnreadReports(manager);
    }

    @Override
    public Report getReportById(long reportId) {
        //TODO: throw exception if not found
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }

    @Override
    public List<ReportBasicInfo> getAllUnreadReportsByEmployeeId(User manager, long employeeId) {
        return reportRepository.findUnreadReportsByEmployeeId(employeeId);
    }

    @Override
    public List<ReportBasicInfo> getAllUnreadReportsByTaskId(long taskId) {
        return reportRepository.findUnreadReportsByTaskId(taskId);
    }



    @Override
    public User getManagerOfEmployeeReport(long reportId) {
        return reportRepository.getManagerOfEmployeeReport(reportId);
    }
}
