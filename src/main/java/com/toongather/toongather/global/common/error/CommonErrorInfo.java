package com.toongather.toongather.global.common.error;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class CommonErrorInfo {

  private HttpStatus status;
  private String code;
  private String message;

  @Builder
  public CommonErrorInfo(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

}
