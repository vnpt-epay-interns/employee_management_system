package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.domain.WorkingSchedule;
import com.example.Employee_Management_System.domain.Task;

import com.example.Employee_Management_System.domain.Report;
import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse;
import com.example.Employee_Management_System.repository.ManagerRepository;
import com.example.Employee_Management_System.repository.UserRepository;

import com.example.Employee_Management_System.mapper.ManagerMapper;
import com.example.Employee_Management_System.model.ReportBasicInfo;
import com.example.Employee_Management_System.repository.ManagerRepository;
import com.example.Employee_Management_System.repository.TaskRepository;
import com.example.Employee_Management_System.service.ManagerService;
import com.example.Employee_Management_System.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ReportService reportService;

    @Override
    public ResponseEntity<Response> createTask(CreateTaskRequest request) {
        Task task = Task
                .builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .completion(request.getCompletion())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .employeeId(request.getEmployeeId())
                .estimateHours(request.getEstimateHours())
                .parentTask(request.getParentTask())
                .priority(request.getPriority())
                .build();
        taskRepository.createTask(task);

        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(200)
                        .message("Create task successfully!")
                        .build()
        );
    }

    @Override
    public ResponseEntity<Response> deleteTask(long taskId) {
        Task task = taskRepository
                .getTask(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found!"));

        taskRepository.deleteTask(task);
        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(200)
                        .message("Delete task successfully!")
                        .build()
        );
    }

    @Override
    public ResponseEntity<Response> updateTask(long taskId, UpdateTaskRequest updateTaskRequest) {
        // Todo: throw custom exception
        Task task = taskRepository
                .getTask(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found!"));

        task.setTitle(updateTaskRequest.getTitle());
        task.setDescription(updateTaskRequest.getDescription());
        task.setStatus(updateTaskRequest.getStatus());
        task.setCompletion(updateTaskRequest.getCompletion());
        task.setPriority(updateTaskRequest.getPriority());
        task.setStartDate(updateTaskRequest.getStartDate());
        task.setEndDate(updateTaskRequest.getEndDate());
        task.setEmployeeId(updateTaskRequest.getEmployeeId());
        task.setEstimateHours(updateTaskRequest.getEstimateHours());

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
    public ResponseEntity<Response> getAllReports() {
        List<ReportBasicInfo> unreadReports = reportService.getAllUnreadReports();

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get all reports successfully!")
                .data(unreadReports)
                .build()
        );
    }

    @Override
    public ResponseEntity<Response> getReportById(long reportId) {
        Report report = reportService.getReportById(reportId);

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get report successfully!")
                .data(report)
                .build()
        );
    }

    @Override
    public ResponseEntity<Response> getReportEmployeeId(long employeeId) {
        List<ReportBasicInfo> unreadReportsByEmployeeId = reportService.getAllUnreadReportsByEmployeeId(employeeId);

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get all reports successfully!")
                .data(unreadReportsByEmployeeId)
                .build()
        );
    }

    // ToDo: view working schedule of all employees in a month
    @Override
    public ResponseEntity<Response> getWorkingSchedules(long monthNumber) {
        List<WorkingScheduleResponse> workingSchedules = managerRepository.getWorkingSchedules(monthNumber);
//        TreeMap<Date, List<WorkingScheduleResponse>> collect = workingSchedules.stream().collect(Collectors.groupingBy(WorkingScheduleResponse::getDate));
        TreeMap<Date, List<WorkingScheduleResponse>> collect = workingSchedules.stream().collect(Collectors.groupingBy(WorkingScheduleResponse::getDate, TreeMap::new, Collectors.toList()));
        return ResponseEntity.ok(Response.builder().status(200).message("Get working schedule successfully!").data(collect).build());
    }

    @Override
    public void save(Manager manager) {
        managerRepository.save(manager);
    }

    @Override
    public ResponseEntity<Response> getAllEmployees() {
        Collection<Employee> employeeList = managerRepository.getAllEmployees();
        return ResponseEntity.ok(Response.builder().status(200).message("!!!").data(employeeList).build());
    }


    public ResponseEntity<Response> getReportsByTaskId(long taskId) {
        List<ReportBasicInfo> unreadReportsByTaskId = reportService.getAllUnreadReportsByTaskId(taskId);

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get all reports successfully!")
                .data(unreadReportsByTaskId)
                .build()
        );
    }
}
