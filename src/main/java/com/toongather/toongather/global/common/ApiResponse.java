package com.toongather.toongather.global.common;

import lombok.Builder;
import lombok.Getter;

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

}
