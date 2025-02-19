package com.toongather.toongather.global.common.advice;

import com.toongather.toongather.global.common.error.CommonErrorInfo;
import com.toongather.toongather.global.common.error.CommonRuntimeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ApiExceptionAdvice {

  @ExceptionHandler({CommonRuntimeException.class})
  public ResponseEntity<CommonErrorInfo> exceptionHandler(
      HttpServletRequest request,
      final CommonRuntimeException e) {
    return ResponseEntity
        .status(e.getError().getStatus())
        .body(CommonErrorInfo.builder()
            .path(request.getRequestURI())
            .code(e.getError().getCode())
            .message(e.getError().getMessage())
            .build());
  }

}
