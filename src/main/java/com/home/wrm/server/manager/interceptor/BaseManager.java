package com.home.wrm.server.manager.interceptor;

import com.home.wrm.server.data.factory.DaoFactory;
import com.home.wrm.server.data.factory.HelperFactory;

public class BaseManager implements HasHelperFactory {
    private HelperFactory helperFactory;
    
    public HelperFactory getHelperFactory() {
        return helperFactory;
    }
    
    public DaoFactory getDaoFactory() {
        return helperFactory.getDaoFactory();
    }
    
    public void setHelperFactory(HelperFactory helperFactory) {
        this.helperFactory = helperFactory;
    }
}
