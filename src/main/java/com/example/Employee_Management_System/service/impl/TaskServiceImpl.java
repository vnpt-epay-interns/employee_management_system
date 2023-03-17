package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.response.TaskDetailedInfo;
import com.example.Employee_Management_System.exception.NotFoundException;
import com.example.Employee_Management_System.repository.TaskRepository;
import com.example.Employee_Management_System.service.TaskService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    // the key for storing the task in the redis
    private final String REDIS_KEY_FOR_TASK = "tasks";

    @Override
    public TaskDetailedInfo getTaskById(Long taskId) {
        List<TaskDetailedInfo> allTasksInRedis = getAllTasksInRedis();
        // if the task is in the redis, return it
        for (TaskDetailedInfo task : allTasksInRedis) {
            if (task.getId().equals(taskId)) {
                return task;
            }
        }

        TaskDetailedInfo task = taskRepository.getTaskById(taskId);
        if (task == null) {
            throw new NotFoundException("Task not found");
        }
        return task;
    }

    @Override
    public TaskDetailedInfo getTaskByIdAndEmployeeId(Long taskId, Long employeeId) {
        TaskDetailedInfo task = getTaskById(taskId);
        if (task == null) {
            throw new NotFoundException("Task not found");
        }
        if (!task.getEmployeeId().equals(employeeId)) {
            throw new IllegalStateException("The task is not assigned to the employee");
        }
        return task;
    }

    @Override
    public List<TaskDetailedInfo> getTasksByEmployeeId(Long employeeId) {

        List<TaskDetailedInfo> employeeTasksInDB = getTasksByEmployeeIdFromRedis(employeeId);

        // if the employee has no task, get all tasks from the database
        if (employeeTasksInDB == null && !employeeTasksInDB.isEmpty()) {
            return employeeTasksInDB;
        } else {
            // filter the tasks that the employee has
            List<TaskDetailedInfo> employeeTaskInDB = taskRepository.getTasksByEmployeeId(employeeId);
            // otherwise, get the tasks from the database
            cacheTasksToRedis(employeeTaskInDB);
            return employeeTaskInDB;
        }


    }

    private List<TaskDetailedInfo> getAllTasksInRedis() {
        List<TaskDetailedInfo> tasksByEmployeeId;
        Gson gson = new Gson();

        // get all tasks from Redis
        List<Object> allTasksInRedis = redisTemplate.opsForHash().values(REDIS_KEY_FOR_TASK);
        if (allTasksInRedis == null || allTasksInRedis.isEmpty()) {
            return null;
        }
        return allTasksInRedis.stream()
                .map(value -> gson.fromJson(value.toString(), TaskDetailedInfo.class))
                .collect(Collectors.toList());
    }

    private List<TaskDetailedInfo> getTasksByEmployeeIdFromRedis(Long employeeId) {
        List<TaskDetailedInfo> allTasksInRedis = getAllTasksInRedis();

        if (allTasksInRedis != null && !allTasksInRedis.isEmpty()) {
            List<TaskDetailedInfo> tasksByEmployeeId = allTasksInRedis.stream()
                    .filter(task -> task.getEmployeeId().equals(employeeId))
                    .collect(Collectors.toList());

            // if the tasks of the employee are in the cache, return them
            if (tasksByEmployeeId != null && !tasksByEmployeeId.isEmpty()) {
                return tasksByEmployeeId;
            }
        }

        return null;
    }

    private void cacheTasksToRedis(List<TaskDetailedInfo> allTasks) {
        // caching using redisTemplate
        Gson gson = new Gson();
        Map<Long, String> map = allTasks.stream().collect(Collectors.toMap(TaskDetailedInfo::getId, task -> gson.toJson(task)));
        redisTemplate.opsForHash().putAll(REDIS_KEY_FOR_TASK, map);
    }

    @Override
    public void deleteTaskById(TaskDetailedInfo task) {
        List<TaskDetailedInfo> allTasksInRedis = getAllTasksInRedis();
        // delete the task from the task list stored in Redis
        if (allTasksInRedis != null && !allTasksInRedis.isEmpty()) {
            allTasksInRedis = allTasksInRedis.stream()
                    .filter(t -> !t.getId().equals(task.getId()))
                    .collect(Collectors.toList());

            cacheTasksToRedis(allTasksInRedis);
        }
        taskRepository.deleteTaskById(task.getId());
    }

    @Override
    public void saveTask(Task task) {
        taskRepository.saveTask(task);
        TaskDetailedInfo taskDetailedInfo = getTaskById(task.getId());
        cacheTasksToRedis(List.of(taskDetailedInfo));
    }

    @Override
    public TaskDetailedInfo updateTask(TaskDetailedInfo taskInfo) {
        // change the task in the task list stored in Redis
        List<TaskDetailedInfo> allTasksInRedis = getAllTasksInRedis();
        for (TaskDetailedInfo task : allTasksInRedis) {
            if (Objects.equals(task.getId(), taskInfo.getId())) {
                task.update(taskInfo);
            }
        }
        cacheTasksToRedis(allTasksInRedis);

        // update the task in the database
        taskRepository.updateTask(taskInfo);
        return taskInfo;
    }

    @Override
    public User getManagerOfTask(long taskId) {
        return taskRepository.getManagerOfTask(taskId);
    }

    @Override
    public List<TaskDetailedInfo> getAllTasksByMangerId(Long managerId) {
        List<TaskDetailedInfo> tasksInRedis = getTasksByManagerIdFromRedis(managerId);

        if (tasksInRedis != null && !tasksInRedis.isEmpty()) {
            return tasksInRedis;
        } else {
            List<TaskDetailedInfo> tasksInDB = taskRepository.getTasksByManagerId(managerId);
            cacheTasksToRedis(tasksInDB);
            return tasksInDB;
        }

    }

    private List<TaskDetailedInfo> getTasksByManagerIdFromRedis(Long managerId) {
        List<TaskDetailedInfo> allTasksInRedis = getAllTasksInRedis();

        if (allTasksInRedis != null && !allTasksInRedis.isEmpty()) {
            List<TaskDetailedInfo> tasksByManagerId = allTasksInRedis.stream()
                    .filter(task -> task.getManagerId().equals(managerId))
                    .collect(Collectors.toList());

            // if the tasks of the employee are in the cache, return them
            if (tasksByManagerId != null) {
                return tasksByManagerId;
            }
        }

        return null;
    }
}
