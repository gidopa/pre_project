package com.greencat.pre_project.infrastructure.repository;

import com.greencat.pre_project.domain.entity.Todo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, UUID> {

  @Query("select distinct t from Todo t left join fetch t.subTasks st where t.user.userId = :userId")
  List<Todo> findAllByUser(@Param("userId") UUID userId);

  @Query("select distinct t from Todo t left join fetch t.subTasks st where t.id = :todoId and t.user.userId = :userId")
  Optional<Todo> findByIdAndUserId(@Param("todoId") UUID todoId,@Param("userId") UUID userId);

  @Query("select distinct t from Todo t left join fetch t.subTasks st where t.dueTime between :from and :to")
  List<Todo> findByDueTime(LocalDateTime from, LocalDateTime to);
}
