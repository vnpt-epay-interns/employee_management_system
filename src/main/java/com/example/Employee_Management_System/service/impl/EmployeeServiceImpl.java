package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.*;
import com.example.Employee_Management_System.dto.request.ScheduleWorkingDayRequest;
import com.example.Employee_Management_System.dto.request.UpdateTaskEmployeeRequest;
import com.example.Employee_Management_System.dto.request.WriteReportRequest;
import com.example.Employee_Management_System.dto.response.Response;
import com.example.Employee_Management_System.dto.response.TaskDTO;
import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse;
import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse.EmployeeSchedule;
import com.example.Employee_Management_System.exception.NotFoundException;
import com.example.Employee_Management_System.mapper.EmployeeMapper;
import com.example.Employee_Management_System.model.EmployeeInformation;
import com.example.Employee_Management_System.model.ReportDetailedInfo;
import com.example.Employee_Management_System.model.WorkingScheduleDetailedInfo;
import com.example.Employee_Management_System.repository.EmployeeRepository;
import com.example.Employee_Management_System.repository.TaskRepository;
import com.example.Employee_Management_System.service.EmployeeService;
import com.example.Employee_Management_System.service.ReportService;
import com.example.Employee_Management_System.service.TaskService;
import com.example.Employee_Management_System.utils.CalendarHelper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static com.example.Employee_Management_System.dto.response.WorkingScheduleResponse.*;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    private final ReportService reportService;

    private final TaskService taskService;

    private final TaskRepository taskRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public ResponseEntity<Response> getTaskById(long id, User user) {
        Task task = employeeRepository
                .getTaskByIdAndEmployeeId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Response response = Response.builder()
                .status(200)
                .data(task)
                .build();
        return ResponseEntity.ok(response);

    }

    @Cacheable(value = "taskById", key = "#id")
    public Task getTaskByIdCaching(long id, User user) {
        Task task = employeeRepository
                .getTaskByIdAndEmployeeId(id, user.getId())
                .orElseThrow(() -> new NotFoundException("Task not found"));

        return task;

    }

    @Override
    public ResponseEntity<Response> getTasks(User employee) {
        return ResponseEntity.ok(
                Response.builder()
                        .message("Get all tasks successfully!")
                        .status(200)
                        .data(employeeRepository.getTasksByEmployeeId(employee.getId()))
                        .build()
        );
    }

    @Override
    public ResponseEntity<Response> updateTask(User employee, Long taskId, UpdateTaskEmployeeRequest updateTaskRequest) {
        Task task = employeeRepository
                .getTaskByIdAndEmployeeId(taskId, employee.getId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(updateTaskRequest.getStatus());
        task.setCompletion(updateTaskRequest.getCompletion());
        taskRepository.updateTask(task);

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
        Task task = taskService.getTaskByTaskId(taskId);
        User assignedEmployee = taskService.getEmployeeOfTask(task.getId());
        return Objects.equals(assignedEmployee.getId(), employee.getId());
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

    @Override
    public ResponseEntity<Response> writeReportForTask(User employee, Long taskId, WriteReportRequest request) {
        Task task = taskService.getTaskByTaskId(taskId);
        if (task == null) {
            throw new IllegalStateException("Task not found");
        }
        if (!checkIfTaskBelongsToEmployee(employee, taskId)) {
            throw new IllegalStateException("The task is not assigned to the you");
        }

        Report report = Report.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .taskId(taskId)
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
                        .build());
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
    public ResponseEntity<Response> getReportsByTaskId(User employee, long taskId) {
        if (!checkIfTaskBelongsToEmployee(employee, taskId)) {
            throw new IllegalStateException("The task is not assigned to the you");
        }
        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .data(reportService.getReportsByTaskId(taskId))
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

    @Cacheable(value = "tasksByEmployee", key = "#employee.id")
    public List<TaskDTO> getAllTasksCaching(User employee) {
        //        Object dataInRedis = redisTemplate.opsForValue().get("tasksByEmployee::" + employee.getId());
//        if (dataInRedis != null) {
//            System.out.println("Getting data from redis");
//            System.out.println(dataInRedis);
//            return (List<TaskDTO>) dataInRedis;
//        } else {
//            List<TaskDTO> tasks = employeeRepository.getTasksByEmployeeId(employee.getId());
//            redisTemplate.opsForValue().set("tasksByEmployee::" + employee.getId(), tasks);
//            return tasks;
//
//        }
        List<TaskDTO> tasksByEmployeeId = employeeRepository.getTasksByEmployeeId(employee.getId());
        return tasksByEmployeeId;
    }
}

