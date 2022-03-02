package hexlet.code.app.controller;

import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.service.TaskStatusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${base-url}" + TaskStatusController.TASK_STATUS_CONTROLLER_PATH)
public class TaskStatusController {

    public final static String TASK_STATUS_CONTROLLER_PATH = "/statuses";

    @Autowired
    private TaskStatusServiceImpl taskStatusService;

    @GetMapping("/{id}")
    public TaskStatus getTaskStatus(@PathVariable long id) {
        return taskStatusService.getTaskStatus(id);
    }

    @GetMapping
    public Iterable<TaskStatus> getAllTaskStatuses() {
        return taskStatusService.getAllTaskStatuses();
    }

    @PostMapping
    public TaskStatus createTaskStatus(@RequestBody TaskStatusDto taskStatusDto) {
        return taskStatusService.createTaskStatus(taskStatusDto);
    }

    @PutMapping("/{id}")
    public TaskStatus updateTaskStatus(@PathVariable long id, @RequestBody TaskStatusDto taskStatusDto) {
        return taskStatusService.updateTaskStatus(id, taskStatusDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTaskStatus(@PathVariable long id) {
        taskStatusService.deleteTaskStatus(id);
    }
}
