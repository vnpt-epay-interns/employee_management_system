package com.example.Employee_Management_System.mapper;

import com.example.Employee_Management_System.domain.Manager;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ManagerMapper {

    @Insert("INSERT INTO managers (id, referenced_code) VALUES (#{id}, #{referencedCode})")
    void save(Manager manager);


    Manager findByReferenceCode(String referenceCode);
}
