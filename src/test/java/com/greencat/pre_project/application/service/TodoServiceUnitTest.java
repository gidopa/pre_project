package com.greencat.pre_project.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.greencat.pre_project.application.dto.todo.TodoCreateRequestDto;
import com.greencat.pre_project.application.dto.todo.TodoResponseWithSubtask;
import com.greencat.pre_project.application.dto.todo.TodoStatusUpdateRequest;
import com.greencat.pre_project.application.dto.todo.TodoUpdateRequestDto;
import com.greencat.pre_project.domain.entity.Todo;
import com.greencat.pre_project.domain.entity.Users;
import com.greencat.pre_project.domain.enums.TodoPriority;
import com.greencat.pre_project.domain.enums.TodoStatus;
import com.greencat.pre_project.domain.enums.UserRole;
import com.greencat.pre_project.infrastructure.repository.TodoRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

@ExtendWith(MockitoExtension.class)
class TodoServiceUnitTest {

  @Mock
  TodoRepository todoRepository;
  @Mock
  UserService userService;
  @Mock
  CacheManager cacheManager;
  @Mock
  Cache todoListCache;
  @Mock
  Cache todoCache;

  @InjectMocks
  TodoService todoService;

  @Test
  void createTodo() {
    // given
    TodoCreateRequestDto req = new TodoCreateRequestDto("todo test", "desc", TodoStatus.TODO,
        TodoPriority.HIGH, LocalDateTime.now());
    UUID userId = UUID.randomUUID();
    Users user = new Users(userId, "admin", "1234", "123@naver.com", UserRole.ADMIN, null);

    Todo todo = Todo.create(req, user);
    // 저장 후 id가 생성된 상태를 가정

    when(userService.findUserByUsername("admin")).thenReturn(user);
    when(todoRepository.save(any(Todo.class))).thenReturn(todo);
    when(cacheManager.getCache("todoListCache")).thenReturn(todoListCache);
    when(cacheManager.getCache("todoCache")).thenReturn(todoCache);

    // when
    TodoResponseWithSubtask result = todoService.createTodo(req);

    // then
    verify(todoRepository, times(1)).save(any(Todo.class));
    verify(todoListCache, times(1)).evict(eq("admin"));
    verify(todoCache, times(1)).put(eq(todo.getId()), any(TodoResponseWithSubtask.class));

    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(todo.getId());
    assertThat(result.title()).isEqualTo("todo test");
  }

  @Test
  void updateTodo() {
    UUID todoId = UUID.randomUUID();
    Users user = new Users(UUID.randomUUID(), "admin", "1234", "123@naver.com", UserRole.ADMIN, null);

    Todo existing = new Todo();
    // 아래에서 getId()가 todoId를 반환하도록 스텁
    when(userService.findUserByUsername("admin")).thenReturn(user);
    when(todoRepository.findById(todoId)).thenReturn(Optional.of(existing));
    when(cacheManager.getCache("todoListCache")).thenReturn(todoListCache);
    when(cacheManager.getCache("todoCache")).thenReturn(todoCache);
    // existing.getId()가 null이면 put 키가 null이 되므로, 안전하게 스파이/스텁

    TodoUpdateRequestDto req = new TodoUpdateRequestDto(
        "updated title", "updated desc",  TodoPriority.MEDIUM, LocalDateTime.now()
    );

    // when
    TodoResponseWithSubtask result = todoService.updateTodo(req, todoId);

    // then
    verify(todoRepository, times(1)).findById(todoId);
    verify(todoListCache, times(1)).evict("admin");
    verify(todoCache, times(1)).put(any(), any(TodoResponseWithSubtask.class));

    assertThat(result).isNotNull();
    assertThat(result.title()).isEqualTo("updated title");
    assertThat(result.description()).isEqualTo("updated desc");
  }

  @Test
  void deleteTodo() {
  }

  @Test
  void getMyTodos() {
  }

  @Test
  void getOneTodo() {
  }

  @Test
  void updateTodoStatus() {
    String username = "admin";
    UUID userId = UUID.randomUUID();
    Users user = new Users(userId, username, "1234", "123@naver.com", UserRole.ADMIN, null);

    UUID todoId = UUID.randomUUID();
    Todo todo = new Todo(todoId, "title", "desc", TodoStatus.TODO, TodoPriority.HIGH, user, null, LocalDateTime.now());

    when(userService.findUserByUsername(username)).thenReturn(user);
    when(todoRepository.findByIdAndUserId(todoId, userId)).thenReturn(Optional.of(todo));
    when(cacheManager.getCache("todoListCache")).thenReturn(todoListCache);
    when(cacheManager.getCache("todoCache")).thenReturn(todoCache);

    TodoStatusUpdateRequest req = new TodoStatusUpdateRequest(TodoStatus.DONE);

    // when
    TodoResponseWithSubtask result = todoService.updateTodoStatus(todoId, req, username);

    // then
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(todoId);
    assertThat(result.status()).isEqualTo(TodoStatus.DONE);

    verify(userService).findUserByUsername(username);
    verify(todoRepository).findByIdAndUserId(todoId, userId);
    verify(todoListCache).evict(username);
    verify(todoCache).put(eq(todoId), any(TodoResponseWithSubtask.class));
  }
}