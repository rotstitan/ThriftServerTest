/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vngcorp.log;

import com.vngcorp.connectionpool.ClientConnectionPool;
import com.vngcorp.log.thrift.LogEntry;
import com.vngcorp.service.ForwardClient;
import java.util.TimerTask;

/**
 *
 * @author rots
 */
public class QueueCheckTask extends TimerTask{
    @Override
    public void run() {
        while(null != ForwardClient.getClientPool() 
                    && ForwardClient.getClientPool().getState() == ClientConnectionPool.PoolState.CONNECTING 
                && BackupQueue.getQueue().size() > 0){
                LogEntry newEntry = BackupQueue.getQueue().poll();
                ForwardClient.getInstance().sendMessage(newEntry);
                //System.out.println("BackupQueue Push: " + BackupQueue.getQueue().size());
        }
    }
}
