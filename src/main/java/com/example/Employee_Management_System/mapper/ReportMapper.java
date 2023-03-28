package com.example.Employee_Management_System.mapper;

import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.model.ReportDetailedInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ReportMapper {
    List<ReportDetailedInfo> getAllReports(long managerId);

    Optional<ReportDetailedInfo> findById(long reportId);

    List<ReportDetailedInfo> findReportsByEmployeeId(long employeeId);

    List<ReportDetailedInfo> findAllReportsByTaskId(long taskId);

    @Insert("INSERT INTO reports (title, content, task_id, created_by, created_at, is_read) VALUES (#{title}, #{content}, #{taskId}, #{createdBy}, #{createdAt}, #{isRead})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Report report);

    User getManagerOfEmployeeReport(long reportId);

    List<ReportDetailedInfo> getReportsByEmployeeId(Long id);

    String getNameFromReportByEmployeeId(Long id);
}
