/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vngcorp.log;

import com.vngcorp.config.ServerConfig;
import com.vngcorp.log.thrift.LogEntry;
import com.vngcorp.service.ForwardClient;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rots
 */
public class LogEntryStore {
    private static boolean isLock = false;
    public static synchronized boolean isStoreLock(){
        return isLock;
    }
    public static synchronized void setIsStoreLock(boolean lock){
        isLock = lock;
    }
    private static long totalStored = 0;
    public static synchronized long getTotalStored(){
        return totalStored;
    }
    public static synchronized void addTotalStored(long add){
        totalStored += add;
    }
    public static synchronized void setTotalStored(long value){
        totalStored = value;
    }
    private static long writeWhenResend = 0;
    public static synchronized void addWriteWhenResend(long add){
        writeWhenResend += add;
    }
    public static void TryingToResend() {
        if(!isStoreLock()){
            try{
                setIsStoreLock(true);
                File inputFile = new File(ServerConfig.QueueData);
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                String currentLine;
                int totalLine = 0;
                while((currentLine = reader.readLine()) != null && !currentLine.isEmpty()) {
                    totalLine++;
                    ResendData(currentLine);
                }
                reader.close();
                PrintWriter writer = new PrintWriter(ServerConfig.QueueData);
                writer.print("");
                writer.close();
                setTotalStored(0);
                if(totalLine > 0) System.out.println("Done Recover " +totalLine + " data");
            }catch(FileNotFoundException e){
                //System.out.println("Error when TryingToResend");
            }
            catch(Exception e){
                System.out.println("Error when TryingToResend:\n" + e);
            }finally{
                setIsStoreLock(false);
            }   
        }
    }
    private static void ResendData(String data){
        String [] arrOfStr = data.split("\t");
        LogEntry entry = new LogEntry(arrOfStr[0],arrOfStr[1],arrOfStr[2]);
        ForwardClient.getInstance().sendMessage(entry);
    }
}
