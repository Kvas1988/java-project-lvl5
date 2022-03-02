package hexlet.code.app.repository;

import hexlet.code.app.model.TaskStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStatusRepository extends CrudRepository<TaskStatus, Long> {
}
