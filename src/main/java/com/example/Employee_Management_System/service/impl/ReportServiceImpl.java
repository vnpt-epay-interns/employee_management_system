package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.model.ReportDetailedInfo;
import com.example.Employee_Management_System.repository.ReportRepository;
import com.example.Employee_Management_System.service.ReportService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String REDIS_REPORTS_KEY = "report";
    private final ReportRepository reportRepository;

    @Override
    public void save(Report report) {
        reportRepository.save(report);
    }

    @Override
    public List<ReportDetailedInfo> getAllReports(User manager) {
        return getAllProjectsInRedis();
    }

    @Override
    public Report getReportById(long reportId) {
        //TODO: throw exception if not found
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }

    @Override
    public List<ReportDetailedInfo> getAllUnreadReportsByEmployeeId(User manager, long employeeId) {
        return reportRepository.findReportsByEmployeeId(employeeId);
    }

    @Override
    public List<ReportDetailedInfo> getAllReportsByTaskId(long taskId) {
        List<ReportDetailedInfo> reports = getReportByTaskIdFromRedis(taskId);

        if (reports == null && !reports.isEmpty()) {
            return reports;
        } else {
            List<ReportDetailedInfo> reportsFromDB = reportRepository.getReportsByTaskId(taskId);
            cacheReportsToRedis(reportsFromDB);
            return reportsFromDB;
        }
    }

    @Override
    public User getManagerOfEmployeeReport(long reportId) {
        return reportRepository.getManagerOfEmployeeReport(reportId);
    }

    @Override
    public List<ReportDetailedInfo> getReportsByEmployeeId(Long id) {
        return getReportsByEmployeeIdFromRedis(id);
    }

    private List<ReportDetailedInfo> getReportsByEmployeeIdFromRedis(Long id) {
        Gson gson = new Gson();

        List<Object> reportsInRedis = redisTemplate.opsForHash().values(REDIS_REPORTS_KEY);
        if (reportsInRedis == null || reportsInRedis.isEmpty()) {
            return null;
        }
        else {
            return reportsInRedis.stream()
                    .map(report -> gson.fromJson(report.toString(), ReportDetailedInfo.class))
                    .filter(report -> Objects.equals(reportRepository.findReportsByEmployeeId(id), id))
                    .toList();
        }

    }

    private void cacheReportsToRedis(List<ReportDetailedInfo> reportsFromDB) {
        Gson gson = new Gson();
        Map<String, String> map = reportsFromDB.stream()
                .collect(Collectors.toMap(report -> report.getEmployeeName(), report -> gson.toJson(report)));
        redisTemplate.opsForHash().putAll(REDIS_REPORTS_KEY, map);
    }





    public List<ReportDetailedInfo> getAllProjectsInRedis() {
        List<ReportDetailedInfo> reports;
        Gson gson = new Gson();

        List<Object> reportsInRedis = redisTemplate.opsForHash().values(REDIS_REPORTS_KEY);
        if (reportsInRedis == null || reportsInRedis.isEmpty()) {
            return null;
        }
        else {
            return reportsInRedis.stream()
                    .map(report -> gson.fromJson(report.toString(), ReportDetailedInfo.class))
                    .toList();
        }
    }


    private List<ReportDetailedInfo> getReportByTaskIdFromRedis(Long id) {
        List<ReportDetailedInfo> allReports = getAllProjectsInRedis();
        if (allReports != null || allReports.isEmpty()) {
            List<ReportDetailedInfo> reportsFromDB = allReports.stream()
                    .filter(report -> report.getTaskId().equals(id))
                    .toList();
            if (reportsFromDB != null && !reportsFromDB.isEmpty()) {
                return reportsFromDB;
            }
        }
        return null;
    }
}
