package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.dto.response.TaskDetailedInfo;
import com.example.Employee_Management_System.model.EmployeeInformation;
import com.example.Employee_Management_System.model.ReportDetailedInfo;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final static int EXPIRE_INTERVAL = 5;

    public void cacheTasksToRedis(List<TaskDetailedInfo> allTasks, String key) {
        Gson gson = new Gson();

        // remove duplicate element
        List<TaskDetailedInfo> distinct = new ArrayList<>();
        for (TaskDetailedInfo taskDetailedInfo : allTasks) {
            boolean isExist = distinct.stream().anyMatch(task -> task.getId().equals(taskDetailedInfo.getId()));
            if (!isExist) {
                distinct.add(taskDetailedInfo);
            }
        }

        // convert list to map
        Map<Long, String> map = distinct.stream()
                .collect(Collectors.toMap(TaskDetailedInfo::getId, gson::toJson));
        cacheHash(key, map);
    }
    public void cacheEmployeeList(List<EmployeeInformation> list, String key) {
        Gson gson = new Gson();
        // remove duplicate element
        List<EmployeeInformation> distinct = new ArrayList<>();
        for (EmployeeInformation employeeInformation : list) {
            boolean isExist = distinct.stream().anyMatch(employee -> employee.getId().equals(employeeInformation.getId()));
            if (!isExist) {
                distinct.add(employeeInformation);
            }
        }

        // convert list to map

        Map<Long, String> map = distinct.stream()
                .collect(Collectors.toMap(EmployeeInformation::getId, gson::toJson));
        cacheHash(key, map);

    }
    public void cacheReportsToRedis(List<ReportDetailedInfo> reportsFromDB, Long id, String key) {
        Gson gson = new Gson();

        // remove duplicate elements
        List<ReportDetailedInfo> distinct = new ArrayList<>();
        for (ReportDetailedInfo reportDetailedInfo : reportsFromDB) {
            boolean isExist = distinct.stream().anyMatch(task -> task.getId().equals(reportDetailedInfo.getId()));
            if (!isExist) {
                distinct.add(reportDetailedInfo);
            }
        }

        // convert list to map
        Map<Long, String> map = distinct.stream()
                .collect(Collectors.toMap(ReportDetailedInfo::getId, gson::toJson));
        redisTemplate.delete(key + id);
        redisTemplate.opsForHash().putAll(key + id, map);
        redisTemplate.expire(key + id, Duration.ofMinutes(EXPIRE_INTERVAL));
    }

    private void cacheHash(String key, Map<Long, String> map) {
        redisTemplate.delete(key); // delete old cache if exists
        redisTemplate.opsForHash().putAll(key, map);
        redisTemplate.expire(key, Duration.ofMinutes(EXPIRE_INTERVAL));
    }

    public void cacheObjectToRedis(Object object, String key) {
        redisTemplate.opsForValue().set(key, object);
        redisTemplate.expire(key, Duration.ofMinutes(EXPIRE_INTERVAL));
    }
    public List<Object> getHash(String key) {
        return redisTemplate.opsForHash().values(key);
    }



}
