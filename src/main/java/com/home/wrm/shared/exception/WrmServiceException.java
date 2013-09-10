package com.home.wrm.shared.exception;

public class WrmServiceException extends WrmException {
    private static final long serialVersionUID = 402466103248684011L;
    private long errorCode;
    private String message;

    protected WrmServiceException() {
    }

    public WrmServiceException(long errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public WrmServiceException(String message) {
        this.message = message;
    }

    public long getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
