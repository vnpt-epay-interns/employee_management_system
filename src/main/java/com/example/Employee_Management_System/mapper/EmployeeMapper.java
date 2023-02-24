package com.example.Employee_Management_System.mapper;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.domain.WorkingSchedule;
import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface EmployeeMapper {
    @Insert("INSERT INTO employees (id, manager_id) VALUES (#{id}, #{managerId})")
    void save(Employee employee);

    User getManagerOfEmployee(long employeeId);

    Optional<Employee> getEmployeeByEmployeeId(long employeeId);
//    Task getTaskById(long id, User user);

    List<Employee> getAllEmployeesByManagerId(long managerId);

    
    List<WorkingScheduleResponse>getWorkingSchedule(User employee);
    @Insert("INSERT INTO working_schedules(date, at_morning, at_afternoon, employee_id) VALUES (#{date}, #{atMorning}, #{atAfternoon}, #{employeeId})")
    void scheduleWorkingDays(WorkingSchedule workingSchedule);
}
