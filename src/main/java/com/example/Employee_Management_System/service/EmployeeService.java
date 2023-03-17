package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.ScheduleWorkingDayRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskEmployeeRequest;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.model.EmployeeInformation;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.example.Employee_Management_System.dto.response.WorkingScheduleResponse.EmployeeSchedule;
import static com.example.Employee_Management_System.dto.response.WorkingScheduleResponse.MonthInfo;

public interface EmployeeService {
    ResponseEntity<Response> getTaskById(Long id, User employee);

    ResponseEntity<Response> getTasks(User employee);

    ResponseEntity<Response> updateTask(User employee, Long taskId, UpdateTaskEmployeeRequest updateTaskRequest);

    ResponseEntity<Response> writeReport(User employee, WriteReportRequest request);

    ResponseEntity<Response> scheduleWorkingDay(User employee, ScheduleWorkingDayRequest request);

    ResponseEntity<Response> getWorkingSchedule(User employee, int year, int monthNumber);

    void save(Employee employee);

    User getManagerOfEmployee(long employeeId);

    Employee getEmployeeByEmployeeId(long employeeId);

    ResponseEntity<Response> writeReportForTask(User employee, Long taskId, WriteReportRequest request);

    EmployeeSchedule getEmployeeSchedule(EmployeeInformation employeeInfo, int year, int monthNumber, MonthInfo monthInfo);

    ResponseEntity<Response> getReports(User employee);

    ResponseEntity<Response> getReportsByTaskId(User employee, long taskId);

    ResponseEntity<Response> getReferenceCode(User employee);

    ResponseEntity<Response> getSubTasks(User employee, long taskId);

    List<EmployeeInformation> getEmployeesBelongToManager(Long id);
}
