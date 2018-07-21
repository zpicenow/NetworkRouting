/*
 * HeartbeatManage
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.PackageManage.receiverManage;

import com.zp.protocol.data.HeartbeatPackage;
import com.zp.node.WriteNodeData;
import com.zp.threadPool.Task;

public class HeartbeatManage extends Task {
    private WriteNodeData node;
    private HeartbeatPackage heartbeatData;

    public HeartbeatManage(WriteNodeData node, HeartbeatPackage heartbeatData){
        this.node = node;
        this.heartbeatData = heartbeatData;
    }

    /**
     * 处理心跳包线程任务
     * 调用处理心跳包函数
     * 更新节点信息以及更新时间
     */
    @Override
    public void run() {
        node.setHeartbeatTime(heartbeatData);
    }
}
