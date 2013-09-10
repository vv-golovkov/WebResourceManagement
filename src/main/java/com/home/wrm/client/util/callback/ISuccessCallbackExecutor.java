package com.home.wrm.client.util.callback;

/**
 * Callback executor. It supports only success scenario:
 * {@link ISuccessCallbackExecutor#onSuccessCallbackAction(Object)}.
 * 
 * @param <T>
 *            - The type of the return value that was declared in the
 *            synchronous version of the method.
 */
public interface ISuccessCallbackExecutor<T> {
    
    /**
     * Called when an asynchronous call completes successfully.
     * 
     * @param result
     *            - the return value of the remote produced call.
     */
    void onSuccessCallbackAction(T result);
}
