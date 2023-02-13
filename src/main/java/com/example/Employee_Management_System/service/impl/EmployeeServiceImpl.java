package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.ScheduleWorkingDayRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskEmployeeRequest;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.response.TaskDTO;
import com.example.Employee_Management_System.repository.EmployeeRepository;
import com.example.Employee_Management_System.repository.TaskRepository;
import com.example.Employee_Management_System.service.EmployeeService;
import com.example.Employee_Management_System.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ReportService reportService;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public ResponseEntity<Response> getTaskById(long id, User user) {
        Task task = employeeRepository
                .getTaskByIdAndEmployeeId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (task.getParentTask() == null) {
            List<Task> subTasks = employeeRepository.getTasksByParentTask(task.getId());
            return ResponseEntity.ok(
                    Response.builder()
                            .status(200)
                            .data(
                                    TaskDTO
                                            .builder()
                                            .parentTask(task)
                                            .subTasks(subTasks)
                                            .build()
                            )
                            .build()
            );
        }

        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .data(task)
                        .build()
        );
    }

    @Override
    public ResponseEntity<Response> getTasks(User employee) {
        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .data(employeeRepository.getTasksByEmployeeId(employee.getId()))
                        .build()
                );
    }

    @Override
    public ResponseEntity<Response> updateTask(User employee, Long taskId, UpdateTaskEmployeeRequest updateTaskRequest) {
        Task task = employeeRepository
                .getTaskByIdAndEmployeeId(taskId, employee.getId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(updateTaskRequest.getStatus());
        task.setCompletion(updateTaskRequest.getCompletion());
        taskRepository.updateTask(task);

        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(200)
                        .message("Update task successfully!")
                        .build()
        );
    }

    @Override
    public ResponseEntity<Response> writeReport(User employee, WriteReportRequest request) {
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

    @Override
    public ResponseEntity<Response> scheduleWorkingDay(User employee, ScheduleWorkingDayRequest request) {
        return null;
    }

    @Override
    public void save(Employee employee) {
        employeeRepository.save(employee);
    }
}

