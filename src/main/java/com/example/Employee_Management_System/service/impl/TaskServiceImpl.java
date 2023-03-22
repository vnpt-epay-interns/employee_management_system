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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    // the key for storing the single task in the redis
    private final String REDIS_KEY_FOR_SINGLE_TASK = "single_task";
    // the key for storing the task in the redis
    private final String REDIS_KEY_FOR_TASK_BY_USER = "tasks_by_user";
    // the key for storing the subtasks in the redis
    private final String REDIS_KEY_FOR_SUBTASK = "subtasks";

    @Cacheable(value = REDIS_KEY_FOR_SINGLE_TASK, key = "#taskId")
    @Override
    public TaskDetailedInfo getTaskById(Long taskId) {
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
    public List<TaskDetailedInfo> getAllTasksByMangerId(Long managerId) {
        List<TaskDetailedInfo> tasksInRedis = getAllTasksByUserIdInRedis(managerId);

        if (tasksInRedis != null && !tasksInRedis.isEmpty()) {
            return tasksInRedis;
        } else {
            List<TaskDetailedInfo> tasksInDB = taskRepository.getTasksByManagerId(managerId);
            cacheTasksToRedis(tasksInDB, REDIS_KEY_FOR_TASK_BY_USER + "::" + managerId.toString());
            return tasksInDB;
        }
    }

    @Override
    public List<TaskDetailedInfo> getTasksByEmployeeId(Long employeeId) {
        // user can be either a manager or an employee
        List<TaskDetailedInfo> employeeTasksInDB = getAllTasksByUserIdInRedis(employeeId);

        // if the employee has no task, get all tasks from the database
        if (employeeTasksInDB != null && !employeeTasksInDB.isEmpty()) {
            return employeeTasksInDB;
        } else {
            // filter the tasks that the employee has
            List<TaskDetailedInfo> employeeTaskInDB = taskRepository.getTasksByEmployeeId(employeeId);
            // otherwise, get the tasks from the database
            cacheTasksToRedis(employeeTaskInDB, REDIS_KEY_FOR_TASK_BY_USER + "::" + employeeId.toString());
            return employeeTaskInDB;
        }


    }

    private List<TaskDetailedInfo> getAllTasksByUserIdInRedis(Long userId) {
        Gson gson = new Gson();

        // get all tasks from Redis
        List<Object> allTasksInRedis = redisTemplate.opsForHash().values(REDIS_KEY_FOR_TASK_BY_USER + "::" + userId.toString());
        if (allTasksInRedis.isEmpty()) {
            return null;
        }
        return allTasksInRedis.stream()
                .map(value -> gson.fromJson(value.toString(), TaskDetailedInfo.class))
                .collect(Collectors.toList());

    }


    private void cacheTasksToRedis(List<TaskDetailedInfo> allTasks, String key) {
        // caching using redisTemplate
        Gson gson = new Gson();
        Map<Long, String> map = allTasks.stream().collect(Collectors.toMap(TaskDetailedInfo::getId, gson::toJson));
        redisTemplate.opsForHash().putAll(key, map);
    }

    @Transactional
    @CacheEvict(value = REDIS_KEY_FOR_SINGLE_TASK, key = "#task.id")
    @Override
    public void deleteTaskById(TaskDetailedInfo task) {
        // because a task can be stored in 3 keys, "tasks", "tasks::employeeId" and "tasks::managerId"
        Long employeeId = task.getEmployeeId();
        Long managerId = task.getManagerId();

        // get the tasks of the employee and manager from the cache
        List<TaskDetailedInfo> employeeTasksInRedis = getTasksByEmployeeId(employeeId);
        List<TaskDetailedInfo> managerTasksInRedis = getAllTasksByMangerId(managerId);

        // remove the task from the list
        removeTaskFromList(employeeTasksInRedis, task);
        removeTaskFromList(managerTasksInRedis, task);

        // delete the task from the task list stored in Redis
        cacheTasksToRedis(employeeTasksInRedis, REDIS_KEY_FOR_TASK_BY_USER + "::" + employeeId.toString());
        cacheTasksToRedis(managerTasksInRedis, REDIS_KEY_FOR_TASK_BY_USER + "::" + managerId.toString());

        // delete the task from the database
        taskRepository.deleteTaskById(task.getId());
    }

    private void removeTaskFromList(List<TaskDetailedInfo> taskListInRedis, TaskDetailedInfo task) {
        if (taskListInRedis != null && !taskListInRedis.isEmpty()) {
            taskListInRedis.removeIf(taskInRedis -> taskInRedis.getId().equals(task.getId()));
        }
    }

    @Transactional
    @CachePut(value = REDIS_KEY_FOR_SINGLE_TASK, key = "#task.id")
    @Override
    public TaskDetailedInfo saveTask(Task task) {
        // save the task to the database first so that the task object has the id
        taskRepository.saveTask(task);
        TaskDetailedInfo taskDetailedInfo = getTaskById(task.getId());// after saving the task to the database, the task object has the id

        // if the task has a parent task, update the subtask cache of parent task
        if (task.getParentId() != null) {

            // save this task as the subtask for its parent task in Redis
            List<Object> subtasksOfParentTask = redisTemplate.opsForHash().values(REDIS_KEY_FOR_SUBTASK + "::" + taskDetailedInfo.getParentId());
            Gson gson = new Gson();
            List<TaskDetailedInfo> subtasksOfParentTaskInRedis = subtasksOfParentTask.stream()
                    .map(value -> gson.fromJson(value.toString(), TaskDetailedInfo.class))
                    .collect(Collectors.toList());

            addTaskToTaskList(subtasksOfParentTaskInRedis, taskDetailedInfo);
            cacheTasksToRedis(subtasksOfParentTaskInRedis, REDIS_KEY_FOR_SUBTASK + "::" + taskDetailedInfo.getParentId());
        }
        // because a task can be stored in 3 keys, "tasks", "tasks::employeeId" and "tasks::managerId"
        // update the cache for dashboard of Employee who is assigned this task
        // get the task list stored in Redis, add the task to the list and save the list to Redis
        Long employeeId = taskDetailedInfo.getEmployeeId();
        List<TaskDetailedInfo> employeeTasksInRedis = getTasksByEmployeeId(employeeId);
        addTaskToTaskList(employeeTasksInRedis, taskDetailedInfo);
        cacheTasksToRedis(employeeTasksInRedis, REDIS_KEY_FOR_TASK_BY_USER + "::" + employeeId.toString());

        // update the cache for dashboard of Manager if the task is a parent task
        // the dashboard of manager doesn't show the subtask, if it not the subtask, change the cache so that can be shown in the dashboard
        if (taskDetailedInfo.getParentId() == null) {
            // get the task list stored in Redis, add the task to the list and save the list to Redis
            Long managerId = taskDetailedInfo.getManagerId();
            List<TaskDetailedInfo> managerTasksInRedis = getAllTasksByMangerId(managerId);
            addTaskToTaskList(managerTasksInRedis, taskDetailedInfo);
            cacheTasksToRedis(managerTasksInRedis, REDIS_KEY_FOR_TASK_BY_USER + "::" + managerId.toString());
        }

        return taskDetailedInfo;
    }

    private void addTaskToTaskList(List<TaskDetailedInfo> taskListInRedis, TaskDetailedInfo taskDetailedInfo) {
        if (taskListInRedis != null && !taskListInRedis.isEmpty()) {
            taskListInRedis.add(taskDetailedInfo);
        }
    }

    @CachePut(value = REDIS_KEY_FOR_SINGLE_TASK, key = "#taskDetailedInfo.id")
    @Transactional
    @Override
    public TaskDetailedInfo updateTask(TaskDetailedInfo taskDetailedInfo) {
        // because a task can be stored in 3 keys, "tasks", "tasks::employeeId" and "tasks::managerId"


        // get the task list stored in Redis, update the task to the list and save the list to Redis
        Long employeeId = taskDetailedInfo.getEmployeeId();
        List<TaskDetailedInfo> employeeTasksInRedis = getTasksByEmployeeId(employeeId);
        updateTaskToTaskList(employeeTasksInRedis, taskDetailedInfo);
        cacheTasksToRedis(employeeTasksInRedis, REDIS_KEY_FOR_TASK_BY_USER + "::" + employeeId.toString());

        // UPDATE THE CACHE FOR MANAGER
        // if the task is a subtask, update the subtask cache of parent task
        if (taskDetailedInfo.getParentId() != null) {
            // save this task as the subtask for its parent task in Redis
            List<Object> subtasksOfParentTask = redisTemplate.opsForHash().values(REDIS_KEY_FOR_SUBTASK + "::" + taskDetailedInfo.getParentId());
            Gson gson = new Gson();
            List<TaskDetailedInfo> subtasksOfParentTaskInRedis = subtasksOfParentTask.stream()
                    .map(value -> gson.fromJson(value.toString(), TaskDetailedInfo.class))
                    .collect(Collectors.toList());

            updateTaskToTaskList(subtasksOfParentTaskInRedis, taskDetailedInfo);
            cacheTasksToRedis(subtasksOfParentTaskInRedis, REDIS_KEY_FOR_SUBTASK + "::" + taskDetailedInfo.getParentId());
        } else {
            // if the task is a parent task, update the cache for dashboard of Manager
            // get the task list stored in Redis, update the task to the list and save the list to Redis
            Long managerId = taskDetailedInfo.getManagerId();
            List<TaskDetailedInfo> managerTasksInRedis = getAllTasksByMangerId(managerId);
            updateTaskToTaskList(managerTasksInRedis, taskDetailedInfo);
            cacheTasksToRedis(managerTasksInRedis, REDIS_KEY_FOR_TASK_BY_USER + "::" + managerId.toString());
        }

        // save the task to the task list stored in Redis
        taskRepository.updateTask(taskDetailedInfo);
        return taskDetailedInfo;
    }

    private void updateTaskToTaskList(List<TaskDetailedInfo> taskListInRedis, TaskDetailedInfo taskDetailedInfo) {
        for (TaskDetailedInfo task : taskListInRedis )  {
            if (task.getId().equals(taskDetailedInfo.getId())) {
                task.update(taskDetailedInfo);
            }
        }
    }

    @Override
    public User getManagerOfTask(long taskId) {
        return taskRepository.getManagerOfTask(taskId);
    }



//    @Cacheable(value = REDIS_KEY_FOR_SUBTASK, key = "#parentId")
    @Override
    public List<TaskDetailedInfo> getSubTasks(long parentId) {
        List<Object> subtasksOfParentTask = redisTemplate.opsForHash().values(REDIS_KEY_FOR_SUBTASK + "::" + parentId);
        Gson gson = new Gson();
        List<TaskDetailedInfo> subtasksOfParentTaskInRedis = subtasksOfParentTask.stream()
                .map(value -> gson.fromJson(value.toString(), TaskDetailedInfo.class))
                .collect(Collectors.toList());

        if (!subtasksOfParentTaskInRedis.isEmpty()) {
            return subtasksOfParentTaskInRedis;
        }

        List<TaskDetailedInfo> subtasksOfParentTaskInDB = taskRepository.getSubTasks(parentId);
        cacheTasksToRedis(subtasksOfParentTaskInDB, REDIS_KEY_FOR_SUBTASK + "::" + parentId);

        return subtasksOfParentTaskInDB;
    }
}
