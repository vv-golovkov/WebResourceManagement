package com.home.wrm.client.view.main;

import com.home.wrm.client.presenter.BasePresenter;

public abstract class AbstractViewProvider<T extends BasePresenter<?>> {
    private T presenter = null;
    
    /**
     * Set presenter for view.
     * 
     * @param presenter
     *            - specified presenter.
     */
    protected void setPresenter(final T presenter) {
        this.presenter = presenter;
    }

    /**
     * Get presenter.
     * 
     * @return appropriate presenter.
     */
    protected T getPresenter() {
        return presenter;
    }

    /**
     * Template method (typical scenario) to load any page (view).
     * <ul>
     * <li>Assign appropriate presenter:
     * {@link AbstractViewProvider#assignPresenter()}.</li>
     * <li>Build UI: {@link AbstractViewProvider#buildUI(GuiFactory)}.</li>
     * <li>Process UI events: {@link AbstractViewProvider#uiEventsProcessing()}.
     * </li>
     * <li>Draw UI: {@link AbstractViewProvider#drawUI()}.</li>
     * </ul>
     */
    public final void load() {
        assignPresenter();
        buildUI();
        presenter.addPageActions();
        drawUI();
    }

    protected abstract void assignPresenter();
    
//    protected abstract void uiEventsProcessing();

    protected abstract void buildUI();

    protected abstract void drawUI();
}