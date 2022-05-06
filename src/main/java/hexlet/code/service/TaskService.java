package hexlet.code.service;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import org.springframework.http.ResponseEntity;

public interface TaskService {
    Task createTask(String authHeader, TaskDto givenTask);
    Task updateTask(Long id, TaskDto newData);
    ResponseEntity<String> deleteTask(String authHeader, Long id);
    Task getTask(Long id);
    Iterable<Task> getAllTasks();
}
