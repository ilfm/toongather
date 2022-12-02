package com.toongather.toongather.global.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@AllArgsConstructor
public enum CommonError {

  JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "E0001", "JWT_REFRESH NEED" ),
  JWT_DENIED(HttpStatus.UNAUTHORIZED, "E0002", "JWT_DENIED");

  private final HttpStatus status;
  private final String code;
  private final String message;

}
