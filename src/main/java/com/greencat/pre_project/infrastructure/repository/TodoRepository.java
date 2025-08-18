package com.greencat.pre_project.infrastructure.repository;

import com.greencat.pre_project.domain.entity.Todo;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, UUID> {

  @Query("select t from Todo t where t.user.userId = :userId")
  List<Todo> findAllByUser(@Param("userId") UUID userId);
}
