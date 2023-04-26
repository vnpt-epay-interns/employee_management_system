package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.domain.Project;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.CreateProjectRequest;
import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.response.ProjectInformationDTO;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.response.TaskDetailedInfo;
import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse;
import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse.EmployeeSchedule;
import com.example.Employee_Management_System.exception.ReportException;
import com.example.Employee_Management_System.model.*;
import com.example.Employee_Management_System.repository.ManagerRepository;
import com.example.Employee_Management_System.repository.ProjectRepository;
import com.example.Employee_Management_System.repository.UserRepository;
import com.example.Employee_Management_System.service.EmployeeService;
import com.example.Employee_Management_System.service.ManagerService;
import com.example.Employee_Management_System.service.ReportService;
import com.example.Employee_Management_System.service.TaskService;
import com.example.Employee_Management_System.utils.CalendarHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.Employee_Management_System.dto.response.WorkingScheduleResponse.MonthInfo;

@Service
@AllArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    private final ManagerRepository managerRepository;
//    private final ProjectService projectService;
    private final TaskService taskService;
    private final ReportService reportService;
    private final EmployeeService employeeService;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
//    private final ReportRepository reportRepository;
//    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public ResponseEntity<Response> createTask(CreateTaskRequest request) {

        taskService.createTask(request);
        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(200)
                        .message("Create task successfully!")
                        .build()
        );
    }

    @Override
    public ResponseEntity<Response> hideTaskById(long taskId) {
        TaskDetailedInfo task = taskService.getTaskById(taskId);
        if (task == null) {
            throw new IllegalStateException("Task not found!");
        }

        taskService.hideTaskById(task);
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
        if (updateTaskRequest.getParentId() != null) {
            TaskDetailedInfo parentTask = taskService.getTaskById(updateTaskRequest.getParentId());
            if (parentTask == null) {
                throw new IllegalStateException("Parent task is not exist!");
            }
        }

        TaskDetailedInfo task = taskService.getTaskById(taskId);
        task.setId(taskId);
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
        task.setProjectId(updateTaskRequest.getProjectId());
        task.setEmployeeReview(updateTaskRequest.getEmployeeReview());
        task.setManagerReview(updateTaskRequest.getManagerReview());
        task.setManagerId(task.getManagerId());

        taskService.updateTask(task);
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
        List<ReportDetailedInfo> unreadReports = reportService.getAllReports(manager);

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
        ReportDetailedInfo report = reportService.getReportById(reportId);
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

    private boolean checkIfReportBelongsToEmployeeOfManager(ReportDetailedInfo report, User manager) {
        User employeeManager = reportService.getManagerOfEmployeeReport(report.getId());
        return employeeManager.getId().equals(manager.getId());
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
        List<TaskDetailedInfo> tasks = taskService.getAllTasksByMangerId(manager.getId());
        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(200)
                        .message("Get all tasks successfully!")
                        .data(tasks)
                        .build()
        );
    }

    @Override
    public List<EmployeeInformation> getEmployeeBelongToManager(User manager) {
        List<EmployeeInformation> employees = employeeService.getEmployeesBelongToManager(manager.getId());
        return employees;
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
    public ResponseEntity<Response> getAllProjectNamesByManagerId(Long managerId) {
        List<Project> allProjects = projectRepository.getAllProjectNamesByManagerId(managerId);
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get all projects successfully!")
                .data(allProjects)
                .build()
        );
    }

    @Override
    public ResponseEntity<Response> getAllSubTasks(User manager, long taskId) {
        if (!checkIfTaskBelongsToEmployeeOfManager(manager, taskId)) {
            throw new ReportException("You are not allowed to view this task");
        }

        List<TaskDetailedInfo> subTasks = taskService.getSubTasks(taskId);
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get all sub tasks successfully!")
                .data(subTasks)
                .build()
        );
    }

    @Override
    public ResponseEntity<Response> getTaskById(User manager, long taskId) {
        TaskDetailedInfo task = taskService.getTaskById(taskId);
        if (!checkIfTaskBelongsToEmployeeOfManager(manager, taskId)) {
            throw new ReportException("You are not allowed to view this task");
        }
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get task successfully!")
                .data(task)
                .build()
        );
    }

    @Override
    public ResponseEntity<Response> getAllProjectInformationByManagerId(Long managerId) {
        List<ProjectBriefInformation> allProjects = projectRepository.getAllProjectInformationByManagerId(managerId);
        List<ProjectInformationDTO> projectInformationDTOS = allProjects.stream().map(ProjectInformationDTO::new).toList();

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get all projects successfully!")
                .data(projectInformationDTOS)
                .build()
        );
    }

    @Override
    public ResponseEntity<Response> getProjectDetailsById(Long id, Long managerId) {
        List<TaskDetailsForProject> taskDetailsForProjectsList = taskService.getAllProjectDetailsById(id);
        Project project = projectRepository.getProjectById(id);

        if (!project.getManagerId().equals(managerId)) {
            throw new ReportException("You are not allowed to view this project");
        }

        ProjectDetails projectDetails = ProjectDetails.builder()
                .id(project.getId())
                .name(project.getName())
                .tasks(taskDetailsForProjectsList)
                .build();

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get project details successfully!")
                .data(projectDetails)
                .build()
        );
    }


    private boolean checkIfTaskBelongsToEmployeeOfManager(User currentManager, long taskId) {
        User manager = taskService.getManagerOfTask(taskId);
        return manager.getId().equals(currentManager.getId());
    }

    @Override
    public ResponseEntity<Response> getReportEmployeeId(User manager, long employeeId) {
        List<ReportDetailedInfo> unreadReportsByEmployeeId = reportService.getAllUnreadReportsByEmployeeId(manager, employeeId);


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
    public ResponseEntity<Response> getEmployeeWorkingSchedules(User manager, int year,  int month) {
        // the year is set to the current year
//        int year = Year.now().getValue();

        MonthInfo monthInfo = CalendarHelper.getMonthInfo(year, month);
        List<EmployeeSchedule> schedules = getEmployeeWorkingSchedule(manager, year, month, monthInfo);

        WorkingScheduleResponse body = WorkingScheduleResponse.builder()
                .monthInfo(monthInfo)
                .schedules(schedules)
                .build();

        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Get employee working schedules successfully!")
                        .data(body)
                        .build()
        );

    }

    private List<EmployeeSchedule> getEmployeeWorkingSchedule(User manager, int year, int month, MonthInfo monthInfo) {

        List<EmployeeInformation> managedEmployee = userRepository.getEmployeeBelongToManager(manager.getId());
        List<EmployeeSchedule> employeeSchedules = new ArrayList<>();

        // sort the employees by first name
        managedEmployee.sort(Comparator.comparing(EmployeeInformation::getFirstName));

        // get all employee schedules of employees who are managed by the manager
        for (EmployeeInformation employeeInfo : managedEmployee) {
            EmployeeSchedule employeeSchedule = employeeService.getEmployeeSchedule(employeeInfo, year, month, monthInfo);
            employeeSchedules.add(employeeSchedule);

        }
        return employeeSchedules;
    }

    @Override
    public void save(Manager manager) {
        managerRepository.save(manager);
    }

}

