package com.greencat.pre_project.application.dto.todo;

import com.greencat.pre_project.domain.entity.Todo;
import com.greencat.pre_project.domain.enums.TodoPriority;
import com.greencat.pre_project.domain.enums.TodoStatus;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TodoResponseDto(
    @NotBlank UUID id,
    @NotBlank String title,
    String description,
    @NotBlank TodoStatus status,
    @NotBlank TodoPriority priority,
    UUID userId,
    List<UUID> subTasksId,
    LocalDateTime dueTime,
    Boolean isDelete,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
   //Entity → DTO 변환 메서드
  public static TodoResponseDto changeEntityToResponse(Todo todo) {
    return new TodoResponseDto(
        todo.getId(),
        todo.getTitle(),
        todo.getDescription(),
        todo.getStatus(),
        todo.getPriority(),
        todo.getUser() != null ? todo.getUser().getUserId() : null,
        todo.getSubTasks() != null
            ? todo.getSubTasks().stream().map(st -> st.getId()).toList()
            : List.of(),
        todo.getDueTime(),
        todo.getIsDeleted(),
        todo.getCreatedAt(),
        todo.getUpdatedAt()
    );
  }
}