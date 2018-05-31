/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vngcorp.log;

import com.vngcorp.connectionpool.ClientConnectionPool;
import com.vngcorp.log.thrift.LogEntry;
import com.vngcorp.service.ForwardClient;
import static com.vngcorp.service.ForwardClient.getClientPool;
import java.util.TimerTask;

/**
 *
 * @author rots
 */
public class StoreCheckTask extends TimerTask{
    @Override
    public void run() {
        if(ForwardClient.getClientPool().getState() == ClientConnectionPool.PoolState.CONNECTING){
            LogEntryStore.TryingToResend();
        }else if(!ForwardClient.getClientPool().getIsTryingToConnect()){
            ForwardClient.getClientPool().TryingToConnect();
        }
    }
}
