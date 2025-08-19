package com.greencat.pre_project.application.dto.subtask;

import com.greencat.pre_project.domain.entity.SubTask;
import com.greencat.pre_project.domain.enums.TodoStatus;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

public record SubtaskResponse(@NotBlank UUID id,
                              @NotBlank String title,
                              String description,
                              @NotBlank TodoStatus status,
                              UUID todoId,
                              Boolean isDeleted,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt) {

  public static SubtaskResponse changeEntityToResponse(SubTask subtask) {
    return new SubtaskResponse(
        subtask.getId(),
        subtask.getTitle(),
        subtask.getDescription(),
        subtask.getStatus(),
        subtask.getTodo().getId(),
        subtask.getIsDeleted(),
        subtask.getCreatedAt(),
        subtask.getUpdatedAt()
    );
  }

}
