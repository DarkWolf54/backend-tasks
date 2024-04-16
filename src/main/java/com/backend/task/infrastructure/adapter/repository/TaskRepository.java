package com.backend.task.infrastructure.adapter.repository;

import com.backend.task.infrastructure.adapter.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findByTaskCodeAndStartDate(Long taskCode, LocalDateTime startDate);

}
