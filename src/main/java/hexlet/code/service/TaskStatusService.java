package hexlet.code.service;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;

public interface TaskStatusService {

    TaskStatus createTaskStatus(TaskStatusDto taskStatusDto);
    TaskStatus updateTaskStatus(Long id, TaskStatusDto newData);
    void deleteTaskStatus(Long id);
    TaskStatus getTaskStatus(Long id);
    Iterable<TaskStatus> getAllTaskStatuses();
}
