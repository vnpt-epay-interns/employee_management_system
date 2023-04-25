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
import com.example.Employee_Management_System.repository.TaskRepository;
import com.example.Employee_Management_System.service.*;
import com.example.Employee_Management_System.utils.CalendarHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.Employee_Management_System.dto.response.WorkingScheduleResponse.*;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;
    private final EmployeeMapper employeeMapper;
    private final ReportService reportService;
    private final TaskService taskService;
    private final RedisService redisService;
    private final ProjectService projectService;
    private final RedisTemplate redisTemplate;
    private final String REDIS_KEY_FOR_TASK_BY_USER = "tasks_by_user";
    private final static String REDIS_KEY_FOR_EMPLOYEE = "employees::";
    private final String REDIS_REPORTS_BY_TASK_KEY = "reports_by_task::";
    private final String REDIS_REPORTS_BY_MANAGER_KEY = "reports_by_manager::";

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

        Gson gson = new Gson();
        Employee employeeInformation = employeeRepository.getEmployeeByEmployeeId(employee.getId());
        ReportDetailedInfo reportDetailedInfo = reportService.getReportById(report.getId());
        List<Object> reportsByTaskInRedis = redisTemplate.opsForHash().values(REDIS_REPORTS_BY_MANAGER_KEY + employeeInformation.getManagerId());
        List<ReportDetailedInfo> reportsByTask = new ArrayList<>(reportsByTaskInRedis.stream()
                .map(reportJson -> gson.fromJson(reportJson.toString(), ReportDetailedInfo.class))
                .toList());

        if (reportsByTask != null || !reportsByTask.isEmpty()) {
            reportsByTask.add(reportDetailedInfo);
            redisService.cacheReportsToRedis(reportsByTask, employeeInformation.getManagerId(), REDIS_REPORTS_BY_MANAGER_KEY);
        }

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
        String key = REDIS_KEY_FOR_EMPLOYEE + id;

        List<EmployeeInformation> employeesInRedis = convertToListOfEmployeeInformation(redisService.getHash(key));
        if (!employeesInRedis.isEmpty()) {
            return employeesInRedis;
        } else {
            List<EmployeeInformation> employees = managerRepository.getAllEmployees(id);
            redisService.cacheEmployeeList(employees, key);
            return employees;
        }

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

    private List<EmployeeInformation> convertToListOfEmployeeInformation(List<Object> employeesInRedis) {
        Gson gson = new Gson();

        return employeesInRedis.stream().map(e -> gson.fromJson(e.toString(), EmployeeInformation.class))
                .collect(Collectors.toList());
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

    @Cacheable(value = "referenceCode", key = "#manager.id")
    public String getReferenceCodeCache(User manager) {
        String referenceCode;
        if (manager.getRole().equals("MANAGER")) {
            referenceCode = managerRepository.getReferenceCode(manager.getId());
        } else {
            referenceCode = employeeRepository.getReferenceCode(manager.getId());
        }
        return referenceCode;
    }

    @Cacheable(value = "reportsByEmployee", key = "#employee.id")
    public List<ReportDetailedInfo> getReportsCache(User employee) {
        List<ReportDetailedInfo> reports = (List<ReportDetailedInfo>) redisTemplate.opsForValue().get("reportsByEmployee::" + employee.getId());
        if (reports == null) {
            reports = reportService.getReportsByEmployeeId(employee.getId());
            redisTemplate.opsForValue().set("reportsByEmployee::" + employee.getId(), reports);
        }
        return reports;
    }



    @Cacheable(value = "reportsByEmployee", key = "#employee.id")
    public void writeReportCache(User employee, WriteReportRequest request) {
        Report report = Report.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(Date.valueOf(LocalDate.now()))
                .createdBy(employee.getId())
                .isRead(false)
                .build();

        reportService.save(report);
        List<ReportDetailedInfo> reports = (List<ReportDetailedInfo>) redisTemplate.opsForValue().get("reportsByEmployee::" + employee.getId());
        if (reports == null) {
            reports = reportService.getReportsByEmployeeId(employee.getId());
            redisTemplate.opsForValue().set("reportsByEmployee::" + employee.getId(), reports);
        } else {
            reports.add(ReportDetailedInfo.builder()
                    .id(report.getId())
                    .title(report.getTitle())
                    .content(report.getContent())
                    .createdAt(report.getCreatedAt())
                    .build());
            redisTemplate.opsForValue().set("reportsByEmployee::" + employee.getId(), reports);
        }
    }
}


