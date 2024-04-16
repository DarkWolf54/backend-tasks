package com.backend.task.application.mapper;

import com.backend.task.domain.model.Task;
import com.backend.task.domain.model.dto.request.TaskRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskRequestMapper {

    Task toDomain(TaskRequest request);
}
