/*
 * TimedManage
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.PackageManage.TimedManage;

import com.zp.node.RouteNode;
import com.zp.threadPool.Task;
import com.zp.threadPool.ThreadPool;

import java.io.FileInputStream;
import java.util.Properties;

public class TimedManage extends Task {
    private RouteNode routeNode;
    private Properties config;
    private ThreadPool threadPool;

    public TimedManage(RouteNode routeNode, ThreadPool threadPool){
        try{
            this.config = new Properties();
            String path = Class.forName("com.zp.Main").getResource("assignment.properties").toString();
            path = path.substring(5,path.length());
            this.config.load(new FileInputStream(path));
        }catch (Exception err){
            err.printStackTrace();
        }
        this.routeNode = routeNode;
        this.threadPool = threadPool;
    }

    @Override
    public void run() {
        try{
            Thread.sleep(Integer.parseInt(this.config.getProperty("DelayTime")));
        }catch (InterruptedException err){
            err.printStackTrace();
        }
        threadPool.execute(new DijkstraManage(routeNode,Integer.parseInt(this.config.getProperty("DijkstraTime"))));
        threadPool.execute(new HeartbeatTestManage(routeNode,Integer.parseInt(this.config.getProperty("HeartbeatTime"))));
    }
}
