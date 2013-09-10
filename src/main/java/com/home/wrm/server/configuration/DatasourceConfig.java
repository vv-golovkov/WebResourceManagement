package com.home.wrm.server.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.MutablePropertySources;

import com.ericsson.springsupport.jdbc.datasource.DataSourceAware;
import com.home.wrm.server.data.dao.jdbc.datasource.WrmDataSourceProvider;

//@ImportResource(value = "classpath:/connection_cp.properties", reader = PropertiesBeanDefinitionReader.class)
//@PropertySource("file:/WebResourceManagement/src/main/webapp/WEB-INF/spring/connection.properties") -> ???
//@PropertySource("classpath:/com/home/wrm/conn2.properties") -> ok
@Configuration
@PropertySource(name = "connection_properties", value = "classpath:/connection_cp.properties")
public class DatasourceConfig {
    private static final String ENVIRONMENT_PROPERTIES_AUTOWIRING_NAME = "connection_properties";
    
    @Autowired
    AbstractEnvironment environment;
    
    @Bean
    DataSourceAware dataSource() {
        
        /*************** custom datasource **************/
        return new WrmDataSourceProvider(environment);
    }
    
    /*************** lookup defined properties file **************/
    private Properties getEnvironmentProperties() {
        MutablePropertySources propertySources = environment.getPropertySources();
        return (Properties) propertySources.get(ENVIRONMENT_PROPERTIES_AUTOWIRING_NAME).getSource();
    }
}
