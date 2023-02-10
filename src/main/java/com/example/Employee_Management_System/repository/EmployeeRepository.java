package com.example.Employee_Management_System.repository;

import com.example.Employee_Management_System.mapper.EmployeeMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class EmployeeRepository {
    private final EmployeeMapper employeeMapper;


}
