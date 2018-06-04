/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import com.thrifttest.thrift.ThriftTestService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author rots
 */
public class MainServer {
    public static void main(String[] args) throws TTransportException {
        StartServer();
    }
    public static void StartServer() throws TTransportException{
        StartCaculateTime();
        ThriftTestService.Processor processor = new ThriftTestService.Processor<ThriftTestHander>(new ThriftTestHander());
        TServerTransport serverTransport = new TServerSocket(7777, 6000);
        TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
        args.transportFactory(new TFastFramedTransport.Factory());
        args.protocolFactory(new TBinaryProtocol.Factory()); 
        args.processor(processor);
        (new TThreadPoolServer(args)).serve();
    }
        public static void StartCaculateTime(){
            (new Thread(){
                public synchronized void run(){
                    try {
                        
                        while(true){
                            System.out.println(ThriftTestHander.index + " req/s");
                            ThriftTestHander.index = 0;
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException ex) {

                    }
                }
                
            }).start();
    }
}
