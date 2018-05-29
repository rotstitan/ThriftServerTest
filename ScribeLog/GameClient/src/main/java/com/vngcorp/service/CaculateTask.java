/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vngcorp.service;

import com.vngcorp.config.ClientConfigs;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimerTask;

/**
 *
 * @author rots
 */
public class CaculateTask extends TimerTask {

    //public static long now; // to display current time
    public static long caculatedTime = 0;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss"); 
    LocalDateTime now;
    int Left = 0;
    int WaitingCircle = 0;
    public CaculateTask(){
        
    }
    // Add your task here
    public void run() {
//        long dt = System.currentTimeMillis() - now;
//        if(dt <= 0) return;
//        now = System.currentTimeMillis();

        if(GameClient.sended >= (ClientConfigs.TotalProcess * ClientConfigs.TotalDataPerProcess)){
            if(GameClient.thread <= 0){
                return;
            }
        }
        now = LocalDateTime.now();
        int tempLeft = ClientConfigs.TotalProcess * ClientConfigs.TotalDataPerProcess - GameClient.received;
        
        if (tempLeft >= Left){
            WaitingCircle ++;
            if(WaitingCircle > ClientConfigs.ConnetTimeOut/1000){ // timeout = 20s
                GameClient.addThread(-GameClient.thread);
                MThreading.ExitWithError(true);
                
            }
        }else{
            WaitingCircle = 0;
        }
        Left = tempLeft;
        System.out.println(dtf.format(now) + " |ThreadWorking:" + GameClient.thread + " |SEND:" + GameClient.sended
                + " |RECV:" + GameClient.received + " |ERR:" + GameClient.failed + " |LEFT:" + Left);
    
    }
}