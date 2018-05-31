/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vngcorp.config;

/**
 *
 * @author rots
 */
public class ServerConfig {
    // Forward Config
    public static final String ForwardIP = "localhost"; 
    public static final int ForwardPort = 7777;
    
    public static final int ForwardSenderThread = 5;
    public static final int ForwardSenderQueue = 1000000;
    // Forward Connection Pool Config
    public static final int ForwardInitConnection = 5;    
    public static final int ForwardMinFreeConnection = 5;
    public static final int ForwardMaxConnection = 10;      
    public static final int ForwardSocketTimeOut = 6000;      
    public static final int ForwardConnetTimeOut = 6000;
    public static final int ForwardTryingToConnectInteval = 1000;
    
    public static final Blocking ForwardBlockingType = Blocking.TSocket;
    public static final Transport ForwardTransportType = Transport.TFramedTransport;
    public static final Protocol ForwardProtocolType = Protocol.TBinaryProtocol;
    public static final Server ForwardServerType = Server.TThreadedSelectorServer;
    //log4j Config
    public static final String QueueData = "backup/QueueData.log";    
    public static final String Log4jConfigFile = "configs/log4j.properties";
    //Queue Config
    public static final int MaxDataQueue = 1000000;    
    public static final int QueueCheckInterval = 1000;
 
    
    // Main Thrift Server Config
    public static final int HostPort = 7700;

    public static final Blocking BlockingType = Blocking.TNonblockingSocket;
    public static final Transport TransportType = Transport.TFramedTransport;
    public static final Protocol ProtocolType = Protocol.TBinaryProtocol;
    public static final Server ServerType = Server.TThreadedSelectorServer;
    
    public static final int WorkerThread = 2;    
    public static final int SelectorThread = 5;    
    public static final int MaxCallQueueSize = 4;    
    public static final long KeepAliveTime = 5000;
    public static final int SocketTimeOut = 30000; //3s   


    
    public enum Blocking {
        TSocket,
        TNonblockingSocket,
    }
    public enum Transport {
        TSocket, // blocking Server
        TFramedTransport,  //nonblocking Server      
        TMemoryTransport,
        TZlibTransport,        
        TFileTransport,
    }
    public enum Protocol {
        TBinaryProtocol,
        TCompactProtocol,
        TDebugProtocol,
        TDenseProtocol,
        TJSONProtocol,
        TSimpleJSONProtocol,
    }
    public enum Server {
        TSimpleServer,
        TThreadPoolServer,
        TNonblockingServer,
        THsHaServer,
        TThreadedSelectorServer,
    }
}
