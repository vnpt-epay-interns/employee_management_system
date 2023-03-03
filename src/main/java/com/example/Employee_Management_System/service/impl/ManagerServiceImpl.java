package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.*;
import com.example.Employee_Management_System.dto.request.CreateProjectRequest;
import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.response.TaskDTO;
import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse;
import com.example.Employee_Management_System.exception.ReportException;
import com.example.Employee_Management_System.model.EmployeeInformation;
import com.example.Employee_Management_System.model.ManagerInformation;
import com.example.Employee_Management_System.model.ReportBasicInfo;
import com.example.Employee_Management_System.repository.ManagerRepository;
import com.example.Employee_Management_System.repository.ProjectRepository;
import com.example.Employee_Management_System.repository.TaskRepository;
import com.example.Employee_Management_System.repository.UserRepository;
import com.example.Employee_Management_System.service.EmployeeService;
import com.example.Employee_Management_System.service.ManagerService;
import com.example.Employee_Management_System.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    private final ManagerRepository managerRepository;
    private final TaskRepository taskRepository;
    private final ReportService reportService;
    private final EmployeeService employeeService;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Override
    public ResponseEntity<Response> createTask(CreateTaskRequest request) {
        if (request.getParentId() != null) {
            System.out.println(request.getParentId());
            Task parenTask = managerRepository
                    .getTaskById(request.getParentId());
            System.out.println(parenTask);
            if (parenTask == null) {
                throw new IllegalStateException("Parent task is not exist!");
            }
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
                .parentId(request.getParentId())
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
        Task task = managerRepository
                .getTaskById(taskId);
        if (task == null) {
            throw new IllegalStateException("Task not found!");
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
    public ResponseEntity<Response> updateTask(long taskId, UpdateTaskRequest updateTaskRequest) {
        Task task = managerRepository
                .getTaskById(taskId);
        if (task == null) {
            throw new IllegalStateException("Task not found!");
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
        task.setParentId(updateTaskRequest.getParentId());

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
                .message("Get all reports ts successfully!")
                .data(unreadReportsByTaskId)
                .build()
        );
    }

    @Override
    public ResponseEntity<Response> getReferenceCode(User manager) {
        String referenceCode = managerRepository.getReferenceCode(manager.getId());
        Map<String, String> map = new HashMap<>();
        map.put("referenceCode", referenceCode);

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get reference code successfully!")
                .data(map)
                .build()
        );
    }

    @Override
    public Optional<ManagerInformation> getManagerInfo(String referencedCode) {
        return managerRepository.getManagerInfo(referencedCode);
    }

    @Override
    public ResponseEntity<Response> getAllTasks(User manager) {
        List<TaskDTO> tasks = managerRepository.getAllTasks(manager.getId());
        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(200)
                        .message("Get all tasks successfully!")
                        .data(tasks)
                        .build()
        );
    }

    public ResponseEntity<Response> getEmployeeBelongToManager(User manager) {
        List<EmployeeInformation> employees = userRepository.getEmployeeBelongToManager(manager.getId());
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get all employees successfully!")
                .data(employees)
                .build()
        );
    }

    @Override
    public ResponseEntity<Response> createProject(CreateProjectRequest createProjectRequest, Long managerId) {
        Project project = Project.builder()
                .name(createProjectRequest.getName())
                .managerId(managerId).build();
        projectRepository.save(project);
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Create project successfully!")
                .data(project)
                .build()
        );
    }

    @Override
    public ResponseEntity<Response> getProjectById(Long id) {
        Project findProject = projectRepository.getProjectById(id);
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get project successfully!")
                .data(findProject)
                .build()
        );
    }

    @Override
    public ResponseEntity<Response> getAllProjects() {
        List<Project> allProjects = projectRepository.getAllProjects();
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get all projects successfully!")
                .data(allProjects)
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
        List<WorkingScheduleResponse> workingSchedules = managerRepository.getWorkingSchedulesOfEmployeeByManagerId(manager, monthNumber);
//        workingSchedules.removeIf(workingSchedule -> !workingSchedule.getEmployeeId().equals(manager.getId()));

        TreeMap<Date, List<WorkingScheduleResponse>> collect = workingSchedules.stream()
                .collect(Collectors.groupingBy(WorkingScheduleResponse::getDate, TreeMap::new, Collectors.toList()));

        System.out.println(collect);
        return ResponseEntity.ok(Response.builder().status(200).message("Get working schedule successfully!").data(collect).build());
    }

    @Override
    public void save(Manager manager) {
        managerRepository.save(manager);
    }


}

