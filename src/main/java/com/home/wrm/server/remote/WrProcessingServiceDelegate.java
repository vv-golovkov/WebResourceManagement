package com.home.wrm.server.remote;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.wrm.client.service.WrProcessingService;
import com.home.wrm.server.logging.SmartLogger;
import com.home.wrm.server.manager.business.WrProcessingManagerBusiness;
import com.home.wrm.shared.exception.ExceptionHelper;
import com.home.wrm.shared.exception.WrmException;
import com.home.wrm.shared.exception.WrmServiceException;
import com.home.wrm.shared.transport.Directory;
import com.home.wrm.shared.transport.Resource;
import com.home.wrm.shared.transport.SearchFilter;

/**
 * The server side implementation of the RPC service.
 */
@Service("WrProcessingService.rpc")
public class WrProcessingServiceDelegate implements WrProcessingService {
    private static final SmartLogger logger = SmartLogger.getLogger(WrProcessingServiceDelegate.class);
    
    @Autowired
    private WrProcessingManagerBusiness businessBean;
    
    public List<Directory> getResourceDirectories() throws WrmServiceException {
        try {
            return businessBean.getResourceDirectories();
        } catch (WrmException e) {
            logger.error(e);
            throw ExceptionHelper.newWrmServiceException(e);
        } catch (RuntimeException e) {
            logger.error(e);
            throw ExceptionHelper.newRuntimeException(e);
        }
    }
    
    public List<Directory> createNewFolder(Directory directory) throws WrmServiceException {
        try {
            return businessBean.createNewFolder(directory);
        } catch (WrmException e) {
            logger.error(e);
            throw ExceptionHelper.newWrmServiceException(e);
        } catch (RuntimeException e) {
            logger.error(e);
            throw ExceptionHelper.newRuntimeException(e);
        }
    }
    
    public Resource saveWebResource(Resource webResource) throws WrmServiceException {
        try {
            return businessBean.saveWebResource(webResource);
        } catch (WrmException e) {
            logger.error(e);
            throw ExceptionHelper.newWrmServiceException(e);
        } catch (RuntimeException e) {
            logger.error(e);
            throw ExceptionHelper.newRuntimeException(e);
        }
    }
    
    public List<Resource> getResourcesFor(int dirId) throws WrmServiceException {
        try {
            return businessBean.getResourcesFor(dirId);
        } catch (WrmException e) {
            logger.error(e);
            throw ExceptionHelper.newWrmServiceException(e);
        } catch (RuntimeException e) {
            logger.error(e);
            throw ExceptionHelper.newRuntimeException(e);
        }
    }
    
    public void removeDirectory(Directory directory) throws WrmServiceException {
        try {
            businessBean.removeDirectory(directory);
        } catch (WrmException e) {
            logger.error(e);
            throw ExceptionHelper.newWrmServiceException(e);
        } catch (RuntimeException e) {
            logger.error(e);
            throw ExceptionHelper.newRuntimeException(e);
        }
    }
    
    public void updateResourceName(int resId, String newName) throws WrmServiceException {
        try {
            businessBean.updateResourceName(resId, newName);
        } catch (WrmException e) {
            logger.error(e);
            throw ExceptionHelper.newWrmServiceException(e);
        } catch (RuntimeException e) {
            logger.error(e);
            throw ExceptionHelper.newRuntimeException(e);
        }
    }
    
    public List<Resource> removeResource(Resource resource) throws WrmServiceException {
        try {
            return businessBean.removeResource(resource);
        } catch (WrmException e) {
            logger.error(e);
            throw ExceptionHelper.newWrmServiceException(e);
        } catch (RuntimeException e) {
            logger.error(e);
            throw ExceptionHelper.newRuntimeException(e);
        }
    }
    
    public List<Resource> searchResource(String wildSearchingValue, SearchFilter searchFilter) throws WrmServiceException {
        try {
            return businessBean.searchResource(wildSearchingValue, searchFilter);
        } catch (WrmException e) {
            logger.error(e);
            throw ExceptionHelper.newWrmServiceException(e);
        } catch (RuntimeException e) {
            logger.error(e);
            throw ExceptionHelper.newRuntimeException(e);
        }
    }
    
    public List<Directory> renameDirectoryTo(int dirId, String newName) throws WrmServiceException {
        try {
            return businessBean.renameDirectoryTo(dirId, newName);
        } catch (WrmException e) {
            logger.error(e);
            throw ExceptionHelper.newWrmServiceException(e);
        } catch (RuntimeException e) {
            logger.error(e);
            throw ExceptionHelper.newRuntimeException(e);
        }
    }
    
    public List<Directory> listTargetDirectories(int dirIdOfResource) throws WrmServiceException {
        try {
            return businessBean.listTargetDirectories(dirIdOfResource);
        } catch (WrmException e) {
            logger.error(e);
            throw ExceptionHelper.newWrmServiceException(e);
        } catch (RuntimeException e) {
            logger.error(e);
            throw ExceptionHelper.newRuntimeException(e);
        }
    }
    
    public void updateResourceTargetDir(int resId, int dirId) throws WrmServiceException {
        try {
            businessBean.updateResourceTargetDir(resId, dirId);
        } catch (WrmException e) {
            logger.error(e);
            throw ExceptionHelper.newWrmServiceException(e);
        } catch (RuntimeException e) {
            logger.error(e);
            throw ExceptionHelper.newRuntimeException(e);
        }
    }
    
    public List<Directory> listAccessableDirs(int dirId) throws WrmServiceException {
        try {
            return businessBean.listAccessableDirs(dirId);
        } catch (WrmException e) {
            logger.error(e);
            throw ExceptionHelper.newWrmServiceException(e);
        } catch (RuntimeException e) {
            logger.error(e);
            throw ExceptionHelper.newRuntimeException(e);
        }
    }
    
    public List<Directory> moveFolderTo(int dirId, int targetDirId) throws WrmServiceException {
        try {
            return businessBean.moveFolderTo(dirId, targetDirId);
        } catch (WrmException e) {
            logger.error(e);
            throw ExceptionHelper.newWrmServiceException(e);
        } catch (RuntimeException e) {
            logger.error(e);
            throw ExceptionHelper.newRuntimeException(e);
        }
    }
}
