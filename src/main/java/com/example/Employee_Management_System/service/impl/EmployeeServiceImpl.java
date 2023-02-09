package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.mapper.EmployeeMapper;
import com.example.Employee_Management_System.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

}
