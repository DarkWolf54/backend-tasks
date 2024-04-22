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

import java.time.LocalDate;
import java.util.List;

import static com.backend.task.domain.utils.DateUtils.differenceInDays;

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
        taskToCreate.setAddedDate(LocalDate.now());
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
        Task taskToEdit = taskPersistencePort.getTaskById(taskCode);
        taskToEdit.validateDoneTask();
        taskToEdit.validateHighPriorityTaskEdition();

        if (differenceInDays(taskToEdit.getStartDate(), request.getEndDate()) < 0){
            throw new TaskException(HttpStatus.BAD_REQUEST, "La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        if (differenceInDays(LocalDate.now(), request.getEndDate()) < 0 && request.getStatus() != EnumStatus.CANCELLED){
            throw new TaskException(HttpStatus.BAD_REQUEST, "Para este caso solo es posible cambiar el estado a Cancelada");
        }

        if ((request.getAssignedPerson() != null && !request.getAssignedPerson().isEmpty()) && taskToEdit.getStatus() != EnumStatus.NEW){
            throw new TaskException(HttpStatus.BAD_REQUEST, "Solo las tareas con status Nueva pueden ser asigandas a otra persona");
        }

        if (request.getAssignedPerson() != null && !request.getAssignedPerson().isEmpty()){
            taskToEdit.setAssignedPerson(request.getAssignedPerson());
        }

        taskToEdit.setAssignedPerson(request.getAssignedPerson());
        taskToEdit.setStatus(request.getStatus());
        taskToEdit.setEndDate(request.getEndDate());
        taskToEdit.setCommentaries(request.getCommentaries());

        Task taskEdited = taskPersistencePort.edit(taskToEdit);

        return taskDtoMapper.toDto(taskEdited);

    }

    @Override
    public List<TaskDto> findByStatus(EnumStatus status, String order) {
        List<Task> tasks = taskPersistencePort.getTasksByStatus(status, order);
        return tasks
                .stream()
                .map(taskDtoMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> findByStartDate(LocalDate startDate, String order) {
        List<Task> tasks = taskPersistencePort.getTasksByStartDate(startDate, order);
        return tasks
                .stream()
                .map(taskDtoMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> findByAssignedPerson(String assignedPerson, String order) {
        List<Task> tasks = taskPersistencePort.getTasksByAssignedPerson(assignedPerson, order);
        return tasks
                .stream()
                .map(taskDtoMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> findByPriority(EnumPriority priority, String order) {
        List<Task> tasks = taskPersistencePort.getTasksByPriority(priority, order);
        return tasks
                .stream()
                .map(taskDtoMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> searchTasks(EnumStatus status, LocalDate startDate, String assignedPerson, EnumPriority priority, String order) {
        List<Task> tasks = taskPersistencePort.searchTasks(status, startDate, assignedPerson, priority, order);
        return tasks
                .stream()
                .map(taskDtoMapper::toDto)
                .toList();
    }
}
