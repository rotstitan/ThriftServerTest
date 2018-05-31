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
    
    public int WriteLog(String gameId, String serviceId, String message){
        Logger logger = LogFactory.getLogger("Admin");
        if(null != logger){
            logger.info( gameId + "\t" + serviceId + "\t" + message);
            LogEntryStore.addTotalStored(1);
        }else{
            System.out.println("Store Fail!! " + LogEntryStore.getTotalStored());
            return 1;
        }
        return 0;
    }
}