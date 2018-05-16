/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thrifttest.thrift;
import com.thrifttest.thrift.TestData;
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

    //@Override
    public void sendTest(TestData data, AsyncMethodCallback resultHandler) throws TException {
        //data.setB2((1+1);        
        data.setB3(2+2);        
        for(int i = 0;i < 50; i ++){
            data.setB4(random.nextInt(1000) + 1000);
        }
        System.out.println("received");
    }

    @Override
    public TestData sendTest(TestData data) throws TException {
         data.setB3(2+2);        
        for(int i = 0;i < 50; i ++){
            data.setB4(random.nextInt(1000) + 1000);
        }
        System.out.println("received");
        return data;
    }
    public class TestMethodCallback
        implements AsyncMethodCallback<ThriftTestService.AsyncClient.sendTest_call> {

        public void onError(Exception e) {
            System.out.println("Fail: " + e);
        }
        @Override
        public void onComplete(ThriftTestService.AsyncClient.sendTest_call t) {
            System.out.println("Success");
        }
    }
   
}