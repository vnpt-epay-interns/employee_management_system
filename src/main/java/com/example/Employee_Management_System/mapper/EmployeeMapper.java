package com.example.Employee_Management_System.mapper;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper {
    @Insert("INSERT INTO employees (id, manager_id) VALUES (#{id}, #{managerId})")
    void save(Employee employee);

//    Task getTaskById(long id, User user);
}
