package com.example.Employee_Management_System.repository;

import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.mapper.ReportMapper;
import com.example.Employee_Management_System.model.ReportBasicInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportRepository {

    @Autowired
    private ReportMapper reportMapper;

    public List<ReportBasicInfo> getAllUnreadReports() {
        return reportMapper.getAllUnreadReports();
    }

    public Report findById(long reportId) {
        return reportMapper.findById(reportId);
    }

    public List<ReportBasicInfo> findUnreadReportsByEmployeeId(long employeeId) {
        return reportMapper.findUnreadReportsByEmployeeId(employeeId);
    }

    public List<ReportBasicInfo> findUnreadReportsByTaskId(long taskId) {
        return reportMapper.findAllReportsByTaskId(taskId);
    }
}
