package com.home.wrm.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.home.wrm.shared.exception.WrmServiceException;
import com.home.wrm.shared.transport.Directory;
import com.home.wrm.shared.transport.Resource;
import com.home.wrm.shared.transport.SearchFilter;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("springGwtServices/WrProcessingService.rpc")
public interface WrProcessingService extends RemoteService {
    public List<Directory> getResourceDirectories() throws WrmServiceException;
    public Resource saveWebResource(Resource webResource) throws WrmServiceException;
    public List<Directory> createNewFolder(Directory directory) throws WrmServiceException;
    public List<Resource> getResourcesFor(int dirId) throws WrmServiceException;
    public void removeDirectory(Directory directory) throws WrmServiceException;
    public void updateResourceName(int resId, String newName) throws WrmServiceException;
    public List<Resource> removeResource(Resource resource) throws WrmServiceException;
    public List<Resource> searchResource(String wildSearchingValue, SearchFilter searchFilter) throws WrmServiceException;
    public List<Directory> renameDirectoryTo(int dirId, String newName) throws WrmServiceException;
    public List<Directory> listTargetDirectories(int dirIdOfResource) throws WrmServiceException;
    public void updateResourceTargetDir(int resId, int dirId) throws WrmServiceException;
    public List<Directory> listAccessableDirs(int dirId) throws WrmServiceException;
    public List<Directory> moveFolderTo(int dirId, int targetDirId) throws WrmServiceException;
}
