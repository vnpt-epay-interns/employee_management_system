package com.example.Employee_Management_System.mapper;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ManagerMapper {

    @Insert("INSERT INTO managers (id, referenced_code) VALUES (#{id}, #{referencedCode})")
    void save(Manager manager);

    Manager findByReferenceCode(String referenceCode);

    Collection<Employee> getAllEmployees();

    List<WorkingScheduleResponse> getWorkingSchedules(long monthNumber);
}
