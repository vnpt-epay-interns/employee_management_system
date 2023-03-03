package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;

public interface TaskService {
    //tODO: this one
    Task getTaskByTaskId(long taskId);
    User getEmployeeOfTask(Long taskId);

}
