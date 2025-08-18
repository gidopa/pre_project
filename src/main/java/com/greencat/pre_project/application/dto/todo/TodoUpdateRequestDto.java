package com.greencat.pre_project.application.dto.todo;

import com.greencat.pre_project.domain.enums.TodoPriority;
import com.greencat.pre_project.domain.enums.TodoStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoUpdateRequestDto {

  private UUID todoId;
  private String title;
  private String description;
  private TodoPriority priority;
  private TodoStatus status;
  private LocalDateTime dueTime;
}
