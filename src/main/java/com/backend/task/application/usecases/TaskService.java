package com.backend.task.application.usecases;

import com.backend.task.domain.model.dto.TaskDto;
import com.backend.task.domain.model.dto.request.TaskRequest;

import java.util.List;

public interface TaskService {

    List<TaskDto> getAllTasks(String order);
    TaskDto createNew(TaskRequest request);
    void deleteTaskById(Long taskCode);

}
