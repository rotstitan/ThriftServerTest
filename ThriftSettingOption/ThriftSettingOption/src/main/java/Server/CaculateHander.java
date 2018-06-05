/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import com.thrifttest.thrift.CaculateService;
import org.apache.thrift.TException;
/**
 *
 * @author rots
 */
public class CaculateHander implements CaculateService.Iface {

    @Override
    public int add(int a1, int a2) throws TException {
        return a1+a2;
    }
    
}
