package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Task;

public interface EmployeeService {

    public Task viewTask(Employee employee);
    public void updateTask(Task task);



}
