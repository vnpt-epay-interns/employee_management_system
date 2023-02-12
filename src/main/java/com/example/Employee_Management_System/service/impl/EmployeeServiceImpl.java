package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.ScheduleWorkingDayRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.mapper.EmployeeMapper;
import com.example.Employee_Management_System.service.EmployeeService;
import com.example.Employee_Management_System.service.ReportService;
import com.example.Employee_Management_System.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Objects;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeMapper employeeMapper;

    private final ReportService reportService;

    private final TaskService taskService;

    @Override
    public ResponseEntity<Response> getTaskById(long id, User employee) {
        return null;
    }

    @Override
    public ResponseEntity<Response> getTasks(User employee) {
        return null;
    }

    @Override
    public ResponseEntity<Response> updateTask(User employee, UpdateTaskRequest updateTaskRequest) {
        return null;
    }

    @Override
    public ResponseEntity<Response> writeReport(User employee, WriteReportRequest request) {
        // TODO: check if the report is assign to the employee
        Long taskId = request.getTaskId();

        if (taskId != null && checkIfTaskBelongsToEmployee(employee, taskId)) {
            //TODO: throw custom exception
            throw new RuntimeException("The task is not assigned to the you");
        }
        Report report = Report.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(new Date(System.currentTimeMillis()))
                .employeeId(employee.getId())
                .taskId(request.getTaskId())
                .isRead(false)
                .build();

        reportService.save(report);

        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Report has been saved successfully")
                        .data(null)
                        .build()
        );
    }

    private boolean checkIfTaskBelongsToEmployee(User employee, Long taskId) {
        Task task = taskService.getTaskByTaskId(taskId);
        User assignedEmployee = taskService.getEmployeeOfTask(task.getId());
        return !Objects.equals(assignedEmployee.getId(), employee.getId());
    }

    @Override
    public ResponseEntity<Response> scheduleWorkingDay(User employee, ScheduleWorkingDayRequest request) {
        return null;
    }

    @Override
    public void save(Employee employee) {
        employeeMapper.save(employee);
    }

    @Override
    public User getManagerOfEmployee(long employeeId) {
        return employeeMapper.getManagerOfEmployee(employeeId);
    }

    @Override
    public Employee getEmployeeByEmployeeId(long employeeId) {
        //TODO: throw custom exception
        return employeeMapper.getEmployeeByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }
}

