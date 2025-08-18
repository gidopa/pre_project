package com.greencat.pre_project.exception.exception;

import lombok.Getter;

@Getter
public class PreTaskException extends RuntimeException {
    private final ErrorCode errorCode;
    public PreTaskException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
