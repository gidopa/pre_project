package com.greencat.pre_project.application.service;

import com.greencat.pre_project.application.dto.todo.TodoCreateRequestDto;
import com.greencat.pre_project.application.dto.todo.TodoResponseWithSubtask;
import com.greencat.pre_project.application.dto.todo.TodoStatusUpdateRequest;
import com.greencat.pre_project.application.dto.todo.TodoUpdateRequestDto;
import com.greencat.pre_project.domain.entity.Todo;
import com.greencat.pre_project.domain.entity.Users;
import com.greencat.pre_project.exception.error_code.TodoErrorCode;
import com.greencat.pre_project.exception.exception.PreTaskException;
import com.greencat.pre_project.infrastructure.repository.TodoRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TodoService {

  private final TodoRepository todoRepository;
  private final UserService userService;
  private final CacheManager cm;

  public TodoResponseWithSubtask createTodo(TodoCreateRequestDto requestDto) {
    // Login 미구현으로 기본 admin 계정 이용
    Users user = userService.findUserByUsername("admin");
    Todo todo = Todo.create(requestDto, user);
    Todo savedTodo = todoRepository.save(todo);
    cm.getCache("todoListCache").evict(user.getUsername());
    cm.getCache("todoCache").put(todo.getId(), TodoResponseWithSubtask.changeEntityToResponse(todo));
    return TodoResponseWithSubtask.changeEntityToResponse(savedTodo);
  }

  public TodoResponseWithSubtask updateTodo(TodoUpdateRequestDto requestDto, UUID todoId) {
    Todo todo = todoRepository.findById(todoId)
        .orElseThrow(() -> new PreTaskException(TodoErrorCode.NOT_FOUND));
    Users user = userService.findUserByUsername("admin");
    todo.updateTodo(requestDto);
    cm.getCache("todoListCache").evict(user.getUsername());
    cm.getCache("todoCache").put(todo.getId(), TodoResponseWithSubtask.changeEntityToResponse(todo));
    return TodoResponseWithSubtask.changeEntityToResponse(todo);
  }

//  @CacheEvict(cacheNames = "todoAllCache", key="#todoId")
  public void deleteTodo(UUID todoId) {
    Todo todo = todoRepository.findById(todoId)
        .orElseThrow(() -> new PreTaskException(TodoErrorCode.NOT_FOUND));
    Users user = userService.findUserByUsername("admin");
    cm.getCache("todoListCache").evict(user.getUsername());
    cm.getCache("todoCache").evict(todo.getId());
    todo.softDelete(todo);
  }

   //Login 구현하면 userId를 JWT에서 추출해서 사용
  // 캐싱의 키도 로그인 구현이후 userId를 기준으로
  @Cacheable(cacheNames = "todoListCache", key="#username")
  @Transactional(readOnly = true)
  public List<TodoResponseWithSubtask> getMyTodos(String username) {
    Users user = userService.findUserByUsername(username);
    List<Todo> todos = todoRepository.findAllByUser(user.getUserId());
    return todos.stream()
        .map(TodoResponseWithSubtask::changeEntityToResponse).collect(Collectors.toList());
  }

  // 필요 시, QueryDSL 이용, 검색 기능 추가 가능
  @Cacheable(cacheNames = "todoCache", key = "#todoId")
  public TodoResponseWithSubtask getOneTodo(UUID todoId) {
    Users user = userService.findUserByUsername("admin");
    Todo todo = todoRepository.findByIdAndUserId(todoId, user.getUserId())
        .orElseThrow(() -> new PreTaskException(TodoErrorCode.NOT_FOUND));

    log.info("subtask 수 : {}", todo.getSubTasks().size());
    return TodoResponseWithSubtask.changeEntityToResponse(todo);
  }


  public TodoResponseWithSubtask updateTodoStatus(UUID todoId, TodoStatusUpdateRequest request, String username) {
    Users user = userService.findUserByUsername(username);
    Todo todo = todoRepository.findByIdAndUserId(todoId, user.getUserId())
        .orElseThrow(() -> new PreTaskException(TodoErrorCode.NOT_FOUND));
    todo.updateTodoStatus(request.getStatus());
    cm.getCache("todoListCache").evict(user.getUsername());
    cm.getCache("todoCache").put(todo.getId(), TodoResponseWithSubtask.changeEntityToResponse(todo));
    return TodoResponseWithSubtask.changeEntityToResponse(todo);
  }
}
