/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.qsthriftclient.pool;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rots
 */
public class FunctionData {
    public String functionName;
    public Map<String,Object> data;
    public FunctionData(){
        this.functionName = "";
        this.data = new HashMap<>();
    }
    public FunctionData(String fName, Map<String,Object> data){
        this.functionName = fName;
        this.data = data;
    }
    public FunctionData putData(String key, String object){
        data.put(key, object);
        return this;
    }
}
