package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.domain.Project;
import com.example.Employee_Management_System.dto.request.CreateProjectRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.model.TaskDetailsForProject;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProjectService {

    void createProject(CreateProjectRequest createProjectRequest, Long managerId);
    Project getProjectById(Long id);
//    List<Project> getAllProjects();
    Project updateProject(Long id, String name, Long managerId);
    void deleteProjectById(Long id);


    List<Project> getAllProjectsByManagerId(Long id);
}
