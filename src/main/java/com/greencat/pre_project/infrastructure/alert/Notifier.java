package com.greencat.pre_project.infrastructure.alert;

// 추후 SMTP 연동 후 email 알림 발송 / Slack Bot을 이용한 dm 등 확장을 위해 인터페이스로 처리
public interface Notifier {
  void notify(String title, String message);

}
