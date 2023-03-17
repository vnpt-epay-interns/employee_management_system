package com.example.Employee_Management_System.repository;

import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.response.TaskDetailedInfo;
import com.example.Employee_Management_System.mapper.TaskMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class TaskRepository {
    private final TaskMapper taskMapper;

    public Long saveTask(Task task) {
        return taskMapper.save(task);
    }

    public void updateTask(Task task) {
        taskMapper.update(task);
    }

    public void updateTask(TaskDetailedInfo taskInfo) {
        Task task = Task.builder()
                .id(taskInfo.getId())
                .title(taskInfo.getTitle())
                .description(taskInfo.getDescription())
                .endDate(taskInfo.getEndDate())
                .startDate(taskInfo.getStartDate())
                .status(taskInfo.getStatus())
                .priority(taskInfo.getPriority())
                .completion(taskInfo.getCompletion())
                .estimateHours(taskInfo.getEstimateHours())
                .employeeId(taskInfo.getEmployeeId())
                .projectId(taskInfo.getProjectId())
                .build();
        taskMapper.update(task);
    }


    public void deleteTaskById(Long id) {
        taskMapper.delete(id);
    }

    public TaskDetailedInfo getTaskById(long taskId) {
        return taskMapper.getTaskById(taskId);
    }



    public User getManagerOfTask(long taskId) {
        return taskMapper.getManagerOfTask(taskId);
    }

    public User getEmployeeOfTask(Long taskId) {
        return taskMapper.getEmployeeOfTask(taskId);
    }

    //TODO: make it return taskDetailedInfo

    public List<TaskDetailedInfo> getTasksByEmployeeId(Long employeeId) {
        return taskMapper.getTasksByEmployeeId(employeeId);
    }

    public List<TaskDetailedInfo> getTasksByManagerId(Long managerId) {
        return taskMapper.getTasksByManagerId(managerId);
    }


}
