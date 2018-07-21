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

    /**
     * 类似抽象，概念方法
     * 接收数据处理线程
     * 对收到的数据执行对应类型的process方法
     * 运行时根据调用对象动态向下实例化
     */
    @Override
    public void run() {
        while (true){
            receiveProtocol.processData();
        }
    }
}
