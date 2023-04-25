package com.example.Employee_Management_System.mapper;

import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.response.TaskDetailedInfo;
import com.example.Employee_Management_System.model.TaskDetailsForProject;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TaskMapper {
    @Insert("INSERT INTO tasks (title, description, start_date, end_date, status, priority, completion, estimate_hours, employee_id, parent_id, project_id) VALUES (#{title}, #{description}, #{startDate}, #{endDate}, #{status}, #{priority}, #{completion}, #{estimateHours}, #{employeeId}, #{parentId}, #{projectId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Task task);

    // delete task and its subtasks (hide them)
    void delete(Long id);

    TaskDetailedInfo getTaskById(Long taskId);

    List<TaskDetailedInfo> getTasksByEmployeeId(Long employeeId);


    @Update("UPDATE tasks SET title = #{title}, description = #{description}, start_date = #{startDate}, end_date = #{endDate}, status = #{status}, priority = #{priority}, completion = #{completion}, estimate_hours = #{estimateHours}, employee_id = #{employeeId}, parent_id = #{parentId}, project_id= #{projectId}, employee_review = #{employeeReview}, manager_review = #{managerReview} WHERE id = #{id}")
    void update(Task task);

    User getManagerOfTask(long taskId);

    User getEmployeeOfTask(Long taskId);

    List<TaskDetailedInfo> getSubTasks(long parentId);

    List<TaskDetailedInfo> getTasksByManagerId(Long managerId);

    List<TaskDetailsForProject> getAllProjectDetailsById(Long id);
}
