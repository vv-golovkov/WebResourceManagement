package com.home.wrm.server.configuration;

import javax.annotation.Resource;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.home.wrm.server.manager.bean.WrProcessingManagerBean;
import com.home.wrm.server.manager.business.WrProcessingManagerBusiness;
import com.home.wrm.server.manager.interceptor.BeanProfilingInterceptor;

@Configuration
@EnableTransactionManagement
@Import(DatasourceConfig.class)
@ComponentScan(basePackages = { "com.home.wrm.server.remote" })
public class SpringIntegrationConfig {

    @Resource
    private DatasourceConfig datasourceConfig;

    @Bean//(name = "wrProcessingManagerBean", autowire = Autowire.BY_NAME)
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    WrProcessingManagerBusiness wrProcessingManagerBean() {
        ProxyFactory proxyFactory = new ProxyFactory(new WrProcessingManagerBean());
        proxyFactory.addAdvice(beanProfilingInterceptor());
        return (WrProcessingManagerBusiness) proxyFactory.getProxy();
    }

    @Bean
    DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(datasourceConfig.dataSource().getDataSource());
    }

    @Bean
    BeanProfilingInterceptor beanProfilingInterceptor() {
        return new BeanProfilingInterceptor(datasourceConfig.dataSource());
    }
}
