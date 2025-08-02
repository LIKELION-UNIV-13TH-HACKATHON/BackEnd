package org.kwakmunsu.dingdongpang.global.exception;

import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;

public class UnAuthenticationException extends RootException {

    public UnAuthenticationException(ErrorStatus status) {
        super(status);
    }

    public UnAuthenticationException(ErrorStatus status, Throwable cause) {
        super(status, cause);
    }

}