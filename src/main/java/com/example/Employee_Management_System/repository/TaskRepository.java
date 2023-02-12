package com.example.Employee_Management_System.repository;

import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.mapper.TaskMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class TaskRepository {
    private final TaskMapper taskMapper;

    public void save(Task task) {
        taskMapper.save(task);
    }

    public void updateTask(Task task) {
        taskMapper.update(task);
    }

    public void createTask(Task task) {
        taskMapper.save(task);
    }

    public void deleteTask(Task task) {
        taskMapper.delete(task);
    }

    public Optional<Task> getTask(long taskId) {
        return Optional.ofNullable(taskMapper.getTask(taskId));
    }

    public User getManagerOfEmployee(Long taskId) {
        return taskMapper.getManagerOfEmployee(taskId);
    }

    public User getManagerOfTask(long taskId) {
        return taskMapper.getManagerOfTask(taskId);
    }

    public User getEmployeeOfTask(Long taskId) {
        return taskMapper.getEmployeeOfTask(taskId);
    }
}
