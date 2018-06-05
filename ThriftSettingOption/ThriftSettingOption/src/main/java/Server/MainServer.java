/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import com.thrifttest.thrift.CaculateService;
import com.thrifttest.thrift.ThriftTestService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author rots
 */
public class MainServer {

    /**
     *
     * @param port
     */
    public static void StartMultiServiceServer() {
        (new Thread(){
            public void run(){
                try {
                    TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(7771);
                    TMultiplexedProcessor processor = new TMultiplexedProcessor();
                    processor.registerProcessor("CaculateService", new   CaculateService.Processor<CaculateService.Iface>(new CaculateHander()));
                    processor.registerProcessor("ThriftTestService", new  ThriftTestService.Processor<ThriftTestService.Iface>(new ThriftTestHander()));
                    TServer server = new TNonblockingServer(
                        new TNonblockingServer.Args(serverTransport).processor(processor));
                    System.out.println("Starting Multi Service server, port: "+ 7771);
                    server.serve();
                } catch (TTransportException ex) {
                    Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }
    public static void StartNormalServer() {
        (new Thread(){
            public void run(){
                try {
                    StartCaculateTime();
                    ThriftTestService.Processor processor = new ThriftTestService.Processor<ThriftTestHander>(new ThriftTestHander());
                    TServerTransport serverTransport;
                    serverTransport = new TServerSocket(7770, 6000);
                    TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
                    args.transportFactory(new TFastFramedTransport.Factory());
                    args.protocolFactory(new TBinaryProtocol.Factory()); 
                    args.processor(processor);
                    System.out.println("Starting Normal server, port: "+ 7770);
                    (new TThreadPoolServer(args)).serve();
                } catch (TTransportException ex) {
                    Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
       
    }
    public static void StartCaculateTime(){
            (new Thread(){
                public synchronized void run(){
                    try {
                        
                        while(true){
                            System.out.println("7770:  " + ThriftTestHander.index + " req/s");
                            ThriftTestHander.index = 0;
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException ex) {

                    }
                }
                
            }).start();
    }
    public static void main(String[] args) throws InterruptedException {

        try {
            StartNormalServer();
            StartMultiServiceServer();
        }catch (Exception e) {
            System.out.println("FatalError: ");
            e.printStackTrace();
        }
    }
}