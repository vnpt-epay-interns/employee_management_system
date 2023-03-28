package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.model.ReportDetailedInfo;
import com.example.Employee_Management_System.repository.ReportRepository;
import com.example.Employee_Management_System.service.RedisService;
import com.example.Employee_Management_System.service.ReportService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final String REDIS_REPORTS_BY_MANAGER_KEY = "reports_by_manager::";
    private final String REDIS_REPORTS_BY_TASK_KEY = "reports_by_task::";
    private final ReportRepository reportRepository;
    private final RedisService redisService;

    @Override
    public void save(Report report) {
        reportRepository.save(report);
    }

    @Override
    public List<ReportDetailedInfo> getAllReports(User manager) {
        List<ReportDetailedInfo> reports = getAllReportsFromRedis(manager.getId());

        if (reports != null && !reports.isEmpty()) {
            return reports;
        } else {
            List<ReportDetailedInfo> reportsFromDB = reportRepository.getAllReports(manager);
            redisService.cacheReportsToRedis(reportsFromDB, manager.getId(), REDIS_REPORTS_BY_MANAGER_KEY);
            return reportsFromDB;
        }
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
        List<ReportDetailedInfo> reports = getReportByTaskIdFromRedis(taskId);

        if (reports != null && !reports.isEmpty()) {
            return reports;
        } else {
            List<ReportDetailedInfo> reportsFromDB = reportRepository.getReportsByTaskId(taskId);
            redisService.cacheReportsToRedis(reportsFromDB, taskId, REDIS_REPORTS_BY_TASK_KEY);
            return reportsFromDB;
        }
    }

    @Override
    public User getManagerOfEmployeeReport(long reportId) {
        return reportRepository.getManagerOfEmployeeReport(reportId);
    }

    @Override
    public List<ReportDetailedInfo> getReportsByEmployeeId(Long id) {
        List<ReportDetailedInfo> reportsByEmployeeInRedis = getReportsByEmployeeIdFromRedis(id);

        if (reportsByEmployeeInRedis == null) {
            List<ReportDetailedInfo> reportsByEmployeeIdFromDB = reportRepository.getReportsByEmployeeId(id);
            redisService.cacheReportsToRedis(reportsByEmployeeIdFromDB, id, REDIS_REPORTS_BY_MANAGER_KEY);
            return reportsByEmployeeIdFromDB;
        } else {
            return reportsByEmployeeInRedis;
        }
    }

    private List<ReportDetailedInfo> getReportsByEmployeeIdFromRedis(Long id) {
        Gson gson = new Gson();

        List<Object> reportsByEmployeeInRedis = redisTemplate.opsForHash().values(REDIS_REPORTS_BY_MANAGER_KEY + id);
        if (reportsByEmployeeInRedis.isEmpty()) {
            return null;
        } else {
            return reportsByEmployeeInRedis.stream()
                    .map(report -> gson.fromJson(report.toString(), ReportDetailedInfo.class))
                    .toList();
        }

    }

    public List<ReportDetailedInfo> getAllReportsFromRedis(Long id) {
        Gson gson = new Gson();

        List<Object> reportsInRedis = redisTemplate.opsForHash().values(REDIS_REPORTS_BY_MANAGER_KEY + id);
        if (reportsInRedis.isEmpty()) {
            return null;
        } else {
            return reportsInRedis.stream()
                    .map(report -> gson.fromJson(report.toString(), ReportDetailedInfo.class))
                    .toList();
        }
    }

    private List<ReportDetailedInfo> getReportByTaskIdFromRedis(Long id) {
        List<ReportDetailedInfo> reportsByTaskIdFromRedis = getReportsByTaskIdFromRedis(id);
        return reportsByTaskIdFromRedis;
    }

    private List<ReportDetailedInfo> getReportsByTaskIdFromRedis(Long id) {
        Gson gson = new Gson();

        List<Object> reportsByTaskIdInRedis = redisTemplate.opsForHash().values(REDIS_REPORTS_BY_TASK_KEY + id);
        if (reportsByTaskIdInRedis.isEmpty()) {
            return null;
        } else {
            return reportsByTaskIdInRedis.stream()
                    .map(report -> gson.fromJson(report.toString(), ReportDetailedInfo.class))
                    .toList();
        }
    }
}
