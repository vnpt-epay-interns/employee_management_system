package com.example.Employee_Management_System.mapper;

import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.model.ReportBasicInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ReportMapper {
    List<ReportBasicInfo> getAllUnreadReports(User manager);

    Optional<Report> findById(long reportId);

    List<ReportBasicInfo> findUnreadReportsByEmployeeId(long employeeId);

    List<ReportBasicInfo> findAllReportsByTaskId(long taskId);

    @Insert("INSERT INTO reports (title, content, task_id, created_by, created_at, is_read) VALUES (#{title}, #{content}, #{taskId}, #{createdBy}, #{createdAt}, #{isRead})")
    void save(Report report);

    User getManagerOfEmployeeReport(long reportId);

    List<ReportBasicInfo> getReportsByEmployeeId(Long id);
}
