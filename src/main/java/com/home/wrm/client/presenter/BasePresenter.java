package com.home.wrm.client.presenter;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.home.wrm.client.service.WrProcessingServiceAsync;
import com.home.wrm.client.util.adapter.Adaptable;
import com.home.wrm.client.util.callback.BaseAsyncCallback;
import com.home.wrm.client.util.callback.IFullCallbackExecutor;
import com.home.wrm.client.util.callback.ISuccessCallbackExecutor;
import com.smartgwt.client.util.SC;

/**
 * Common class for all presenters.
 * 
 * @param <A>
 *            - {@link Adaptable} type.
 */
public abstract class BasePresenter<A extends Adaptable> {
	final WrProcessingServiceAsync rpc = WrProcessingServiceAsync.Util.getInstance();
    private A adapter;
    
    public abstract void addPageActions();

    public BasePresenter(A adapter) {
        this.adapter = adapter;
    }
    
    /**
     * Create success callback for RPC call. It supports only
     * {@link AsyncCallback#onSuccess(Object)} method.
     * 
     * @param callbackExecutor
     *            - success callback executor.
     * @return {@link BaseAsyncCallback} instance with required method.
     */
    protected <T> BaseAsyncCallback<T> newSuccessCallback(ISuccessCallbackExecutor<T> callbackExecutor) {
        return new BaseAsyncCallback<T>(callbackExecutor);
    }

    /**
     * Create full callback for RPC call. It supports all methods of
     * {@link AsyncCallback}: {@link AsyncCallback#onSuccess(Object)} and
     * {@link AsyncCallback#onFailure(Throwable)}.
     * 
     * @param callbackExecutor
     *            - full callback executor.
     * @return {@link BaseAsyncCallback} instance with required methods.
     */
    protected <T> BaseAsyncCallback<T> newFullCallback(IFullCallbackExecutor<T> callbackExecutor) {
        return new BaseAsyncCallback<T>(callbackExecutor);
    }

    /**
     * Checks, if invoking operation is first - shows loading dialog, otherwise
     * event counter will be incremented.
     * 
     * @param loadingMessage
     */
    protected void checkExecutionBefore(String loadingMessage) {
        BaseAsyncCallback.checkExecutionBefore(loadingMessage);
    }

    /**
     * Show a modal prompt to the user. This method will display the message
     * using the Dialog.Prompt singleton object.
     * 
     * @param message
     *            - message to display.
     */
    protected void showModalPromptToUser(String message) {
        SC.showPrompt(message);
    }

    /**
     * Show a modal dialog with warning icon.
     * 
     * @param warningMessage
     *            warning message.
     */
    protected void showWarning(String warningMessage) {
        SC.warn(warningMessage);
    }

    /**
     * Show a modal dialog with a message, icon, and "OK" button.
     * 
     * @param infoMessage
     *            - information message.
     */
    protected void showInformation(String infoMessage) {
        SC.say(infoMessage);
    }

    /**
     * Show dialog with error message.
     * 
     * @param caughtError
     *            - caught exception.
     */
    protected void showError(Throwable caughtError) {
        SC.warn(caughtError.getMessage());
    }

    /**
     * Get adapter. Adapter is required for access to view class from
     * appropriate presenter.
     * 
     * @return adapter.
     */
    protected A getAdapter() {
        return adapter;
    }

    /**
     * Opens a new browser window.
     * 
     * @param link - the URL that the new window will display.
     */
    public void openInNewTab(String link) {
        Window.open(link, "", "");
    }
}
