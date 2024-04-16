package com.backend.task.application.mapper;

import com.backend.task.domain.model.Task;
import com.backend.task.domain.model.dto.TaskDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskDtoMapper {

    TaskDto toDto(Task domain);
}
