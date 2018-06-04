/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;
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
        index ++;
        return data;
    }

    @Override
    public synchronized void ping(){
        index++;
    }
}