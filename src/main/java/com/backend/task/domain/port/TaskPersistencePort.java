package com.backend.task.domain.port;

import com.backend.task.domain.model.Task;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskPersistencePort {

    List<Task> getAllTasks();
    Task create(Task request);
    boolean doesTaskAlreadyExists(Long taskCode, LocalDateTime startDate);
    void deleteById(Long taskCode);
    Task getTaskById(Long taskCode);
}
