package com.greencat.pre_project.presentation.controller;

import com.greencat.pre_project.application.dto.subtask.SubtaskCreateRequest;
import com.greencat.pre_project.application.dto.subtask.SubtaskResponse;
import com.greencat.pre_project.application.dto.subtask.SubtaskStatusUpdateRequest;
import com.greencat.pre_project.application.dto.subtask.SubtaskUpdateRequest;
import com.greencat.pre_project.application.dto.todo.TodoResponseWithSubtask;
import com.greencat.pre_project.application.service.SubtaskService;
import com.greencat.pre_project.application.service.TodoService;
import com.greencat.pre_project.application.service.UserService;
import com.greencat.pre_project.domain.entity.Users;
import com.greencat.pre_project.exception.error_code.SubtaskErrorCode;
import com.greencat.pre_project.exception.exception.PreTaskException;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subtask")
public class SubtaskController {

  private final TodoService todoService;
  private final SubtaskService subtaskService;
  private final UserService userService;

  @Operation(summary = "Todo 하위 Subtask 생성")
  @PostMapping
  public SubtaskResponse createSubtask(@RequestBody SubtaskCreateRequest request) {
    // subtask에 대한 권한 검증
    TodoResponseWithSubtask oneTodo = todoService.getOneTodo(request.getTodoId());
    Users user = userService.findUserByUsername("admin");
    if (oneTodo.userId().equals(user.getUserId())) {
      return subtaskService.createSubtask(request, user.getUsername());
    }else{
      throw new PreTaskException(SubtaskErrorCode.NOT_AUTHORIZED);
    }
  }

  @Operation(summary = "Subtask 수정", description = "subtask 진행 상태 외 정보 수정")
  @PatchMapping("/{subtaskId}")
  public SubtaskResponse updateSubtask(@PathVariable UUID subtaskId, @RequestBody SubtaskUpdateRequest request) {
    // subtask에 대한 권한 검증
    SubtaskResponse foundSubTask = subtaskService.getOneSubTask(subtaskId);
    TodoResponseWithSubtask foundTodo = todoService.getOneTodo(foundSubTask.todoId());
    Users user = userService.findUserByUsername("admin");
    if(foundTodo.userId().equals(user.getUserId())) {
      return subtaskService.updateSubtask(subtaskId, request, user.getUsername());
    }else{
      throw new PreTaskException(SubtaskErrorCode.NOT_AUTHORIZED);
    }
  }

  @Operation(summary = "subtask 진행 상태 수정")
  @PatchMapping("/{subtaskId}/status")
  public SubtaskResponse updateSubtaskStatus(@PathVariable UUID subtaskId, @RequestBody SubtaskStatusUpdateRequest request) {
    SubtaskResponse foundSubTask = subtaskService.getOneSubTask(subtaskId);
    TodoResponseWithSubtask foundTodo = todoService.getOneTodo(foundSubTask.todoId());
    Users user = userService.findUserByUsername("admin");
    if(foundTodo.userId().equals(user.getUserId())) {
      return subtaskService.updateSubtaskStatus(subtaskId, request, user.getUsername());
    }else{
      throw new PreTaskException(SubtaskErrorCode.NOT_AUTHORIZED);
    }
  }

  @Operation(summary = "subtask soft delete")
  @DeleteMapping("/{subtaskId}")
  public ResponseEntity<String> deleteSubtask(@PathVariable UUID subtaskId) {
    Users user = userService.findUserByUsername("admin");
    // 캐싱 무효화에 쓰일 키를 위해 username을 같이 넘김
    subtaskService.softDelete(subtaskId, user.getUsername());
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  // Subtask의 경우 Todo에서 하위 subtask 전체 조회를 하거나
  // 단건 조회만 될거라고 예상해서 전체 조회 api 구현X
  @Operation(summary = "subtask 단건 조회")
  @GetMapping("/{subtaskId}")
  public SubtaskResponse getAllSubtasks(@PathVariable UUID subtaskId) {
    return subtaskService.getOneSubTask(subtaskId);
  }


}
