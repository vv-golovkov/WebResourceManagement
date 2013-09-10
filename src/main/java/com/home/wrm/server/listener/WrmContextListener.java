package com.home.wrm.server.listener;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.home.wrm.server.logging.SmartLogger;
import com.home.wrm.server.manager.business.WrProcessingManagerBusiness;
import com.home.wrm.shared.exception.WrmException;

public class WrmContextListener implements ServletContextListener {
    private static final SmartLogger logger = SmartLogger.getLogger(WrmContextListener.class);
    private static final int CLEANING_UP_PERIOD = 30;
    private static final int EVERY_DAY = 1000*60*60*24;
    
    private WrProcessingManagerBusiness businessBean;

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //skip it
    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        WebApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
        businessBean = (WrProcessingManagerBusiness) appCtx.getBean(WrProcessingManagerBusiness.class);
        scheduleCleanupTask();
    }
    
    private void scheduleCleanupTask() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new ArchiveCleanupTask(), new Date(), EVERY_DAY);
    }
    
    private class ArchiveCleanupTask extends TimerTask {
        @Override
        public void run() {
            cleanupArchivedResources();
        }
    }
    
    private void cleanupArchivedResources() {
        Date currentDate = new Date();
        logger.debugf("Current date [%s]", currentDate);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, -CLEANING_UP_PERIOD);
        Date monthBefore = calendar.getTime();
        logger.debugf(CLEANING_UP_PERIOD + " days before [%s]", monthBefore);
        try {
            businessBean.doRecurrentDatabaseCleanup(monthBefore);
        } catch (WrmException e) {
            e.printStackTrace();
            logger.error(e, "Error during cleaning up archived resources!");
        }
    }
    
//    public static void main(String[] args) {
//        Date currentDate = new Date();
//        logger.debugf("Current date [%s]", currentDate);
//        
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(currentDate);
//        calendar.add(Calendar.DAY_OF_YEAR, -14);
//        Date monthBefore = calendar.getTime();
//        logger.debugf("30 days before [%s]", monthBefore);
//    }
}
