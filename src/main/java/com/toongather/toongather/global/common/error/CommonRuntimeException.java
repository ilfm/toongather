package com.toongather.toongather.global.common.error;

public class CommonRuntimeException extends RuntimeException{

  private CommonError error;

  public CommonRuntimeException(CommonError error) {
    super(error.getMessage());

    this.error = error;
  }

  public CommonError getError() {
    return this.error;
  }

}
