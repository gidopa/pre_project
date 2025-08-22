package com.greencat.pre_project.presentation.controller;

import com.greencat.pre_project.application.service.SlackService;
import io.swagger.v3.oas.annotations.Operation;
import java.time.Duration;
import java.time.ZoneId;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alert")
public class AlertController {
  private final SlackService slackService;

  // slack 연동 테스트용 controller 기본적으로 scheduler를 통해 자동 알림 발송
  @Operation(summary = "알림 기능 강제 실행")
  @PostMapping("/run")
  public Map<String, Object> runOnce(
      @RequestParam(defaultValue = "Asia/Seoul") String zone,
      @RequestParam(defaultValue = "3") int windowMinutes
  ) {
    int sent = slackService.sendOneDayBeforeDueAlerts(ZoneId.of(zone), Duration.ofMinutes(windowMinutes));
    return Map.of("sent", sent, "windowMinutes", windowMinutes, "zone", zone);
  }
}
