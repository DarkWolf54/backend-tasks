package com.backend.task.infrastructure.adapter;

import com.backend.task.domain.model.Task;
import com.backend.task.domain.model.enums.EnumPriority;
import com.backend.task.domain.model.enums.EnumStatus;
import com.backend.task.domain.port.TaskPersistencePort;
import com.backend.task.infrastructure.adapter.entity.TaskEntity;
import com.backend.task.infrastructure.adapter.exception.TaskException;
import com.backend.task.infrastructure.adapter.mapper.TaskDbMapper;
import com.backend.task.infrastructure.adapter.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public List<Task> getAllTasks(String order) {
        if ("desc".equalsIgnoreCase(order)){
            return taskRepository.findAllOrderByAddedDateDesc()
                    .stream()
                    .map(taskDBMapper::toDomain)
                    .toList();
        }
        else {
            return taskRepository.findAllOrderByAddedDateAsc()
                    .stream()
                    .map(taskDBMapper::toDomain)
                    .toList();
        }
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
    public List<Task> getTasksByStatus(EnumStatus status) {
        return taskRepository.findByStatus(status)
                .stream()
                .map(taskDBMapper::toDomain)
                .toList();
    }

    @Override
    public List<Task> getTasksByStartDate(LocalDate startDate) {
        return taskRepository.findByStartDate(startDate)
                .stream()
                .map(taskDBMapper::toDomain)
                .toList();
    }

    @Override
    public List<Task> getTasksByAssignedPerson(String assignedPerson) {
        return taskRepository.findByAssignedPerson(assignedPerson)
                .stream()
                .map(taskDBMapper::toDomain)
                .toList();
    }

    @Override
    public List<Task> getTasksByPriority(EnumPriority priority) {
        return taskRepository.findByPriority(priority)
                .stream()
                .map(taskDBMapper::toDomain)
                .toList();
    }

    //TODO: Reviar el edit para las validacione y addedDate, mirar si al final es necesario un request solo para edit, buscar la tarea, modificarla y ya enviarla para save, mirar si se puede cambiar LDT por LD
    @Override
    public Task edit(Task request) {
        TaskEntity taskEntityToEdit = taskDBMapper.toDb(request);
        TaskEntity taskEntityEdited = taskRepository.save(taskEntityToEdit);

        return taskDBMapper.toDomain(taskEntityEdited);
    }

    @Override
    public boolean doesTaskAlreadyExists(Long taskCode, LocalDate startDate) {
        Optional<TaskEntity> taskEntity = taskRepository.findByTaskCodeAndStartDate(taskCode, startDate);
        return taskEntity.isPresent();
    }





}
