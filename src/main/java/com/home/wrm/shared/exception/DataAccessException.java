package com.home.wrm.shared.exception;

/**
 * 
 */
public class DataAccessException extends WrmException {
    private static final long serialVersionUID = 8841423700733431406L;

    protected DataAccessException() {
    }
    
    public DataAccessException(final long errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public DataAccessException(String message, Throwable t) {
        super(message, t);
    }

    public DataAccessException(Throwable t) {
        super(t);
    }

    public DataAccessException(String message) {
        super(message);
    }
}