package com.home.wrm.server.data.dao.jdbc.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.home.wrm.shared.transport.Directory;
import com.home.wrm.shared.transport.Resource;


/**
 * Container class. All wrapper-classes should be locate here and should extend
 * {@link AbstractSqlQueryRowMapper}.
 */
public final class MapContainer {
    
    public static final class DirectoyMapper extends AbstractSqlQueryRowMapper<Directory> {
        public DirectoyMapper() {
        }
        public DirectoyMapper(DataSource dataSource, String sqlQuery) {
            super(dataSource, sqlQuery);
        }
        @Override
        protected Directory processRow(ResultSet rs) throws SQLException {
            Directory directory = new Directory();
            directory.setId(rs.getInt(1));
            directory.setName(rs.getString(2));
            directory.setFullPath(rs.getString(3));
            directory.setParentDirectoryId(rs.getInt(4));
            return directory;
        }
    }
    
    public static class ResourceMapper extends AbstractSqlQueryRowMapper<Resource> {
        public ResourceMapper() {
        }
        public ResourceMapper(DataSource dataSource, String sqlQuery) {
            super(dataSource, sqlQuery);
        }
        @Override
        protected Resource processRow(ResultSet rs) throws SQLException {
            Resource resource = new Resource();
            resource.setId(rs.getInt(1));
            resource.setName(rs.getString(2));
            resource.setLink(rs.getString(3));
            resource.setSavingDate(rs.getString(4));
            resource.setSavingTime(rs.getString(5));
            resource.setDirectoryId(rs.getInt(6));
//            resource.setFullDirPath(rs.getString(7));
            return resource;
        }
    }
    
    public static final class ResourceMapper2 extends ResourceMapper {
        public ResourceMapper2() {
        }
        public ResourceMapper2(DataSource dataSource, String sqlQuery) {
            super(dataSource, sqlQuery);
        }
        @Override
        protected Resource processRow(ResultSet rs) throws SQLException {
            Resource resource = super.processRow(rs);
            resource.setFullDirPath(rs.getString(7));
            return resource;
        }
    }
}
