/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vngcorp.service;

import com.vngcorp.config.ServerConfig;
import com.vngcorp.connectionpool.ClientConnectionPool;
import com.vngcorp.connectionpool.MyConnection;
import com.vngcorp.log.BackupQueue;
import com.vngcorp.log.LogEntryStore;
import com.vngcorp.log.ScribeLog;
import com.vngcorp.log.thrift.LogEntry;
import com.vngcorp.log.thrift.MainService;
import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.hdfs.server.protocol.ServerCommand;
import org.apache.thrift.transport.TTransport;

/**
 *
 * @author rots
 */
public class ForwardClient {
    private static ForwardClient instance = null;
    public static ClientConnectionPool<MainService.Client> ConnectionPool;
    public static synchronized ClientConnectionPool<MainService.Client> getClientPool() {
        return ConnectionPool;
    }
    public static BlockingQueue<LogEntry> SenderQueue;
    public static synchronized BlockingQueue<LogEntry> getSenderQueue() {
        return SenderQueue;
    }
    public static synchronized void setSenderQueue(BlockingQueue<LogEntry> queue) {
        SenderQueue = queue;
    }

    ForwardClient(){
        ConnectionPool = new ClientConnectionPool<MainService.Client>(MainService.Client.class);
        setSenderQueue(new ArrayBlockingQueue<LogEntry>(ServerConfig.ForwardSenderQueue));
    }
    public static ForwardClient getInstance() {
        if (instance == null) {
            instance = new ForwardClient();
        }
        return instance;
    }
    public void RunClient(){
        for(int i = 0;i < ServerConfig.ForwardSenderThread;i++){
            (new Thread(() -> {
                while(true){
                    try {
                        while(getSenderQueue().size() > 0 && getClientPool().getState() == ClientConnectionPool.PoolState.CONNECTING){
                            MyConnection<MainService.Client> connection = null;
                            LogEntry entry = getSenderQueue().poll();
                            if(null == entry) continue;
                            try{
                                connection = getClientPool().getConnection();
                                if(connection != null){
                                    MainService.Client client = connection.getClient();
                                    ReceivedData(entry, client.sendLog(entry));
                                    LogEntry newEntry = null;
                                    getClientPool().returnConnectionToPool(connection);
                                }else{
                                    storeLog(entry);
                                }
                            }catch(ConnectException e){
                                System.out.println("ConnectionError:" + e);
                                try {
                                    getClientPool().handerConnectException(connection);
                                    storeLog(entry);
                                } catch (Exception ex) {
                                    System.out.println("Exception When Trying To StoreLog: " + ex);       
                                }
                            }catch(Exception te){
                                System.out.println("Fatal Error: " + te);
                                try {
                                    getClientPool().handerConnectException(connection);
                                    storeLog(entry);
                                } catch (Exception ex) {
                                    System.out.println("Exception When Trying To StoreLog: " + ex);
                                }
                            }
                        }
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        System.out.println("Thread Interrupted!");
                    }
                }
            })).start();
        }
    }
    public void sendMessage(LogEntry entry){
        try{
            if(getSenderQueue() != null 
                    && getClientPool().getState() == ClientConnectionPool.PoolState.CONNECTING){
                
                if(getSenderQueue().size() < ServerConfig.ForwardSenderQueue){
                    getSenderQueue().add(entry);
                }else{
                    BackupQueue.getQueue().add(entry);
                }
            }else{
                storeLog(entry);
            }
        }catch(Exception e){
            System.out.println("Cannot add Message to Queue, Size:" + getSenderQueue().size());
        }
        
    }
    public static int ReceivedData(LogEntry entry, int code){
        if(code != 0){
            storeLog(entry);
            System.out.println("Log Sended but Faild!!");
        }
        CaculateTask.addtotalSendToParent();
        return code;
    }
    public static void storeLog(LogEntry entry){
        if(LogEntryStore.isStoreLock()){
            BackupQueue.getQueue().add(entry);
            System.out.println("put data to BackupQueue, Size:" + BackupQueue.getQueue().size());
        }else{
            //System.out.println("put data to Store, Size: " + LogEntryStore.getTotalStored());
            ScribeLog.getInstance().WriteLog(entry.gameId, entry.serviceId, entry.message);
        }

    }
}
