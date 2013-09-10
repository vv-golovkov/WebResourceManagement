package com.home.wrm.shared.exception;

/**
 * 
 */
public class AuthenticationException extends WrmException {
    private static final long serialVersionUID = 8940452504709950245L;

    protected AuthenticationException() {}

    public AuthenticationException(String message, Throwable t) {
        super(message, t);
    }

    public AuthenticationException(Throwable t) {
        super(t);
    }

    public AuthenticationException(String message) {
        super(message);
    }
    
    public AuthenticationException(final long errorCode, final String message) {
        super(errorCode, message);
    }
}
