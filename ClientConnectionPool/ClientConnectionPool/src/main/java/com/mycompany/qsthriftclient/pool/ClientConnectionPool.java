/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.qsthriftclient.pool;

import com.mycompany.qsthriftclient.ClientConfigs;
import com.mycompany.qsthriftclient.GameClient;
import com.thrifttest.thrift.ThriftTestService;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.apache.thrift.TServiceClient;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author rots
 */
public class ClientConnectionPool <T extends TServiceClient> {
    
    
    private BlockingQueue<MyConnection<T>> freeConnections;
    
    public enum PoolState{
        NOT_CONNECT,
        CONNECTING,
        FAIL_CONNECT,
    }
    private PoolState state = PoolState.NOT_CONNECT;
    private Timer connectTimer = new Timer();
    private boolean isTryingToConnect = false;
    private Class<? extends TServiceClient> clientClass;
    private int wolkingConnection = 0;
    public BlockingQueue<MyConnection<T>> getConnectionPool() {
        return freeConnections;
    }

    public PoolState getState() {
        return state;
    }
    
    public synchronized int getTotalConnection() {
        return wolkingConnection + freeConnections.size();
    }
    public synchronized int getWolkingConnection(){
        return wolkingConnection;
    }
    public synchronized void addWolkingConnection(int add){
        wolkingConnection += add;
    }
   
    public ClientConnectionPool(Class<? extends TServiceClient> clientClass){
        this.clientClass = clientClass;
        try{
            if(!ping()){
                state = PoolState.FAIL_CONNECT;
                ScheduleAutoReconnect();
                System.out.println("FAIL TO ESTABLISH CONNECTION!");
            }else{
                InitPool();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public boolean ping(){
        
        try{
            MyConnection<T> connection = new MyConnection(ClientConfigs.HostIP
                                                           ,ClientConfigs.HostPort
                                                           ,ClientConfigs.SocketTimeOut
                                                           ,ClientConfigs.ProtocolType
                                                           ,ThriftTestService.Client.class);
            ThriftTestService.Client client = (ThriftTestService.Client) connection.getClient();
            client.ping();
            System.out.println("Trying to connect: Success!");
            return true;
        }catch(Exception e){
            System.out.println("Trying to connect: Fail!");
            //e.printStackTrace();
            return false;
        }
    }
    public void InitPool(){
        try{
            freeConnections = new ArrayBlockingQueue<>(ClientConfigs.MaxConnection);
            for(int i = getWolkingConnection(); i < ClientConfigs.InitConnection;i++){
                MyConnection<T> connection = getNewConnection();
                freeConnections.add(connection);
            }
            state = PoolState.CONNECTING;
            System.out.println("Pool is Initialized!");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public MyConnection<T> getNewConnection() throws Exception{
        MyConnection<T> connection = new MyConnection(ClientConfigs.HostIP
                                ,ClientConfigs.HostPort
                                ,ClientConfigs.SocketTimeOut
                                ,ClientConfigs.ProtocolType
                                ,clientClass);
        return connection;
    }
    public void ScheduleAutoReconnect(){
        if(!isTryingToConnect){
            TimerTask task = new TimerTask() { 
                @Override
                public void run() {
                    boolean connecting = ping();
                    if(connecting){
                        isTryingToConnect = false;
                        state = PoolState.CONNECTING;
                        connectTimer.cancel();
                        InitPool();
                    }else{
                        //System.out.println("Trying to connect!");
                    }
                }
            };
            connectTimer.scheduleAtFixedRate(task, 0, ClientConfigs.TryingToConnectInteval);
        }
        isTryingToConnect = true;
    }
    public synchronized MyConnection<T> getConnection() throws Exception{
        if(state != PoolState.CONNECTING){
            return null;
        }
        MyConnection<T> connection = freeConnections.poll();
        while(state == PoolState.CONNECTING && connection != null && connection.isClosed()){
            connection = freeConnections.poll();
        }
        if(connection == null && state == PoolState.CONNECTING){
            if(getTotalConnection() < ClientConfigs.MaxConnection){
                try{
                    connection = getNewConnection();
                    System.out.println("Create new Connection: Success ("+ freeConnections.size()  +")");
                }catch(Exception e){
                    state = PoolState.FAIL_CONNECT;
                    ScheduleAutoReconnect();
                    System.out.println("Create new Connection: Fail ("+ freeConnections.size()  +")");
                    return null;
                }
            }else{
                System.out.println("Create new Connection: Full, Reject ("+ freeConnections.size()  +")");
            }
          
        }else{
            System.out.println("get Connection in Pool: Success ("+ freeConnections.size()  +")");
        }
        if(connection == null){
            System.out.println("Return null connection ("+ freeConnections.size()  +")");
        }else{
            addWolkingConnection(1);
        }
        return connection;
    }
    public synchronized void returnConnectionToPool(MyConnection<T> connection) throws IOException{
        if(connection != null){
            addWolkingConnection(-1);
            if(!connection.isClosed()) {
                if(freeConnections.size() < ClientConfigs.MaxConnection){
                    freeConnections.add(connection);
                    System.out.println("Return connection to Pool ("+ freeConnections.size()  +")");
                }else{
                    System.out.println("Pool Full, close connection ("+ freeConnections.size()  +")");
                    try{
                       connection.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public synchronized void handerConnectException(MyConnection<T> connection) throws IOException{
        if(connection != null){
            addWolkingConnection(-1);
        }
        state = PoolState.FAIL_CONNECT;
        ScheduleAutoReconnect();
    }

}
