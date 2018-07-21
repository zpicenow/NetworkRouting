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

    /**
     * 广播包发送线程任务
     * 初始化
     * 对邻接表里的每个邻接点发送自己的邻接表
     * 期望对方根据自己邻接表更新邻接表
     * 约定广播包标记位为0
     */
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
