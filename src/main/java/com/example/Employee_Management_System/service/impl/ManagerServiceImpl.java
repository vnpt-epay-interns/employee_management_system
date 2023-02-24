package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.*;
import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse;
import com.example.Employee_Management_System.exception.ReportException;
import com.example.Employee_Management_System.model.ReportBasicInfo;
import com.example.Employee_Management_System.repository.EmployeeRepository;
import com.example.Employee_Management_System.repository.ManagerRepository;
import com.example.Employee_Management_System.repository.TaskRepository;
import com.example.Employee_Management_System.service.EmployeeService;
import com.example.Employee_Management_System.service.ManagerService;
import com.example.Employee_Management_System.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;

    private final EmployeeRepository employeeRepository;

    private final TaskRepository taskRepository;

    private final ReportService reportService;

    private final EmployeeService employeeService;
    @Override
    public ResponseEntity<Response> createTask(User manager, CreateTaskRequest request) {
        if (checkEmployeeBelongsToManager(manager.getId(), request.getEmployeeId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee not belong to the manager!");
        }

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

        Task task = managerRepository
                .getTaskById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found!"));

        if (checkEmployeeBelongsToManager(manager.getId(), task.getEmployeeId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee not belong to the manager!");
        }

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
        Task task = managerRepository
                .getTaskById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found!"));

        if (checkEmployeeBelongsToManager(manager.getId(), task.getEmployeeId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee not belong to the manager!");
        }

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

    private boolean checkEmployeeBelongsToManager(Long managerId, Long employeeId) {
        return employeeRepository
                .getAllEmployeesByManagerId(managerId)
                .stream()
                .noneMatch(employee -> Objects.equals(employee.getId(), employeeId));
    }

    @Override
    public ResponseEntity<Response> getAllReports(User manager) {
        List<ReportBasicInfo> unreadReports = reportService.getAllUnreadReports(manager);

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get all reports successfully!")
                .message("Get report successfully!")
                .data(unreadReports)
                .build()
        );
    }

    @Override
    public ResponseEntity<Response> getReportById(User manager, long reportId) {
        Report report = reportService.getReportById(reportId);
        // check if report belongs to one of the employees of the manager
        if (!checkIfReportBelongsToEmployeeOfManager(report, manager)) {
            throw new ReportException("You are not allowed to access this report!");
        }


        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get report successfully!")
                .data(report)
                .build()
        );
    }

    private boolean checkIfReportBelongsToEmployeeOfManager(Report report, User manager) {
        User employeeManager = reportService.getManagerOfEmployeeReport(report.getId());
        return employeeManager.getId().equals(manager.getId());
    }

    public ResponseEntity<Response> getReportsByTaskId(User manager, long taskId) {

        if (!checkIfTaskBelongsToEmployeeOfManager(manager, taskId)) {
            throw new ReportException("You are not allowed to view this report");
        }
        List<ReportBasicInfo> unreadReportsByTaskId = reportService.getAllUnreadReportsByTaskId(taskId);

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get all reports successfully!")
                .data(unreadReportsByTaskId)
                .build()
        );
    }

    private boolean checkIfTaskBelongsToEmployeeOfManager(User manager, long taskId) {
        User employeeManager = taskRepository.getManagerOfTask(taskId);
        return employeeManager.getId().equals(manager.getId());
    }

    @Override
    public ResponseEntity<Response> getReportEmployeeId(User manager, long employeeId) {
        List<ReportBasicInfo> unreadReportsByEmployeeId = reportService.getAllUnreadReportsByEmployeeId(manager, employeeId);


        if (!checkIfEmployeeIsManagedByManager(employeeId, manager)) {
            throw new ReportException("You are not allowed to access this report!");
        }
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get all reports successfully!")
                .data(unreadReportsByEmployeeId)
                .build()
        );
    }

    private boolean checkIfEmployeeIsManagedByManager(long employeeId, User manager) {
        // check if employee is managed by manager
        // if the employee doesn't exist, an error will be thrown when getting by id
        Employee employee = employeeService.getEmployeeByEmployeeId(employeeId);
        User employeeManager = employeeService.getManagerOfEmployee(employeeId);
        return employeeManager.getId().equals(manager.getId());
    }


    @Override
    public ResponseEntity<Response> getWorkingSchedules(User manager, long monthNumber) {
        List<WorkingScheduleResponse> workingSchedules = managerRepository.getWorkingSchedules(monthNumber);
        workingSchedules.removeIf(workingSchedule -> !workingSchedule.getEmployeeId().equals(manager.getId()));
        TreeMap<Date, List<WorkingScheduleResponse>> collect = workingSchedules.stream().collect(Collectors.groupingBy(WorkingScheduleResponse::getDate, TreeMap::new, Collectors.toList()));
        return ResponseEntity.ok(Response.builder().status(200).message("Get working schedule successfully!").data(collect).build());
    }

    @Override
    public void save(Manager manager) {
        managerRepository.save(manager);
    }

    @Override
    public ResponseEntity<Response> getAllEmployees(User manager) {
        Collection<Employee> employeeList = managerRepository.getAllEmployees();
        employeeList.removeIf(employee -> !Objects.equals(employee.getManagerId(), manager.getId()));
        return ResponseEntity.ok(Response.builder().status(200).message("!!!").data(employeeList).build());
    }

}

