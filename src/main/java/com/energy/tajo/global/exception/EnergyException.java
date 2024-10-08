package com.energy.tajo.global.exception;

import lombok.Getter;

@Getter
public class EnergyException extends RuntimeException {

    private final ErrorCode errorCode;

    public EnergyException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public int getHttpStatus() {
        return errorCode.getHttpStatus().value();
    }
}