package com.greencat.pre_project.application.dto.todo;

import com.greencat.pre_project.domain.enums.TodoPriority;
import com.greencat.pre_project.domain.enums.TodoStatus;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoResponseDto {

  @NotBlank
  private UUID id;

  @NotBlank
  private String title;
  private String description;
  @NotBlank
  private TodoStatus status;
  @NotBlank
  private TodoPriority priority;
  private UUID userId;
  private List<UUID> subTasksId = new ArrayList<>();
  private LocalDateTime dueTime;
  private Boolean isDelete = false;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
