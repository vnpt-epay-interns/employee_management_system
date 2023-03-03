package com.example.Employee_Management_System.repository;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.response.TaskDTO;
import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse;
import com.example.Employee_Management_System.mapper.ManagerMapper;
import com.example.Employee_Management_System.mapper.TaskMapper;
import com.example.Employee_Management_System.model.ManagerInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class ManagerRepository {

    @Autowired
    private ManagerMapper managerMapper;

    @Autowired
    private TaskMapper taskMapper;

    public void save(Manager manager) {
        managerMapper.save(manager);
    }

    public Manager findManagerByReferenceCode(String referenceCode) {
        return managerMapper.findByReferenceCode(referenceCode);
    }

    public Collection<Employee> getAllEmployees(Long managerId) {
        return managerMapper.getAllEmployees(managerId);
    }

    public List<WorkingScheduleResponse> getWorkingSchedulesOfEmployeeByManagerId(User manager, long monthNumber) {
        return managerMapper.getWorkingSchedules(manager.getId(), monthNumber);
    }

    public Task getTaskById(long taskId) {
        return taskMapper.getTaskById(taskId);
    }

    public String getReferenceCode(Long id) {
        return managerMapper.getReferenceCode(id);
    }

    public Optional<ManagerInformation> getManagerInfo(String referencedCode) {
        return managerMapper.getManagerInfo(referencedCode);
    }

    public List<TaskDTO> getAllTasks(Long managerId) {
        return managerMapper.getAllTasks(managerId);
    }
}
