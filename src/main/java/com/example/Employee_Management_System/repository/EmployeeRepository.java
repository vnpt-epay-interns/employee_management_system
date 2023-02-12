package com.example.Employee_Management_System.repository;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.mapper.EmployeeMapper;
import com.example.Employee_Management_System.mapper.TaskMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class EmployeeRepository {
    private final EmployeeMapper employeeMapper;
    private final TaskMapper taskMapper;

    public Task getTaskById(long taskId) {
        return taskMapper.getTask(taskId);
    }

    public void save(Employee employee) {
        employeeMapper.save(employee);
    }
}
