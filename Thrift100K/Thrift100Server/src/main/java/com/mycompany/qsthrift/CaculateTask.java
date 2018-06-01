/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.qsthrift;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimerTask;

/**
 *
 * @author rots
 */
class CaculateTask extends TimerTask {

    //public static long now; // to display current time
    public static long index = 0;
    public static long caculatedTime = 0;
    public static long currentSpeed = 0;    
    public static long maxSpeed = 0;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); 
    LocalDateTime now;

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
            System.out.println(dtf.format(now) + " |Request: " + CaculateTask.currentSpeed + " req/s"
                        + " | Max: " + CaculateTask.maxSpeed + " req/s");
        }
    }
}