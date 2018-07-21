/*
 * Main
 *
 * @author ZhaoPeng
 * @date 18-7-16
 */
package com.zp;

import com.zp.graph.Graph;
import com.zp.node.RouteNode;
import com.zp.PackageManage.TimedManage.TimedManage;
import com.zp.PackageManage.receiverManage.ReceiverManage;
import com.zp.PackageManage.senderManage.SenderManage;
import com.zp.threadPool.ThreadPool;
import com.zp.threadPool.ThreadPoolManger;
import com.zp.udp.UdpData;
import com.zp.udp.UdpReceiverTask;
import com.zp.udp.UdpSenderTask;

import java.util.Scanner;

public class Main {
    /**
     * 入口函数
     * 接收命令行参数，格式为 java com.zp.Main {name} {port} {confFilePath}
     * @param args string[]
     */
    public static void main(String[] args){
        ThreadPool threadPool = new ThreadPoolManger(20,10);
        UdpData udpData = new UdpData();
        RouteNode routeNode = new RouteNode(args[0],Integer.parseInt(args[1]),args[2]);
        ((ThreadPoolManger) threadPool).start();
        threadPool.execute(new UdpReceiverTask(udpData, routeNode.getPort()));
        threadPool.execute(new UdpSenderTask(udpData));
        threadPool.execute(new ReceiverManage(udpData,threadPool, routeNode));
        threadPool.execute(new SenderManage(routeNode,threadPool,udpData));
        threadPool.execute(new TimedManage(routeNode,threadPool));
        Scanner scanner = new Scanner(System.in);
        while (true){
            if(scanner.nextLine().equals("update")){
                Graph graph = new Graph();
                graph.addGraphEdges(routeNode.getGraphEdges());
                graph.dijkstra(routeNode.getId());
            }
        }
    }
}
