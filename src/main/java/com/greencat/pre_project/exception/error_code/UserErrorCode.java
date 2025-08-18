package com.greencat.pre_project.exception.error_code;

import com.greencat.pre_project.exception.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode {
  NOT_FOUND(HttpStatus.NOT_FOUND, "User_001", "유저가 존재하지 않습니다"),
  DUPLICATE_USER(HttpStatus.CONFLICT, "User_002", "중복된 username이 존재합니다"),
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