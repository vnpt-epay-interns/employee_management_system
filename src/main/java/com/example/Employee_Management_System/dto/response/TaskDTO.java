package com.example.Employee_Management_System.dto.response;

import com.example.Employee_Management_System.domain.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Task parentTask;
    private List<Task> subTasks;
}
