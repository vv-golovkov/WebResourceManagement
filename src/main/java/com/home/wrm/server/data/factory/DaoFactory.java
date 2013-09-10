package com.home.wrm.server.data.factory;

import com.ericsson.springsupport.jdbc.datasource.DataSourceAware;
import com.home.wrm.server.data.dao.DirectoryDao;
import com.home.wrm.server.data.dao.ResourceDao;

public class DaoFactory {
    private DirectoryDao directoryDao;
    private ResourceDao resourceDao;
    private DataSourceAware dataSourceAware;
    
    public DaoFactory(DataSourceAware dataSourceAware) {
        this.dataSourceAware = dataSourceAware;
    }
    
    public DirectoryDao getDirectoryDao() {
        if (directoryDao == null) {
            directoryDao = new DirectoryDao(dataSourceAware);
        }
        return directoryDao;
    }
    
    public ResourceDao getResourceDao() {
        if (resourceDao == null) {
            resourceDao = new ResourceDao(dataSourceAware);
        }
        return resourceDao;
    }
}
