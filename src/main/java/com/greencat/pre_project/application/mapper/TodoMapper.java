package com.greencat.pre_project.application.mapper;

import com.greencat.pre_project.application.dto.todo.TodoResponseDto;
import com.greencat.pre_project.domain.entity.Todo;
import org.springframework.stereotype.Component;

@Component
public class TodoMapper {

  public TodoResponseDto changeEntityToResponse(Todo todo) {
    TodoResponseDto response = new TodoResponseDto();
    response.setId(todo.getId());
    response.setTitle(todo.getTitle());
    response.setStatus(todo.getStatus());
    response.setDescription(todo.getDescription());
    response.setPriority(todo.getPriority());
    response.setDueTime(todo.getDueTime());
    response.setCreatedAt(todo.getCreatedAt());
    response.setUpdatedAt(todo.getUpdatedAt());
    response.setUserId(todo.getUser().getUserId());
    return response;
  }
}
