package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.mapper.ManagerMapper;
import com.example.Employee_Management_System.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private ManagerMapper managerMapper;
}
