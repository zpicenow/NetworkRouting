/*
 * TimedUpdateManage
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.PackageManage.senderManage;


import com.zp.protocol.data.TimedUpdatePackage;
import com.zp.node.NeighNode;
import com.zp.node.ReadNodeData;
import com.zp.protocol.SenderProtocol;
import com.zp.threadPool.Task;

import java.util.LinkedList;

/**
 * 定时发送更新任务类
 */
public class TimedUpdateManage extends Task {
    private ReadNodeData node;
    private SenderProtocol sendProtocol;
    private int time;

    public TimedUpdateManage(SenderProtocol sendProtocol, ReadNodeData node, int time){
        this.sendProtocol = sendProtocol;
        this.node = node;
        this.time = time;
    }

    /**
     * 更新数据包线程任务
     * 休眠配置文件里的时间
     * 初始化更新包并分配
     * 对邻接表里每个邻接节点发送
     * 约定更新包的标记位为2
     */
    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LinkedList<NeighNode> neighNodes = node.getNeighNodes();
            for (NeighNode neighNode : neighNodes){
                try {
                    sendProtocol.generateProtocolData(2, neighNode.getPort(),new TimedUpdatePackage(node.getGraphEdges()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
