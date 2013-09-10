package com.home.wrm.server.manager.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.home.wrm.server.manager.business.WrProcessingManagerBusiness;
import com.home.wrm.server.manager.interceptor.BaseManager;
import com.home.wrm.shared.exception.WrmException;
import com.home.wrm.shared.transport.Directory;
import com.home.wrm.shared.transport.Resource;
import com.home.wrm.shared.transport.SearchFilter;

public class WrProcessingManagerBean extends BaseManager implements WrProcessingManagerBusiness {
    
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<Directory> getResourceDirectories() throws WrmException {
        return getDaoFactory().getDirectoryDao().getResourceDirectories();
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = WrmException.class, timeout = 300)
    public List<Directory> createNewFolder(Directory directory) throws WrmException {
        return getDaoFactory().getDirectoryDao().createNewFolder(directory);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = WrmException.class, timeout = 300)
    public Resource saveWebResource(Resource webResource) throws WrmException {
        return getDaoFactory().getResourceDao().saveWebResource(webResource);
    }
    
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<Resource> getResourcesFor(int dirId) throws WrmException {
        return getDaoFactory().getResourceDao().getResourcesFor(dirId);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = WrmException.class, timeout = 300)
    public void removeDirectory(Directory directory) throws WrmException {
        getDaoFactory().getDirectoryDao().removeDirectory(directory);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = WrmException.class, timeout = 300)
    public void updateResourceName(int resId, String newName) throws WrmException {
        getDaoFactory().getResourceDao().updateResourceName(resId, newName);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = WrmException.class, timeout = 300)
    public List<Resource> removeResource(Resource resource) throws WrmException {
        List<Resource> resources = new ArrayList<Resource>();
        resources.add(resource);
        return getDaoFactory().getResourceDao().removeResource(resources);
    }
    
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<Resource> searchResource(String wildSearchingValue, SearchFilter searchFilter) throws WrmException {
        return getDaoFactory().getResourceDao().searchResources(wildSearchingValue, searchFilter);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = WrmException.class, timeout = 300)
    public List<Directory> renameDirectoryTo(int dirId, String newName) throws WrmException {
        getDaoFactory().getDirectoryDao().updateDirectoryName(dirId, newName);
        return getResourceDirectories();
    }
    
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<Directory> listTargetDirectories(int dirIdOfResource) throws WrmException {
        return getDaoFactory().getDirectoryDao().listTargetDirectories(dirIdOfResource);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = WrmException.class, timeout = 300)
    public void updateResourceTargetDir(int resId, int dirId) throws WrmException {
        getDaoFactory().getResourceDao().updateResourceTargetDir(resId, dirId);
    }
    
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<Directory> listAccessableDirs(int dirId) throws WrmException {
        return getDaoFactory().getDirectoryDao().listAccessableDirs(dirId);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = WrmException.class, timeout = 300)
    public List<Directory> moveFolderTo(int dirId, int targetDirId) throws WrmException {
        getDaoFactory().getDirectoryDao().moveFolderTo(dirId, targetDirId);
        return getResourceDirectories();
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = WrmException.class, timeout = 300)
    public void doRecurrentDatabaseCleanup(Date date) throws WrmException {
        getDaoFactory().getResourceDao().doRecurrentDatabaseCleanup(date);
    }
}
