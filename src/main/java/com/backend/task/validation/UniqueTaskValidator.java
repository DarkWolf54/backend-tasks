package com.backend.task.validation;

import com.backend.task.infrastructure.adapter.entity.TaskEntity;
import com.backend.task.infrastructure.adapter.repository.TaskRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueTaskValidator implements ConstraintValidator<UniqueTask, TaskEntity> {

    private TaskRepository taskRepository;

    public UniqueTaskValidator() {
    }

    public UniqueTaskValidator(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public boolean isValid(TaskEntity task, ConstraintValidatorContext constraintValidatorContext) {
        if (taskRepository == null) return true;
        return taskRepository.findByTaskCodeAndStartDate(task.getTaskCode(), task.getStartDate())
                .isEmpty();
    }
}
