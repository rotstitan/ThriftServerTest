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
    // GameID
    public static final String[] GameID = {
            "VLTK_MOBILE",
            "TIENLEN",
            "YUGIOH_H5",
    };
    //log4j Config
    
    // Log Path
    public static final String LogPath = "logs/";
    
    // Main Thrift Server Config
    public static final int HostPort = 7777;

    public static final Blocking BlockingType = Blocking.TNonblockingSocket;
    public static final Transport TransportType = Transport.TFramedTransport;
    public static final Protocol ProtocolType = Protocol.TBinaryProtocol;
    public static final Server ServerType = Server.TThreadedSelectorServer;
    
    public static final int WorkerThread = 2;    
    public static final int SelectorThread = 5;    
    public static final int MaxCallQueueSize = 4;    
    public static final long KeepAliveTime = 5000;
    public static final int SocketTimeOut = 3000; //3s   


    
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
