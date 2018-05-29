/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vngcorp.thriftasyncclient;

import com.thrifttest.thrift.TestData;
import com.thrifttest.thrift.ThriftTestService;
import java.io.IOException;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author rots
 */
public class AsyncClient {
    
     private Integer mPort;
    private String mIpServer;
    
    public static ThriftTestService.AsyncClient client;
    public AsyncClient(Integer port, String ipServer) {
        mIpServer = ipServer;
        mPort = port;
    }
    public static void main(String[] args) throws InterruptedException {

        try {
            AsyncClient client = new AsyncClient(7777, "localhost");
            client.invoke();
        }catch (Exception e) {
            System.out.println("FatalError: "+ e);
            e.printStackTrace();
        }
    }
    public void invoke() throws InterruptedException {
     
        try {
            client = new ThriftTestService.AsyncClient(
                new TBinaryProtocol.Factory(), new TAsyncClientManager(),
                new TNonblockingSocket(mIpServer, mPort));
            
            TestData data = new TestData();
            data.setB1(true);                
            data.setB2(10);
            data.setB3(10l);
            data.setB4(4d);
            data.setB5("This is Test Data make by RotS"); 
            client.sendTest(data, new TestMethodCallback());
//            for(int i = 0;i < 1000000;i++){
//                 client.sendTest(data, new TestMethodCallback());
//            }
            for(int i = 0 ;i < 10000;i++){
                Thread.sleep(4000);
            }
        } catch (TTransportException e) {
             System.out.println("TTransportException:" + e);
        } catch (TException e) {
             System.out.println("TException:" + e);
        } catch (IOException e) {
            System.out.println("IOException:" + e);
        }
    }
    
    
    
    public class TestMethodCallback
    implements AsyncMethodCallback<ThriftTestService.AsyncClient.sendTest_call> {
     @Override
    public void onError(Exception e) {
        System.out.println("Fail:" + e);
    }
    @Override
    public void onComplete(ThriftTestService.AsyncClient.sendTest_call t) {
        try {
            TestData result = t.getResult();
            System.out.println("Success:" + result.toString());
            TestData data = new TestData();
            data.setB1(true);                
            data.setB2(10);
            data.setB3(10l);
            data.setB4(4d);
            data.setB5("This is Test Data make by RotS"); //ClientConfig.BigData
            client.sendTest(data, new TestMethodCallback());
            // elaboration of the result
        } catch (TException e) {
            
        }
    }
 
}
}
