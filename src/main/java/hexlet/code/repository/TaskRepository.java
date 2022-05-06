package hexlet.code.repository;

import hexlet.code.model.Task;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long>,
        QuerydslPredicateExecutor<Task> {
}
