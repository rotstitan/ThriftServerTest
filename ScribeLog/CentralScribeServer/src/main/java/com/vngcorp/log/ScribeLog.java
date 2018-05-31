/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vngcorp.log;
import com.vngcorp.config.ServerConfig;
import com.vngcorp.service.CaculateTask;
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

    
    public static ScribeLog getInstance() {
        if (instance == null) {
            instance = new ScribeLog();
        }
        return instance;
    }
    public void Getlog4jPropeties() {
        //PropertyConfigurator.configure(ServerConfig.Log4jConfigFile);
    }
    
    protected ScribeLog() {
        Getlog4jPropeties();
    }
    
    public static int WriteLog(String gameId, String serviceId, String message){
        Logger logger = LogFactory.getLogger(gameId);
        if(null != logger){
            logger.info( String.format("%10s", "[" + serviceId + "]") + "\t" + message);
            CaculateTask.addTotalStored(1);
        }else{
            return 1;
        }
        return 0;
    }
}