package com.example.Employee_Management_System.mapper;

import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.response.TaskDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TaskMapper {
    @Insert("INSERT INTO tasks (title, description, start_date, end_date, status, priority, completion, estimate_hours, employee_id, parent_id, project_id) VALUES (#{title}, #{description}, #{startDate}, #{endDate}, #{status}, #{priority}, #{completion}, #{estimateHours}, #{employeeId}, #{parentId}, #{projectId})")
    void save(Task task);

    @Delete("DELETE FROM tasks WHERE id = #{id}")
    void delete(Task task);

    Task getTaskAndEmployeeInfo(Long taskId);

    Task getTaskByIdAndEmployeeId(@Param("id") Long taskId, Long employeeId);

    List<TaskDTO> getTasksByEmployeeId(Long employeeId);

    List<Task> getTasks(Long employeeId);

    List<Task> getTasksByParentTask(Long parentTaskId);

    @Update("UPDATE tasks SET title = #{title}, description = #{description}, start_date = #{startDate}, end_date = #{endDate}, status = #{status}, priority = #{priority}, completion = #{completion}, estimate_hours = #{estimateHours}, employee_id = #{employeeId}, parent_id = #{parentId}, project_id= #{projectId} WHERE id = #{id}")
    void update(Task task);

    User getManagerOfEmployee(Long taskId);

    User getManagerOfTask(long taskId);

    User getEmployeeOfTask(Long taskId);

    Task getTaskById(Long taskId);

    List<TaskDTO> getSubTasks(long taskId);
}
