package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.response.TaskDetailedInfo;
import com.example.Employee_Management_System.model.TaskDetailsForProject;

import java.util.List;

public interface TaskService {
    TaskDetailedInfo getTaskById(Long taskId);

    TaskDetailedInfo getTaskByIdAndEmployeeId(Long taskId, Long employeeId);

    List<TaskDetailedInfo> getTasksByEmployeeId(Long employeeId);

    void hideTaskById(TaskDetailedInfo task);

    TaskDetailedInfo createTask( CreateTaskRequest request);

    TaskDetailedInfo saveTask(Task task);

    TaskDetailedInfo updateTask(TaskDetailedInfo task);

    User getManagerOfTask(long taskId);

    List<TaskDetailedInfo> getSubTasks(long taskId);

    List<TaskDetailedInfo> getAllTasksByMangerId(Long managerId);

    List<TaskDetailsForProject> getAllProjectDetailsById(Long id);
}
