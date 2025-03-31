package com.toongather.toongather.global.common.error.custom;

import com.toongather.toongather.global.common.error.CommonError;
import com.toongather.toongather.global.common.error.CommonRuntimeException;

public class ReviewException extends CommonRuntimeException {

  public ReviewException(CommonError error) {
    super(error);
  }

  public static class ReviewNotFoundException extends ReviewException {

    public ReviewNotFoundException() {
      super(CommonError.REVIEW_NOT_FOUND);
    }
  }

  public static class RecordNotFoundException extends ReviewException {

    public RecordNotFoundException() {
      super(CommonError.RECORD_NOT_FOUND);
    }
  }
}
