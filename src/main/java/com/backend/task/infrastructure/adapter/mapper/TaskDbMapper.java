package com.backend.task.infrastructure.adapter.mapper;

import com.backend.task.domain.model.Task;
import com.backend.task.infrastructure.adapter.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskDbMapper {

    TaskEntity toDb(Task tasks);

    @Mapping(source = "addedDate", target = "addedDate")
    Task toDomain(TaskEntity tasksEntity);
}
