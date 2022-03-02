package hexlet.code.app.service;

import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.model.TaskStatus;

public interface TaskStatusService {

    TaskStatus createTaskStatus(TaskStatusDto taskStatusDto);
    TaskStatus updateTaskStatus(Long id, TaskStatusDto newData);
    void deleteTaskStatus(Long id);
    TaskStatus getTaskStatus(Long id);
    Iterable<TaskStatus> getAllTaskStatuses();
}
