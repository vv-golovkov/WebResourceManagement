package com.home.wrm.server.data.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ericsson.springsupport.jdbc.datasource.DataSourceAware;
import com.ericsson.springsupport.jdbc.param.SqlParamsBuilder;
import com.home.wrm.server.data.dao.jdbc.JdbcDao;
import com.home.wrm.server.data.dao.jdbc.Query;
import com.home.wrm.server.data.dao.jdbc.mapping.MapContainer;
import com.home.wrm.server.logging.SmartLogger;
import com.home.wrm.shared.exception.DataAccessException;
import com.home.wrm.shared.exception.ExceptionHelper;
import com.home.wrm.shared.exception.ExceptionHelper.ErrorCode;
import com.home.wrm.shared.exception.WrmException;
import com.home.wrm.shared.transport.Resource;
import com.home.wrm.shared.transport.SearchFilter;

public class ResourceDao extends JdbcDao {
    private static final SmartLogger logger = SmartLogger.getLogger(ResourceDao.class);
    
    public ResourceDao(DataSourceAware dataSourceAware) {
        super(dataSourceAware);
    }

    private String getSavingDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    private String getSavingTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
    
    public Resource saveWebResource(Resource resource) throws WrmException {
        Resource resourceByLink = getResourceIdByLink(resource.getLink());
        if (resourceByLink != null) {
            logger.errorf("Web resource with such link [%s] already exists.", resource.getLink());
            throw ExceptionHelper.newWrmException(ErrorCode.RESOURCE_ALREADY_EXISTS, "<b>" + resource.getLink() + "</b>");
        }
        
        resource.setSavingDate(getSavingDate());
        resource.setSavingTime(getSavingTime());
        
        SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("resName", resource.getName()).addPair("resLink", resource.getLink()).
            addPair("savDate", resource.getSavingDate()).addPair("savTime", resource.getSavingTime()).addPair("dirId", resource.getDirectoryId());
        
        logger.debugf("Inserting new web resource [%s]...", resource.getName());
        executeInsert(Query.INSERT_NEW_WEB_RESOURCE, parameters);
        logger.debug("Inserted - ok.");
        
        return getResourceIdByLink(resource.getLink());
    }
    
    public List<Resource> getResourcesFor(int dirId) throws DataAccessException {
        List<Integer> dirs = new ArrayList<Integer>();
        dirs.add(dirId);
        SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("dirs", dirs);
        logger.debugf("Looking for resources for dir [%d]...", dirId);
        List<Resource> resource = queryForWrappedList(Query.GET_RESOURCES_FOR_SPEC_DIR, parameters, new MapContainer.ResourceMapper());
        logger.debugf("Found [%d] web resources.", resource.size());
        return resource;
    }
    
    private Resource getResourceIdByLink(String link) throws DataAccessException {
        SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("link", link);
        logger.debugf("Looking for resource by link[%s]...", link);
        Resource res = queryForWrappedObject(Query.GET_RESOURCE_BY_LINK, parameters, new MapContainer.ResourceMapper());
        logger.debugf("Found resources: %s", res);
        return res;
    }
    
    public void updateResourceName(int resId, String newName) throws DataAccessException {
        SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("resId", resId).addPair("newName", newName);
        logger.debugf("Set new name [%s] to resource [%d]...", newName, resId);
        executeInsert(Query.UPDATE_RESOURCE_NAME, parameters);
        logger.debug("Updated - ok.");
    }
    
    public void updateResourceTargetDir(int resId, int dirId) throws DataAccessException {
        SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("resId", resId).addPair("newDir", dirId);
        logger.debugf("Set new target dir [%d] for resource [%d]...", dirId, resId);
        executeInsert(Query.UPDATE_RESOURCE_TARGET_DIR, parameters);
        logger.debug("Updated - ok.");
    }
    
    public List<Resource> searchResources(String wildResourceValue, SearchFilter searchFilter) throws DataAccessException {
        switch (searchFilter) {
        case ONLY_BY_NAME: return searchResourcesOnlyByName(wildResourceValue);
        case ONLY_BY_LINK: return searchResourcesOnlyByLink(wildResourceValue);
        case BY_BOTH_NAME_AND_LINK: return searchResourcesByBothNameAndLink(wildResourceValue);
        default:
            throw ExceptionHelper.newDataAccessException("Unexpected search filter: " + searchFilter.toString());
        }
    }
    
    private List<Resource> doSearch(String query, String wildResourceValue) throws DataAccessException {
        String composedQuery = StringUtils.replace(query, ":wildValue:", wildResourceValue);
        logger.debugf("Searching filtering resources [%s]...", wildResourceValue);
        List<Resource> resources = queryForWrappedList(composedQuery, new MapContainer.ResourceMapper());
        logger.debugf("Found [%d] filtered web resources.", resources.size());
        return resources;
    }
    
    private List<Resource> searchResourcesByBothNameAndLink(String wildResourceValue) throws DataAccessException {
        return doSearch(Query.FIND_RESOURCES_BY_BOTH_NAME_AND_LINK_QUERY, wildResourceValue);
    }
    
    private List<Resource> searchResourcesOnlyByName(String wildResourceValue) throws DataAccessException {
        return doSearch(Query.FIND_RESOURCES_ONLY_BY_NAME_QUERY, wildResourceValue);
    }
    
    private List<Resource> searchResourcesOnlyByLink(String wildResourceValue) throws DataAccessException {
        return doSearch(Query.FIND_RESOURCES_ONLY_BY_LINK_QUERY, wildResourceValue);
    }
    
    public void doRecurrentDatabaseCleanup(Date date) throws DataAccessException {
        String qq = "delete from archived_resource where deleting_date <= :expiredDate";
        SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("expiredDate", date);
        logger.debug("DATABASE CLEANING UP...");
        executeDelete(qq, parameters);
        logger.debug("DATABASE CLEANING UP DONE.");
    }
}
