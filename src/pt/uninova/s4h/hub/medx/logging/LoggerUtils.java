package pt.uninova.s4h.hub.medx.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import java.lang.invoke.MethodHandles;
import org.slf4j.LoggerFactory;

/**
 * Utility class to manage the logging level of the entire application.
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 07 September 2019 - First version.
 */
public class LoggerUtils {

    private static final ch.qos.logback.classic.Logger UTILS_LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String FILE_NAME = "lib/logback-1.2.3.jar";

    private LoggerUtils() {
    }  // Prevents instantiation

    /**
     * To define the logging level of a class
     *
     * @throws Exception
     */
//    public static void beforeEachTest() throws Exception {
//        final Logger loggerInterface = LoggerFactory.getLogger("org.openmuc.openiec61850.ServerAssociation");
//
//        //The other lines implement logging for all classes
////        final Logger loggerInterface = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME); 
////        final Logger loggerInterface = LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME); 
//        String loggerLevelNew = Level.DEBUG.levelStr;
//        if (!loggerInterface.isTraceEnabled()) {
//            try {
//                Class<?> levelLogBackClass = Class.forName("ch.qos.logback.classic.Level");
//                Method toLevelMethod = levelLogBackClass.getDeclaredMethod("toLevel", String.class);
//                Object traceLvel = toLevelMethod.invoke(null, loggerLevelNew);
//                Method loggerSetLevelMethod = loggerInterface.getClass().getDeclaredMethod("setLevel", levelLogBackClass);
//                loggerSetLevelMethod.invoke(loggerInterface, traceLvel);
//            } catch (Exception e) {
//                UTILS_LOGGER.warn("Problem setting logger level to:{}, msg: {}", loggerLevelNew, e.getMessage());
//                throw e;
//            }
//        }
//    }
    /**
     *
     * @param consoleLoggingLevel
     * @param fileLoggingLevel
     */
    public static void setRootLoggingConsoleAndFile(Level consoleLoggingLevel, Level fileLoggingLevel) {

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%date{YYYY-MM-dd HH:mm:ss.SSS} [%thread] %level %logger - %msg%n");
        encoder.start();

        ThresholdFilter consoleFilter = new ThresholdFilter();
        consoleFilter.setLevel(consoleLoggingLevel.levelStr);
        consoleFilter.start();

        ThresholdFilter fileFilter = new ThresholdFilter();
        fileFilter.setLevel(fileLoggingLevel.levelStr);
        fileFilter.start();

        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setContext(loggerContext);
        consoleAppender.setName("ConsoleLog");
        consoleAppender.setEncoder(encoder);
        consoleAppender.addFilter(consoleFilter);
        consoleAppender.start();

        FileAppender fileAppender = new FileAppender();
        fileAppender.setContext(loggerContext);
        fileAppender.setName("FileLog");
        fileAppender.setFile(FILE_NAME);
        fileAppender.setEncoder(encoder);
        fileAppender.addFilter(fileFilter);
        fileAppender.start();

        final ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.detachAndStopAllAppenders();
        rootLogger.addAppender(fileAppender);
        rootLogger.addAppender(consoleAppender);
    }

    public static void setRootLoggingConsole(Level consoleLoggingLevel) {

        final ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(consoleLoggingLevel);
    }
}
