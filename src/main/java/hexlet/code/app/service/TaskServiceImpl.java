package hexlet.code.app.service;

import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskStatusService taskStatusService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;

    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskStatusService taskStatusService,
                           UserService userService,
                           UserRepository userRepository,
                           LabelRepository labelRepository) {
        this.taskRepository = taskRepository;
        this.taskStatusService = taskStatusService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.labelRepository = labelRepository;
    }

    @Override
    public Task createTask(String authHeader, TaskDto givenTask) {
        Task task = new Task();

        User author = userService.getCurrentUser(authHeader);
        TaskStatus taskStatus = taskStatusService.getTaskStatus(givenTask.getTaskStatusId());

        task.setAuthor(author);
        task.setName(givenTask.getName());
        task.setDescription(givenTask.getDescription());
        task.setTaskStatus(taskStatus);

        if (givenTask.getExecutorId() != null) {
            User executor = userRepository.findById(givenTask.getExecutorId())
                    .orElse(null);
            task.setExecutor(executor);
        }

        if (givenTask.getLabelsIds() != null) {
            List<Label> labels = labelRepository.findAllById(givenTask.getLabelsIds());
            task.setLabels(new HashSet<>(labels));
        }

        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, TaskDto newData) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No such task"));

        // User author = userService.getCurrentUser();
        TaskStatus taskStatus = taskStatusService.getTaskStatus(newData.getTaskStatusId());

        // task.setAuthor(author);
        task.setName(newData.getName());
        task.setDescription(newData.getDescription());
        task.setTaskStatus(taskStatus);

        if (newData.getExecutorId() != null) {
            User executor = userRepository.findById(newData.getExecutorId())
                    .orElse(null);
            task.setExecutor(executor);
        }
        if (newData.getLabelsIds() != null) {
            List<Label> labels = labelRepository.findAllById(newData.getLabelsIds());
            task.setLabels(new HashSet<>(labels));
        }

        return taskRepository.save(task);
    }

    @Override
    public ResponseEntity<String> deleteTask(String authHeader, Long id) {
        User user = userService.getCurrentUser(authHeader);
        Task currentTask = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No such task"));

        if ( !(user.getId() == currentTask.getAuthor().getId()) ) {
            log.error("Only author can delete his tasks");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        taskRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
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
