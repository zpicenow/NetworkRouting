/*
 * UdpSenderTask
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.PackageManage.senderManage;

import com.zp.node.ReadNodeData;

import com.zp.protocol.SenderProtocol;
import com.zp.threadPool.Task;
import com.zp.threadPool.ThreadPool;
import com.zp.udp.UdpData;

import java.io.FileInputStream;
import java.util.Properties;

public class SenderManage extends Task {
    private Properties config;
    private SenderProtocol sendProtocol;
    private ThreadPool threadPool;
    private ReadNodeData node;

    public SenderManage(ReadNodeData node, ThreadPool threadPool, UdpData udpData){
        try {
            this.sendProtocol = new SenderProtocol(node.getPort(),udpData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            this.config = new Properties();
            String path = Class.forName("com.zp.Main").getResource("assignment.properties").toString();
            path = path.substring(5,path.length());
            this.config.load(new FileInputStream(path));
        }catch (Exception err){
            err.printStackTrace();
        }
        this.threadPool = threadPool;
        this.node = node;
    }

    @Override
    public void run() {
        threadPool.execute(new HeartbeatManage(sendProtocol,node,Integer.parseInt(this.config.getProperty("HeartbeatTime"))));
        try{
            Thread.sleep(Integer.parseInt(this.config.getProperty("DelayTime")));
        }catch (InterruptedException err){
            err.printStackTrace();
        }
        threadPool.execute(new BroadcastManage(sendProtocol,node));
        threadPool.execute(new TimedUpdateManage(sendProtocol,node,Integer.parseInt(this.config.getProperty("TimedUpdateTime"))));
    }
}
