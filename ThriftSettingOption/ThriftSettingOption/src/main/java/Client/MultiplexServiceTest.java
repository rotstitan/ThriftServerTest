/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import com.thrifttest.thrift.CaculateService;
import com.thrifttest.thrift.TestData;
import com.thrifttest.thrift.ThriftTestService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author rots
 */
public class MultiplexServiceTest {
    public static TestData testData;
    public static void initTestData(){
        testData = new TestData();
        testData.setB1(true);                
        testData.setB2(10);
        testData.setB3(10l);
        testData.setB4(4d);
        testData.setB5("This is Test Data make by RotS"); 
    }
     public static void main(String[] args){
        (new Thread(){
            @Override
            public synchronized void run(){
                try {
                    
                    initTestData();
                    TTransport socket = new TSocket("localhost", 7771);
                    TTransport transport = new TFastFramedTransport(socket);
                    transport.open();
                    TProtocol protocol = new TBinaryProtocol(transport);
                    System.out.println("============= MultiServiceTest =============");
                    TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol, "ThriftTestService");
                    ThriftTestService.Client thriftTestClient = new ThriftTestService.Client(mp);
                    System.out.println("ThriftTestService RECV:" + thriftTestClient.sendTest(testData));

                    TMultiplexedProtocol mp2 = new TMultiplexedProtocol(protocol, "CaculateService");
                    CaculateService.Client caculateClient = new CaculateService.Client(mp2);
                    System.out.println("CaculateService RECV:" + caculateClient.add(1, 3));

                } catch (TTransportException ex) {
                    ex.printStackTrace();
                } catch (TException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }
}
