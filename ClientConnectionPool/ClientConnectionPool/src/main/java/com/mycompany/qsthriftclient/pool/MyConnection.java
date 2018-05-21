/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.qsthriftclient.pool;

import com.mycompany.qsthriftclient.ClientConfigs;
import com.mycompany.qsthriftclient.ClientConfigs.Protocol;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocol;
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

    public void CreateConnection() throws Exception {
        transport = new TSocket(host, port, socketTimeOut);
        transport.open();
        TProtocol protocol = CreateProtocol(transport);
        Constructor<? extends TServiceClient> clientConstructor = clientClass.getConstructor(TProtocol.class);
        client = (T) clientConstructor.newInstance(protocol);
    }
    private TProtocol CreateProtocol(TTransport transport) {
        switch(ClientConfigs.ProtocolType){
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
