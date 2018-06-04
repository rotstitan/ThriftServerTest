/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.qsthrift;
import com.thrifttest.thrift.TestData;
import com.thrifttest.thrift.ThriftTestService;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.TException;

/**
 *
 * @author rots
 */


public class ThriftTestHander implements ThriftTestService.Iface {
    public static long index = 0;
    public static Random random = new Random();
    @Override
    public TestData sendTest(TestData data) {
        CaculateTask.index ++;
//        try {
//            Thread.sleep(12000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(ThriftTestHander.class.getName()).log(Level.SEVERE, null, ex);
//        }
        //data.setB2((int) CaculateTask.currentSpeed);        
        //data.setB3(CaculateTask.maxSpeed);        
//        for(int i = 0;i < 50; i ++){
//            data.setB4(random.nextInt(1000) + 1000);
//        }
//        for(int i = 0;i < 10000; i ++){
//            data.setB5(data.getB5() + "|");
//        }
        //data.setB5("NeedHelp");
        return data;
    }

    @Override
    public void ping(){
        CaculateTask.index ++;
    }
}