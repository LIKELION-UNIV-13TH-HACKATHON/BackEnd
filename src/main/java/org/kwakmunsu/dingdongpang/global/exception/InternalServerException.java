package org.kwakmunsu.dingdongpang.global.exception;

import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;

public class InternalServerException extends RootException {

    public InternalServerException(ErrorStatus status) {
        super(status);
    }

    public InternalServerException(ErrorStatus status, Throwable cause) {
        super(status, cause);
    }

}