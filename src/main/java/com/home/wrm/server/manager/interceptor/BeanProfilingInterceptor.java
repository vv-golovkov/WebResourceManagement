package com.home.wrm.server.manager.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StopWatch;

import com.ericsson.springsupport.jdbc.datasource.DataSourceAware;
import com.home.wrm.server.data.factory.DaoFactory;
import com.home.wrm.server.data.factory.HelperFactory;
import com.home.wrm.server.logging.SmartLogger;

public class BeanProfilingInterceptor implements MethodInterceptor {
    private static final SmartLogger logger = SmartLogger.getLogger(BeanProfilingInterceptor.class);
    private DataSourceAware dataSourceAware;
    
    public BeanProfilingInterceptor(DataSourceAware dataSourceAware) {
        this.dataSourceAware = dataSourceAware;
    }
    
    public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        try {
            Object interceptedObject = methodInvocation.getThis();
            if (interceptedObject instanceof HasHelperFactory) {
                HasHelperFactory businessObject = ((HasHelperFactory) interceptedObject);
                stopWatch.start();
                businessObject.setHelperFactory(new HelperFactory(new DaoFactory(dataSourceAware)));
            }
            String method = methodInvocation.getMethod().getName();
            logger.debugf("Proceeding method [%s] of class [%s]...", method, methodInvocation.getThis());
            return methodInvocation.proceed();
        } finally {
            if (stopWatch.isRunning()) {
                stopWatch.stop();
                double elapsedTime = stopWatch.getTotalTimeSeconds();
                logger.debugf("Elapsed time: %f sec.\n", elapsedTime);
            } else {
                logger.debug("Stop Watch is not running.");
            }
        }
    }
}
