package com.example.Employee_Management_System.mapper;

import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.response.TaskDetailedInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TaskMapper {
    Long save(Task task);

    // delete task and its subtasks (hide them)
    void delete(Long id);

    TaskDetailedInfo getTaskById(Long taskId);

    List<TaskDetailedInfo> getTasksByEmployeeId(Long employeeId);


    @Update("UPDATE tasks SET title = #{title}, description = #{description}, start_date = #{startDate}, end_date = #{endDate}, status = #{status}, priority = #{priority}, completion = #{completion}, estimate_hours = #{estimateHours}, employee_id = #{employeeId}, parent_id = #{parentId}, project_id= #{projectId} WHERE id = #{id}")
    void update(Task task);

    User getManagerOfTask(long taskId);

    User getEmployeeOfTask(Long taskId);

    List<TaskDetailedInfo> getSubTasks(long parentId);

    List<TaskDetailedInfo> getTasksByManagerId(Long managerId);
}
