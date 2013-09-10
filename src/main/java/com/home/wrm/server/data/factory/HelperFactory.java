package com.home.wrm.server.data.factory;

import com.home.wrm.server.data.helper.ResourceHelper;
import com.home.wrm.server.data.helper.DirectoryHelper;

public class HelperFactory {
    private ResourceHelper resourceHelper;
    private DirectoryHelper directoryHelper;
    private DaoFactory daoFactory;
    
    public HelperFactory(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    public DaoFactory getDaoFactory() {
        return daoFactory;
    }
    
    public ResourceHelper getResourceHelper() {
        if (resourceHelper == null) {
            resourceHelper = new ResourceHelper(this);
        }
        return resourceHelper;
    }
    
    public DirectoryHelper getDirectoryHelper() {
        if (directoryHelper == null) {
            directoryHelper = new DirectoryHelper(this);
        }
        return directoryHelper;
    }
}
