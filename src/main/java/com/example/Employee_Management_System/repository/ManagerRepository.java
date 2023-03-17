package com.example.Employee_Management_System.repository;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.dto.response.TaskDetailedInfo;
import com.example.Employee_Management_System.mapper.ManagerMapper;
import com.example.Employee_Management_System.mapper.TaskMapper;
import com.example.Employee_Management_System.model.EmployeeInformation;
import com.example.Employee_Management_System.model.ManagerInformation;
import com.example.Employee_Management_System.service.TaskService;
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
    private TaskService taskService;

    @Autowired
    private TaskMapper taskMapper;

    public void save(Manager manager) {
        managerMapper.save(manager);
    }

    public Manager findManagerByReferenceCode(String referenceCode) {
        return managerMapper.findByReferenceCode(referenceCode);
    }

    public TaskDetailedInfo getTaskById(long taskId) {
        return taskService.getTaskById(taskId);
    }

    public String getReferenceCode(Long id) {
        return managerMapper.getReferenceCode(id);
    }

    public Optional<ManagerInformation> getManagerInfo(String referencedCode) {
        return managerMapper.getManagerInfo(referencedCode);
    }

    public List<TaskDetailedInfo> getAllTasks(Long managerId) {
        return taskService.getAllTasksByMangerId(managerId);
    }

    public List<EmployeeInformation> getAllEmployees(Long id) {
        return managerMapper.getAllEmployees(id);
    }
}
