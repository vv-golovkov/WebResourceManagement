package com.home.wrm.client.util.datasource;

import com.home.wrm.shared.transport.Directory;
import com.smartgwt.client.widgets.tree.TreeNode;

public class DirectoryTreeNode extends TreeNode {
    public static final String DIR_ID_FIELD = "Directory id field";
    public static final String DIR_NAME_FIELD = "Directory name";
    public static final String PAR_DIR_ID_FIELD = "Parent directory id field";
//    private DirectoryDto directory;
    private Directory directory;
    
//    public DirectoryTreeNode(DirectoryDto directory) {
//        this.directory = directory;
//        init();
//    }
//    public DirectoryDto asDirectory() {
//        return directory;
//    }
//    private void init() {
//        setAttribute(DIR_ID_FIELD, directory.getId());
//        setAttribute(DIR_NAME_FIELD, directory.getName());
//        Directory parentFolder = directory.getParentFolder();
//        if (parentFolder != null) {
//            setAttribute(PAR_DIR_ID_FIELD, parentFolder.getId());
//        } else {
//            setAttribute(PAR_DIR_ID_FIELD, 0);
//        }
//    }
    
    public DirectoryTreeNode(Directory directory) {
        this.directory = directory;
        init();
    }
    
    public Directory asDirectory() {
        return directory;
    }
    
    private void init() {
        setAttribute(DIR_ID_FIELD, directory.getId());
        setAttribute(DIR_NAME_FIELD, directory.getName());
        setAttribute(PAR_DIR_ID_FIELD, directory.getParentDirectoryId());
    }
    
    @Override
    public String toString() {
        return directory.getId() + "-" + directory.getName() + "-" + directory.getParentDirectoryId();
    }
}
