package com.backend.task.domain.port;

import com.backend.task.domain.model.Task;
import com.backend.task.domain.model.enums.EnumPriority;
import com.backend.task.domain.model.enums.EnumStatus;

import java.time.LocalDate;
import java.util.List;

public interface TaskPersistencePort {

    List<Task> getAllTasks(String order);
    Task create(Task request);
    boolean doesTaskAlreadyExists(Long taskCode, LocalDate startDate);
    void deleteById(Long taskCode);
    Task getTaskById(Long taskCode);
    List<Task> getTasksByStatus(EnumStatus status, String order);
    List<Task> getTasksByStartDate(LocalDate startDate, String order);
    List<Task> getTasksByAssignedPerson(String assignedPerson, String order);
    List<Task> getTasksByPriority(EnumPriority priority, String order);
    Task edit(Task request);
    List<Task> searchTasks(EnumStatus status, LocalDate startDate, String assignedPerson, EnumPriority priority, String order);
}
