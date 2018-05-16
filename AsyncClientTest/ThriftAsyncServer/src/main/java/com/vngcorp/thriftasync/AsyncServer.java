/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vngcorp.thriftasync;

import com.thrifttest.thrift.ThriftTestHander;
import com.thrifttest.thrift.ThriftTestService;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author rots
 */
public class AsyncServer {

    /**
     *
     * @param port
     */
    public void start(Integer port) throws TTransportException{
        TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(port);
        ThriftTestService.Processor processor =
             new ThriftTestService.Processor(
                new ThriftTestHander());
 
        TServer server = new TNonblockingServer(
            new TNonblockingServer.Args(serverTransport).processor(processor));
        System.out.println("Starting server ...");
        server.serve();
        
    }
    public static void main(String[] args) throws InterruptedException {

        try {
            AsyncServer server = new AsyncServer();
            server.start(7777);
            
        }catch (Exception e) {
            System.out.println("FatalError: ");
            e.printStackTrace();
        }
    }
}