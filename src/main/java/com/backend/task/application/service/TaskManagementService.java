package com.backend.task.application.service;

import com.backend.task.application.mapper.TaskDtoMapper;
import com.backend.task.application.mapper.TaskRequestMapper;
import com.backend.task.application.usecases.TaskService;
import com.backend.task.domain.model.Task;
import com.backend.task.domain.model.dto.TaskDto;
import com.backend.task.domain.model.dto.request.TaskRequest;
import com.backend.task.domain.model.enums.EnumPriority;
import com.backend.task.domain.model.enums.EnumStatus;
import com.backend.task.domain.port.TaskPersistencePort;
import com.backend.task.infrastructure.adapter.exception.TaskException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskManagementService implements TaskService {

    private final TaskPersistencePort taskPersistencePort;
    private final TaskRequestMapper taskRequestMapper;
    private final TaskDtoMapper taskDtoMapper;

    public TaskManagementService(TaskPersistencePort taskPersistencePort,
                                 TaskRequestMapper taskRequestMapper,
                                 TaskDtoMapper taskDtoMapper) {
        this.taskPersistencePort = taskPersistencePort;
        this.taskRequestMapper = taskRequestMapper;
        this.taskDtoMapper = taskDtoMapper;
    }


    @Override
    public List<TaskDto> getAllTasks(String order) {
        if (!"asc".equalsIgnoreCase(order) && !"desc".equalsIgnoreCase(order)) {
            throw new IllegalArgumentException("El parámetro 'order' debe ser 'asc' o 'desc'");
        }
        List<Task> tasks = taskPersistencePort.getAllTasks(order);
        return tasks
                .stream()
                .map(taskDtoMapper::toDto)
                .toList();
    }

    @Override
    public TaskDto createNew(TaskRequest request) {
        Task taskToCreate = taskRequestMapper.toDomain(request);
        if (taskPersistencePort.doesTaskAlreadyExists(request.getTaskCode(), request.getStartDate())){
            throw new TaskException(HttpStatus.BAD_REQUEST, "Ya existe una tarea con ese código y fecha de inicio");
        }
        taskToCreate.setAddedDate(LocalDateTime.now());
        taskToCreate.validateStartDate();
        taskToCreate.validateCommentariesLength();
        taskToCreate.validateHighPriorityTaskCreation();
        Task taskCreated = taskPersistencePort.create(taskToCreate);
        return taskDtoMapper.toDto(taskCreated);
    }

    @Override
    public void deleteTaskById(Long taskCode) {
        Task taskToDelete = taskPersistencePort.getTaskById(taskCode);
        taskToDelete.validateTaskStatus();
        taskToDelete.validateEndDate();
        taskToDelete.validateHighPriorityTaskDeletion();
        taskPersistencePort.deleteById(taskCode);
    }

    @Override
    public TaskDto editTask(Long taskCode, TaskRequest request) {
        Task taskToEdit = taskRequestMapper.toDomain(request);
        taskToEdit.validateDoneTask();
        taskToEdit.validateHighPriorityTaskEdition();

        taskToEdit.setAssignedPerson(request.getAssignedPerson());
        taskToEdit.setStatus(request.getStatus());
        taskToEdit.setEndDate(request.getEndDate());
        taskToEdit.setCommentaries(request.getCommentaries());

        Task taskEdited = taskPersistencePort.edit(taskCode, taskToEdit);

        return taskDtoMapper.toDto(taskEdited);

    }

    @Override
    public List<TaskDto> findByStatus(EnumStatus status) {
        List<Task> tasks = taskPersistencePort.getTasksByStatus(status);
        return tasks
                .stream()
                .map(taskDtoMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> findByStartDate(LocalDateTime startDate) {
        List<Task> tasks = taskPersistencePort.getTasksByStartDate(startDate);
        return tasks
                .stream()
                .map(taskDtoMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> findByAssignedPerson(String assignedPerson) {
        List<Task> tasks = taskPersistencePort.getTasksByAssignedPerson(assignedPerson);
        return tasks
                .stream()
                .map(taskDtoMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> findByPriority(EnumPriority priority) {
        List<Task> tasks = taskPersistencePort.getTasksByPriority(priority);
        return tasks
                .stream()
                .map(taskDtoMapper::toDto)
                .toList();
    }
}
