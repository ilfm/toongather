package com.toongather.toongather.global.common.error;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonErrorInfo {

  private String path;
  private String code;
  private String message;

  @Builder
  public CommonErrorInfo(String code, String path, String message) {
    this.code = code;
    this.message = message;
    this.path = path;
  }

}
