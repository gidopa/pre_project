package com.greencat.pre_project.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseDto <T>{
  private String status;
  private String message;
  private T data;

  public static <T> ResponseDto <T> success(String meesage) {
    return new ResponseDto<>("success", meesage, null);
  }

  public static <T> ResponseDto<T> success(String message, T data) {
    return new ResponseDto<>("success", message, data);
  }

  public static <T> ResponseDto <T> error(String message) {
    return new ResponseDto<>("error", message, null);
  }
}
