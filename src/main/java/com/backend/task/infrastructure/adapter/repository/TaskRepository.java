package com.backend.task.infrastructure.adapter.repository;

import com.backend.task.domain.model.enums.EnumPriority;
import com.backend.task.domain.model.enums.EnumStatus;
import com.backend.task.infrastructure.adapter.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findByTaskCodeAndStartDate(Long taskCode, LocalDate startDate);

    @Query("SELECT o FROM TaskEntity o ORDER BY o.addedDate ASC")
    List<TaskEntity> findAllOrderByAddedDateAsc();

    @Query("SELECT o FROM TaskEntity o ORDER BY o.addedDate DESC")
    List<TaskEntity> findAllOrderByAddedDateDesc();

    List<TaskEntity> findByStatus(EnumStatus status);

    List<TaskEntity> findByStartDate(LocalDate startDate);

    List<TaskEntity> findByAssignedPerson(String assignedPerson);

    List<TaskEntity> findByPriority(EnumPriority priority);




}
