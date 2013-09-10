package com.home.wrm.shared.exception;



/**
 * Exception low level logic.
 * 
 */
public final class ExceptionHelper {

    /**
     * The list of error codes.
     * 
     */
    public enum ErrorCode {
        RESOURCE_ALREADY_EXISTS("Web Resource already exists: "),
        CONNECTION_FAILED("Connection failed"),
        LOGIN_FAILED("Login failed");

        private static final long BASE = 1000000000000000L;

        private String errorMessage;

        private ErrorCode(final String message) {
            errorMessage = message;
        }

        /**
         * Get the code of error.
         * 
         * @return the code of error.
         */
        public long code() {
            return BASE + ordinal() + 1;
        }

        /**
         * Get the message of error.
         * 
         * @return the message of error.
         */
        public String message() {
            return errorMessage;
        }
    }

    private ExceptionHelper() {
    }
    
    /********************************** Factory methods ****************************/
    
    /**
     * 
     * @param exception
     * @return
     */
    public static WrmServiceException newWrmServiceException(WrmException exception) {
        String message = (exception.getCause() != null) ? exception.getCause().getMessage() : exception.getMessage();
        return new WrmServiceException(exception.getErrorCode(), message);
    }
    
    /**
     * 
     * @param runtimeEx
     * @throws WrmServiceException 
     */
    public static WrmServiceException newRuntimeException(RuntimeException runtimeEx) {
        if (runtimeEx instanceof NullPointerException) {
            return new WrmServiceException("Null Pointer Exception has occured. See logs for more details.");
        }
        String message = (runtimeEx.getCause() != null) ? runtimeEx.getCause().getMessage() : runtimeEx.getMessage();
        return new WrmServiceException(message);
    }
    
    /********************************** Factory methods ****************************/
    
    public static WrmException newWrmException(final ErrorCode errorCode) {
        return new WrmException(errorCode.code(), errorCode.message());
    }
    
    public static WrmException newWrmException(final String errorMessage) {
        return new WrmException(errorMessage);
    }
    
    public static WrmException newWrmExceptionFormatted(final ErrorCode errorCode, final Object... args) {
        return new WrmException(errorCode.code(), String.format(errorCode.message(), args));
    }
    
    public static WrmException newWrmException(final Throwable error) {
        String errorMessage = (error.getCause() != null) ? error.getCause().getMessage() : error.getMessage();
        return new WrmException(errorMessage);
    }
    
    public static WrmException newWrmException(final ErrorCode errorCode, final Object message) {
        return new WrmException(errorCode.code(), errorCode.message() + " " + message);
    }
    
    public static DataAccessException newDataAccessException(final Throwable error) {
        String errorMessage = (error.getCause() != null) ? error.getCause().getMessage() : error.getMessage();
        return new DataAccessException(errorMessage);
    }
    
    public static DataAccessException newDataAccessException(final String errorMessage) {
        return new DataAccessException(errorMessage);
    }
    
    public static DataAccessException newDataAccessException(final String errorMessage, final Throwable error) {
        return new DataAccessException(errorMessage, error);
    }
    
    public static AuthenticationException newAuthenticationException(final ErrorCode errorCode) {
        return new AuthenticationException(errorCode.code(), errorCode.message());
    }
}
