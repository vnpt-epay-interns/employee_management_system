package com.example.Employee_Management_System.mapper;

import com.example.Employee_Management_System.domain.Project;
import com.example.Employee_Management_System.model.ProjectBriefInformation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProjectMapper {

    @Insert("INSERT INTO projects (name, manager_id) VALUES (#{name}, #{managerId})")
    void save(Project project);

    Project getProjectById(Long id);

    List<Project> getAllProjectsByManagerId(Long managerId);

    @Update("UPDATE projects SET name = #{name}, manager_id = #{managerId} WHERE id = #{id}")
    void updateProject(Long id, String name, Long managerId);

    @Delete("DELETE FROM projects WHERE id = #{id}")
    void deleteProjectById(Long id);


    List<ProjectBriefInformation> getAllProjectInformationByManagerId(Long managerId);
}
