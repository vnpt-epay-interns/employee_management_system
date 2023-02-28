package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.*;
import com.example.Employee_Management_System.dto.request.ScheduleWorkingDayRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskEmployeeRequest;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.response.TaskDTO;
import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse;
import com.example.Employee_Management_System.mapper.EmployeeMapper;
import com.example.Employee_Management_System.model.EmployeeInformation;
import com.example.Employee_Management_System.repository.EmployeeRepository;
import com.example.Employee_Management_System.repository.TaskRepository;
import com.example.Employee_Management_System.service.EmployeeService;
import com.example.Employee_Management_System.service.ReportService;
import com.example.Employee_Management_System.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    private final ReportService reportService;

    private final TaskService taskService;

    private final TaskRepository taskRepository;

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
        WorkingSchedule workingSchedule = WorkingSchedule.builder()
                .date(request.getDate())
                .atMorning(request.isAtMorning())
                .atAfternoon(request.isAtAfternoon())
                .employeeId(employee.getId()).build();
        employeeRepository.scheduleWorkingDays(workingSchedule);
        return ResponseEntity.ok(Response.builder().message("!!!").status(200).data(null).build());
    }

    @Override
    public ResponseEntity<Response> getWorkingDays(User employee) {
        List<WorkingScheduleResponse> workingScheduleResponses = employeeRepository.getSchedule(employee);
        return ResponseEntity.ok(Response.builder().data(workingScheduleResponses).build());
    }

    @Override
    public void save(Employee employee) {
        employeeRepository.save(employee);
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

    @Override
    public ResponseEntity<Response> getEmployeeInfo(User employee) {

        // the employee information is the same as the user information, no need query the database
        EmployeeInformation employeeInformation = new EmployeeInformation();
        employeeInformation.setId(employee.getId());
        employeeInformation.setFirstName(employee.getFirstName());
        employeeInformation.setLastName(employee.getLastName());
        employeeInformation.setEmail(employee.getEmail());
        employeeInformation.setAvatar(employee.getAvatar());

        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Get employee info successfully")
                        .data(employeeInformation)
                        .build()
        );
    }

}

