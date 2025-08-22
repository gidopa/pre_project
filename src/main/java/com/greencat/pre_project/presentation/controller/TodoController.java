package com.greencat.pre_project.presentation.controller;

import com.greencat.pre_project.application.dto.todo.TodoCreateRequestDto;
import com.greencat.pre_project.application.dto.todo.TodoResponseWithSubtask;
import com.greencat.pre_project.application.dto.todo.TodoStatusUpdateRequest;
import com.greencat.pre_project.application.dto.todo.TodoUpdateRequestDto;
import com.greencat.pre_project.application.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
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

  @Operation(summary = "Todo 생성", description = "title, 마감기한, 설명 등을 받아 todo 생성")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TodoResponseWithSubtask createTodo(@RequestBody TodoCreateRequestDto requestDto) {
    return todoService.createTodo(requestDto);
  }

  @Operation(summary = "Todo 수정", description = "상태(Todo, Done)를 제외한 정보 수정")
  @PatchMapping("/{todoId}")
  @ResponseStatus(HttpStatus.OK)
  public TodoResponseWithSubtask updateTodo(
      @RequestBody TodoUpdateRequestDto requestDto,
      @PathVariable UUID todoId) {
    return todoService.updateTodo(requestDto, todoId);
  }

  @Operation(summary = "Todo 상태 수정", description = "진행 상태를 수정")
  @PatchMapping("/{todoId}/status")
  @ResponseStatus(HttpStatus.OK)
  public TodoResponseWithSubtask updateTodoStatus(@PathVariable UUID todoId, @RequestBody TodoStatusUpdateRequest request) {
    String username = "admin";
    return todoService.updateTodoStatus(todoId, request, username);
  }

  @Operation(summary = "todo soft delete")
  @DeleteMapping("/{todoId}")
  public ResponseEntity<String> deleteTodo(@PathVariable UUID todoId) {
    todoService.deleteTodo(todoId);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @Operation(summary = "이용자별 todo list")
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<TodoResponseWithSubtask> getMyTodos() {
    // principal.getUserId();
    String username = "admin";
    return todoService.getMyTodos(username);
  }

  @Operation(summary = "이용자 별 todo 단건 조회")
  @GetMapping("/{todoId}")
  @ResponseStatus(HttpStatus.OK)
  public TodoResponseWithSubtask getTodo(@PathVariable UUID todoId) {
    return todoService.getOneTodo(todoId);
  }




}
