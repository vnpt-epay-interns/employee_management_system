package com.example.Employee_Management_System.mapper;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.domain.WorkingSchedule;
import com.example.Employee_Management_System.model.WorkingScheduleDetailedInfo;
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

    String getReferenceCode(long employeeId);

    List<Employee> getAllEmployeesByManagerId(long managerId);

    List<WorkingScheduleDetailedInfo> getWorkingSchedule(long employeeId, int year, int month);

    void saveWorkingSchedules(List<WorkingSchedule> workingSchedules);
}
