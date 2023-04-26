package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Project;
import com.example.Employee_Management_System.dto.request.CreateProjectRequest;
import com.example.Employee_Management_System.exception.NotFoundException;
import com.example.Employee_Management_System.repository.ProjectRepository;
import com.example.Employee_Management_System.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public void createProject(CreateProjectRequest createProjectRequest, Long managerId) {
        Project project = Project.builder()
                .name(createProjectRequest.getName())
                .managerId(managerId)
                .build();
        projectRepository.save(project);
    }

    @Override
    public Project getProjectById(Long id) {

        Project project = projectRepository.getProjectById(id);
        if (project == null) {
            throw new NotFoundException("Project not found");
        }
        return project;
    }

    @Override
    public Project updateProject(Long id, String name, Long managerId) {
        Project updatedProject = new Project(id, name, managerId);


        projectRepository.updateProject(updatedProject.getId(), updatedProject.getName(), updatedProject.getManagerId());
        return updatedProject;
    }

    @Override
    public void deleteProjectById(Long id) {
        projectRepository.deleteProjectById(id);
    }

    @Override
    public List<Project> getAllProjectsByManagerId(Long id) {
        return projectRepository.getAllProjectNamesByManagerId(id);
    }


}
