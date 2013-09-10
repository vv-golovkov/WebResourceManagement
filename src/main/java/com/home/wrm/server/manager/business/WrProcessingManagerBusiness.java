package com.home.wrm.server.manager.business;

import java.util.Date;
import java.util.List;

import com.home.wrm.shared.exception.WrmException;
import com.home.wrm.shared.transport.Directory;
import com.home.wrm.shared.transport.Resource;
import com.home.wrm.shared.transport.SearchFilter;

public interface WrProcessingManagerBusiness {
    public List<Directory> getResourceDirectories() throws WrmException;
    public List<Directory> createNewFolder(Directory directory) throws WrmException;
    public Resource saveWebResource(Resource webResource) throws WrmException;
    public List<Resource> getResourcesFor(int dirId) throws WrmException;
    public void removeDirectory(Directory directory) throws WrmException;
    public void updateResourceName(int resId, String newName) throws WrmException;
    public List<Resource> removeResource(Resource resource) throws WrmException;
    public List<Resource> searchResource(String wildSearchingValue, SearchFilter searchFilter) throws WrmException;
    public List<Directory> renameDirectoryTo(int dirId, String newName) throws WrmException;
    public List<Directory> listTargetDirectories(int dirIdOfResource) throws WrmException;
    public void updateResourceTargetDir(int resId, int dirId) throws WrmException;
    public List<Directory> listAccessableDirs(int dirId) throws WrmException;
    public List<Directory> moveFolderTo(int dirId, int targetDirId) throws WrmException;
    
    public void doRecurrentDatabaseCleanup(Date date)throws WrmException;
}
