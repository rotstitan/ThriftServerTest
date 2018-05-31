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
            logger = Logger.getLogger(name);

            DailyRollingFileAppender appender = new DailyRollingFileAppender();
            appender.setName(name);
            appender.setFile(ServerConfig.LogPath + name + "/"+ name + ".log");
            appender.setDatePattern( "'.'yyyy-MM-dd-HH");
            appender.setLayout(new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} - %m%n"));
            appender.setEncoding("UTF-8");
            appender.setAppend(true);
            appender.activateOptions();

            ConsoleAppender ca = new ConsoleAppender();
            ca.setWriter(new OutputStreamWriter(System.out));
            ca.setLayout(new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} - %m%n"));
            logger.addAppender(appender);
            //logger.addAppender(ca);
            logger.setAdditivity(false);
            scribeLoggers.put(name, logger);
        }
        return logger;
    }
}
