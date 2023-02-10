package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.domain.WorkingSchedule;
import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse;
import com.example.Employee_Management_System.repository.ManagerRepository;
import com.example.Employee_Management_System.repository.UserRepository;
import com.example.Employee_Management_System.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public ResponseEntity<Response> createTask(CreateTaskRequest request) {
        return ResponseEntity.ok(Response.builder().status(200).message("Create task successfully!").build());
    }

    @Override
    public ResponseEntity<Response> getAllReports() {
        return null;
    }

    @Override
    public ResponseEntity<Response> getReportById(long employeeId) {
        return null;
    }

    @Override
    public ResponseEntity<Response> getReportEmployeeId(long employeeId) {
        return null;
    }

    // ToDo: view working schedule of all employees in a month
    @Override
    public ResponseEntity<Response> getWorkingSchedules(long monthNumber) {
        List<WorkingScheduleResponse> workingSchedules = managerRepository.getWorkingSchedules(monthNumber);
//        TreeMap<Date, List<WorkingScheduleResponse>> collect = workingSchedules.stream().collect(Collectors.groupingBy(WorkingScheduleResponse::getDate));
        TreeMap<Date, List<WorkingScheduleResponse>> collect = workingSchedules.stream().collect(Collectors.groupingBy(WorkingScheduleResponse::getDate, TreeMap::new, Collectors.toList()));
        return ResponseEntity.ok(Response.builder().status(200).message("Get working schedule successfully!").data(collect).build());
    }

    @Override
    public void save(Manager manager) {
        managerRepository.save(manager);
    }

    @Override
    public ResponseEntity<Response> getAllEmployees() {
        Collection<Employee> employeeList = managerRepository.getAllEmployees();
        return ResponseEntity.ok(Response.builder().status(200).message("!!!").data(employeeList).build());
    }


}
