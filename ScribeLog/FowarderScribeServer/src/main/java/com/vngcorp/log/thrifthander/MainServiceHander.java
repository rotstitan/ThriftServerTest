/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vngcorp.log.thrifthander;
import com.vngcorp.log.ScribeLog;
import com.vngcorp.log.thrift.LogEntry;
import com.vngcorp.log.thrift.MainService;
import com.vngcorp.service.CaculateTask;
import com.vngcorp.service.ForwardClient;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.TException;

/**
 *
 * @author rots
 */


public class MainServiceHander implements MainService.Iface {
    public static long index = 0;
    public static Random random = new Random();

    @Override
    public int sendLog(LogEntry entry) throws TException {
        CaculateTask.addRECV();
        ForwardClient.getInstance().sendMessage(entry);
        return 0;
    }
    @Override
    public boolean ping() throws TException {
        return true;
    }

}