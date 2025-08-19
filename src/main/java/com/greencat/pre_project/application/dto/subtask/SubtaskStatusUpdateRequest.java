package com.greencat.pre_project.application.dto.subtask;

import com.greencat.pre_project.domain.enums.TodoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubtaskStatusUpdateRequest {

  public TodoStatus status;
}
