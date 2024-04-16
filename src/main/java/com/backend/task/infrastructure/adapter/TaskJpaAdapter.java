package com.backend.task.infrastructure.adapter;

import com.backend.task.domain.model.Task;
import com.backend.task.domain.port.TaskPersistencePort;
import com.backend.task.infrastructure.adapter.entity.TaskEntity;
import com.backend.task.infrastructure.adapter.exception.TaskException;
import com.backend.task.infrastructure.adapter.mapper.TaskDbMapper;
import com.backend.task.infrastructure.adapter.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskJpaAdapter implements TaskPersistencePort {

    private final TaskRepository taskRepository;
    private final TaskDbMapper taskDBMapper;

    public TaskJpaAdapter(TaskRepository taskRepository, TaskDbMapper taskDBMapper) {
        this.taskRepository = taskRepository;
        this.taskDBMapper = taskDBMapper;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(taskDBMapper::toDomain)
                .toList();
    }

    @Override
    public Task create(Task request) {
        TaskEntity taskEntityToCreate = taskDBMapper.toDb(request);
        TaskEntity taskEntityCreated = taskRepository.save(taskEntityToCreate);
        return taskDBMapper.toDomain(taskEntityCreated);
    }

    @Override
    public void deleteById(Long taskCode) {
        taskRepository.deleteById(taskCode);
    }

    @Override
    public Task getTaskById(Long taskCode) {
        Optional<TaskEntity> taskEntityFound = taskRepository.findById(taskCode);
        if (taskEntityFound.isEmpty()){
            throw new TaskException(HttpStatus.NOT_FOUND, "No existe una tarea con el id buscado: " + taskCode);
        }
        return taskDBMapper.toDomain(taskEntityFound.get());
    }

    @Override
    public boolean doesTaskAlreadyExists(Long taskCode, LocalDateTime startDate) {
        Optional<TaskEntity> taskEntity = taskRepository.findByTaskCodeAndStartDate(taskCode, startDate);
        return taskEntity.isPresent();
    }





}
