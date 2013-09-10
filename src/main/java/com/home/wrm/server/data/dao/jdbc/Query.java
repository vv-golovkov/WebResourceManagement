package com.home.wrm.server.data.dao.jdbc;

public final class Query {
    private Query() {}
    
    /******************************* CREATE *******************************/
    public static final String CREATE_DIRECTORY_TABLE = "create table if not exists directory (id int not null auto_increment primary key, " +
    		"name varchar(255) not null, full_path varchar(255) not null, par_dir_id int (11) default null) " +
    		"character set cp1251 collate cp1251_general_ci";
    public static final String CREATE_RESOURCE_TABLE = "create table if not exists resource (id int not null auto_increment primary key, " +
    		"name varchar(255) not null, link text not null, saving_date varchar(50) not null, saving_time varchar(50) not null, dir_id int (11) default null) " +
    		"character set cp1251 collate cp1251_general_ci";
    public static final String CREATE_ARCHIVED_RESOURCE_TABLE = "create table if not exists archived_resource (id int not null auto_increment primary key, " +
    		"name varchar(255) not null, link text not null, dir_path varchar(255) not null, saving_date varchar(50) not null, deleting_date date not null) " +
    		"character set cp1251 collate cp1251_general_ci";
    
    /******************************* SELECT *******************************/
    //Directory
    public static final String GET_ALL_DIRECTORIES = "select * from directory";
    public static final String GET_DIRECTORY_BY_ID = "select * from directory d where d.id = :dirId";
    public static final String GET_CHILD_DIRS_FOR = "select d.id from directory d where d.par_dir_id in (:dirs)";
    public static final String LIST_TARGET_DIRECTORIES_FOR_MOVING_RESOURCE = "select * from directory d where d.id <> :dirId order by d.full_path";
    //Resource
    public static final String GET_RESOURCE_BY_LINK = "select * from resource r where r.link = :link";
    public static final String GET_RESOURCES_FOR_SPEC_DIR = "select * from resource where dir_id in (:dirs)";
    public static final String GET_RESOURCES_FOR_SELECTION = "select r.*, d.full_path from resource r inner join directory d on d.id = r.dir_id where r.dir_id in (:dirs)";
    public static final String GET_DIR_PATH_FOR_RESOURCE = "select d.full_path from directory d inner join resource r on r.dir_id = d.id where r.id = :resourceId";
//    public static final String FIND_RESOURCES_QUERY = "select * from resource r where upper(r.name) like '%'||:wildValue1||'%' or upper(r.link) like '%'||:wildValue2||'%'";
    public static final String FIND_RESOURCES_BY_BOTH_NAME_AND_LINK_QUERY = "select * from resource r where upper(r.name) like '%:wildValue:%' or upper(r.link) like '%:wildValue:%'";
    public static final String FIND_RESOURCES_ONLY_BY_NAME_QUERY = "select * from resource r where upper(r.name) like '%:wildValue:%'";
    public static final String FIND_RESOURCES_ONLY_BY_LINK_QUERY = "select * from resource r where upper(r.link) like '%:wildValue:%'";
    
    /******************************* INSERT *******************************/
    //Directory
    public static final String INSERT_NEW_DIRECTORY = "insert into directory(name, full_path, par_dir_id) values(:dirName, :fullPath, :parDirId)";
    //Resource
    public static final String INSERT_NEW_WEB_RESOURCE = "insert into resource(name, link, saving_date, saving_time, dir_id) " +
            "values(:resName, :resLink, :savDate, :savTime, :dirId)";
    public static final String INSERT_ARCHIVED_WEB_RESOURCE = "insert into archived_resource(name, link, dir_path, saving_date, deleting_date) " +
            "values(:name, :link, :dirPath, :savingDate, :deletingDate)";
    
    /******************************* UPDATE *******************************/
    //Directory
    public static final String UPDATE_DIRECTORY_NAME = "update directory set name = :newName where id = :dirId";
    public static final String UPDATE_DIRECTORY_FULL_PATH = "update directory set full_path = :newPath where id = :dirId";
    //Resource
    public static final String UPDATE_RESOURCE_NAME = "update resource set name = :newName where id = :resId";
    public static final String UPDATE_RESOURCE_TARGET_DIR = "update resource set dir_id = :newDir where id = :resId";
    /******************************* DELETE *******************************/
    //Directory
    public static final String REMOVE_DIR = "delete from directory where id in (:dirs)";
    //Resource
    public static final String REMOVE_RESOURCE = "delete from resource where id in (:resources)";
    public static final String REMOVE_RESOURCES_FROM_DIRS = "delete from resource where dir_id in (:dirs)";
}
