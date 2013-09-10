package com.home.wrm.server.data.helper;

import com.home.wrm.server.data.factory.DaoFactory;
import com.home.wrm.server.data.factory.HelperFactory;

public class CommonHelper {
    private HelperFactory helperFactory;
    
    public CommonHelper(HelperFactory helperFactory) {
        this.helperFactory = helperFactory;
    }
    
    public HelperFactory getHelperFactory() {
        return helperFactory;
    }
    
    public DaoFactory getDaoFactory() {
        return helperFactory.getDaoFactory();
    }
}
