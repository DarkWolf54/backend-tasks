package com.backend.task.infrastructure.adapter.repository;

import com.backend.task.infrastructure.adapter.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findByTaskCodeAndStartDate(Long taskCode, LocalDateTime startDate);

    @Query("SELECT o FROM TaskEntity o ORDER BY o.addedDate ASC")
    List<TaskEntity> findAllOrderByAddedDateAsc();

    @Query("SELECT o FROM TaskEntity o ORDER BY o.addedDate DESC")
    List<TaskEntity> findAllOrderByAddedDateDesc();


}
