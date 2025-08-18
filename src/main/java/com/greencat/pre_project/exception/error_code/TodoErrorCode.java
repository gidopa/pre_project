package com.greencat.pre_project.exception.error_code;

import com.greencat.pre_project.exception.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum TodoErrorCode implements ErrorCode {
  NOT_FOUND(HttpStatus.NOT_FOUND, "TODO_001", "할 일이 존재하지 않습니다"),
  ;

  private final HttpStatus status;
  private final String code;
  private final String message;

  @Override
  public HttpStatus getStatus() {
    return status;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }
}