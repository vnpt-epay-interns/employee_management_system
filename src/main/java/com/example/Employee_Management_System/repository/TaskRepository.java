package com.example.Employee_Management_System.repository;

import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.mapper.TaskMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
