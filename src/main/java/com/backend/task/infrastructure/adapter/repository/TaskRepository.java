package com.backend.task.infrastructure.adapter.repository;

import com.backend.task.domain.model.enums.EnumPriority;
import com.backend.task.domain.model.enums.EnumStatus;
import com.backend.task.infrastructure.adapter.entity.TaskEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findAll(Sort sort);

    Optional<TaskEntity> findByTaskCodeAndStartDate(Long taskCode, LocalDate startDate);

    List<TaskEntity> findByStatus(EnumStatus status, Sort sort);

    List<TaskEntity> findByStartDate(LocalDate startDate, Sort sort);

    List<TaskEntity> findByAssignedPerson(String assignedPerson, Sort sort);

    List<TaskEntity> findByPriority(EnumPriority priority, Sort sort);

    List<TaskEntity> findByStatusAndStartDateAndAssignedPersonAndPriority(
            EnumStatus status, LocalDate startDate, String assignedPerson, EnumPriority priority, Sort sort);




}
