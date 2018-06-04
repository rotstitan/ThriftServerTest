/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Server.MainServer;
import com.thrifttest.thrift.ThriftTestService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author rots
 */
public class MainClient {
    public static void main(String[] args){
        for(int i = 0;i < 10; i++){
            (new Thread(){
                @Override
                public synchronized void run(){
                    try {
                        TTransport socket = new TSocket("localhost", 7777);
                        TTransport transport = new TFastFramedTransport(socket);
                        transport.open();
                        TProtocol protocol = new TBinaryProtocol(transport); 
                        ThriftTestService.Client client = new ThriftTestService.Client(protocol);
                        while(true){
                            client.ping();
                        }
                    } catch (TTransportException ex) {
                        Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (TException ex) {
                        Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();
        }
    }
}
