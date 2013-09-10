package com.home.wrm.server.logging;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 * Convenient wrapper over log4j Logger.
 * 
 * @author vitaliy.golovkov
 * 
 */
public class SmartLogger {
    private static Map<Class<?>, SmartLogger> classToLoggerCache = new HashMap<Class<?>, SmartLogger>();
    private static final String FQCN = SmartLogger.class.getName();
    private static final String NULL_STRING = "null";
    private final Logger logger;
    private static final String PREF = ")> ";
//
    public static final SmartLogger getLogger(Class<?> clazz) {
        SmartLogger logger = classToLoggerCache.get(clazz);
        if (logger == null) {
            logger = new SmartLogger(clazz.getName());
            classToLoggerCache.put(clazz, logger);
        }
        return logger;
    }
//
    private SmartLogger(String name) {
        logger = Logger.getLogger(name);
    }

    /*--- trace, info, debug, error ---*/
    /**
     * Log a message object with the TRACE level.
     * 
     * This method first checks if this category is TRACE enabled by comparing
     * the level of this category with the TRACE level. If this category is
     * TRACE enabled, then it converts the message object (passed as parameter)
     * to a string by invoking the appropriate ObjectRenderer. It then proceeds
     * to call all the registered appenders in this category and also higher in
     * the hierarchy depending on the value of the additivity flag.
     * 
     * @param args
     *            - the message object to log.
     */
    public void trace(Object... args) {
        logIfEnabled(Level.TRACE, args);
    }

    /**
     * Log <b>formatted</b> message object with the TRACE level.
     * 
     * @param format
     *            - format message.
     * @param args
     *            - appropriate format arguments.
     */
    public void tracef(String format, Object... args) {
        logFormattedIfEnabled(Level.TRACE, format, args);
    }

    /**
     * Log a message object with the DEBUG level.
     * 
     * This method first checks if this category is DEBUG enabled by comparing
     * the level of this category with the DEBUG level. If this category is
     * DEBUG enabled, then it converts the message object (passed as parameter)
     * to a string by invoking the appropriate ObjectRenderer. It then proceeds
     * to call all the registered appenders in this category and also higher in
     * the hierarchy depending on the value of the additivity flag.
     * 
     * @param args
     *            - the message object to log.
     */
    public void debug(Object... args) {
        logIfEnabled(Level.DEBUG, args);
    }

    /**
     * Log <b>formatted</b> message object with the DEBUG level.
     * 
     * @param format
     *            - format message.
     * @param args
     *            - appropriate format arguments.
     */
    public void debugf(String format, Object... args) {
        logFormattedIfEnabled(Level.DEBUG, format, args);
    }

    /**
     * Log a message object with the INFO Level.
     * 
     * This method first checks if this category is INFO enabled by comparing
     * the level of this category with INFO Level. If the category is INFO
     * enabled, then it converts the message object passed as parameter to a
     * string by invoking the appropriate ObjectRenderer. It proceeds to call
     * all the registered appenders in this category and also higher in the
     * hierarchy depending on the value of the additivity flag.
     * 
     * @param args
     *            - the message object to log.
     */
    public void info(Object... args) {
        logIfEnabled(Level.INFO, args);
    }

    /**
     * Log <b>formatted</b> message object with the INFO level.
     * 
     * @param format
     *            - format message.
     * @param args
     *            - appropriate format arguments.
     */
    public void infof(String format, Object... args) {
        logFormattedIfEnabled(Level.INFO, format, args);
    }

    /**
     * Log a message object with the ERROR level including the stack trace of
     * the Throwable passed as parameter.
     * 
     * @param cause
     *            - the exception to log, including its stack trace.
     * @param args
     *            - the message object to log.
     */
    public void error(Throwable cause, Object... args) {
        joinAndLog(cause, Level.ERROR, args);
    }

    /**
     * Log <b>formatted</b> message object with the ERROR level.
     * 
     * @param format
     *            - format message.
     * @param args
     *            - appropriate format arguments.
     */
    public void errorf(String format, Object... args) {
        logFormatted(Level.ERROR, format, args);
    }

    /**
     * Log <b>formatted</b> message object with the ERROR level including the
     * stack trace of the Throwable passed as parameter.
     * 
     * @param cause
     *            - the exception to log, including its stack trace.
     * @param format
     *            - format message.
     * @param args
     *            - appropriate format arguments.
     */
    public void errorf(Throwable cause, String format, Object... args) {
        logFormatted(cause, Level.ERROR, format, args);
    }

    /*--- log formatted or if enabled---*/
    private void logFormatted(Level level, String format, Object... args) {
        logFormatted(null, level, format, args);
    }

    private void logFormatted(Throwable t, Level level, String format, Object... args) {
        String message = (format == null) ? NULL_STRING : (PREF + new LoggingStringFormatter().format(format, args));
        logger.log(FQCN, level, message, null);
    }

    private void logIfEnabled(Level level, Object... args) {
        if (logger.isEnabledFor(level)) {
            joinAndLog(level, args);
        }
    }

    private void logFormattedIfEnabled(Level level, String format, Object... args) {
        if (logger.isEnabledFor(level)) {
            logFormatted(level, format, args);
        }
    }

    /*--- join and log ---*/
    private void joinAndLog(Level level, Object... args) {
        joinAndLog(null, level, args);
    }

    private void joinAndLog(Throwable cause, Level level, Object... args) {
        String message = PREF + join(args);
        logger.log(FQCN, level, message, cause);
    }

    private String join(Object[] args) {
        if (args == null) {
            return NULL_STRING;
        }
        StringBuilder builder = new StringBuilder();
        for (Object o : args) {
            builder.append(o);
        }
        return builder.toString();
    }
}
