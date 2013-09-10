package com.home.wrm.server.data.dao.jdbc.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.object.MappingSqlQuery;

import com.home.wrm.shared.transport.DtoBase;

/**
 * 
 * @param <T>
 */
public abstract class AbstractSqlQueryRowMapper<T extends DtoBase> extends MappingSqlQuery<T> implements RowMapper<T> {
    
    /**
     * For SQL queries with parameters.
     */
    public AbstractSqlQueryRowMapper() {
    }

    /**
     * For SQL queries without parameters.
     * 
     * @param dataSource
     *            - datasource.
     * @param sqlQuery
     *            - SQL query with parameters.
     */
    public AbstractSqlQueryRowMapper(DataSource dataSource, String sqlQuery) {
        super(dataSource, sqlQuery);
    }

    public T mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return processRow(resultSet);
    }

    /**
     * 
     * @param resultSet
     * @return
     * @throws SQLException
     */
    protected abstract T processRow(ResultSet resultSet) throws SQLException;
}
