/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vngcorp.log;

import com.vngcorp.config.ServerConfig;
import com.vngcorp.log.thrift.LogEntry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author rots
 */
public class BackupQueue {
    private static BlockingQueue<LogEntry> logEntries = new ArrayBlockingQueue<>(ServerConfig.MaxDataQueue);
    public static BlockingQueue<LogEntry> getQueue() {
        return logEntries;
    }
}
