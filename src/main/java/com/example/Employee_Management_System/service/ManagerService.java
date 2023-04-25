package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.CreateProjectRequest;
import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.model.EmployeeInformation;
import com.example.Employee_Management_System.model.ManagerInformation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ManagerService {

    ResponseEntity<Response> createTask(CreateTaskRequest request);

    ResponseEntity<Response> hideTaskById(long taskId);

    ResponseEntity<Response> updateTask(long taskId, UpdateTaskRequest updateTaskRequest);

    ResponseEntity<Response> getAllReports(User manager);

    ResponseEntity<Response> getReportById(User manager, long reportId);

    ResponseEntity<Response> getReportEmployeeId(User manager, long employeeId);

    ResponseEntity<Response> getEmployeeWorkingSchedules(User manager, int year, int month);

    void save(Manager manager);

    ResponseEntity<Response> getReferenceCode(User manager);

    Optional<ManagerInformation> getManagerInfo(String referencedCode);

    ResponseEntity<Response> getAllTasks(User manager);

    List<EmployeeInformation> getEmployeeBelongToManager(User manager);

    ResponseEntity<Response> createProject(CreateProjectRequest createProjectRequest, Long managerId);

    ResponseEntity<Response> getProjectById(Long id);

    ResponseEntity<Response> getAllProjectNamesByManagerId(Long managerId);

    ResponseEntity<Response> getAllSubTasks(User manager, long taskId);

    ResponseEntity<Response> getTaskById(User manager, long taskId);

    ResponseEntity<Response> getAllProjectInformationByManagerId(Long managerId);

    ResponseEntity<Response> getProjectDetailsById(Long id, Long managerId);
}
