/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vngcorp.log;

import com.vngcorp.config.ServerConfig;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.RollingFileAppender;
/**
 *
 * @author rots
 */
public class LogFactory {
    
    private static Map<String, Logger> scribeLoggers = new HashMap<>();;
    
    public synchronized static Logger getLogger(String name) {
        Logger logger = scribeLoggers.get(name);
        if(null == logger){
            System.out.println("Init new Logger: " + name);
            logger = Logger.getLogger(name);
            FileAppender appender = new FileAppender();
            appender.setName(name);
            appender.setFile(ServerConfig.QueueData);
            appender.setLayout(new PatternLayout("%m%n"));
            appender.setEncoding("UTF-8");
            appender.setAppend(true);
            appender.activateOptions();
            logger.addAppender(appender);
            //logger.addAppender(ca);
            logger.setAdditivity(false);
            scribeLoggers.put(name, logger);
        }
        return logger;
    }
}
