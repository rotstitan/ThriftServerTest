/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;
import com.thrifttest.thrift.TestData;
import com.thrifttest.thrift.TestData;
import com.thrifttest.thrift.ThriftTestService;
import com.thrifttest.thrift.ThriftTestService;
import java.util.*;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

/**
 *
 * @author rots
 */


public class ThriftTestHander implements ThriftTestService.Iface {
    public static long index = 0;
    public static Random random = new Random();
    @Override
    public TestData sendTest(TestData data) throws TException {
        System.out.println("Normal Received:" + data);
        data.setB3(2+2);        
        data.setB4(random.nextInt(1000) + 1000);
        return data;
    }

    @Override
    public synchronized void ping() throws TException {
        index++;
    }
    
    
    public class TestMethodCallback
        implements AsyncMethodCallback<ThriftTestService.AsyncClient.sendTest_call> {

        public void onError(Exception e) {
            System.out.println("Async Fail: " + e);
        }
        @Override
        public void onComplete(ThriftTestService.AsyncClient.sendTest_call t) {
            System.out.println("Async Received:");
        }
    }
   
}