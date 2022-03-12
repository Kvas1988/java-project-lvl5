package hexlet.code.app.service;

import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusServiceImpl taskStatusService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Task createTask(TaskDto givenTask) {
        Task task = new Task();

        User author = userService.getCurrentUser();
        task.setAuthor(author);

        task.setName(givenTask.getName());

        task.setDescription(givenTask.getDescription());

        TaskStatus taskStatus = taskStatusService.getTaskStatus(givenTask.getTaskStatusId());
        task.setTaskStatus(taskStatus);

        if (givenTask.getExecutorId() != null) {
            User executor = userRepository.findById(givenTask.getExecutorId())
                    .orElse(null);
            task.setExecutor(executor);
        }

        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, TaskDto newData) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No such task"));

        User author = userService.getCurrentUser();
        task.setAuthor(author);

        task.setName(newData.getName());

        task.setDescription(newData.getDescription());

        TaskStatus taskStatus = taskStatusService.getTaskStatus(newData.getTaskStatusId());
        task.setTaskStatus(taskStatus);

        if (newData.getExecutorId() != null) {
            User executor = userRepository.findById(newData.getExecutorId())
                    .orElse(null);
            task.setExecutor(executor);
        }

        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No such task"));
    }

    @Override
    public Iterable<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
