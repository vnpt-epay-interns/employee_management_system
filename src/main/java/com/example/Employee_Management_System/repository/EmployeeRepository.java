package com.example.Employee_Management_System.repository;

import com.example.Employee_Management_System.domain.Employee;

import com.example.Employee_Management_System.domain.WorkingSchedule;
import com.example.Employee_Management_System.mapper.EmployeeMapper;
import com.example.Employee_Management_System.model.WorkingScheduleDetailedInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@AllArgsConstructor
public class EmployeeRepository {
    private final EmployeeMapper employeeMapper;

    public void save(Employee employee) {
        employeeMapper.save(employee);
    }

    public Employee getEmployeeByEmployeeId(long employeeId) {
        return employeeMapper.getEmployeeByEmployeeId(employeeId).get();
    }

    public List<WorkingScheduleDetailedInfo> getWorkingSchedule(long employeeId, int year, int month) {
        return employeeMapper.getWorkingSchedule(employeeId, year, month + 1); // in Java month starts from 0, but in SQL month starts from 1
    }

    public void scheduleWorkingDays(List<WorkingSchedule> workingSchedules) {
        employeeMapper.saveWorkingSchedules(workingSchedules);
    }

    public String getReferenceCode(Long id) {
        return employeeMapper.getReferenceCode(id);
    }
}
