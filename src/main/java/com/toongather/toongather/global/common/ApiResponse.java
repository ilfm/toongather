package com.toongather.toongather.global.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

  private String path;
  private String code;
  private String message;
  private T data;

  @Builder
  private ApiResponse(String path, String code, String message, T data) {
    this.path = path;
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public static <T> ApiResponse<T> of(HttpStatus status, String path, String message, T data) {
    return ApiResponse.<T>builder()
        .path(path)
        .code(status.toString())
        .message(message)
        .data(data)
        .build();
  }

  public static <T> ApiResponse<T> ok(String path, T data) {
    return of(HttpStatus.OK, path, "success", data);
  }

}
