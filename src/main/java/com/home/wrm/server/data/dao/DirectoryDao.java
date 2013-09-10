package com.home.wrm.server.data.dao;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.ericsson.springsupport.jdbc.datasource.DataSourceAware;
import com.ericsson.springsupport.jdbc.param.SqlParamsBuilder;
import com.home.wrm.server.data.dao.jdbc.JdbcDao;
import com.home.wrm.server.data.dao.jdbc.Query;
import com.home.wrm.server.data.dao.jdbc.mapping.MapContainer;
import com.home.wrm.server.logging.SmartLogger;
import com.home.wrm.shared.exception.DataAccessException;
import com.home.wrm.shared.exception.ExceptionHelper;
import com.home.wrm.shared.exception.WrmException;
import com.home.wrm.shared.transport.Directory;
import com.home.wrm.shared.transport.Resource;

public class DirectoryDao extends JdbcDao {
    private static final SmartLogger logger = SmartLogger.getLogger(DirectoryDao.class);
    private static final String SLASH = "/";
    
    public DirectoryDao(DataSourceAware dataSourceAware) {
        super(dataSourceAware);
    }
    
    public List<Directory> getResourceDirectories() throws DataAccessException {
        logger.debug("Looking for all directories...");
        List<Directory> list = queryForWrappedList(Query.GET_ALL_DIRECTORIES, new MapContainer.DirectoyMapper());
        logger.debugf("Found [%d] web directories.", list.size());
        return list;
    }
    
    public List<Directory> createNewFolder(Directory directory) throws DataAccessException {
        int parentDirectoryId = directory.getParentDirectoryId();
        String name = directory.getName();
        String rootPath = ">> " + name;
        
        SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("dirName", name).
            addPair("fullPath", (parentDirectoryId == 0) ? rootPath : (findDirectoryById(parentDirectoryId).getFullPath() + "/" + name)).
            addPair("parDirId", (parentDirectoryId == 0) ? null : parentDirectoryId);
        
        logger.debugf("Inserting new dir [%s] to parent dir [%d]...", name, parentDirectoryId);
        executeInsert(Query.INSERT_NEW_DIRECTORY, parameters);
        logger.debug("Inserted - ok.");
        
        return getResourceDirectories();
    }
    
    private Directory findDirectoryById(int dirId) throws DataAccessException {
        logger.debugf("Looking for directory by id[%d]...", dirId);
        SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("dirId", dirId);
        Directory dir = queryForWrappedObject(Query.GET_DIRECTORY_BY_ID, parameters, new MapContainer.DirectoyMapper());
        logger.debugf("Found dir: %s", dir);
        return dir;
    }
    
    public void removeDirectory(Directory directory) throws WrmException {
        int topDirId = directory.getId();
        Set<Integer> dirs = new LinkedHashSet<Integer>();
        dirs.add(topDirId);
        Set<Integer> childDirs = new LinkedHashSet<Integer>(dirs);
        do {
            childDirs = getChildDirsFor(childDirs);
            if (!childDirs.isEmpty()) {
                dirs.addAll(childDirs);
            }
        } while (!childDirs.isEmpty());
        
        List<Resource> resources = getResources(dirs);
        removeResource(resources);
        
        SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("dirs", dirs);
        logger.debugf("Deleting folders %s...", dirs);
        executeDelete(Query.REMOVE_DIR, parameters);
        logger.debug("Deleted - ok.");
    }
    
    public List<Resource> getResources(Set<Integer> dirs) throws DataAccessException {
        logger.debug("Looking for all resources...");
        SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("dirs", dirs);
        List<Resource> list = queryForWrappedList(Query.GET_RESOURCES_FOR_SELECTION, parameters, new MapContainer.ResourceMapper2());
        logger.debugf("Found [%d] web resources.", list.size());
        return list;
    }
    
    private Set<Integer> getChildDirsFor(Set<Integer> dirs) throws DataAccessException {
        SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("dirs", dirs);
        List<String> listOfDirs = queryForList(Query.GET_CHILD_DIRS_FOR, parameters, String.class);
        return transform(listOfDirs);
    }
    
    private Set<Integer> transform(List<String> listOfStrings) {
        Set<Integer> listOfIntegers = new LinkedHashSet<Integer>(listOfStrings.size());
        for (String string : listOfStrings) {
            listOfIntegers.add(Integer.valueOf(string));
        }
        return listOfIntegers;
    }
    
    public void updateDirectoryName(int dirId, String newTopDirName) throws DataAccessException {
        Directory mainDir = findDirectoryById(dirId);
        String oldTopDirName = mainDir.getName();
        String fullPath = mainDir.getFullPath();
        String newFullPath = StringUtils.EMPTY;
        if (StringUtils.contains(fullPath, SLASH)) {
            newFullPath = StringUtils.substringBeforeLast(fullPath, SLASH) + SLASH + newTopDirName;
        } else {
            newFullPath = StringUtils.replace(fullPath, oldTopDirName, newTopDirName);
        }
        
        String qq = "update directory set name = :newName, full_path = :newfullPath where id = :dirId";
        SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("dirId", dirId).
                addPair("newName", newTopDirName).addPair("newfullPath", newFullPath);
        executeUpdate(qq, parameters);
        updateChildsOf(dirId, oldTopDirName, newTopDirName);
    }
    
    private void updateChildsOf(int dirId, String oldTopDirName, String newTopDirName) throws DataAccessException {
        List<Directory> childDirs = getChildDirsOf(dirId);
        for (Directory child : childDirs) {
            if (StringUtils.equals(oldTopDirName, child.getName())) {
                throw ExceptionHelper.newDataAccessException("Parent and child directories have the same names.");
            }
            String fullPath = child.getFullPath();
            String beforeSubFullPath = StringUtils.substringBeforeLast(fullPath, oldTopDirName);
            String afterSubFullPath = StringUtils.substringAfter(fullPath, (beforeSubFullPath + oldTopDirName));
            String newChildFullPath = beforeSubFullPath + newTopDirName + afterSubFullPath;
            
            String qq = "update directory set full_path = :newfullPath where id = :dirId";
            
            SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("dirId", child.getId()).addPair("newfullPath", newChildFullPath);
            executeUpdate(qq, parameters);
            updateChildsOf(child.getId(), oldTopDirName, newTopDirName);
        }
    }
    
    private List<Directory> getChildDirsOf(int dirId) throws DataAccessException {
        String qq = "select * from directory d where d.par_dir_id = :dirId";
        SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("dirId", dirId);
        return queryForWrappedList(qq, parameters, new MapContainer.DirectoyMapper());
    }
    
    /**
     * List dirs to move resources.
     * @param dirIdOfResource
     * @return
     * @throws DataAccessException
     */
    public List<Directory> listTargetDirectories(int dirIdOfResource) throws DataAccessException {
        SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("dirId", dirIdOfResource);
        logger.debugf("Looking for target directories by [%d]...", dirIdOfResource);
        List<Directory> list = queryForWrappedList(Query.LIST_TARGET_DIRECTORIES_FOR_MOVING_RESOURCE, parameters, new MapContainer.DirectoyMapper());
        logger.debugf("Found [%d] target directories.", list.size());
        return list;
    }
    
    /**
     * List dirs to move directory.
     * @param dirIdOfResource
     * @return
     * @throws DataAccessException
     */
    public List<Directory> listAccessableDirs(int dirId) throws DataAccessException {
        Directory directory = findDirectoryById(dirId);
        int parDirId = directory.getParentDirectoryId();
        if (parDirId == 0) {
            String qq = "select * from directory d where d.par_dir_id is null and d.id <> :dirId";
            SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("dirId", dirId);
            return queryForWrappedList(qq, parameters, new MapContainer.DirectoyMapper());
        }
        Set<Integer> dirs = new LinkedHashSet<Integer>();
        dirs.add(dirId);
        
        Set<Integer> childDirs = new LinkedHashSet<Integer>(dirs);
        do {
            childDirs = getChildDirsFor(childDirs);
            if (!childDirs.isEmpty()) {
                dirs.addAll(childDirs);
            }
        } while (!childDirs.isEmpty());
        dirs.add(parDirId);
        
        String qq = "select * from directory d where d.id not in (:dirs) order by d.full_path";
        SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("dirs", dirs);
        List<Directory> list = queryForWrappedList(qq, parameters, new MapContainer.DirectoyMapper());
        
        Directory root = new Directory();
        root.setId(0);
        root.setFullPath("ROOT");
        list.add(0, root);
        return list;
    }
    
    public void moveFolderTo(int dirId, int targetDirId) throws DataAccessException {
        String qq = "update directory set par_dir_id = :targetDirId where id = :dirId";
        
        SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("dirId", dirId).addPair("targetDirId", (targetDirId == 0) ? null : targetDirId);
        executeUpdate(qq, parameters);
        
        //get all root dirs
        String qq2 = "select * from directory d where d.par_dir_id is null";
        List<Directory> rootDirs = queryForWrappedList(qq2, new MapContainer.DirectoyMapper());
        for (Directory rootDir : rootDirs) {
            updatePathsRecursively(rootDir.getId(), rootDir.getFullPath());
        }
    }
    
    private void updatePathsRecursively(int dirId, String dirPath) throws DataAccessException {
        List<Directory> childs = getChildDirsOf(dirId);
        for (Directory child : childs) {
            String currentChildName = child.getFullPath();
            String expectedChildName = dirPath + SLASH + child.getName();
            if (!StringUtils.equals(currentChildName, expectedChildName)) {
                //update child full_path
                String qq = "update directory set full_path = :newfullPath where id = :dirId";
                SqlParamsBuilder parameters = getSqlParamsBuilder().addPair("dirId", child.getId()).addPair("newfullPath", expectedChildName);
                executeUpdate(qq, parameters);
            }
            updatePathsRecursively(child.getId(), expectedChildName);
        }
    }
}
