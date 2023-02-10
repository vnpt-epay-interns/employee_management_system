package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.repository.ManagerRepository;
import com.example.Employee_Management_System.repository.TaskRepository;
import com.example.Employee_Management_System.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private TaskRepository taskRepository;

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
        return null;
    }

    @Override
    public ResponseEntity<Response> getReportById(long employeeId) {
        return null;
    }

    @Override
    public ResponseEntity<Response> getReportEmployeeId(long employeeId) {
        return null;
    }

    @Override
    public ResponseEntity<Response> getWorkingSchedule(long monthNumber) {
        return null;
    }

    @Override
    public void save(Manager manager) {
        managerRepository.save(manager);
    }
}
