package com.example.Employee_Management_System.mapper;

import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TaskMapper {
    @Insert("INSERT INTO tasks (title, description, start_date, end_date, status, priority, completion, estimate_hours, employee_id, parent_task) VALUES (#{title}, #{description}, #{startDate}, #{endDate}, #{status}, #{priority}, #{completion}, #{estimateHours}, #{employeeId}, #{parentTask})")
    void save(Task task);

    @Delete("DELETE FROM tasks WHERE id = #{id}")
    void delete(Task task);

    Task getTaskById(Long taskId);

    Task getTaskByIdAndEmployeeId(@Param("id") Long taskId, Long employeeId);

    List<Task> getTasksByEmployeeId(Long employeeId);

    List<Task> getTasks(Long employeeId);

    List<Task> getTasksByParentTask(Long parentTaskId);

    @Update("UPDATE tasks SET title = #{title}, description = #{description}, start_date = #{startDate}, end_date = #{endDate}, status = #{status}, priority = #{priority}, completion = #{completion}, estimate_hours = #{estimateHours}, employee_id = #{employeeId} WHERE id = #{id}")
    void update(Task task);

    User getManagerOfEmployee(Long taskId);

    User getManagerOfTask(long taskId);

    User getEmployeeOfTask(Long taskId);
}
