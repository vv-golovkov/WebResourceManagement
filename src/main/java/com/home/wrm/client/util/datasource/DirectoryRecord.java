package com.home.wrm.client.util.datasource;

import com.home.wrm.shared.transport.Directory;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DirectoryRecord extends ListGridRecord {
    public static final String ID_FIELD = "idfield";
    public static final String DIR_PATH_FIELD = "dirpathfield";
    private Directory directory;
    
    public Directory asDirectory() {
        return directory;
    }
    
    public DirectoryRecord(Directory directory) {
        this.directory = directory;
        init();
    }
    
    private void init() {
        setAttribute(ID_FIELD, directory.getId());
        setAttribute(DIR_PATH_FIELD, directory.getFullPath());
    }
}
