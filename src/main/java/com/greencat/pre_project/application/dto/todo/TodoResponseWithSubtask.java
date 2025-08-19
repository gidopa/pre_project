package com.greencat.pre_project.application.dto.todo;

import com.greencat.pre_project.application.dto.subtask.SubtaskResponse;
import com.greencat.pre_project.domain.entity.Todo;
import com.greencat.pre_project.domain.enums.TodoPriority;
import com.greencat.pre_project.domain.enums.TodoStatus;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TodoResponseWithSubtask(
    @NotBlank UUID id,
    @NotBlank String title,
    String description,
    @NotBlank TodoStatus status,
    @NotBlank TodoPriority priority,
    UUID userId,
    List<SubtaskResponse> Subtasks,
    LocalDateTime dueTime,
    Boolean isDelete,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

  public static TodoResponseWithSubtask changeEntityToResponse(Todo todo) {
    return new TodoResponseWithSubtask(
        todo.getId(),
        todo.getTitle(),
        todo.getDescription(),
        todo.getStatus(),
        todo.getPriority(),
        todo.getUser() != null ? todo.getUser().getUserId() : null,
        todo.getSubTasks() != null
            ? todo.getSubTasks().stream()
            .map(SubtaskResponse::changeEntityToResponse) // 서브태스크도 DTO 변환
            .toList()
            : List.of(),
        todo.getDueTime(),
        todo.getIsDeleted(),
        todo.getCreatedAt(),
        todo.getUpdatedAt()
    );
  }
}
