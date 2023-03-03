package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.repository.TaskRepository;
import com.example.Employee_Management_System.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task getTaskByTaskId(long taskId) {
        // TODO: custom exception
        return taskRepository.getTask(taskId);
    }

    @Override
    public User getEmployeeOfTask(Long taskId) {
        return taskRepository.getEmployeeOfTask(taskId);
    }
}
