/*
 * TimedUpdateManage
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.PackageManage.receiverManage;

import com.zp.protocol.data.TimedUpdatePackage;
import com.zp.node.WriteNodeData;
import com.zp.threadPool.Task;


public class TimedUpdateManage extends Task {
    private WriteNodeData node;
    private TimedUpdatePackage timedUpdateData;

    public TimedUpdateManage(WriteNodeData node, TimedUpdatePackage timedUpdateData){
        this.node = node;
        this.timedUpdateData = timedUpdateData;
    }

    /**
     * 更新线程任务
     * 根据收到的更新边表/路径表进行对原表更新，党同伐异
     */
    @Override
    public void run() {
        node.processTimedUpdate(timedUpdateData);
    }
}
