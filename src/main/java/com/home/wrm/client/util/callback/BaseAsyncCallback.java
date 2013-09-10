package com.home.wrm.client.util.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;

/**
 * See {@link AsyncCallback} class.
 * 
 * @param <T>
 *            - The type of the return value that was declared in the
 *            synchronous version of the method.
 */
public class BaseAsyncCallback<T> implements AsyncCallback<T> {
    private ISuccessCallbackExecutor<T> successCallbackExecutor;
    private IFullCallbackExecutor<T> fullCallbackExecutor;
    private static int eventCounter = 0;

    /**
     * Create asynchronous callback with only success scenario.
     * 
     * @param callbackExecutor
     *            - success callback executor.
     * @param statusbarManager
     *            - status bar manager.
     */
    public BaseAsyncCallback(ISuccessCallbackExecutor<T> callbackExecutor) {
        this.successCallbackExecutor = callbackExecutor;
    }

    /**
     * Create asynchronous callback with all scenarios supported by
     * {@link AsyncCallback}.
     * 
     * @param callbackExecutor
     *            - full callback executor.
     * @param statusbarManager
     *            - status bar manager.
     */
    public BaseAsyncCallback(IFullCallbackExecutor<T> callbackExecutor) {
        this.fullCallbackExecutor = callbackExecutor;
    }

    public void onFailure(Throwable caught) {
        checkExecutionAfter();
        if (fullCallbackExecutor != null) {
            fullCallbackExecutor.onFailureCallbackAction(caught);
        } else {
            /* when usual exception is occurred */
            SC.warn(caught.getMessage());
        }
    }

    public void onSuccess(T result) {
        checkExecutionAfter();
        if (successCallbackExecutor != null) {
            successCallbackExecutor.onSuccessCallbackAction(result);
        } else {
            fullCallbackExecutor.onSuccessCallbackAction(result);
        }
    }
    
    /**
     * Checks, if invoking operation is first - shows loading dialog, otherwise
     * event counter will be incremented.
     * 
     * @param loadingMessage
     */
    public static void checkExecutionBefore(String loadingMessage) {
        if (eventCounter == 0) SC.showPrompt(loadingMessage);
        eventCounter++;
    }

    /**
     * Checks if some async operation is performing. If invoking operation is
     * last, modal loading dialog will be closed, otherwise event counter will
     * be decremented.
     */
    private void checkExecutionAfter() {
        eventCounter--;
        if (eventCounter <= 0) {
            SC.clearPrompt();
            eventCounter = 0;
        }
    }
}
