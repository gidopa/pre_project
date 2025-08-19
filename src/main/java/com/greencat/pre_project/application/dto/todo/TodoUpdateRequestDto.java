package com.greencat.pre_project.application.dto.todo;

import com.greencat.pre_project.domain.enums.TodoPriority;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoUpdateRequestDto {

  private String title;
  private String description;
  private TodoPriority priority;
  private LocalDateTime dueTime;
}
