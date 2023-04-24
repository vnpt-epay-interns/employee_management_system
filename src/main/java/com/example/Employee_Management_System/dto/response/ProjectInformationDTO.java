package com.example.Employee_Management_System.dto.response;

import com.example.Employee_Management_System.model.ProjectBriefInformation;
import lombok.Data;

import java.util.Map;

@Data
public class ProjectInformationDTO {
    private Long id;
    private String name;
    private Integer employeeNum;
    private Integer taskNum;
    private Map<String, Integer> taskNumByStatus;

    public ProjectInformationDTO(ProjectBriefInformation information) {
        this.id = information.getId();
        this.name = information.getName();
        this.employeeNum = information.getEmployeeNum();
        this.taskNum = information.getTaskNum();
        this.taskNumByStatus = Map.of(
                "NEW", information.getNewTaskNum(),
                "IN_PROGRESS", information.getInProgressTaskNum(),
                "READY_FOR_REVIEW", information.getReadyForReviewTaskNum(),
                "DONE", information.getDoneTaskNum()
        );
    }
    public Integer getTaskNum() {
        return taskNumByStatus.values().stream().reduce(0, Integer::sum);
    }
}
