package com.greencat.pre_project.application.service;

import com.greencat.pre_project.application.dto.todo.TodoCreateRequestDto;
import com.greencat.pre_project.application.dto.todo.TodoResponseDto;
import com.greencat.pre_project.application.dto.todo.TodoUpdateRequestDto;
import com.greencat.pre_project.domain.entity.Todo;
import com.greencat.pre_project.domain.entity.Users;
import com.greencat.pre_project.exception.error_code.TodoErrorCode;
import com.greencat.pre_project.exception.error_code.UserErrorCode;
import com.greencat.pre_project.exception.exception.PreTaskException;
import com.greencat.pre_project.infrastructure.repository.TodoRepository;
import com.greencat.pre_project.infrastructure.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoService {

  private final TodoRepository todoRepository;
  private final UserRepository userRepository;

  @CacheEvict(cacheNames = "todoAllCache", allEntries = true)
  public TodoResponseDto createTodo(TodoCreateRequestDto requestDto) {
    // Login 미구현으로 기본 admin 계정 이용
    Users user = userRepository.findByUsername("admin")
        .orElseThrow(() -> new PreTaskException(UserErrorCode.NOT_FOUND));
    Todo todo = Todo.create(requestDto, user);
    Todo savedTodo = todoRepository.save(todo);
    return TodoResponseDto.changeEntityToResponse(savedTodo);
  }

  @CacheEvict(cacheNames = "todoAllCache", allEntries = true)
  public TodoResponseDto updateTodo(TodoUpdateRequestDto requestDto, UUID todoId) {
    Todo todo = todoRepository.findById(todoId)
        .orElseThrow(() -> new PreTaskException(TodoErrorCode.NOT_FOUND));
    todo.updateTodo(requestDto);
    return TodoResponseDto.changeEntityToResponse(todo);
  }

  @CacheEvict(cacheNames = "todoAllCache", allEntries = true)
  public void deleteTodo(UUID todoId) {
    Todo todo = todoRepository.findById(todoId)
        .orElseThrow(() -> new PreTaskException(TodoErrorCode.NOT_FOUND));
    todo.softDelete(todo);
  }

   //Login 구현하면 userId를 JWT에서 추출해서 사용
  // 캐싱의 키도 로그인 구현이후 userId를 기준으로
  @Cacheable(cacheNames = "todoAllCache", key="'admin'")
  @Transactional(readOnly = true)
  public List<TodoResponseDto> getMyTodos() {
    Users user = userRepository.findByUsername("admin")
        .orElseThrow(() -> new PreTaskException(UserErrorCode.NOT_FOUND));
    List<Todo> todos = todoRepository.findAllByUser(user.getUserId());
    return todos.stream()
        .map(TodoResponseDto::changeEntityToResponse).collect(Collectors.toList());
  }
}
