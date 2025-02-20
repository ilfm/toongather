package com.toongather.toongather.global.common.advice;

import com.toongather.toongather.global.common.error.CommonError;
import com.toongather.toongather.global.common.error.CommonErrorInfo;
import com.toongather.toongather.global.common.error.CommonRuntimeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

  //검증 실패
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CommonErrorInfo> handleMethodArgumentNotValidException(
      HttpServletRequest request,
      MethodArgumentNotValidException e) {
    return ResponseEntity
        .status(CommonError.VALIDATION_ERROR.getStatus())
        .body(CommonErrorInfo.builder()
            .path(request.getRequestURI())
            .code(CommonError.VALIDATION_ERROR.getCode())
            .message(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
            .build());
  }

}
