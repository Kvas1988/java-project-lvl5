package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.service.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("${base-url}" + TaskStatusController.TASK_STATUS_CONTROLLER_PATH)
public class TaskStatusController {

    public static final String TASK_STATUS_CONTROLLER_PATH = "/statuses";

    private final TaskStatusService taskStatusService;

    public TaskStatusController(TaskStatusService taskStatusService) {
        this.taskStatusService = taskStatusService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get TaskStatus by given id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "TaskStatus data",
            content = @Content( mediaType = "application/json",
                    schema = @Schema(implementation = TaskStatus.class) )),
        @ApiResponse(responseCode = "403", description = "Unauthorized request"),
        @ApiResponse(responseCode = "404", description = "No TaskStatus with such id")
    })
    public TaskStatus getTaskStatus(@Parameter(description = "TaskStatus's id")
                                    @PathVariable long id) {
        return taskStatusService.getTaskStatus(id);
    }

    @GetMapping
    @Operation(summary = "Get list of all TaskStatuses")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of all TaskStatuses",
            content = @Content( mediaType = "application/json",
                    schema = @Schema(implementation = TaskStatus.class) ))
    })
    public Iterable<TaskStatus> getAllTaskStatuses() {
        return taskStatusService.getAllTaskStatuses();
    }

    @PostMapping
    @Operation(summary = "Create new TaskStatus")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "TaskStatuses created",
                content = @Content( mediaType = "application/json",
                        schema = @Schema(implementation = TaskStatus.class) )),
        @ApiResponse(responseCode = "403", description = "Unauthorized request"),
            @ApiResponse(responseCode = "422", description = "Invalid data given")
    })
    public TaskStatus createTaskStatus(@Parameter(description = "new TaskStatus data")
                                       @RequestBody @Valid TaskStatusDto taskStatusDto) {
        return taskStatusService.createTaskStatus(taskStatusDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update TaskStatus with given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TaskStatuses updated",
                    content = @Content( mediaType = "application/json",
                            schema = @Schema(implementation = TaskStatus.class) )),
            @ApiResponse(responseCode = "403", description = "Unauthorized request"),
            @ApiResponse(responseCode = "422", description = "Invalid data given"),
            @ApiResponse(responseCode = "404", description = "No TaskStatus with such id")
    })
    public TaskStatus updateTaskStatus(@Parameter(description = "TaskStatus id")
                                       @PathVariable long id,
                                       @Parameter(description = "new TaskStatus data")
                                       @RequestBody @Valid TaskStatusDto taskStatusDto) {
        return taskStatusService.updateTaskStatus(id, taskStatusDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete TaskStatus with given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TaskStatuses deleted",
                    content = @Content( mediaType = "application/json",
                            schema = @Schema(implementation = TaskStatus.class) )),
            @ApiResponse(responseCode = "403", description = "Unauthorized request"),
            @ApiResponse(responseCode = "404", description = "No TaskStatus with such id")
    })
    public void deleteTaskStatus(@Parameter(description = "TaskStatus id")
                                 @PathVariable long id) {
        taskStatusService.deleteTaskStatus(id);
    }
}
