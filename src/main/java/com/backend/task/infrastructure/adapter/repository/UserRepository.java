package com.backend.task.infrastructure.adapter.repository;

import com.backend.task.infrastructure.adapter.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity,String> {

    Optional<UserEntity> findByEmail(String email);
}
