/*
 * HeartbeatManage
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.PackageManage.senderManage;

import com.zp.node.NeighNode;

import com.zp.protocol.data.HeartbeatPackage;
import com.zp.node.ReadNodeData;
import com.zp.protocol.SenderProtocol;
import com.zp.threadPool.Task;

import java.util.LinkedList;

/**
 * 心跳包类
 */
public class HeartbeatManage extends Task {
    private SenderProtocol sendProtocol;
    private ReadNodeData node;
    private int time;

    public HeartbeatManage(SenderProtocol sendProtocol, ReadNodeData node, int time){
        this.sendProtocol = sendProtocol;
        this.node = node;
        this.time = time;
    }

    @Override
    public void run() {
        while (true){
            LinkedList<NeighNode> neighNodes = node.getNeighNodes();
            for (NeighNode neighNode : neighNodes){
                try {
                    sendProtocol.generateProtocolData(1, neighNode.getPort(),new HeartbeatPackage(node.getId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
