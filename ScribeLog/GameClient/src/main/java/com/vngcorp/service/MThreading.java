/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vngcorp.service;

import com.vngcorp.config.ClientConfigs;
import com.vngcorp.log.LogActionConstant;
import com.vngcorp.log.LogConstant;
import com.vngcorp.log.thrift.LogEntry;
import com.vngcorp.log.thrift.MainService;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.protocol.TSimpleJSONProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TNonblockingTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author rots
 */
class ThreadIndex{
    public int Variable = 0;
}
public class MThreading extends Thread 
{
        
    public int tSended = 0; 
    public ThreadIndex iDone = new ThreadIndex();
    public int tFailed = 0;
    public static Random rand = new Random();
    private static int TestPacketIndex = 0;
    public static synchronized int getTestPacketIndex(){
        return TestPacketIndex;
    } 
    public static synchronized void addTestPacketIndex(int add){
        TestPacketIndex += add;
    } 
    //CharacterService.Client client;
    public MThreading() {
        //this.client = client;
    }

    @Override
    public void run() {
        if(GameClient.mainErr > 0) return;
        try {
            int i;
            GameClient.addThread(1);
            TTransport socket;
            switch(ClientConfigs.BlockingType){
                case TSocket:
                    socket = new TSocket(ClientConfigs.HostIP, ClientConfigs.HostPort, ClientConfigs.SocketTimeOut);
                    break;
                case TNonblockingSocket:
                default:
                    socket = new TNonblockingSocket(ClientConfigs.HostIP, ClientConfigs.HostPort);
                    break;
            }
            TTransport transport;
            switch(ClientConfigs.TransportType){
                
                case TFramedTransport: 
                    transport = new TFramedTransport(socket);
                    break;
                case TMemoryTransport:
                case TZlibTransport:      
                case TFileTransport: 
                case TSocket:
                default:
                    transport = socket;
                    break;
            }
            transport.open();

            TProtocol protocol;
            
            switch(ClientConfigs.ProtocolType){
                case TBinaryProtocol:
                     protocol = new TBinaryProtocol(transport); 
                     break;

                case TJSONProtocol:
                case TSimpleJSONProtocol:
                case TCompactProtocol:
                case TDebugProtocol:
                case TDenseProtocol:
                default:
                    protocol = new TCompactProtocol(transport); 
                    break;
            } 
             
            
            MainService.Client client = new MainService.Client(protocol);
            

                for (i = 0;i < ClientConfigs.TotalDataPerProcess;i++){
                try{
                    GameClient.addSend(1);
                    tSended++;
                    //client.sendTest(data);
                    ReceivedData(client.sendLog(getTestLogEntry()), iDone);
                    //Thread.sleep(500 + rand.nextInt(2500));
                }catch(Exception te){
                    //System.out.println ("Socket Error: " + te);
                    tFailed++;
                    GameClient.addFail(1);
                    //System.exit(1);
                }
               
            }
            transport.close();
        }
        catch(Exception e){
            tFailed++;
            GameClient.addThreadFail(1);
        }
        GameClient.addThread(-1);     
        if(GameClient.received + GameClient.failed >= (ClientConfigs.TotalProcess * ClientConfigs.TotalDataPerProcess)
           || GameClient.thread <= 0){
                getTestResult(true);
                return; 
        }
    }
    public LogEntry getTestLogEntry(){
        LogEntry entry = new LogEntry();
        int gid = 0;        
        //int gid = rand.nextInt(3);
        entry.gameId = ClientConfigs.GameID[gid];
        
        int serviceid = 0;
        if(serviceid == 0) entry.serviceId = LogConstant.USER_SERVICE;
        else if(serviceid == 1) entry.serviceId = LogConstant.PAYMENT_SERVICE;
        else entry.serviceId = LogConstant.ACTION_SERVICE;
        addTestPacketIndex(1);
        entry.message = System.currentTimeMillis() +  "|rots|Data " + getTestPacketIndex();
        return entry;
    }
    public static void ReceivedData(int code, ThreadIndex iDone){
        iDone.Variable++;
        GameClient.addRecv(1);
    }
    public static void getTestResult(boolean error){
        System.out.println ("Final Result ------------------------- ");             
        System.out.println ("Total Disconect: " + GameClient.mainErr);                
        int percent = GameClient.received * 100 / (ClientConfigs.TotalProcess * ClientConfigs.TotalDataPerProcess);
        
        System.out.println ("Send: " + GameClient.sended + " |Recv: " + GameClient.received + " |Total: " + (ClientConfigs.TotalProcess * ClientConfigs.TotalDataPerProcess) + " ("+ percent + "%)");            
        System.out.println ("Failed: " + GameClient.failed + " |ThreadFailed: " + GameClient.threadFailed);        
        long delta = System.currentTimeMillis() - GameClient.startTestTime;
        double testTime = delta / 1000.0d;
        System.out.println ("TotalTestTime: " + testTime + "s");
        double avgSpeed = Math.round(100000d * GameClient.received / delta)/100;
        System.out.println ("AVGSpeed: " + avgSpeed + "req/s");
        
        if(!error){
            System.out.println ("SYSTEM EXIT BY ERROR!");
            System.exit(1);
        }
        else
            System.out.println ("SYSTEM EXIT SUCCESS!");
        
    }
}

 
