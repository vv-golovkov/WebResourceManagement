package com.home.wrm.shared.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Application exception.
 */
public class WrmException extends Exception implements IsSerializable {
    private static final long serialVersionUID = 3006271041328618856L;
    protected long errorCode;

    /**
     * Default constructor. Required by GWT RPC.
     */
    protected WrmException() {
    }

    /**
     * Construct {@link WrmException} object.
     * 
     * @param errorCode
     *            - error code.
     * @param message
     *            - current message.
     */
    public WrmException(final long errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public WrmException(final String message) {
        super(message);
    }

    public WrmException(final Throwable e) {
        super(e);
    }

    public WrmException(final String message, final Throwable e) {
        super(message, e);
    }

    public long getErrorCode() {
        return errorCode;
    }
}
