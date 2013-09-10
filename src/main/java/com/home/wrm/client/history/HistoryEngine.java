package com.home.wrm.client.history;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.home.wrm.client.util.Page;
import com.home.wrm.client.view.WrProcessingView;
import com.home.wrm.client.view.main.AbstractHeaderSupportView;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;

/**
 * History handler class.
 */
public class HistoryEngine implements ValueChangeHandler<String> {
    private Page previousPage = Page.WR_PROCESSING_PAGE;

    public void onValueChange(ValueChangeEvent<String> event) {
        final String historyToken = event.getValue();
        /* if page is't specified */
        if (historyToken.length() == 0) {
            redirectTo(Page.WR_PROCESSING_PAGE);
            return;
        }
        
        /* if page is specified */
        if (Page.WR_PROCESSING_PAGE.getToken().equals(historyToken)) {
            markPageAsPrevious(Page.WR_PROCESSING_PAGE);
            activateView(new WrProcessingView());
            
//        } else if (Page.WR_SELECTION_PAGE.getToken().equals(historyToken)) {
//            markPageAsPrevious(Page.WR_SELECTION_PAGE);
//            activateView(new WrSelectionView());
        } else {
            /* if specified page does not exists */
            SC.warn("Specifed page not found: " + historyToken, new BooleanCallback() {
                public void execute(Boolean value) {
                    redirectTo(previousPage);
                }
            });
        }
    }
    
    /**
     * 
     * @param view
     */
    private void activateView(final AbstractHeaderSupportView<?> view) {
        view.load();
    }
    
    /**
     * 
     * @param page
     */
    private void markPageAsPrevious(Page page) {
        previousPage = page;
    }

    /**
     * Redirect to specified page.
     * 
     * @param page
     *            - specified page.
     */
    private void redirectTo(Page page) {
        History.newItem(page.getToken());
    }
}
