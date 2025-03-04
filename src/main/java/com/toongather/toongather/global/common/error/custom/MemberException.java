package com.toongather.toongather.global.common.error.custom;

import com.toongather.toongather.global.common.error.CommonError;
import com.toongather.toongather.global.common.error.CommonRuntimeException;

public class MemberException extends CommonRuntimeException {

    public MemberException(CommonError error) {
        super(error);
    }

    public static class TempCodeInvalidException extends MemberException {
        public TempCodeInvalidException() {
            super(CommonError.TEMP_CODE_INVALID);
        }
    }

    public static class TempCodeExpiredException extends MemberException {
        public TempCodeExpiredException() {
            super(CommonError.TEMP_CODE_EXPIRED);
        }
    }
}
