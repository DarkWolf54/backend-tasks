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
    List<Task> getTasksByStatus(EnumStatus status);
    List<Task> getTasksByStartDate(LocalDate startDate);
    List<Task> getTasksByAssignedPerson(String assignedPerson);
    List<Task> getTasksByPriority(EnumPriority priority);
    Task edit(Task request);
}
