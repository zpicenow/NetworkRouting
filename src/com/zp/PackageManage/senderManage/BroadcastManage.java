/*
 * BroadcastManage
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.PackageManage.senderManage;

import com.zp.node.NeighNode;
import com.zp.protocol.data.BroadcastPackage;

import com.zp.node.ReadNodeData;
import com.zp.protocol.SenderProtocol;
import com.zp.threadPool.Task;

import java.util.LinkedList;

/**
 * 广播任务类
 */
public class BroadcastManage extends Task {
    private SenderProtocol sendProtocol;
    private ReadNodeData node;

    public BroadcastManage(SenderProtocol sendProtocol, ReadNodeData node){
        this.sendProtocol = sendProtocol;
        this.node = node;
    }

    @Override
    public void run() {
        LinkedList<NeighNode> neighNodes = node.getNeighNodes();
        BroadcastPackage broadcastPackage = new BroadcastPackage(node.getId(),node.getPort(), neighNodes);
        for (NeighNode neighNode : neighNodes){
            try {
                sendProtocol.generateProtocolData(0, neighNode.getPort(), broadcastPackage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
