/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.qsthriftclient;

import com.mycompany.qsthriftclient.pool.ClientConnectionPool;
import com.mycompany.qsthriftclient.pool.MyConnection;
import com.thrifttest.thrift.TestData;
import com.thrifttest.thrift.ThriftTestService;
import java.net.ConnectException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;
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
        try {
            if(GameClient.mainErr > 0) return;
            int i;
            GameClient.addThread(1);
            for (i = 0;i < ClientConfigs.TotalDataPerProcess;i++){
                MyConnection<ThriftTestService.Client> connection = null;
                try{
                    while( GameClient.getClientPool().getState() != ClientConnectionPool.PoolState.CONNECTING){
                        Thread.sleep(1000);
                    }
                    connection = GameClient.getClientPool().getConnection();
                    if(connection != null){
                        ThriftTestService.Client client = connection.getClient();
                        TestData data = new TestData();
                        data.setB1(true);                
                        data.setB2(10);
                        data.setB3(System.currentTimeMillis());
                        data.setB4(4d);
                        data.setB5("This is Test Data make by RotS");
                        GameClient.addSend(1);
                        tSended++;
                        ReceivedData(client.sendTest(data), iDone);
                        GameClient.getClientPool().returnConnectionToPool(connection); 
                    }
                }catch(ConnectException e){
                    System.out.println ("Connect Error: " + e);
                    tFailed++;
                    GameClient.addFail(1);
                    GameClient.getClientPool().handerConnectException(connection);
                    
                }catch(Exception te){
                    System.out.println ("Socket Error: " + te);
                    tFailed++;
                    GameClient.addFail(1);
                    GameClient.getClientPool().handerConnectException(connection);
                    //System.exit(1);
                }
                Thread.sleep(200);
            }
        }
        catch(Exception e){
            tFailed++;
            GameClient.addThreadFail(1);
            System.out.println("FatalError From Thread: " + e);
            //ExitWithError(false);
        }
        GameClient.addThread(-1);     
        if(GameClient.received + GameClient.failed >= (ClientConfigs.TotalProcess * ClientConfigs.TotalDataPerProcess)
           || GameClient.thread <= 0){
                ExitWithError(true);
                return; 
        }
    }
    public static void ReceivedData(TestData recv, ThreadIndex iDone) throws InterruptedException{
        //GameClient.maxSpeed = (int)recv.getB3();
        GameClient.addTotalExTime(System.currentTimeMillis() - recv.getB3());
        iDone.Variable++;
        GameClient.addRecv(1);
        //Thread.sleep(1000);
    }
    public static void ExitWithError(boolean error){
        if(!GameClient.getIsDone()){
            GameClient.setIsDone(true);
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

 
