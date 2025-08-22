package com.greencat.pre_project.application.service;

import com.greencat.pre_project.domain.entity.Todo;
import com.greencat.pre_project.infrastructure.alert.Notifier;
import com.greencat.pre_project.infrastructure.repository.TodoRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SlackService {

  private final TodoRepository todoRepository;
  private final Notifier notifier;

  @Transactional
  public int sendOneDayBeforeDueAlerts(ZoneId zone, Duration windowHalf) {
    LocalDateTime now = LocalDateTime.now(zone);
    LocalDateTime target = now.plusHours(24);
    LocalDateTime from = target.minus(windowHalf);
    LocalDateTime to   = target.plus(windowHalf);

    List<Todo> targets = todoRepository.findByDueTime(from, to);
    int count = 0;
    for (Todo t : targets) {
      // 아이템 단위로 격리하여 실패 전파 차단
      try {
        sendOneItem(t);
        count++;
      } catch (Exception e) {
      }
    }
    return count;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  protected void sendOneItem(Todo todo) {

    String title = "⏰ TODO 마감 24시간 전 알림";
    String body = String.format(
        "- 제목: %s%n- 마감: %s%n- 우선순위: %s%n- 남은 시간: 약 24시간",
        todo.getTitle(), todo.getDueTime(), todo.getPriority()
    );

    notifier.notify(title, body);
  }

}
