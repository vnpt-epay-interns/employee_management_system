package com.example.Employee_Management_System.repository;

import com.example.Employee_Management_System.domain.Project;
import com.example.Employee_Management_System.mapper.ProjectMapper;
import com.example.Employee_Management_System.model.ProjectBriefInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectRepository {

    @Autowired
    private ProjectMapper projectMapper;


    public void save(Project project) {
        projectMapper.save(project);
    }

    public Project getProjectById(Long id) {
        return projectMapper.getProjectById(id);
    }

    public List<Project> getAllProjectNamesByManagerId(Long managerId) {
        return projectMapper.getAllProjectsByManagerId(managerId);
    }

    public void updateProject(Long id, String name, Long managerId) {
        projectMapper.updateProject(id, name, managerId);
    }

    public void deleteProjectById(Long id) {
        projectMapper.deleteProjectById(id);
    }


    public List<ProjectBriefInformation> getAllProjectInformationByManagerId(Long managerId) {
        return projectMapper.getAllProjectInformationByManagerId(managerId);
    }
}
