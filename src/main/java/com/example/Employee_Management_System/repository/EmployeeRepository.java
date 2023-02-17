package com.example.Employee_Management_System.repository;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Task;

import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.domain.WorkingSchedule;
import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse;
import com.example.Employee_Management_System.mapper.EmployeeMapper;
import com.example.Employee_Management_System.mapper.TaskMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class EmployeeRepository {
    private final EmployeeMapper employeeMapper;
    private final TaskMapper taskMapper;

    public Optional<Task> getTaskByIdAndEmployeeId(Long taskId, Long employeeId) {
        return Optional.ofNullable(taskMapper.getTaskByIdAndEmployeeId(taskId, employeeId));
    }

    public List<Task> getTasksByParentTask(Long parentTaskId) {
        return taskMapper.getTasksByParentTask(parentTaskId);
    }

    public List<Task> getTasksByEmployeeId(Long employeeId) {
        return taskMapper.getTasksByEmployeeId(employeeId);
    }

    public void save(Employee employee) {
        employeeMapper.save(employee);
    }

    public List<Employee> getAllEmployeesByManagerId(Long managerId) {
        return employeeMapper.getAllEmployeesByManagerId(managerId);
    }

    public List<WorkingScheduleResponse> getSchedule(User employee) {
        return employeeMapper.getWorkingSchedule(employee);
    }

    public void scheduleWorkingDays(WorkingSchedule workingSchedule) {
        employeeMapper.scheduleWorkingDays(workingSchedule);
    }
}
