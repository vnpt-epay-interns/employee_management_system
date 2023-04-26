package com.example.Employee_Management_System.service.impl;

import com.example.Employee_Management_System.domain.Task;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.request.CreateTaskRequest;
import com.example.Employee_Management_System.dto.response.TaskDetailedInfo;
import com.example.Employee_Management_System.exception.NotFoundException;
import com.example.Employee_Management_System.model.TaskDetailsForProject;
import com.example.Employee_Management_System.repository.TaskRepository;
import com.example.Employee_Management_System.service.TaskService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
//    private final RedisService redisService;
    // the key for storing the single task in the redis
//    private final String REDIS_KEY_FOR_SINGLE_TASK = "single_task";
//    // the key for storing the task in the redis
//    private final String REDIS_KEY_FOR_TASK_BY_USER = "tasks_by_user";
//    // the key for storing the subtasks in the redis
//    private final String REDIS_KEY_FOR_SUBTASK = "subtasks";

//    @Cacheable(value = REDIS_KEY_FOR_SINGLE_TASK, key = "#taskId")
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
        return task;
    }

    @Override
    public List<TaskDetailedInfo> getAllTasksByMangerId(Long managerId) {
//        List<TaskDetailedInfo> tasksInRedis = getAllTasksByUserIdInRedis(managerId);

//        if (tasksInRedis != null && !tasksInRedis.isEmpty()) {
//            return tasksInRedis;
//        } else {
//            List<TaskDetailedInfo> tasksInDB = taskRepository.getTasksByManagerId(managerId);
//            String key = REDIS_KEY_FOR_TASK_BY_USER + "::" + managerId.toString();
//            redisService.cacheTasksToRedis(tasksInDB, key);
//            return tasksInDB;
//        }
        List<TaskDetailedInfo> tasksInDB = taskRepository.getTasksByManagerId(managerId);
        return tasksInDB;

    }

    @Override
    public List<TaskDetailsForProject> getAllProjectDetailsById(Long id) {
        return taskRepository.getAllProjectDetailsById(id);
    }

    @Override
    public List<TaskDetailedInfo> getTasksByEmployeeId(Long employeeId) {
        // user can be either a manager or an employee
//        List<TaskDetailedInfo> employeeTasksInDB = getAllTasksByUserIdInRedis(employeeId);

//        // if the employee has no task, get all tasks from the database
//        if (employeeTasksInDB != null && !employeeTasksInDB.isEmpty()) {
//            return employeeTasksInDB;
//        } else {
//            // filter the tasks that the employee has
//            List<TaskDetailedInfo> employeeTaskInDB = taskRepository.getTasksByEmployeeId(employeeId);
//            // otherwise, get the tasks from the database
//            String key = REDIS_KEY_FOR_TASK_BY_USER + "::" + employeeId.toString();
//            redisService.cacheTasksToRedis(employeeTaskInDB, key);
//            return employeeTaskInDB;
//        }

        List<TaskDetailedInfo> employeeTaskInDB = taskRepository.getTasksByEmployeeId(employeeId);
        return employeeTaskInDB;



    }
//
//    private List<TaskDetailedInfo> getAllTasksByUserIdInRedis(Long userId) {
//
//        // get all tasks from Redis
//        String key = REDIS_KEY_FOR_TASK_BY_USER + "::" + userId.toString();
//        List<Object> allTasksInRedis = redisService.getHash(key);
//        if (allTasksInRedis.isEmpty()) {
//            return null;
//        }
//        return convertToListOfTaskDetailedInfo(allTasksInRedis);
//
//    }


    @Transactional
//    @CacheEvict(value = REDIS_KEY_FOR_SINGLE_TASK, key = "#task.id")
    @Override
    public void hideTaskById(TaskDetailedInfo task) {
        // because a task can be stored in 3 keys, "single_tasks", "tasks::employeeId" and "tasks::managerId"
//        Long employeeId = task.getEmployeeId();
//        Long managerId = task.getManagerId();
//
//        // get the tasks of the employee and manager from the cache
//        List<TaskDetailedInfo> employeeTasksInRedis = getTasksByEmployeeId(employeeId);
//        List<TaskDetailedInfo> managerTasksInRedis = getAllTasksByMangerId(managerId);
//
//        // remove the task from the list
//        removeTaskFromList(employeeTasksInRedis, task);
//        removeTaskFromList(managerTasksInRedis, task);
//
//        // delete the task from the task list stored in Redis
//        redisService.cacheTasksToRedis(employeeTasksInRedis, REDIS_KEY_FOR_TASK_BY_USER + "::" + employeeId.toString());
//        redisService.cacheTasksToRedis(managerTasksInRedis, REDIS_KEY_FOR_TASK_BY_USER + "::" + managerId.toString());

        // delete the task from the database
        taskRepository.deleteTaskById(task.getId());
    }

    @Override
    public TaskDetailedInfo createTask(CreateTaskRequest request) {

        if (request.getParentId() != null) {
            TaskDetailedInfo parenTask = getTaskById(request.getParentId());
            if (parenTask == null) {
                throw new IllegalStateException("Parent task is not exist!");
            }
        }

        Task task = Task
                .builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .completion(request.getCompletion())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .employeeId(request.getEmployeeId())
                .estimateHours(request.getEstimateHours())
                .parentId(request.getParentId())
                .priority(request.getPriority())
                .projectId(request.getProjectId())
                .build();

        return saveTask(task);
    }

    private void removeTaskFromList(List<TaskDetailedInfo> taskListInRedis, TaskDetailedInfo task) {
        // remove the task and its subtasks from the list
        if (taskListInRedis != null && !taskListInRedis.isEmpty()) {
            taskListInRedis.removeIf(taskInRedis -> taskInRedis.getId().equals(task.getId()) || task.getId().equals(taskInRedis.getParentId()));
        }

    }

    @Transactional
//    @CachePut(value = REDIS_KEY_FOR_SINGLE_TASK, key = "#task.id")
    @Override
    public TaskDetailedInfo saveTask(Task task) {
        // because a task can be stored in 3 keys, "single_tasks", "tasks::employeeId" and "tasks::managerId"
        // save the task to the database first so that the task object has the id
        taskRepository.saveTask(task);
        TaskDetailedInfo taskDetailedInfo = getTaskById(task.getId());// after saving the task to the database, the task object has the id

        /**
        // if the task has a parent task, update the subtask cache of parent task
        if (task.getParentId() != null) {
            // save this task as the subtask for its parent task in Redis
            String key = REDIS_KEY_FOR_SUBTASK + "::" + taskDetailedInfo.getParentId();
            List<Object> subtasksOfParentTask = redisService.getHash(key);
            List<TaskDetailedInfo> subtasksOfParentTaskInRedis = convertToListOfTaskDetailedInfo(subtasksOfParentTask);

            addTaskToTaskList(subtasksOfParentTaskInRedis, taskDetailedInfo);
            redisService.cacheTasksToRedis(subtasksOfParentTaskInRedis, key);
        }

        // update the cache for dashboard of Employee who is assigned this task
        // get the task list stored in Redis, add the task to the list and save the list to Redis
        Long employeeId = taskDetailedInfo.getEmployeeId();
        List<TaskDetailedInfo> employeeTasksInRedis = getTasksByEmployeeId(employeeId);
        addTaskToTaskList(employeeTasksInRedis, taskDetailedInfo);
        String employeeKey = REDIS_KEY_FOR_TASK_BY_USER + "::" + employeeId.toString();
        redisService.cacheTasksToRedis(employeeTasksInRedis, employeeKey);

        // update the cache for dashboard of Manager if the task is a parent task
        // the dashboard of manager doesn't show the subtas
        // if it not the subtask, change the cache so that can be shown in the dashboard
        Long managerId = taskDetailedInfo.getManagerId();
        List<TaskDetailedInfo> managerTasksInRedis = getAllTasksByMangerId(managerId);
        String managerKey = REDIS_KEY_FOR_TASK_BY_USER + "::" + managerId.toString();

        if (taskDetailedInfo.getParentId() == null) {
            // get the task list stored in Redis, add the task to the list and save the list to Redis
            addTaskToTaskList(managerTasksInRedis, taskDetailedInfo);
            redisService.cacheTasksToRedis(managerTasksInRedis, managerKey);
        } else {
            // if it is the subtask, change the cache for the parent so that can be shown accurately in the dashboard of both manager and employee
            TaskDetailedInfo parentTask = managerTasksInRedis.stream().filter(taskInRedis -> taskInRedis.getId().equals(taskDetailedInfo.getParentId())).findFirst().orElse(null);


            parentTask.setNumberSubtasks(parentTask.getNumberSubtasks() + 1);

            // update the number of subtasks of the parent task of the manager dashboard in the Redis
            updateTaskToTaskList(managerTasksInRedis, parentTask);
            redisService.cacheTasksToRedis(managerTasksInRedis, managerKey);

            // udpate the number of subtasks of the parent task of the employee dashboard in the Redis
            Long employeeIdOfParentTask = parentTask.getEmployeeId();
            List<TaskDetailedInfo> employeeTasksInRedisOfParentTask = getTasksByEmployeeId(employeeIdOfParentTask);
            // TODO: when the duplicated key error happens? where the task is cached on client side in the store
            TaskDetailedInfo parentTaskOfEmployee = employeeTasksInRedisOfParentTask.stream().filter(taskInRedis -> taskInRedis.getId().equals(taskDetailedInfo.getParentId())).findFirst().orElse(null);
            parentTaskOfEmployee.setNumberSubtasks(parentTaskOfEmployee.getNumberSubtasks() + 1);
            updateTaskToTaskList(employeeTasksInRedisOfParentTask, parentTaskOfEmployee);
            String employeeOfParentTaskKey = REDIS_KEY_FOR_TASK_BY_USER + "::" + employeeIdOfParentTask.toString();
            redisService.cacheTasksToRedis(employeeTasksInRedisOfParentTask, employeeOfParentTaskKey);
        }
        */
        return taskDetailedInfo;
    }

    private void addTaskToTaskList(List<TaskDetailedInfo> taskListInRedis, TaskDetailedInfo taskDetailedInfo) {
        if (taskListInRedis != null && !taskListInRedis.isEmpty()) {
            taskListInRedis.add(taskDetailedInfo);
        }
    }

//    @CachePut(value = REDIS_KEY_FOR_SINGLE_TASK, key = "#taskDetailedInfo.id")
    @Transactional
    @Override
    public TaskDetailedInfo updateTask(TaskDetailedInfo taskDetailedInfo) {
        // because a task can be stored in 3 keys, "single_tasks", "tasks::employeeId" and "tasks::managerId"

        /**
        //TODO: check the employee cache
//        // get the task list stored in Redis, update the task to the list and save the list to Redis
//        Long employeeId = taskDetailedInfo.getEmployeeId();
//        List<TaskDetailedInfo> employeeTasksInRedis = getTasksByEmployeeId(employeeId);
//        updateTaskToTaskList(employeeTasksInRedis, taskDetailedInfo);
//        String taskByUserKey = REDIS_KEY_FOR_TASK_BY_USER + "::" + employeeId.toString();
//        redisService.cacheTasksToRedis(employeeTasksInRedis, taskByUserKey);
//
//        // UPDATE THE CACHE FOR MANAGER
//        // if the task is a subtask, update the subtask cache of parent
//
//        if (taskDetailedInfo.getParentId() != null) {
//            // save this task as the subtask for its parent task in Redis
//            String subtaskByParentIdKey = REDIS_KEY_FOR_SUBTASK + "::" + taskDetailedInfo.getParentId();
//
//            List<Object> subtasksOfParentTask = redisService.getHash(subtaskByParentIdKey);
//            List<TaskDetailedInfo> subtasksOfParentTaskInRedis = convertToListOfTaskDetailedInfo(subtasksOfParentTask);
//
//            updateTaskToTaskList(subtasksOfParentTaskInRedis, taskDetailedInfo);
//            redisService.cacheTasksToRedis(subtasksOfParentTaskInRedis, subtaskByParentIdKey);
//        } else {
//            // if the task is a parent task, update the cache for dashboard of Manager
//            // get the task list stored in Redis, update the task to the list and save the list to Redis
//            Long managerId = taskDetailedInfo.getManagerId();
//            List<TaskDetailedInfo> managerTasksInRedis = getAllTasksByMangerId(managerId);
//            updateTaskToTaskList(managerTasksInRedis, taskDetailedInfo);
//            String taskByManagerIdKey = REDIS_KEY_FOR_TASK_BY_USER + "::" + managerId.toString();
//            redisService.cacheTasksToRedis(managerTasksInRedis, taskByManagerIdKey);
//        }


         **/
        // save the task to the task list stored in Redis
        taskRepository.updateTask(taskDetailedInfo);
        return taskDetailedInfo;
    }

    private List<TaskDetailedInfo> convertToListOfTaskDetailedInfo(List<Object> subtasksOfParentTask) {
        Gson gson = new Gson();
        List<TaskDetailedInfo> subtasksOfParentTaskInRedis = subtasksOfParentTask.stream().map(value -> gson.fromJson(value.toString(), TaskDetailedInfo.class)).collect(Collectors.toList());

        return subtasksOfParentTaskInRedis;
    }

//    private void updateTaskToTaskList(List<TaskDetailedInfo> taskListInRedis, TaskDetailedInfo taskDetailedInfo) {
//        for (TaskDetailedInfo task : taskListInRedis) {
//            if (task.getId().equals(taskDetailedInfo.getId())) {
//                task.update(taskDetailedInfo);
//                break;
//            }
//        }
//    }

    @Override
    public User getManagerOfTask(long taskId) {
        return taskRepository.getManagerOfTask(taskId);
    }


    //    @Cacheable(value = REDIS_KEY_FOR_SUBTASK, key = "#parentId")
    @Override
    public List<TaskDetailedInfo> getSubTasks(long parentId) {
//        String subtaskKey = REDIS_KEY_FOR_SUBTASK + "::" + parentId;
//        List<Object> subtasksOfParentTask = redisService.getHash(subtaskKey);
//        List<TaskDetailedInfo> subtasksOfParentTaskInRedis = convertToListOfTaskDetailedInfo(subtasksOfParentTask);
//
//        if (!subtasksOfParentTaskInRedis.isEmpty()) {
//            return subtasksOfParentTaskInRedis;
//        }

        List<TaskDetailedInfo> subtasksOfParentTaskInDB = taskRepository.getSubTasks(parentId);
//        redisService.cacheTasksToRedis(subtasksOfParentTaskInDB, subtaskKey);

        return subtasksOfParentTaskInDB;
    }
}
