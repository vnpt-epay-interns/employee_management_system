package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.*;
import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.request.ScheduleWorkingDayRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskEmployeeRequest;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.response.TaskDetailedInfo;
import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse;
import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse.EmployeeSchedule;
import com.example.Employee_Management_System.mapper.EmployeeMapper;
import com.example.Employee_Management_System.model.EmployeeInformation;
import com.example.Employee_Management_System.model.ReportDetailedInfo;
import com.example.Employee_Management_System.model.WorkingScheduleDetailedInfo;
import com.example.Employee_Management_System.repository.EmployeeRepository;
import com.example.Employee_Management_System.repository.ManagerRepository;
import com.example.Employee_Management_System.service.EmployeeService;
import com.example.Employee_Management_System.service.ProjectService;
import com.example.Employee_Management_System.service.ReportService;
import com.example.Employee_Management_System.service.TaskService;
import com.example.Employee_Management_System.utils.CalendarHelper;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.Employee_Management_System.dto.response.WorkingScheduleResponse.MonthInfo;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;
    private final EmployeeMapper employeeMapper;
    private final ReportService reportService;
    private final TaskService taskService;
    private final ProjectService projectService;

    @Override
    public ResponseEntity<Response> getTaskById(Long taskId, User user) {
        TaskDetailedInfo task = taskService.getTaskById(taskId);
        Response response = Response.builder()
                .status(200)
                .data(task)
                .build();
        return ResponseEntity.ok(response);

    }

    @Override
    public ResponseEntity<Response> getTasks(User employee) {
        List<TaskDetailedInfo> tasks = taskService.getTasksByEmployeeId(employee.getId());

        return ResponseEntity.ok(
                Response.builder()
                .status(200)
                .data(tasks)
                .build()
        );
    }

    @Override
    public ResponseEntity<Response> updateTask(User employee, Long taskId, UpdateTaskEmployeeRequest updateTaskRequest) {
        TaskDetailedInfo task = taskService.getTaskByIdAndEmployeeId(taskId, employee.getId());

        task.setStatus(updateTaskRequest.getStatus());
        task.setCompletion(updateTaskRequest.getCompletion());
        task.setEmployeeReview(updateTaskRequest.getEmployeeReview());
        taskService.updateTask(task);

        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(200)
                        .message("Update task successfully!")
                        .build()
        );
    }

    @Override
    public ResponseEntity<Response> writeReport(User employee, WriteReportRequest request) {
        Report report = Report.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(Date.valueOf(LocalDate.now()))
                .createdBy(employee.getId())
                .isRead(false)
                .build();

        reportService.save(report);


        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Report has been saved successfully")
                        .data(null)
                        .build()
        );
    }

    private boolean checkIfTaskBelongsToEmployee(User employee, Long taskId) {
        TaskDetailedInfo taskDetailedInfo = taskService.getTaskById(taskId);
        return Objects.equals(taskDetailedInfo.getEmployeeId(), employee.getId());
    }

    @Override
    public ResponseEntity<Response> scheduleWorkingDay(User employee, ScheduleWorkingDayRequest request) {
        List<WorkingSchedule> workingSchedules = new ArrayList<>();

        int year = request.getYear();
        int month = request.getMonth();
        long employeeId = employee.getId();

        // the day of the month starts from 1
        for (int i = 1; i <= request.getDays().size(); i++) {
            int day = i;
            String status = request.getStatuses().get(i - 1);

            // ignore the day that employee not working
            if (!status.equals("OFF")) {
                // this is java.sql.Date
                Date date = Date.valueOf(LocalDate.of(year, month + 1, day)); // because in Java month starts from 0, but in SQA it starts from 1

                WorkingSchedule workingSchedule = WorkingSchedule.builder()
                        .date(date)
                        .status(status)
                        .employeeId(employeeId)
                        .build();

                workingSchedules.add(workingSchedule);
            }
        }

        employeeRepository.scheduleWorkingDays(workingSchedules);
        //TODO: rewrite the response
        return ResponseEntity.ok(
                Response.builder()
                        .message("Save working schedule successfully!")
                        .status(200)
                        .data(null)
                        .build()
        );
    }

    @Override
    public ResponseEntity<Response> getWorkingSchedule(User employee, int year, int month) {
        MonthInfo monthInfo = CalendarHelper.getMonthInfo(year, month);

        EmployeeInformation employeeInfo = new EmployeeInformation();
        employeeInfo.setId(employee.getId());
        employeeInfo.setFirstName(employee.getFirstName());

        EmployeeSchedule schedule = getEmployeeSchedule(employeeInfo, year, month, monthInfo);

        WorkingScheduleResponse response = WorkingScheduleResponse.builder()
                .monthInfo(monthInfo)
                .schedules(List.of(schedule))
                .build();

        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .data(response)
                        .build()

        );
    }

    public EmployeeSchedule getEmployeeSchedule(EmployeeInformation employeeInfo, int year, int monthNumber, MonthInfo monthInfo) {
        List<WorkingScheduleDetailedInfo> workingSchedule = employeeRepository.getWorkingSchedule(employeeInfo.getId(), year, monthNumber);

        // format multiple working schedules into one employee schedule
        // because all the schedules belong to the same employee

        List<Integer> days = new ArrayList<Integer>();
        List<String> statuses = new ArrayList<String>();


        // the day will start from 1 to the number of days in the month
        for (int day = 1; day <= monthInfo.getNumberOfDays(); day++) {
            // initialize the days with the day of the month and the status with OFF
            days.add(day); // the index of a day will be the day - 1, for example the index of day 1 is 0 [1,2,3,...,30]
            statuses.add("OFF");
        }

        // then update the days and statuses with the working schedules of the employee
        for (WorkingScheduleDetailedInfo workingDay : workingSchedule) {
            statuses.set(workingDay.getDay() - 1, workingDay.getStatus());
        }


        EmployeeSchedule employeeSchedule = EmployeeSchedule.builder()
                .employeeId(employeeInfo.getId())
                .employeeName(employeeInfo.getFirstName())
                .days(days)
                .statuses(statuses)
                .build();


        return employeeSchedule;
    }

    public ResponseEntity<Response> getReports(User employee) {
        List<ReportDetailedInfo> reports = reportService.getReportsByEmployeeId(employee.getId());
        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .data(reports)
                        .build()
        );
    }


    @Override
    public ResponseEntity<Response> getReferenceCode(User employee) {
        String referenceCode = employeeRepository.getReferenceCode(employee.getId());
        Map<String, String> map = new HashMap<>();
        map.put("referenceCode", referenceCode);
        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .data(map)
                        .build()
        );
    }

    @Override
    public ResponseEntity<Response> getSubTasks(User employee, long taskId) {
        if (!checkIfTaskBelongsToEmployee(employee, taskId)) {
            throw new IllegalStateException("The task is not assigned to the you");
        }
        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .data(taskService.getSubTasks(taskId))
                        .build()
        );
    }

    @Override
    @Transactional
    public List<EmployeeInformation> getEmployeesBelongToManager(Long id) {

        List<EmployeeInformation> employees = managerRepository.getAllEmployees(id);
        return employees;


    }

    @Override
    public ResponseEntity<Response> createTask(User employee, CreateTaskRequest request) {

       if (!employee.getId().equals(request.getEmployeeId())) {
            throw new IllegalStateException("You can onl assign a task to yourself");
        }

        taskService.createTask(request);

        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(200)
                        .message("Create task successfully!")
                        .build()
        );
    }

    @Override
    public ResponseEntity<Response> getAllProjectsByManagerId(User employee) {
        Employee employeeInformation = employeeRepository.getEmployeeByEmployeeId(employee.getId());
        List<Project> projects = projectService.getAllProjectsByManagerId(employeeInformation.getManagerId());
        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(200)
                        .data(projects)
                        .build()
        );
    }


    @Override
    public void save(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public User getManagerOfEmployee(long employeeId) {
        return employeeMapper.getManagerOfEmployee(employeeId);
    }

    @Override
    public Employee getEmployeeByEmployeeId(long employeeId) {
        //TODO: throw custom exception
        return employeeMapper.getEmployeeByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

}


