package com.home.wrm.client.util;

/**
 * Application pages enumeration. All pages, except pop up views (that extend
 * {@link AbstractPopupWindowSupportView}), should be here.
 * 
 */
public enum Page {
    
    WR_PROCESSING_PAGE("main");
//    WR_SELECTION_PAGE("selection");
    
    private String historyToken;

    private Page(String historyToken) {
        this.historyToken = historyToken;
    }

    /**
     * Get history token for page.
     * 
     * @return history token.
     */
    public String getToken() {
        return historyToken;
    }
}
