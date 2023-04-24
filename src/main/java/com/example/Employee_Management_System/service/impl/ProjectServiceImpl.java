package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Project;
import com.example.Employee_Management_System.dto.request.CreateProjectRequest;
import com.example.Employee_Management_System.exception.NotFoundException;
import com.example.Employee_Management_System.repository.ProjectRepository;
import com.example.Employee_Management_System.service.ProjectService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final String REDIS_KEY_FOR_PROJECT = "project";

    @Override
    public void createProject(CreateProjectRequest createProjectRequest, Long managerId) {
        Project project = Project.builder()
                .name(createProjectRequest.getName())
                .managerId(managerId)
                .build();
        projectRepository.save(project);
        cacheProjectsToRedis(List.of(project));
    }

    @Override
    public Project getProjectById(Long id) {
        List<Project> allProjectInRedis = getAllProjectsInRedis();
        for (Project project: allProjectInRedis) {
            if (project.getId().equals(id)) {
                return project;
            }
        }
        Project project = projectRepository.getProjectById(id);
        if (project == null) {
            throw new NotFoundException("Project not found");
        }
        return project;
    }

    @Override
    public List<Project> getAllProjects() {
        return getAllProjectsInRedis();
    }

    @Override
    public Project updateProject(Long id, String name, Long managerId) {
        Project updatedProject = new Project(id, name, managerId);
        List<Project> allProjectsInRedis = getAllProjectsInRedis();
        for (Project project: allProjectsInRedis) {
            if (Objects.equals(project.getId(), updatedProject.getId())) {
                projectRepository.updateProject(updatedProject.getId(), updatedProject.getName(), updatedProject.getManagerId());
            }
        }
        cacheProjectsToRedis(allProjectsInRedis);

        projectRepository.updateProject(updatedProject.getId(), updatedProject.getName(), updatedProject.getManagerId());
        return updatedProject;
    }

    @Override
    public void deleteProjectById(Long id) {
        List<Project> allProjectsInRedis = getAllProjectsInRedis();

        if (allProjectsInRedis != null && !allProjectsInRedis.isEmpty()) {
            allProjectsInRedis = allProjectsInRedis.stream()
                    .filter(project -> !project.getId().equals(id))
                    .collect(Collectors.toList());
            cacheProjectsToRedis(allProjectsInRedis);
        }
        projectRepository.deleteProjectById(id);
    }

    @Override
    public List<Project> getAllProjectsByManagerId(Long id) {
        return projectRepository.getAllProjectNamesByManagerId(id);
    }

    private List<Project> getAllProjectsInRedis() {
        List<Project> projects;
        Gson gson = new Gson();


        //get all project from Redis
        List<Object> allProjectsInRedis = redisTemplate.opsForHash().values(REDIS_KEY_FOR_PROJECT);
        if (allProjectsInRedis == null || allProjectsInRedis.isEmpty()) {
            return null;
        } else {
            //if there are projects in Redis, get them
            return allProjectsInRedis.stream()
                    .map(project -> gson.fromJson((String) project, Project.class))
                    .collect(Collectors.toList());
        }
    }

    private void cacheProjectsToRedis(List<Project> allProjects) {
        Gson gson = new Gson();
        Map<String, String> map = allProjects.stream()
                .collect(Collectors.toMap(
                        project -> String.valueOf(project.getId()),
                        project -> gson.toJson(project)
                ));
        redisTemplate.opsForHash().putAll(REDIS_KEY_FOR_PROJECT, map);
    }
}
