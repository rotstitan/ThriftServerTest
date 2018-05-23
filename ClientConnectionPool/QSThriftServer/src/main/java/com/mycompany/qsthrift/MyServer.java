/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.qsthrift;

import com.thrifttest.thrift.ThriftTestService;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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
    public static void StartCaculateTime(){
        Timer time = new Timer(); // Instantiate Timer Object
        CaculateTask ct = new CaculateTask(); // Instantiate SheduledTask class
        //ct.now = System.currentTimeMillis();
        time.schedule(ct, 0, 1000); // Create Repetitively task for every 1 secs
    }
    public static void StartsimpleServer(ThriftTestService.Processor<ThriftTestHander
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
            StartCaculateTime();
            System.out.println("Starting the "+ ServerConfig.ServerType +"...");
            server.serve();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public static void main(String[] args) {
        StartsimpleServer(new ThriftTestService.Processor<ThriftTestHander>(new ThriftTestHander()));
    }
   
    public static TServer poolServer(ThriftTestService.Processor<ThriftTestHander> processor) throws Exception {

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
    public static TServer simpleServer(ThriftTestService.Processor<ThriftTestHander> processor) throws Exception {

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
    public static TServer threadedSelectorServer(ThriftTestService.Processor<ThriftTestHander> processor) throws Exception {

        TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(new TNonblockingServerSocket(ServerConfig.HostPort, ServerConfig.SocketTimeOut));
        args.workerThreads(ServerConfig.WorkerThread);        
        args.selectorThreads(ServerConfig.SelectorThread);
        //args.acceptPolicy(TThreadedSelectorServer.Args.AcceptPolicy.FAIR_ACCEPT);
        

//        switch(ServerConfig.TransportType){
//            case TFramedTransport: 
//                args.transportFactory(new TFramedTransport.Factory());
//                break;
//            case TMemoryTransport:
//            case TZlibTransport:      
//            case TFileTransport: 
//            case TSocket:
//            default:
//                 args.transportFactory(new TTransportFactory());
//                break;
//        }
//        
//        switch(ServerConfig.ProtocolType){
//            case TBinaryProtocol:
//                 args.protocolFactory(new TBinaryProtocol.Factory()); 
//                 break;
//
//            case TJSONProtocol:
//                args.protocolFactory(new TJSONProtocol.Factory());
//                break;
//            case TSimpleJSONProtocol:
//                args.protocolFactory(new TSimpleJSONProtocol.Factory());
//                break;
//            case TCompactProtocol:
//            case TDebugProtocol:
//            case TDenseProtocol:
//            default:
//                args.protocolFactory(new TCompactProtocol.Factory()); 
//                break;
//        }
        
//        SynchronousQueue<Runnable> executorQueue = // NOSONAR
//        new SynchronousQueue<Runnable>();
//        ExecutorService executorService = new ThreadPoolExecutor(5, ServerConfig.WorkerThread,
//        ServerConfig.KeepAliveTime, TimeUnit.SECONDS, executorQueue);
//        args.executorService(executorService);
        args.processor(processor);
        return new TThreadedSelectorServer(args);
    }
    public static TServer tHsHaServer(ThriftTestService.Processor<ThriftTestHander> processor) throws Exception {
        
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
    public static TServer nonblockingServer(ThriftTestService.Processor<ThriftTestHander> processor) throws Exception {
        
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
