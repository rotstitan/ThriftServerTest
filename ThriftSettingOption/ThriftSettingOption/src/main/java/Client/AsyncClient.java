/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import static Client.MultiplexServiceTest.testData;
import com.thrifttest.thrift.CaculateService;
import com.thrifttest.thrift.TestData;
import com.thrifttest.thrift.ThriftTestService;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author rots
 */
public class AsyncClient {
    
     private Integer mPort;
    private String mIpServer;
    
    public static ThriftTestService.AsyncClient ASyncClient; 
    public static CaculateService.AsyncClient ASyncCalClient; 
    public static ThriftTestService.Client Client;
    public static TestData testData;
    public static void initTestData(){
        testData = new TestData();
        testData.setB1(true);                
        testData.setB2(10);
        testData.setB3(10l);
        testData.setB4(4d);
        testData.setB5("This is Test Data make by RotS"); 
    }

    public AsyncClient(Integer port, String ipServer) {
        mIpServer = ipServer;
        mPort = port;
    }
    public static void main(String[] args) throws InterruptedException {

        try {
            initTestData();
            AsyncClient client = new AsyncClient(7770, "localhost");
            client.invoke();
        }catch (Exception e) {
            System.out.println("FatalError: "+ e);
            e.printStackTrace();
        }
    }
    public void invoke() throws InterruptedException {
     
        try {
            //Init Async Client
            ASyncClient = new ThriftTestService.AsyncClient(
                new TBinaryProtocol.Factory(), new TAsyncClientManager(),
                new TNonblockingSocket(mIpServer, mPort));
            //Init Normal Client
            TTransport socket = new TSocket(mIpServer, mPort);
            TTransport transport = new TFastFramedTransport(socket);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport); 
            Client = new ThriftTestService.Client(protocol);
            
            System.out.println("============= Normal Start Send =============");
            
            System.out.println("Normal: Start Send Data 1");
            System.out.println("Normal Result: " +  Client.sendTest(testData));
            System.out.println("Normal: DoneSend Data 1");
             
            System.out.println("Normal: Start Send Data 2");
            System.out.println("Normal Result: " +  Client.sendTest(testData));
            System.out.println("Normal: DoneSend Data 2");
            transport.close();
            System.out.println("============= Async Start Send =============");
            
            System.out.println("Async: Start Send Data 1");
            TestMethodCallback callback1 = new TestMethodCallback();
            ASyncClient.sendTest(testData, callback1);
            System.out.println("Async: DoneSend Data 1");
            
            while(callback1.result == null){
                Thread.sleep(100);
            }
            System.out.println("Async: Start Send Data 2");
            TestMethodCallback callback2 = new TestMethodCallback();
            ASyncClient.sendTest(testData, callback2);
            System.out.println("Async: DoneSend Data 2");
            
            while(callback2.result == null){
                Thread.sleep(100);
            }
            
            
//            System.out.println("============= Other Service Send =============");
//            ASyncCalClient = new CaculateService.AsyncClient(
//                new TBinaryProtocol.Factory(), new TAsyncClientManager(),
//                new TNonblockingSocket(mIpServer, 7771));
//            CaculateMethodCallback callback3 = new CaculateMethodCallback();
//            ASyncCalClient.add(10,20, callback3);
//            while(!callback3.recv){
//                Thread.sleep(100);
//            }
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
        public TestData result = null;  
        @Override
        public void onError(Exception e) {
            System.out.println("Async Fail: " + e);
        }
        @Override
        public void onComplete(ThriftTestService.AsyncClient.sendTest_call t) {
            try {
                result = t.getResult();
                System.out.println("Async Result: " + result.toString());
            } catch (TException e) {
                System.out.println("Async: Fail to get Data!");
            }
        }
    }
    public class CaculateMethodCallback
    implements AsyncMethodCallback<CaculateService.AsyncClient.add_call> {
        public int result = 0;
        public boolean recv = false;
        @Override
        public void onComplete(CaculateService.AsyncClient.add_call t) {
            try {
                result = t.getResult();
                System.out.println("Caculate Async Result: " + result);
            } catch (TException ex) {
                System.out.println("Caculate Async: Fail to get Data!");
            }
            recv = true;
        }

        @Override
        public void onError(Exception ex) {
            System.out.println("Calculate Async Fail: " + ex);
            recv = true;
        }
    }
}
