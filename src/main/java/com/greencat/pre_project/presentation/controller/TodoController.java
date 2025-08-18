package com.greencat.pre_project.presentation.controller;

import com.greencat.pre_project.application.dto.todo.TodoCreateRequestDto;
import com.greencat.pre_project.application.dto.todo.TodoResponseDto;
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
import org.springframework.web.bind.annotation.PutMapping;
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
  public TodoResponseDto createTodo(@RequestBody TodoCreateRequestDto requestDto) {
    return todoService.createTodo(requestDto);
  }

  @PatchMapping("/{todoId}")
  @ResponseStatus(HttpStatus.OK)
  public TodoResponseDto updateTodo(
      @RequestBody TodoUpdateRequestDto requestDto,
      @PathVariable UUID todoId) {
    return todoService.updateTodo(requestDto, todoId);
  }

  @DeleteMapping("/{todoId}")
  public ResponseEntity<String> deleteTodo(@PathVariable UUID todoId) {
    todoService.deleteTodo(todoId);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<TodoResponseDto> getMyTodos() {
    // principal.getUserId();
    return todoService.getMyTodos();
  }




}
