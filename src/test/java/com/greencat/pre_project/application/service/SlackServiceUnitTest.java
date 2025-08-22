package com.greencat.pre_project.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.greencat.pre_project.domain.entity.Todo;
import com.greencat.pre_project.domain.entity.Users;
import com.greencat.pre_project.domain.enums.TodoPriority;
import com.greencat.pre_project.domain.enums.TodoStatus;
import com.greencat.pre_project.domain.enums.UserRole;
import com.greencat.pre_project.infrastructure.alert.Notifier;
import com.greencat.pre_project.infrastructure.repository.TodoRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SlackServiceUnitTest {

  @Mock
  TodoRepository todoRepository;

  @Mock
  Notifier notifier;

  @InjectMocks
  SlackService service;

  private static final ZoneId KST = ZoneId.of("Asia/Seoul");

  private Todo newTodo(String title, LocalDateTime due) {
    UUID userId = UUID.randomUUID();
    Users user = new Users(userId, "admin", "1234", "123@naver.com", UserRole.ADMIN, null);
    Todo t = new Todo(UUID.randomUUID(), title, "desc", TodoStatus.TODO, TodoPriority.HIGH, user, null, due);
    return t;
  }

  @Test
  void 윈도우내_두건이면_두번발송() {
    LocalDateTime now = LocalDateTime.now(KST);
    LocalDateTime target = now.plusHours(24);
    Duration half = Duration.ofMinutes(3);

    Todo t1 = newTodo("A", target.plusMinutes(1));
    Todo t2 = newTodo("B", target.plusMinutes(1));

    when(todoRepository.findByDueTime(any(), any()))
        .thenReturn(List.of(t1, t2));

    int sent = service.sendOneDayBeforeDueAlerts(KST, half);

    assertThat(sent).isEqualTo(2);
    verify(notifier).notify(startsWith("⏰"), contains("A")); // 첫 호출에 A 포함
    verify(notifier, atLeastOnce()).notify(anyString(), contains("B"));
  }

  @Test
  void 윈도우밖이면_0건() {
    when(todoRepository.findByDueTime(any(), any()))
        .thenReturn(List.of()); // 빈 목록

    int sent = service.sendOneDayBeforeDueAlerts(KST, Duration.ofMinutes(3));

    assertThat(sent).isEqualTo(0);
    verify(notifier, never()).notify(anyString(), anyString());
  }


}