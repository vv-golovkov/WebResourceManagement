package com.home.wrm.server.data.dao.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ericsson.springsupport.jdbc.SpringJdbcSupport;
import com.ericsson.springsupport.jdbc.datasource.DataSourceAware;
import com.ericsson.springsupport.jdbc.param.SqlParamsBuilder;
import com.home.wrm.server.logging.SmartLogger;
import com.home.wrm.shared.exception.DataAccessException;
import com.home.wrm.shared.transport.Resource;

public class JdbcDao extends SpringJdbcSupport {
    private static final SmartLogger logger = SmartLogger.getLogger(JdbcDao.class);
    
    public JdbcDao(DataSourceAware customDataSource) {
        super(customDataSource);
        createTablesIfNotExist();
    }

    private void createTablesIfNotExist() {
        executeCreate(Query.CREATE_DIRECTORY_TABLE);
        executeCreate(Query.CREATE_RESOURCE_TABLE);
        executeCreate(Query.CREATE_ARCHIVED_RESOURCE_TABLE);
    }
    
    private List<Integer> extractIds(List<Resource> resources) {
        List<Integer> listOfInteger = new ArrayList<Integer>(resources.size());
        for (Resource resource : resources) {
            listOfInteger.add(resource.getId());
        }
        return listOfInteger;
    }
    
    public List<Resource> removeResource(List<Resource> resources) throws DataAccessException {
        if (!resources.isEmpty()) {
            saveResourceToArchive(resources);
            
            List<Integer> ids = extractIds(resources);
            SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("resources", ids);
            logger.debugf("Deleting web resources with ids %s...", ids);
            executeDelete(Query.REMOVE_RESOURCE, parameters);
            logger.debug("Deleted - ok.");
        }
        return new ArrayList<Resource>();
    }
    
    private void saveResourceToArchive(List<Resource> resources) throws DataAccessException {
        for (Resource resource : resources) {
            String name = resource.getName();
            String link = resource.getLink();
            String dirPath = getDirPathForResource(resource.getId());
            String savingDate = resource.getSavingDate();
            Date deletingDate = new Date(System.currentTimeMillis());
            
            SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("name", name).addPair("link", link).
                    addPair("dirPath", dirPath).addPair("savingDate", savingDate).addPair("deletingDate", deletingDate);
            
            logger.debugf("Saving resource into archive [%s]...", resource.getName());
            executeInsert(Query.INSERT_ARCHIVED_WEB_RESOURCE, parameters);
            logger.debug("Inserted - ok.");
        }
    }
    
    private String getDirPathForResource(long resourceId) throws DataAccessException {
        SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("resourceId", resourceId);
        return queryForObject(Query.GET_DIR_PATH_FOR_RESOURCE, parameters, String.class);
    }
}
