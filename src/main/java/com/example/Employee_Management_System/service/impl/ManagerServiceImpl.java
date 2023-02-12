package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.*;

import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse;
import com.example.Employee_Management_System.repository.ManagerRepository;
import com.example.Employee_Management_System.repository.UserRepository;

import com.example.Employee_Management_System.model.ReportBasicInfo;
import com.example.Employee_Management_System.repository.TaskRepository;
import com.example.Employee_Management_System.service.ManagerService;
import com.example.Employee_Management_System.service.ReportService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;

    private final TaskRepository taskRepository;

    private final ReportService reportService;

    @Override
    public ResponseEntity<Response> createTask(User manager, CreateTaskRequest request) {
        //TODO: (Vu) check if the employeeId is one of the employees of the manager
        Long employeeId = request.getEmployeeId();

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
    public ResponseEntity<Response> deleteTask(User manager, long taskId) {
        //TODO:(Vu) check if the task belongs to an employee of the manager
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
    public ResponseEntity<Response> updateTask(User manager, long taskId, UpdateTaskRequest updateTaskRequest) {
        //TODO:(Vu) check if the task belongs to an employee of the manager

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
    public ResponseEntity<Response> getAllReports(User manager) {
        //TODO: (Hai) get all unread reports from the employees of the manager
        List<ReportBasicInfo> unreadReports = reportService.getAllUnreadReports();

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get all reports successfully!")
                .data(unreadReports)
                .build()
        );
    }

    @Override
    public ResponseEntity<Response> getReportById(User manager, long reportId) {
        //TODO:(Hai) check if the task belongs to an employee of the manager
        Report report = reportService.getReportById(reportId);

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get report successfully!")
                .data(report)
                .build()
        );
    }

    public ResponseEntity<Response> getReportsByTaskId(User manager, long taskId) {
        //TODO: (Hai) check if the task belongs to an employee of the manager
        List<ReportBasicInfo> unreadReportsByTaskId = reportService.getAllUnreadReportsByTaskId(taskId);

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get all reports successfully!")
                .data(unreadReportsByTaskId)
                .build()
        );
    }


    @Override
    public ResponseEntity<Response> getReportEmployeeId(User manager, long employeeId) {
        //TODO:(Hai) check if the employee is one of the employees of the manager

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
    public ResponseEntity<Response> getWorkingSchedules(User manager, long monthNumber) {
        //TODO:(Vy) only get the schedule of the employees of the manager

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
    public ResponseEntity<Response> getAllEmployees(User manager) {
        //TODO: (Vy) only get the employees of the manager
        Collection<Employee> employeeList = managerRepository.getAllEmployees();
        return ResponseEntity.ok(Response.builder().status(200).message("!!!").data(employeeList).build());
    }


}
