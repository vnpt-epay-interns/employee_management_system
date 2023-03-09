package com.example.Employee_Management_System.repository;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Task;

import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.domain.WorkingSchedule;
import com.example.Employee_Management_System.dto.response.TaskDTO;
import com.example.Employee_Management_System.mapper.EmployeeMapper;
import com.example.Employee_Management_System.mapper.TaskMapper;
import com.example.Employee_Management_System.model.WorkingScheduleDetailedInfo;
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

    public void save(Employee employee) {
        employeeMapper.save(employee);
    }

    public List<WorkingScheduleDetailedInfo> getWorkingSchedule(long employeeId, int year, int month) {
        return employeeMapper.getWorkingSchedule(employeeId, year, month + 1); // in Java month starts from 0, but in SQL month starts from 1
    }

    public void scheduleWorkingDays(List<WorkingSchedule> workingSchedules) {
        employeeMapper.saveWorkingSchedules(workingSchedules);
    }

    public List<TaskDTO> getTasksByEmployeeId(Long employeeId) {
        return taskMapper.getTasksByEmployeeId(employeeId);
    }

    public String getReferenceCode(Long id) {
        return employeeMapper.getReferenceCode(id);
    }
}
