package hexlet.code.app.service;

import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TaskStatusServiceImpl implements TaskStatusService {

    @Autowired
    private TaskStatusRepository repository;

    @Override
    public TaskStatus createTaskStatus(TaskStatusDto taskStatusDto) {
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setName(taskStatus.getName());
        return repository.save(taskStatus);
    }

    @Override
    public TaskStatus updateTaskStatus(Long id, TaskStatusDto newData) {
        TaskStatus taskStatus = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(""));
        taskStatus.setName(newData.getName());
        return repository.save(taskStatus);
    }

    @Override
    public void deleteTaskStatus(Long id) {
        TaskStatus existingUser = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(""));

        repository.deleteById(id);
    }

    @Override
    public TaskStatus getTaskStatus(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(""));
    }

    @Override
    public Iterable<TaskStatus> getAllTaskStatuses() {
        return repository.findAll();
    }
}
