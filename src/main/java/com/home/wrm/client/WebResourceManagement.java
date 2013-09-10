package com.home.wrm.client;

import com.google.gwt.core.client.EntryPoint;
import com.home.wrm.client.history.HistoryManager;

public class WebResourceManagement implements EntryPoint {

	public void onModuleLoad() {
		HistoryManager history = new HistoryManager();
        history.apply();
	}
}
