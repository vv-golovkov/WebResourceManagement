package com.home.wrm.client.history;

import com.google.gwt.user.client.History;

/**
 * Manages {@link History} extensions.
 * 
 */
public class HistoryManager {

    public void apply() {
        applyAndFireHistoryState();
    }

    /**
     * Add history listener and fire history state. All pages, except Pop up
     * windows, should be considered. Pages should control application access.
     */
    private void applyAndFireHistoryState() {
        History.addValueChangeHandler(new HistoryEngine());
        History.fireCurrentHistoryState();
    }
}
