/*
 * receiverManage
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.PackageManage.receiverManage;

import com.zp.node.WriteNodeData;
import com.zp.protocol.ReceiverProtocol;
import com.zp.threadPool.Task;
import com.zp.threadPool.ThreadPool;
import com.zp.udp.UdpData;

public class ReceiverManage extends Task {
    private ReceiverProtocol receiveProtocol;

    public ReceiverManage(UdpData udpData, ThreadPool threadPool, WriteNodeData node){
        this.receiveProtocol = new ReceiverProtocol(udpData,threadPool,node);
    }

    @Override
    public void run() {
        while (true){
            receiveProtocol.processData();
        }
    }
}
