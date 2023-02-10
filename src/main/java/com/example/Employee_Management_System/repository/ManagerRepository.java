package com.example.Employee_Management_System.repository;

import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.mapper.ManagerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ManagerRepository {

    @Autowired
    private ManagerMapper managerMapper;

    public void save(Manager manager) {
        managerMapper.save(manager);
    }

    public Manager findManagerByReferenceCode(String referenceCode) {
        return managerMapper.findByReferenceCode(referenceCode);
    }
}
