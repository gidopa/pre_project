package com.greencat.pre_project.application.service;

import java.time.Duration;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OneDayBeforeDueAlert {

  private final SlackService slackService;

  @Scheduled(cron = "0 */5 * * * *") // 매 5분
  public void run() {
    slackService.sendOneDayBeforeDueAlerts(ZoneId.of("Asia/Seoul"), Duration.ofMinutes(3));
  }
}
