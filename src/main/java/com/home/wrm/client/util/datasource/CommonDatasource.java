package com.home.wrm.client.util.datasource;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;

/**
 * Common class for data sources.
 */
public class CommonDatasource extends DataSource {
    protected static final String WIDTH = "width";
    
    public CommonDatasource() {
        setClientOnly(true);
    }
    
    /**
     * Get primary key field.
     * 
     * @param fieldName
     *            - name of primary key field.
     * @return data source field with primary key.
     */
    protected DataSourceIntegerField getPrimaryKeyDatasourceField(final String fieldName) {
        DataSourceIntegerField primaryKeyField = new DataSourceIntegerField(fieldName);
        primaryKeyField.setPrimaryKey(true);
        primaryKeyField.setHidden(true);
        return primaryKeyField;
    }
}
