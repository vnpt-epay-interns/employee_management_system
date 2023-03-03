package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Project;
import com.example.Employee_Management_System.dto.request.CreateProjectRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.repository.ProjectRepository;
import com.example.Employee_Management_System.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

//    @Autowired
//    private ProjectRepository projectRepository;
//
//    @Override
//    public ResponseEntity<Response> createProject(CreateProjectRequest createProjectRequest, Long managerId) {
//        Project project = Project.builder()
//                .name(createProjectRequest.getName())
//                .managerId(managerId)
//                .build();
//        projectRepository.save(project, ma);
//        return ResponseEntity.ok(Response.builder()
//                .message("Project created successfully")
//                .build());
//    }
//
//    @Override
//    public ResponseEntity<Response> getProjectById(Long id) {
//        Project project = projectRepository.getProjectById(id);
//        return ResponseEntity.ok(Response.builder()
//                .message("Project retrieved successfully")
//                .data(project)
//                .build());
//    }
//
//    @Override
//    public ResponseEntity<Response> getAllProjects() {
//        List<Project> allProjects = projectRepository.getAllProjects();
//        return ResponseEntity.ok(Response.builder()
//                .message("All projects retrieved successfully")
//                .data(allProjects)
//                .build());
//    }
//
//    @Override
//    public ResponseEntity<Response> updateProject(Long id, String name, Long managerId) {
//        projectRepository.updateProject(id, name, managerId);
//        return ResponseEntity.ok(Response.builder()
//                .message("Project updated successfully")
//                .build());
//    }
//
//    @Override
//    public ResponseEntity<Response> deleteProjectById(Long id) {
//        projectRepository.deleteProjectById(id);
//        return ResponseEntity.ok(Response.builder()
//                .message("Project deleted successfully")
//                .build());
//    }
}
