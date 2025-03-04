package com.toongather.toongather.global.common.error;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonErrorInfo {

  private String path;
  private String message;

  @Builder
  public CommonErrorInfo(String path, String message) {
    this.path = path;
    this.message = message;
  }

}
