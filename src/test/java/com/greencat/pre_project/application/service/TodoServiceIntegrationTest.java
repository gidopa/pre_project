package com.greencat.pre_project.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.greencat.pre_project.application.dto.todo.TodoCreateRequestDto;
import com.greencat.pre_project.application.dto.todo.TodoResponseWithSubtask;
import com.greencat.pre_project.application.dto.todo.TodoStatusUpdateRequest;
import com.greencat.pre_project.application.dto.todo.TodoUpdateRequestDto;
import com.greencat.pre_project.application.dto.user.UserCreateRequest;
import com.greencat.pre_project.application.dto.user.UserResponseDto;
import com.greencat.pre_project.domain.entity.Todo;
import com.greencat.pre_project.domain.entity.Users;
import com.greencat.pre_project.domain.enums.TodoPriority;
import com.greencat.pre_project.domain.enums.TodoStatus;
import com.greencat.pre_project.infrastructure.repository.TodoRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

@SpringBootTest
class TodoServiceIntegrationTest {

  @Autowired
  TodoService todoService;
  @Autowired
  CacheManager cacheManager;
  @Autowired
  UserService userService;
  @Autowired
  TodoRepository todoRepository;

  @Test
  void createTodo() {
    // given
    UUID userId = UUID.randomUUID();
    TodoCreateRequestDto request = new TodoCreateRequestDto("todo test", "desc", TodoStatus.TODO, TodoPriority.HIGH, LocalDateTime.now());
    cacheManager.getCache("todoListCache").put("admin", "SEED");
    // when
    TodoResponseWithSubtask result = todoService.createTodo(request);
    Todo todo = todoRepository.findById(result.id()).get();
    // then 캐시 evict / put 확인
    assertThat(cacheManager.getCache("todoListCache").get("admin")).isNull();
    assertThat(cacheManager.getCache("todoCache")
        .get(result.id(), TodoResponseWithSubtask.class)).isNotNull();
    // 결과 검증
    assertThat(result.id()).isEqualTo(todo.getId());
    assertThat(result.title()).isEqualTo(todo.getTitle());
  }

  @Test
  void updateTodo() {
    // given: admin 유저 목
    UserResponseDto userResponse = userService.creatUser(
        new UserCreateRequest("admin1", "1234", "1234@naver.com"));
    String username = userResponse.userName();
    Users user = userService.findUserByUsername(username);
    // given
    TodoCreateRequestDto createReq = new TodoCreateRequestDto(
        "todo test", "desc", TodoStatus.TODO, TodoPriority.HIGH, LocalDateTime.now()
    );
    Todo created = todoRepository.save(Todo.create(createReq, user));
    UUID todoId = created.getId();

    // 캐시에 사전 값 세팅 → update 후 변화 확인 목적
    Cache listCache = cacheManager.getCache("todoListCache");
    if (listCache == null) listCache.put("admin1", "SEED_BEFORE_UPDATE");

    // when: 업데이트 호출
    TodoUpdateRequestDto updateReq = new TodoUpdateRequestDto(
        "updated title", "updated desc",  TodoPriority.MEDIUM, LocalDateTime.now()
    );
    TodoResponseWithSubtask response = todoService.updateTodo(updateReq, todoId);

    // then: 목록 캐시 evict 확인
    assertThat(cacheManager.getCache("todoListCache").get("admin1")).isNull();

    // 단건 캐시 put 확인
    assertThat(cacheManager.getCache("todoCache").get(todoId)).isNotNull();
    // 결과 확인
    assertThat(response).isNotNull();
    assertThat(response.id()).isEqualTo(todoId);
    assertThat(response.title()).isEqualTo(updateReq.getTitle());

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
    UserResponseDto userResponse = userService.creatUser(
        new UserCreateRequest("admin1", "1234", "1234@naver.com"));
    String username = userResponse.userName();
    Users user = userService.findUserByUsername(username);

    // DB에 Todo 하나 저장(실제 리포지토리)
    TodoCreateRequestDto createReq = new TodoCreateRequestDto(
        "todo test", "desc", TodoStatus.TODO, TodoPriority.HIGH, LocalDateTime.now()
    );
    Todo saved = todoRepository.save(Todo.create(createReq, user));
    UUID todoId = saved.getId();

    // when
    TodoStatusUpdateRequest req = new TodoStatusUpdateRequest(TodoStatus.DONE);
    TodoResponseWithSubtask response = todoService.updateTodoStatus(todoId, req, username);

    // then: 응답/DB 상태
    assertThat(response).isNotNull();
    assertThat(response.id()).isEqualTo(todoId);
    assertThat(response.status()).isEqualTo(TodoStatus.DONE);

    // then: 캐시 확인
    assertThat(cacheManager.getCache("todoListCache").get(username)).isNull();
    assertThat(cacheManager.getCache("todoCache").get(todoId)).isNotNull();
    assertThat(cacheManager.getCache("todoCache").get(todoId, TodoResponseWithSubtask.class).status()).isEqualTo(TodoStatus.DONE);
  }
}