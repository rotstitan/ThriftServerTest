/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.qsthriftclient;

import com.thrifttest.thrift.TestData;
import com.thrifttest.thrift.ThriftTestService;
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
             
            
            ThriftTestService.Client client = new ThriftTestService.Client(protocol);
            

                for (i = 0;i < ClientConfigs.TotalDataPerProcess;i++){
                try{
                    client.ping();
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
            //System.out.println("FatalError From Thread: " + e);
            //ExitWithError(false);
        }
         
//        while(!(iDone.Variable + tFailed >= ClientConfigs.TotalDataPerProcess) ){
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(MThreading.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        GameClient.addThread(-1);     
        if(GameClient.received + GameClient.failed >= (ClientConfigs.TotalProcess * ClientConfigs.TotalDataPerProcess)
           || GameClient.thread <= 0){
                ExitWithError(true);
                return; 
        }
    }
    public static void ReceivedData(TestData recv, ThreadIndex iDone){
        //GameClient.maxSpeed = (int)recv.getB3();
        GameClient.addTotalExTime(System.currentTimeMillis() - recv.getB3());
        iDone.Variable++;
        GameClient.addRecv(1);
    }
    public static void ExitWithError(boolean error){
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
        if(GameClient.received > 0){
            long avgTime = GameClient.totalExecuteTime / GameClient.received;
            System.out.println ("AVGTotalExecuteTime: " + avgTime+ " ms/recv");
        }
        
        if(!error){
            System.out.println ("SYSTEM EXIT BY ERROR!");
            System.exit(1);
        }
        else
            System.out.println ("SYSTEM EXIT SUCCESS!");
        
    }
    class TestMethodCallback
            implements AsyncMethodCallback<ThriftTestService.AsyncClient.sendTest_call> {
 
        public void onComplete(ThriftTestService.AsyncClient.sendTest_call sendTest_call) {
            try {
                TestData result = sendTest_call.getResult();
                GameClient.maxSpeed = (int)result.getB3();
                iDone.Variable++;
                GameClient.addRecv(1);
            } catch (TException e) {
                e.printStackTrace();
            }
        }
        public void onError(Exception e) {
            tFailed++;
            GameClient.addFail(1);
        }
 
    }
    
}

 
