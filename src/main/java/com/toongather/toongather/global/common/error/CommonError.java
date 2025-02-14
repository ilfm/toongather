package com.toongather.toongather.global.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@AllArgsConstructor
public enum CommonError {

  COMMON_AUTH_ERROR(HttpStatus.UNAUTHORIZED, "E0000", "AUTH_ERROR"),
  JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "E0001", "JWT_REFRESH NEED"),
  JWT_DENIED(HttpStatus.UNAUTHORIZED, "E0002", "JWT_DENIED"),
  USER_NOT_PASSWORD(HttpStatus.UNAUTHORIZED, "E0010", "NOT_PASSWORD"),
  USER_NOT_ACTIVE(HttpStatus.UNAUTHORIZED, "E0011", "NOT_ACTIVE_USER")

  ;
  private final HttpStatus status;
  private final String code;
  private final String message;

}
