package com.greencat.pre_project.application.dto.todo;

import com.greencat.pre_project.domain.enums.TodoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TodoStatusUpdateRequest {

  public TodoStatus status;
}
