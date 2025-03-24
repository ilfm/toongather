package com.toongather.toongather.global.common.error.custom;

import com.toongather.toongather.global.common.error.CommonError;
import com.toongather.toongather.global.common.error.CommonRuntimeException;

public class WebtoonException extends CommonRuntimeException {

    public WebtoonException(CommonError error) {
        super(error);
    }

    public static class WebtoonNotFoundException extends WebtoonException {
        public WebtoonNotFoundException() {
            super(CommonError.WEBTOON_NOT_FOUND);
        }
    }
}
