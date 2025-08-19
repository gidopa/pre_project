package com.greencat.pre_project.application.dto.subtask;

import com.greencat.pre_project.domain.enums.TodoStatus;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubtaskCreateRequest {

  private UUID todoId;
  @NotBlank
  private String title;
  private String description;
  @NotBlank
  private TodoStatus status;
}
