package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.ScheduleWorkingDayRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.repository.EmployeeRepository;
import com.example.Employee_Management_System.repository.TaskRepository;
import com.example.Employee_Management_System.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public ResponseEntity<Response> getTaskById(long id, User user) {
        Task task = taskRepository.getTask(id).orElseThrow(() -> new RuntimeException("Task not found"));

        if (task.getId() != user.getId()) {
            throw new RuntimeException("You are not allowed to access this task");
        }
    }

    @Override
    public ResponseEntity<Response> getTasks(User employee) {
        return null;
    }

    @Override
    public ResponseEntity<Response> updateTask(User employee, UpdateTaskRequest updateTaskRequest) {
        return null;
    }

    @Override
    public ResponseEntity<Response> writeReport(User employee, WriteReportRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Response> scheduleWorkingDay(User employee, ScheduleWorkingDayRequest request) {
        return null;
    }

    @Override
    public void save(Employee employee) {
        employeeRepository.save(employee);
    }
}
