package com.example.Employee_Management_System.service;

import com.example.Employee_Management_System.dto.response.TaskDetailedInfo;
import com.example.Employee_Management_System.model.EmployeeInformation;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final static int EXPIRE_INTERVAL = 5;

    public void cacheTasksToRedis(List<TaskDetailedInfo> allTasks, String key) {
        // convert list to map
        Gson gson = new Gson();
        Map<Long, String> map = allTasks.stream().collect(Collectors.toMap(TaskDetailedInfo::getId, gson::toJson));
        cacheHash(key, map);
    }
    public void cacheEmployeeList(List<EmployeeInformation> list, String key) {
        // convert list to map
        Gson gson = new Gson();
        Map<Long, String> map = list.stream().collect(Collectors.toMap(EmployeeInformation::getId, gson::toJson));
        cacheHash(key, map);

    }
    private void cacheHash(String key, Map<Long, String> map) {
        redisTemplate.opsForHash().putAll(key, map);
        redisTemplate.expire(key, Duration.ofMinutes(EXPIRE_INTERVAL));
    }

    public List<Object> getHash(String key) {
        return redisTemplate.opsForHash().values(key);
    }


}