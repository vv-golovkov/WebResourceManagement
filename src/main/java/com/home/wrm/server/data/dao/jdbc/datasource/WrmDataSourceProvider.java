package com.home.wrm.server.data.dao.jdbc.datasource;

import javax.sql.DataSource;

import org.springframework.core.env.AbstractEnvironment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.ericsson.springsupport.jdbc.datasource.DataSourceAware;

public class WrmDataSourceProvider implements DataSourceAware {
    private static final String DB_DRIVER_CLASS_NAME = "db.driverClassName";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_USERNAME = "db.username";
    private static final String DB_URL = "db.url";
    
    private AbstractEnvironment environment;
    
    public WrmDataSourceProvider(AbstractEnvironment environment) {
        this.environment = environment;
    }

    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(environment.getProperty(DB_URL));
        dataSource.setDriverClassName(environment.getProperty(DB_DRIVER_CLASS_NAME));
        dataSource.setUsername(environment.getProperty(DB_USERNAME));
        dataSource.setPassword(environment.getProperty(DB_PASSWORD));
        return dataSource;
    }
}
