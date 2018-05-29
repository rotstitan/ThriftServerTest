/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vngcorp.log;
import com.vngcorp.config.ServerConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.*;
/**
 *
 * @author rots
 */
public class ScribeLog {
    
    private static ScribeLog instance = null;
    
    private static Map<String, Logger> scribeLoggers;

    
    public static ScribeLog getInstance() {
        if (instance == null) {
            instance = new ScribeLog();
        }
        return instance;
    }
    public void Getlog4jPropeties() {
        scribeLoggers = new HashMap<>();
        Properties log4jProperties = new Properties();
        for (String gameid : ServerConfig.GameID) {
            if(true){
                log4jProperties.setProperty("log4j.appender."+ gameid, "org.apache.log4j.DailyRollingFileAppender");
                log4jProperties.setProperty("log4j.appender."+ gameid +".File", ServerConfig.LogPath + gameid + "/"+ gameid + ".log");
                log4jProperties.setProperty("log4j.appender."+ gameid +".DatePattern", "'.'yyyy-MM-dd-HH");            
                log4jProperties.setProperty("log4j.appender."+ gameid +".layout", "org.apache.log4j.PatternLayout");
                //"%d{yyyy-MM-dd HH:mm:ss}\t%-5p - %m%n"
                log4jProperties.setProperty("log4j.appender."+ gameid +".layout.ConversionPattern", "%d{yyyy-MM-dd HH:mm:ss} - %m%n");
                log4jProperties.setProperty("log4j.logger." + gameid, "INFO, " + gameid);
                log4jProperties.setProperty("log4j.additivity." + gameid, "false");
            }else{
                log4jProperties.setProperty("log4j.appender."+ gameid, "org.apache.log4j.rolling.RollingFileAppender");
                log4jProperties.setProperty("log4j.appender."+ gameid +".rollingPolicy", "org.apache.log4j.rolling.TimeBasedRollingPolicy");
                log4jProperties.setProperty("log4j.appender."+ gameid +".rollingPolicy.ActiveFileName", ServerConfig.LogPath + gameid + "/"+ gameid + ".log");
                log4jProperties.setProperty("log4j.appender."+ gameid +".rollingPolicy.FileNamePattern", ServerConfig.LogPath + gameid + "/"+ gameid + "_%d{yyyyMMdd-HH}.log.gz");            
                log4jProperties.setProperty("log4j.appender."+ gameid +".DatePattern", "'.'yyyy-MM-dd-HH");            
                log4jProperties.setProperty("log4j.appender."+ gameid +".layout", "org.apache.log4j.PatternLayout");
                log4jProperties.setProperty("log4j.appender."+ gameid +".layout.ConversionPattern", "%d{yyyy-MM-dd HH:mm:ss} - %m%n");
                log4jProperties.setProperty("log4j.logger." + gameid, "INFO, " + gameid);
                log4jProperties.setProperty("log4j.additivity." + gameid, "false");
            }
        
        }
        PropertyConfigurator.configure(log4jProperties);
        for (String gameid : ServerConfig.GameID) {
            Logger logger = LogManager.getLogger(gameid);
            //AsyncAppender asyncAppender = (AsyncAppender) logger.getAppender("ASYNC");
            //asyncAppender.setBufferSize(4);
            scribeLoggers.put(gameid, logger);
        }
    }
    
    protected ScribeLog() {
        Getlog4jPropeties();
    }
    
    public int WriteLog(String gameId, String serviceId, String message){
        Logger logger = scribeLoggers.get(gameId);
        if(null != logger){
            logger.info( String.format("%10s", "[" + serviceId + "]") + "\t" + message);
            return 0;
        }
        return 1;
    }
}
