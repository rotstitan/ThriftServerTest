/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vngcorp.connectionpool;

import com.vngcorp.config.ServerConfig;
import com.vngcorp.config.ServerConfig.Protocol;
import com.vngcorp.log.thrift.MainService;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author rots
 */
public class MyConnection<T extends TServiceClient> implements Closeable {
    private T client;
    public T getClient() {
        return client;
    }
    private String host;
    private int port;
    private Protocol protocolType; 
    private TTransport transport;
    private int socketTimeOut;
	
    private Class<? extends TServiceClient> clientClass;
    public MyConnection(String host, int port, int socketTimeOut, Protocol protocolType, Class<? extends TServiceClient> clientClass) throws Exception{
        this.host = host;
        this.port = port;
        this.socketTimeOut = socketTimeOut;
        this.protocolType = protocolType;
        this.clientClass = clientClass;
        CreateConnection();
    }

    public  T CreateConnection() throws Exception {
        TTransport socket;
        switch(ServerConfig.ForwardBlockingType){
            case TSocket:
                socket = new TSocket(host, port, socketTimeOut);
                break;
            case TNonblockingSocket:
            default:
                socket = new TNonblockingSocket(host, port);
                break;
        }
        switch(ServerConfig.ForwardTransportType){

            case TFramedTransport: 
                transport = new TFramedTransport(socket);
                break;
            case TMemoryTransport:
            case TZlibTransport:      
            case TFileTransport: 
            case TSocket:
            default:
                transport = socket;
                break;
        } 
        if(ServerConfig.ForwardBlockingType == ServerConfig.Blocking.TSocket) transport.open();
        TProtocol protocol = CreateProtocol(transport);
        Constructor<? extends TServiceClient> clientConstructor = clientClass.getConstructor(TProtocol.class);
        client = (T) clientConstructor.newInstance(protocol);
        return client;
    }
    private TProtocol CreateProtocol(TTransport transport) {
        switch(protocolType){
            case TBinaryProtocol:
                return new TBinaryProtocol(transport); 
            case TJSONProtocol:
            case TSimpleJSONProtocol:
            case TCompactProtocol:
            case TDebugProtocol:
            case TDenseProtocol:
            default:
                return new TCompactProtocol(transport); 
        }
    }
    public boolean isClosed() {
        return !transport.isOpen();
    }
    @Override
     public void close() throws IOException {
        if (transport != null) {
                transport.close();
        }
    }
}
