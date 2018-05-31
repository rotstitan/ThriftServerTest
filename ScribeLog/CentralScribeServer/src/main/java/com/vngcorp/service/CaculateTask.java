/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vngcorp.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimerTask;

/**
 *
 * @author rots
 */
public class CaculateTask extends TimerTask {

    //public static long now; // to display current time
    public static long index = 0;
    public static long totalRecv = 0;
    public static long caculatedTime = 0;
    public static long currentSpeed = 0;    
    public static long maxSpeed = 0;
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
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); 
    LocalDateTime now;
    public static synchronized void addRECV(){
        index++;
        totalRecv++;
    }
    // Add your task here
    public void run() {
//        long dt = System.currentTimeMillis() - now;
//        if(dt <= 0) return;
//        now = System.currentTimeMillis();
        
        currentSpeed = index;
        if(currentSpeed > maxSpeed)
            maxSpeed = currentSpeed;
        index = 0;
        
        if(currentSpeed > 0) {
            now = LocalDateTime.now();
            System.out.println(dtf.format(now) + " | TotalRECV: " + CaculateTask.totalRecv + " recv"
                                            + " | StoreSize: " + getTotalStored() + " data");
        }
    }
}