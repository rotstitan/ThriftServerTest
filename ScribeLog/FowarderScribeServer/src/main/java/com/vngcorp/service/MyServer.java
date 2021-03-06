/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vngcorp.service;

import com.vngcorp.connectionpool.ClientConnectionPool;
import com.vngcorp.config.ServerConfig;
import com.vngcorp.connectionpool.MyConnection;
import com.vngcorp.log.QueueCheckTask;
import com.vngcorp.log.ScribeLog;
import com.vngcorp.log.StoreCheckTask;
import com.vngcorp.log.thrifthander.MainServiceHander;
import com.vngcorp.log.thrift.MainService;
import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TSimpleJSONProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportFactory;

/**
 *
 * @author rots
 */
public class MyServer {
   
    
    
    public static void ScheduleAllTasks(){
        Timer time = new Timer();
        CaculateTask ct = new CaculateTask();
        time.schedule(ct, 0, 1000);
        QueueCheckTask qct = new QueueCheckTask();
        time.schedule(qct, 500, ServerConfig.QueueCheckInterval);
        StoreCheckTask sct = new StoreCheckTask();
        time.schedule(sct, 1500, ServerConfig.QueueCheckInterval);
        
    }
    public static void StartsimpleServer(MainService.Processor<MainServiceHander
                > processor) {
        try {
            
            TServer server;
            switch (ServerConfig.ServerType){
                case TSimpleServer:
                    server = simpleServer(processor);
                    break;
                case TNonblockingServer:
                    server = nonblockingServer(processor);
                    break;
                case THsHaServer:
                    server = tHsHaServer(processor);
                    break;
                case TThreadedSelectorServer:
                    server = threadedSelectorServer(processor);
                    break;
                case TThreadPoolServer:
                default:
                    server = poolServer(processor);
                    break;
            }
            ScheduleAllTasks();
            System.out.println("Starting the "+ ServerConfig.ServerType +"...");
            server.serve();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public static void main(String[] args){
        ForwardClient.getInstance().RunClient();
        StartsimpleServer(new MainService.Processor<MainServiceHander>(new MainServiceHander()));
    }
   
    public static TServer poolServer(MainService.Processor<MainServiceHander> processor) throws Exception {

            TServerTransport serverTransport;
            switch(ServerConfig.BlockingType){
                case TNonblockingSocket:
                    serverTransport = new TNonblockingServerSocket(ServerConfig.HostPort, ServerConfig.SocketTimeOut);
                    break;
                case TSocket:
                default:
                    serverTransport = new TServerSocket(ServerConfig.HostPort, ServerConfig.SocketTimeOut);
                    break;
            }
            TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
            
            switch(ServerConfig.TransportType){
                case TFramedTransport: 
                    args.transportFactory(new TFramedTransport.Factory());
                    break;
                case TMemoryTransport:
                case TZlibTransport:      
                case TFileTransport: 
                case TSocket:
                default:
                     args.transportFactory(new TTransportFactory());
                    break;
            }
            
            switch(ServerConfig.ProtocolType){
                case TBinaryProtocol:
                     args.protocolFactory(new TBinaryProtocol.Factory()); 
                     break;

                case TJSONProtocol:
                    args.protocolFactory(new TJSONProtocol.Factory());
                    break;
                case TSimpleJSONProtocol:
                    args.protocolFactory(new TSimpleJSONProtocol.Factory());
                    break;
                case TCompactProtocol:
                case TDebugProtocol:
                case TDenseProtocol:
                default:
                    args.protocolFactory(new TCompactProtocol.Factory()); 
                    break;
            }

            args.processor(processor);
            args.executorService(new ThreadPoolExecutor(512, 65535, 1,
                            TimeUnit.SECONDS, new SynchronousQueue<Runnable>()));

            return new TThreadPoolServer(args);
    }
    public static TServer simpleServer(MainService.Processor<MainServiceHander> processor) throws Exception {

            TServerTransport serverTransport;
            switch(ServerConfig.BlockingType){
                case TNonblockingSocket:
                    serverTransport = new TNonblockingServerSocket(ServerConfig.HostPort, ServerConfig.SocketTimeOut);
                    break;
                case TSocket:
                default:
                    serverTransport = new TServerSocket(ServerConfig.HostPort, ServerConfig.SocketTimeOut);
                    break;
            }
            
            TSimpleServer.Args args = new TSimpleServer.Args(serverTransport);
            
            switch(ServerConfig.TransportType){
                case TFramedTransport: 
                    args.transportFactory(new TFramedTransport.Factory());
                    break;
                case TMemoryTransport:
                case TZlibTransport:      
                case TFileTransport: 
                case TSocket:
                default:
                     args.transportFactory(new TTransportFactory());
                    break;
            }
            switch(ServerConfig.ProtocolType){
                case TBinaryProtocol:
                     args.protocolFactory(new TBinaryProtocol.Factory()); 
                     break;

                case TJSONProtocol:
                    args.protocolFactory(new TJSONProtocol.Factory());
                    break;
                case TSimpleJSONProtocol:
                    args.protocolFactory(new TSimpleJSONProtocol.Factory());
                    break;
                case TCompactProtocol:
                case TDebugProtocol:
                case TDenseProtocol:
                default:
                    args.protocolFactory(new TCompactProtocol.Factory()); 
                    break;
            }
            args.processor(processor);
        return new TSimpleServer(args);
    }
    public static TServer threadedSelectorServer(MainService.Processor<MainServiceHander> processor) throws Exception {

        TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(new TNonblockingServerSocket(ServerConfig.HostPort, ServerConfig.SocketTimeOut));
        args.workerThreads(ServerConfig.WorkerThread);        
        args.selectorThreads(ServerConfig.SelectorThread);
        //args.acceptPolicy(TThreadedSelectorServer.Args.AcceptPolicy.FAIR_ACCEPT);
        

        switch(ServerConfig.TransportType){
            case TFramedTransport: 
                args.transportFactory(new TFramedTransport.Factory());
                break;
            case TMemoryTransport:
            case TZlibTransport:      
            case TFileTransport: 
            case TSocket:
            default:
                 args.transportFactory(new TTransportFactory());
                break;
        }
        
        switch(ServerConfig.ProtocolType){
            case TBinaryProtocol:
                 args.protocolFactory(new TBinaryProtocol.Factory()); 
                 break;

            case TJSONProtocol:
                args.protocolFactory(new TJSONProtocol.Factory());
                break;
            case TSimpleJSONProtocol:
                args.protocolFactory(new TSimpleJSONProtocol.Factory());
                break;
            case TCompactProtocol:
            case TDebugProtocol:
            case TDenseProtocol:
            default:
                args.protocolFactory(new TCompactProtocol.Factory()); 
                break;
        }
        args.processor(processor);
        return new TThreadedSelectorServer(args);
    }
    public static TServer tHsHaServer(MainService.Processor<MainServiceHander> processor) throws Exception {
        
        THsHaServer.Args args = new THsHaServer.Args(new TNonblockingServerSocket(ServerConfig.HostPort, ServerConfig.SocketTimeOut));
        
        switch (ServerConfig.TransportType) {
            case TFramedTransport:
                args.transportFactory(new TFramedTransport.Factory());
                break;
            case TMemoryTransport:
            case TZlibTransport:
            case TFileTransport:
            case TSocket:
            default:
                args.transportFactory(new TTransportFactory());
                break;
        }
        switch(ServerConfig.ProtocolType){
            case TBinaryProtocol:
                 args.protocolFactory(new TBinaryProtocol.Factory()); 
                 break;

            case TJSONProtocol:
                args.protocolFactory(new TJSONProtocol.Factory());
                break;
            case TSimpleJSONProtocol:
                args.protocolFactory(new TSimpleJSONProtocol.Factory());
                break;
            case TCompactProtocol:
            case TDebugProtocol:
            case TDenseProtocol:
            default:
                args.protocolFactory(new TCompactProtocol.Factory()); 
                break;
        }
        args.processor(processor);
        return new THsHaServer(args);
    }
    public static TServer nonblockingServer(MainService.Processor<MainServiceHander> processor) throws Exception {
        
        TNonblockingServer.Args args = new TNonblockingServer.Args(new TNonblockingServerSocket(ServerConfig.HostPort, ServerConfig.SocketTimeOut));
        
        switch (ServerConfig.TransportType) {
            case TFramedTransport:
                args.transportFactory(new TFramedTransport.Factory());
                break;
            case TMemoryTransport:
            case TZlibTransport:
            case TFileTransport:
            case TSocket:
            default:
                args.transportFactory(new TTransportFactory());
                break;
        }
        switch(ServerConfig.ProtocolType){
            case TBinaryProtocol:
                 args.protocolFactory(new TBinaryProtocol.Factory()); 
                 break;

            case TJSONProtocol:
                args.protocolFactory(new TJSONProtocol.Factory());
                break;
            case TSimpleJSONProtocol:
                args.protocolFactory(new TSimpleJSONProtocol.Factory());
                break;
            case TCompactProtocol:
            case TDebugProtocol:
            case TDenseProtocol:
            default:
                args.protocolFactory(new TCompactProtocol.Factory()); 
                break;
        }
        args.processor(processor);
        return new TNonblockingServer(args);
    }
}
