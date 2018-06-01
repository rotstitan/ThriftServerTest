/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.qsthrift;

/**
 *
 * @author rots
 */
public class ServerConfig {
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
    public static final int HostPort = 7777;

    public static final Blocking BlockingType = Blocking.TSocket;
    public static final Transport TransportType = Transport.TFramedTransport;
    public static final Protocol ProtocolType = Protocol.TBinaryProtocol;
    public static final Server ServerType = Server.TThreadPoolServer;
    
    public static final int WorkerThread = 1;    
    public static final int SelectorThread = 1;    
    public static final int MaxCallQueueSize = 4;    
    public static final long KeepAliveTime = 5000;
    public static final int SocketTimeOut = 3000; //6s   


    
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
