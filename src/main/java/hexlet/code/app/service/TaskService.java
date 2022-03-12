package hexlet.code.app.service;

import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.model.Task;

public interface TaskService {
    Task createTask(TaskDto givenTask);
    Task updateTask(Long id, TaskDto newData);
    void deleteTask(Long id);
    Task getTask(Long id);
    Iterable<Task> getAllTasks();
}
