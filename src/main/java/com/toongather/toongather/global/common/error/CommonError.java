package com.toongather.toongather.global.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@AllArgsConstructor
public enum CommonError {

  //auth
  COMMON_AUTH_ERROR(HttpStatus.UNAUTHORIZED, "AUTH_ERROR"),
  JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT_REFRESH NEED"),
  JWT_DENIED(HttpStatus.UNAUTHORIZED, "JWT_DENIED"),
  USER_NOT_PASSWORD(HttpStatus.UNAUTHORIZED, "NOT_PASSWORD"),
  USER_NOT_ACTIVE(HttpStatus.UNAUTHORIZED,  "NOT_ACTIVE_USER"),

  //validation
  VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR"),

  //member
  TEMP_CODE_INVALID(HttpStatus.BAD_REQUEST, "임시번호가 일치하지 않습니다."),
  TEMP_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "임시번호가 만료되었습니다."),

  //webtoon
  WEBTOON_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 웹툰이 존재하지 않습니다.")

  ;
  private final HttpStatus status;
  private final String message;

}
