package com.backend.task.application.usecases;

import com.backend.task.domain.model.dto.TaskDto;
import com.backend.task.domain.model.dto.request.TaskRequest;
import com.backend.task.domain.model.enums.EnumPriority;
import com.backend.task.domain.model.enums.EnumStatus;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    List<TaskDto> getAllTasks(String order);
    TaskDto createNew(TaskRequest request);
    void deleteTaskById(Long taskCode);
    TaskDto editTask(Long taskCode, TaskRequest request);
    List<TaskDto> findByStatus(EnumStatus status);
    List<TaskDto> findByStartDate(LocalDate startDate);
    List<TaskDto> findByAssignedPerson(String assignedPerson);
    List<TaskDto> findByPriority(EnumPriority priority);



}
