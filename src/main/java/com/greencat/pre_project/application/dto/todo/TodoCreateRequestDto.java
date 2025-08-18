package com.greencat.pre_project.application.dto.todo;

import com.greencat.pre_project.domain.enums.TodoPriority;
import com.greencat.pre_project.domain.enums.TodoStatus;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TodoCreateRequestDto {

  @NotBlank
  private String title;
  private String description;
  @NotBlank
  private TodoStatus status;
  @NotBlank
  private TodoPriority priority;
  private LocalDateTime dueTime;
}
