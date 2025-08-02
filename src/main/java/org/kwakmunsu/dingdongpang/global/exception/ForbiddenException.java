package org.kwakmunsu.dingdongpang.global.exception;


import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;

public class ForbiddenException extends RootException {

    public ForbiddenException(ErrorStatus status) {
        super(status);
    }

    public ForbiddenException(ErrorStatus status, Throwable cause) {
        super(status, cause);
    }

}