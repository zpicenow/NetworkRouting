/*
 * BroadcastManage
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.PackageManage.receiverManage;

import com.zp.node.NeighNode;
import com.zp.protocol.data.BroadcastPackage;
import com.zp.protocol.ProtocolData;
import com.zp.node.WriteNodeData;
import com.zp.threadPool.Task;
import com.zp.udp.UdpData;

import java.util.LinkedList;

public class BroadcastManage extends Task {
    private WriteNodeData node;
    private UdpData udpData;
    private ProtocolData protocolData;

    public BroadcastManage(WriteNodeData node, UdpData udpData, ProtocolData protocolData){
        this.node = node;
        this.udpData = udpData;
        this.protocolData = protocolData;
    }

    /**
     * 处理广播包线程任务
     * 当邻接点集不包含时添加到邻接点集中，并且返回true
     * 邻接点集已经更新，即该路由表已经更新，将新路由表数据加到待发送队列中等待发送进程发送
     */
    @Override
    public void run() {
        if(node.processBroadcast((BroadcastPackage) protocolData.getaPackage())){   //当邻接点集不包含时添加到邻接点集中，并且返回true
            try {
                LinkedList<NeighNode> neighNodes = node.getNeighNodes();
                for (NeighNode neighNode : neighNodes){
                    udpData.setSendData(new ProtocolData(0,node.getPort(), neighNode.getPort(),protocolData.getaPackage()));        //邻接点集已经更新，即该路由表已经更新，将新路由表数据加到待发送队列中等待发送进程发送
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
