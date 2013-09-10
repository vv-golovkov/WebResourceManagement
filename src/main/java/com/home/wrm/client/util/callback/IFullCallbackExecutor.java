package com.home.wrm.client.util.callback;

/**
 * Callback executor. It supports all methods:
 * {@link IFullCallbackExecutor#onFailureCallbackAction(Throwable)} and
 * {@link IFullCallbackExecutor#onSuccessCallbackAction(Object)}.
 * 
 * @param <T>
 *            - The type of the return value that was declared in the
 *            synchronous version of the method.
 */
public interface IFullCallbackExecutor<T> extends ISuccessCallbackExecutor<T> {
    
    /**
     * Called when an asynchronous call fails to complete normally.
     * 
     * @param caught
     *            - failure encountered while executing a remote procedure call.
     */
    void onFailureCallbackAction(Throwable caught);
}
