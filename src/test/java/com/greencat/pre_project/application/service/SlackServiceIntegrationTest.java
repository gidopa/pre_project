package com.greencat.pre_project.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.greencat.pre_project.domain.entity.Todo;
import com.greencat.pre_project.domain.entity.Users;
import com.greencat.pre_project.domain.enums.TodoPriority;
import com.greencat.pre_project.domain.enums.TodoStatus;
import com.greencat.pre_project.domain.enums.UserRole;
import com.greencat.pre_project.infrastructure.repository.TodoRepository;
import com.greencat.pre_project.infrastructure.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase // H2
class SlackServiceIntegrationTest {
  static MockWebServer server;

  @BeforeAll
  static void beforeAll() throws Exception {
    server = new MockWebServer();
    server.start();
  }

  @AfterAll
  static void afterAll() throws Exception {
    server.shutdown();
  }

  @DynamicPropertySource
  static void props(DynamicPropertyRegistry r) {
    r.add("alert.slack.webhook-url", () -> server.url("/webhook/it").toString());
  }

  @Autowired
  SlackService slackService;
  @Autowired
  TodoRepository todoRepository;
  @Autowired
  UserRepository usersRepository;

  private static final ZoneId KST = ZoneId.of("Asia/Seoul");

  private Todo newTodo(String title, LocalDateTime due, Users user) {
    return new Todo(
        null,
        title,
        "desc",
        TodoStatus.TODO,
        TodoPriority.HIGH,
        user,
        null,
        due
    );
  }

  // Slack 연동 테스트 까지 확인.
  @Test
  @Transactional
  void 윈도우내_두건_발송되고_Slack에_두번_POST된다() throws Exception {
    // given
    Users u = new Users(null,"admin1", "1234", "admin@test.com", UserRole.ADMIN, null);
    Users user = usersRepository.save(u);
    LocalDateTime now = LocalDateTime.now(KST);
    LocalDateTime target = now.plusHours(24);

    Todo t1 = newTodo("test_success_A", target, user);
    Todo t2 = newTodo("test_success_B", target.plusMinutes(1), user);
    Todo t3 = newTodo("test_failed_C", target.plusMinutes(10), user); // 윈도우 밖(예: half=3분일 때)

    todoRepository.saveAll(List.of(t1, t2, t3));

    // Mock Slack 응답: 2건만 실제 윈도우에 걸릴 거라 2개면 충분
    server.enqueue(new MockResponse().setResponseCode(200));
    server.enqueue(new MockResponse().setResponseCode(200));

    // when
    int sent = slackService.sendOneDayBeforeDueAlerts(KST, Duration.ofMinutes(3));

    // then
    assertThat(sent).isEqualTo(2);

    // Slack 요청 2건을 꺼내 내용 확인
    var req1 = server.takeRequest();
    var req2 = server.takeRequest();

    String body1 = req1.getBody().readString(StandardCharsets.UTF_8);
    String body2 = req2.getBody().readString(StandardCharsets.UTF_8);

    assertThat(body1).contains("\"text\"");
    assertThat(body2).contains("\"text\"");
    // 두 요청 중 하나는 A, 다른 하나는 B를 포함해야 함
    assertThat(body1 + body2).contains("A");
    assertThat(body1 + body2).contains("B");
    // OUT은 포함되면 안 됨
    assertThat(body1).doesNotContain("test_failed_C");
    assertThat(body2).doesNotContain("test_failed_C");
  }
}
