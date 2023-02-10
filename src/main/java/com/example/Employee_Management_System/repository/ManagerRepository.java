package com.example.Employee_Management_System.repository;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.domain.WorkingSchedule;
import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse;
import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.mapper.ManagerMapper;
import com.example.Employee_Management_System.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Repository
public class ManagerRepository {

    @Autowired
    private ManagerMapper managerMapper;

    public void save(Manager manager) {
        managerMapper.save(manager);
    }

    public Manager findManagerByReferenceCode(String referenceCode) {
        return managerMapper.findByReferenceCode(referenceCode);
    }

    public Collection<Employee> getAllEmployees() {
        return managerMapper.getAllEmployees();
    }

    public List<WorkingScheduleResponse> getWorkingSchedules(long monthNumber) {
        return managerMapper.getWorkingSchedules(monthNumber);
    }
}
