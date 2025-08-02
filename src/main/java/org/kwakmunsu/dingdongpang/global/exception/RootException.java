package org.kwakmunsu.dingdongpang.global.exception;

import lombok.Getter;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;

@Getter
public abstract class RootException extends RuntimeException {

    private final ErrorStatus errorStatus;

    protected RootException(ErrorStatus status) {
        super(status.getMessage());
        this.errorStatus = status;
    }

    protected RootException(ErrorStatus status, Throwable cause) {
        super(status.getMessage(), cause);
        this.errorStatus = status;
    }

}