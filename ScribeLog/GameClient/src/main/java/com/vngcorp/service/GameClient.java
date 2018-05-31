/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vngcorp.service;

import com.vngcorp.config.ClientConfigs;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author rots
 */

public class GameClient {
    public static int thread = 0;    
    public static int mainErr = 0;
    public static int received = 0;    
    public static int sended = 0;    
    public static int failed = 0;    
    public static int threadFailed = 0;

    public static int maxSpeed = 0;
    
    
    public static boolean Error = false;
    public static boolean DoneSend = false;
    public static long startTestTime = 0;
    public static long totalExecuteTime = 0;
   
    

    public static synchronized void addSend(int add) {
        sended+= add;
    }
    public static synchronized void addRecv(int add) {
        received+= add;
    }
    public static synchronized void addThread(int add) {
        thread+= add;
    }
    public static synchronized void addFail(int add) {
        failed+= add;
    }
    public static synchronized void addThreadFail(int add) {
        threadFailed+= add;
    }
    public static synchronized void addTotalExTime(long add) {
       totalExecuteTime += add;
    }
    public static void StartCaculateTime(){
        Timer time = new Timer(); // Instantiate Timer Object
        CaculateTask ct = new CaculateTask(); // Instantiate SheduledTask class
        //ct.now = System.currentTimeMillis();
        time.schedule(ct, 0l, 1000l); // Create Repetitively task for every 1 secs
    }
    public static void main(String[] args) throws InterruptedException {

        try {
            startTestTime = System.currentTimeMillis();
            StartCaculateTime();
           for(int i = 0;i < ClientConfigs.TotalProcess; i++){
                new MThreading().start();
            }
            DoneSend = true;
            while(thread > 0){
                Thread.sleep(100);
            }
            
        }catch (Exception e) {
            System.out.println("FatalError: ");
            e.printStackTrace();
            MThreading.getTestResult(false);
        }
    }
}

 