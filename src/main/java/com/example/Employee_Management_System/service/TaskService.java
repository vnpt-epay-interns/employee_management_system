package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.response.TaskDTO;

import java.util.List;

public interface TaskService {
    //tODO: this one
    Task getTaskByTaskId(long taskId);
    User getEmployeeOfTask(Long taskId);

    List<TaskDTO> getSubTasks(long taskId);
}
