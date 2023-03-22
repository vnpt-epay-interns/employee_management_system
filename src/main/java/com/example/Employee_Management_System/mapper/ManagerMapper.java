package com.example.Employee_Management_System.mapper;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.dto.response.TaskDetailedInfo;
import com.example.Employee_Management_System.model.EmployeeInformation;
import com.example.Employee_Management_System.model.ManagerInformation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ManagerMapper {

    @Insert("INSERT INTO managers (id, referenced_code) VALUES (#{id}, #{referencedCode})")
    void save(Manager manager);

    Manager findByReferenceCode(String referenceCode);

    List<EmployeeInformation> getAllEmployees(Long managerId);

    String getReferenceCode(Long id);

    Optional<ManagerInformation> getManagerInfo(String referencedCode);

    List<TaskDetailedInfo> getAllTasks(Long managerId);
}
