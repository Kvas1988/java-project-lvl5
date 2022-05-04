package hexlet.code.app.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.service.TaskService;
import hexlet.code.app.service.TaskServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get list of all Tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all Tasks",
                content = @Content( mediaType = "application/json",
                    schema = @Schema(implementation = Task.class) )),
            @ApiResponse(responseCode = "403", description = "Unauthorized request")
    })

    public Iterable<Task> getAllTasks(
            @Parameter(description = "Querydsl predicate")
            @QuerydslPredicate(root = Task.class) Predicate predicate) {
        return taskRepository.findAll(predicate);
        // return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Task by given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task's data",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Task.class) )),
            @ApiResponse(responseCode = "404", description = "No Task with such id"),
            @ApiResponse(responseCode = "403", description = "Unauthorized request")
    })
    public Task getTask(
            @Parameter(description = "Task's id")
            @PathVariable long id) {
        return taskService.getTask(id);
    }

    @PostMapping
    @Operation(summary = "Create new Task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task created",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Task.class) )),
            @ApiResponse(responseCode = "403", description = "Unauthorized request")
    })
    public Task createTask(@RequestHeader(AUTHORIZATION) String authHeader,
                           @Parameter(description = "Task's data")
                           @RequestBody @Valid TaskDto newTask) {
        return taskService.createTask(authHeader, newTask);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Task with given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task update",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Task.class) )),
            @ApiResponse(responseCode = "403", description = "Unauthorized request"),
            @ApiResponse(responseCode = "404", description = "No Label with such id"),
            @ApiResponse(responseCode = "422", description = "Invalid data given")
    })
    public Task updateTask(@RequestBody @Valid TaskDto taskDto,
                           @PathVariable long id) {
        return taskService.updateTask(id, taskDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Task.class) )),
            @ApiResponse(responseCode = "403", description = "Unauthorized request"),
            @ApiResponse(responseCode = "404", description = "No Label with such id")
    })
    public ResponseEntity<String> deleteTask(@RequestHeader(AUTHORIZATION) String authHeader,
                                             @Parameter(description = "Task's id", required = true)
                                             @PathVariable long id) {
        return taskService.deleteTask(authHeader, id);
    }
}
