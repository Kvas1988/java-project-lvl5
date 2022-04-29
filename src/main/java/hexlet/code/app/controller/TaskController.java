package hexlet.code.app.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.service.TaskService;
import hexlet.code.app.service.TaskServiceImpl;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static hexlet.code.app.controller.TaskController.TASK_CONTROLLER_PATH;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
public class TaskController {

    public static final String TASK_CONTROLLER_PATH = "/tasks";

    private final TaskService taskService;
    private final TaskRepository taskRepository;

    public TaskController(TaskService taskService, TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    @GetMapping("")
    public Iterable<Task> getAllTasks(@QuerydslPredicate(root = Task.class) Predicate predicate) {
        return taskRepository.findAll(predicate);
        // return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable long id) {
        return taskService.getTask(id);
    }

    @PostMapping
    public Task createTask(@RequestHeader(AUTHORIZATION) String authHeader,
                           @RequestBody @Valid TaskDto newTask) {
        return taskService.createTask(authHeader, newTask);
    }

    @PutMapping("/{id}")
    public Task updateTask(@RequestBody @Valid TaskDto taskDto,
                           @PathVariable long id) {
        return taskService.updateTask(id, taskDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@RequestHeader(AUTHORIZATION) String authHeader,
                                     @PathVariable long id) {
        return taskService.deleteTask(authHeader, id);
    }
}
