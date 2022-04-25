package hexlet.code.app.service;

import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.model.Task;
import org.springframework.http.ResponseEntity;

public interface TaskService {
    Task createTask(String authHeader, TaskDto givenTask);
    Task updateTask(Long id, TaskDto newData);
    ResponseEntity<String> deleteTask(String authHeader, Long id);
    Task getTask(Long id);
    Iterable<Task> getAllTasks();
}
