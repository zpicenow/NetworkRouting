/*
 * ReceiverProtocol
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.protocol;

import com.zp.PackageManage.receiverManage.BroadcastManage;
import com.zp.PackageManage.receiverManage.HeartbeatManage;
import com.zp.protocol.data.HeartbeatPackage;
import com.zp.protocol.data.TimedUpdatePackage;
import com.zp.node.WriteNodeData;
import com.zp.PackageManage.receiverManage.TimedUpdateManage;
import com.zp.threadPool.ThreadPool;
import com.zp.udp.UdpData;

public class ReceiverProtocol  {
    private UdpData udpData;
    private ThreadPool threadPool;
    private WriteNodeData node;

    public ReceiverProtocol(UdpData udpData, ThreadPool threadPool, WriteNodeData node) {
        this.udpData = udpData;
        this.threadPool = threadPool;
        this.node = node;
    }


    public void processData() {
        try {
            ProtocolData protocolData = udpData.getReceiveData();
            switch (protocolData.getType()) {
                case 0:
                    threadPool.execute(new BroadcastManage(node,udpData,protocolData));
                    break;
                case 1:
                    threadPool.execute(new HeartbeatManage(node,(HeartbeatPackage) protocolData.getaPackage()));
                    break;
                case 2:
                    threadPool.execute(new TimedUpdateManage(node,(TimedUpdatePackage) protocolData.getaPackage()));
                    break;
                default:
                    //to-do 错误type处理
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
