package com.home.wrm.client.view.main;

import com.home.wrm.client.presenter.BasePresenter;
import com.smartgwt.client.widgets.Window;

/**
 * Common class for Pop up windows (views).
 * 
 * @param <T>
 *            - appropriate presenter type.
 */
public abstract class AbstractPopupWindowSupportView<T extends BasePresenter<?>> extends AbstractViewProvider<T> {
    private static final String defaultTitle = "Moving...";
    private static final String defaultWidth = "50%";
    private static final String defaultHeight = "40%";
    protected Window window;

    public AbstractPopupWindowSupportView() {
        createPopupWindow(defaultTitle, defaultWidth, defaultHeight);
    }

    /**
     * Create Pop up window.
     * 
     * @param title
     *            - window title.
     * @param width
     *            - window width.
     * @param height
     *            - window height.
     */
    protected void createPopupWindow(String title, String width, String height) {
        createWindowWithParams(title, width, height);
    }

    /**
     * Create Pop up window with default parameters, like:
     * <ul>
     * <li><code>setShowModalMask(true);</code></li>
     * <li><code>setCanDragReposition(false);</code></li>
     * <li><code>setIsModal(true);</code></li>
     * <li><code>centerInPage();</code></li>
     * </ul>
     * 
     * @param title
     *            - window title.
     * @param width
     *            - window width.
     * @param height
     *            - window height.
     * @return created Pop up window.
     */
    private void createWindowWithParams(String title, String width, String height) {
        window = new Window();
        /* At pleasure */
        window.setShowModalMask(true);
        window.setTitle(title);
        window.setSize(width, height);
        window.setCanDragReposition(true);
        window.setIsModal(true);
        window.centerInPage();
    }

    /**
     * Show pop up widow.
     */
    protected void showPopupWindow() {
        window.show();
    }

    /**
     * Close pop up widow.
     */
    protected void closePopupWindow() {
        window.destroy();
        // XXX: may be used window.hide();
    }
}
