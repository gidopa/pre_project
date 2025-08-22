package com.greencat.pre_project.infrastructure.alert;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class SlackNotifier implements Notifier {

  private final WebClient webClient = WebClient.builder().build();

  @Value("${alert.slack.url}")
  private String url;


  @Override
  public void notify(String title, String message) {
    Map<String, Object> payload = Map.of("text", "*" + title + "*\n" + message);
    webClient.post()
        .uri(url)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(payload)
        .retrieve()
        .toBodilessEntity()
        .block(); // 데모는 블로킹, 운영은 비동기/재시도 권장

  }
}
