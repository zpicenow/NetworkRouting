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

    @Override
    public void run() {
        if(node.processBroadcast((BroadcastPackage) protocolData.getaPackage())){
            try {
                LinkedList<NeighNode> neighNodes = node.getNeighNodes();
                for (NeighNode neighNode : neighNodes){
                    udpData.setSendData(new ProtocolData(0,node.getPort(), neighNode.getPort(),protocolData.getaPackage()));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
