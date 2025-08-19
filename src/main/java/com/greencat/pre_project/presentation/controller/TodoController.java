package com.greencat.pre_project.presentation.controller;

import com.greencat.pre_project.application.dto.todo.TodoCreateRequestDto;
import com.greencat.pre_project.application.dto.todo.TodoResponseWithSubtask;
import com.greencat.pre_project.application.dto.todo.TodoStatusUpdateRequest;
import com.greencat.pre_project.application.dto.todo.TodoUpdateRequestDto;
import com.greencat.pre_project.application.service.TodoService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {

  private final TodoService todoService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TodoResponseWithSubtask createTodo(@RequestBody TodoCreateRequestDto requestDto) {
    return todoService.createTodo(requestDto);
  }

  @PatchMapping("/{todoId}")
  @ResponseStatus(HttpStatus.OK)
  public TodoResponseWithSubtask updateTodo(
      @RequestBody TodoUpdateRequestDto requestDto,
      @PathVariable UUID todoId) {
    return todoService.updateTodo(requestDto, todoId);
  }

  @PatchMapping("/{todoId}/status")
  @ResponseStatus(HttpStatus.OK)
  public TodoResponseWithSubtask updateTodoStatus(@PathVariable UUID todoId, @RequestBody TodoStatusUpdateRequest request) {
    String username = "admin";
    return todoService.updateTodoStatus(todoId, request, username);
  }

  @DeleteMapping("/{todoId}")
  public ResponseEntity<String> deleteTodo(@PathVariable UUID todoId) {
    todoService.deleteTodo(todoId);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<TodoResponseWithSubtask> getMyTodos() {
    // principal.getUserId();
    String username = "admin";
    return todoService.getMyTodos(username);
  }

  @GetMapping("/{todoId}")
  @ResponseStatus(HttpStatus.OK)
  public TodoResponseWithSubtask getTodo(@PathVariable UUID todoId) {
    return todoService.getOneTodo(todoId);
  }




}
